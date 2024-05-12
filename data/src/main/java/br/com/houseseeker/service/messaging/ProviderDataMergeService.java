package br.com.houseseeker.service.messaging;

import br.com.houseseeker.domain.property.AbstractUrbanPropertyMetadata;
import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.entity.UrbanPropertyConvenience;
import br.com.houseseeker.entity.UrbanPropertyLocation;
import br.com.houseseeker.entity.UrbanPropertyMeasure;
import br.com.houseseeker.entity.UrbanPropertyMedia;
import br.com.houseseeker.entity.UrbanPropertyPriceVariation;
import br.com.houseseeker.mapper.UrbanPropertyConvenienceMapper;
import br.com.houseseeker.mapper.UrbanPropertyLocationMapper;
import br.com.houseseeker.mapper.UrbanPropertyMapper;
import br.com.houseseeker.mapper.UrbanPropertyMeasureMapper;
import br.com.houseseeker.mapper.UrbanPropertyMediaMapper;
import br.com.houseseeker.service.UrbanPropertyConvenienceService;
import br.com.houseseeker.service.UrbanPropertyLocationService;
import br.com.houseseeker.service.UrbanPropertyMeasureService;
import br.com.houseseeker.service.UrbanPropertyMediaService;
import br.com.houseseeker.service.UrbanPropertyPriceVariationService;
import br.com.houseseeker.service.UrbanPropertyService;
import br.com.houseseeker.service.messaging.ProviderDataCollectorService.UrbanPropertyFullData;
import br.com.houseseeker.service.calculator.PriceVariationCalculatorService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProviderDataMergeService {

    private final UrbanPropertyMapper urbanPropertyMapper;
    private final UrbanPropertyLocationMapper urbanPropertyLocationMapper;
    private final UrbanPropertyMeasureMapper urbanPropertyMeasureMapper;
    private final UrbanPropertyConvenienceMapper urbanPropertyConvenienceMapper;
    private final UrbanPropertyMediaMapper urbanPropertyMediaMapper;
    private final PriceVariationCalculatorService priceVariationCalculatorService;
    private final UrbanPropertyService urbanPropertyService;
    private final UrbanPropertyLocationService urbanPropertyLocationService;
    private final UrbanPropertyMeasureService urbanPropertyMeasureService;
    private final UrbanPropertyConvenienceService urbanPropertyConvenienceService;
    private final UrbanPropertyMediaService urbanPropertyMediaService;
    private final UrbanPropertyPriceVariationService urbanPropertyPriceVariationService;
    private final Clock clock;

    @Transactional
    public void merge(
            @NotNull Provider provider,
            @NotNull Map<UrbanProperty, UrbanPropertyFullData> propertyFullDataMap,
            @NotNull List<AbstractUrbanPropertyMetadata> extractedData
    ) {
        Map<String, UrbanProperty> existingPropertiesByCodeMap = groupByProviderCode(propertyFullDataMap, Map.Entry::getKey);
        Map<String, AbstractUrbanPropertyMetadata> extractedPropertiesByCodeMap = groupByProviderCode(extractedData);

        log.info("Merging step 1 for provider {}: saving urban properties ...", provider.getName());
        createOrUpdateExistingProperties(provider, existingPropertiesByCodeMap, extractedPropertiesByCodeMap);
        deleteExistingProperties(existingPropertiesByCodeMap, extractedPropertiesByCodeMap);
        existingPropertiesByCodeMap = saveAllProperties(existingPropertiesByCodeMap.values());

        log.info("Merging step 2 for provider {}: saving property locations ...", provider.getName());
        createOrUpdateExistingPropertyLocations(
                existingPropertiesByCodeMap,
                groupByProviderCode(propertyFullDataMap, e -> e.getValue().getLocation()),
                extractedPropertiesByCodeMap
        );

        log.info("Merging step 3 for provider {}: saving property measures ...", provider.getName());
        createOrUpdateExistingPropertyMeasures(
                existingPropertiesByCodeMap,
                groupByProviderCode(propertyFullDataMap, e -> e.getValue().getMeasure()),
                extractedPropertiesByCodeMap
        );

        log.info("Merging step 4 for provider {}: saving property conveniences ...", provider.getName());
        recreatePropertyConveniences(
                existingPropertiesByCodeMap,
                groupByProviderCode(propertyFullDataMap, e -> e.getValue().getConveniences()),
                extractedPropertiesByCodeMap
        );

        log.info("Merging step 5 for provider {}: saving property medias ...", provider.getName());
        recreatePropertyMedias(
                existingPropertiesByCodeMap,
                groupByProviderCode(propertyFullDataMap, e -> e.getValue().getMedias()),
                extractedPropertiesByCodeMap
        );

        log.info("Merging step 6 for provider {}: saving property price variations ...", provider.getName());
        registerPropertyVariations(
                existingPropertiesByCodeMap,
                groupByProviderCode(propertyFullDataMap, e -> e.getValue().getPriceVariations())
        );
    }

    private <T> Map<String, T> groupByProviderCode(
            Map<UrbanProperty, UrbanPropertyFullData> propertyFullDataMap,
            Function<Map.Entry<UrbanProperty, UrbanPropertyFullData>, T> valueMapper
    ) {
        return propertyFullDataMap.entrySet()
                                  .stream()
                                  .collect(Collectors.toMap(e -> e.getKey().getProviderCode(), valueMapper));
    }

    private Map<String, AbstractUrbanPropertyMetadata> groupByProviderCode(List<AbstractUrbanPropertyMetadata> extractedData) {
        return extractedData.stream()
                            .collect(Collectors.toMap(
                                    AbstractUrbanPropertyMetadata::getProviderCode,
                                    Function.identity(),
                                    (a, b) -> a
                            ));
    }

    private void createOrUpdateExistingProperties(
            Provider provider,
            Map<String, UrbanProperty> existingPropertiesByCodeMap,
            Map<String, AbstractUrbanPropertyMetadata> extractedPropertiesByCodeMap
    ) {
        extractedPropertiesByCodeMap.forEach((key, value) -> {
            boolean isNewProperty = !existingPropertiesByCodeMap.containsKey(key);
            if (isNewProperty) {
                existingPropertiesByCodeMap.put(key, urbanPropertyMapper.createEntity(provider, value));
            } else {
                urbanPropertyMapper.copyToEntity(value, existingPropertiesByCodeMap.get(key));
            }
        });
    }

    private void deleteExistingProperties(
            Map<String, UrbanProperty> existingPropertiesByCodeMap,
            Map<String, AbstractUrbanPropertyMetadata> extractedPropertiesByCodeMap
    ) {
        existingPropertiesByCodeMap.forEach((key, value) -> {
            if (!extractedPropertiesByCodeMap.containsKey(key)) {
                value.setLastAnalysisDate(LocalDateTime.now(clock));
                value.setExclusionDate(LocalDateTime.now(clock));
            }
        });
    }

    private Map<String, UrbanProperty> saveAllProperties(Iterable<UrbanProperty> urbanProperties) {
        return urbanPropertyService.saveAll(urbanProperties)
                                   .stream()
                                   .collect(Collectors.toMap(UrbanProperty::getProviderCode, Function.identity()));
    }

    private void createOrUpdateExistingPropertyLocations(
            Map<String, UrbanProperty> existingPropertiesByCodeMap,
            Map<String, UrbanPropertyLocation> propertyLocationsMap,
            Map<String, AbstractUrbanPropertyMetadata> extractedPropertiesByCodeMap
    ) {
        extractedPropertiesByCodeMap.forEach((key, value) -> {
            boolean isNewLocation = isNull(propertyLocationsMap.get(key));
            if (isNewLocation) {
                propertyLocationsMap.put(key, urbanPropertyLocationMapper.createEntity(existingPropertiesByCodeMap.get(key), value));
            } else {
                urbanPropertyLocationMapper.copyToEntity(existingPropertiesByCodeMap.get(key), value, propertyLocationsMap.get(key));
            }
        });
        urbanPropertyLocationService.saveAll(propertyLocationsMap.values());
    }

    private void createOrUpdateExistingPropertyMeasures(
            Map<String, UrbanProperty> existingPropertiesByCodeMap,
            Map<String, UrbanPropertyMeasure> propertyMeasuresMap,
            Map<String, AbstractUrbanPropertyMetadata> extractedPropertiesByCodeMap
    ) {
        extractedPropertiesByCodeMap.forEach((key, value) -> {
            boolean isNewMeasure = isNull(propertyMeasuresMap.get(key));
            if (isNewMeasure) {
                propertyMeasuresMap.put(key, urbanPropertyMeasureMapper.createEntity(existingPropertiesByCodeMap.get(key), value));
            } else {
                urbanPropertyMeasureMapper.copyToEntity(existingPropertiesByCodeMap.get(key), value, propertyMeasuresMap.get(key));
            }
        });
        urbanPropertyMeasureService.saveAll(propertyMeasuresMap.values());
    }

    private void recreatePropertyConveniences(
            Map<String, UrbanProperty> existingPropertiesByCodeMap,
            Map<String, List<UrbanPropertyConvenience>> propertyConveniencesMap,
            Map<String, AbstractUrbanPropertyMetadata> extractedPropertiesByCodeMap
    ) {
        List<UrbanPropertyConvenience> saveList = new LinkedList<>();
        List<UrbanPropertyConvenience> exclusionList = new LinkedList<>();

        extractedPropertiesByCodeMap.forEach((key, value) -> {
            UrbanProperty urbanProperty = existingPropertiesByCodeMap.get(key);

            if (!CollectionUtils.isEmpty(value.getConveniences()))
                saveList.addAll(
                        value.getConveniences()
                             .stream()
                             .map(description -> urbanPropertyConvenienceMapper.createEntity(urbanProperty, description.trim().toUpperCase()))
                             .toList()
                );

            if (!CollectionUtils.isEmpty(propertyConveniencesMap.get(key)))
                exclusionList.addAll(propertyConveniencesMap.get(key));
        });

        if (!CollectionUtils.isEmpty(saveList))
            urbanPropertyConvenienceService.saveAll(saveList);

        if (!CollectionUtils.isEmpty(exclusionList))
            urbanPropertyConvenienceService.deleteAll(exclusionList);
    }

    private void recreatePropertyMedias(
            Map<String, UrbanProperty> existingPropertiesByCodeMap,
            Map<String, List<UrbanPropertyMedia>> propertyMediasMap,
            Map<String, AbstractUrbanPropertyMetadata> extractedPropertiesByCodeMap
    ) {
        List<UrbanPropertyMedia> saveList = new LinkedList<>();
        List<UrbanPropertyMedia> exclusionList = new LinkedList<>();

        extractedPropertiesByCodeMap.forEach((key, value) -> {
            UrbanProperty urbanProperty = existingPropertiesByCodeMap.get(key);

            if (!CollectionUtils.isEmpty(value.getMedias()))
                saveList.addAll(
                        value.getMedias()
                             .stream()
                             .map(mediaMetadata -> urbanPropertyMediaMapper.createEntity(urbanProperty, mediaMetadata))
                             .toList()
                );

            if (!CollectionUtils.isEmpty(propertyMediasMap.get(key)))
                exclusionList.addAll(propertyMediasMap.get(key));
        });

        if (!CollectionUtils.isEmpty(saveList))
            urbanPropertyMediaService.saveAll(saveList);

        if (!CollectionUtils.isEmpty(exclusionList))
            urbanPropertyMediaService.deleteAll(exclusionList);
    }

    private void registerPropertyVariations(
            Map<String, UrbanProperty> existingPropertiesByCodeMap,
            Map<String, List<UrbanPropertyPriceVariation>> propertyVariationsMap
    ) {
        List<UrbanPropertyPriceVariation> saveList = new LinkedList<>();
        for (UrbanProperty urbanProperty : existingPropertiesByCodeMap.values())
            saveList.addAll(
                    priceVariationCalculatorService.calculate(
                            urbanProperty,
                            propertyVariationsMap.getOrDefault(urbanProperty.getProviderCode(), Collections.emptyList())
                    )
            );
        urbanPropertyPriceVariationService.saveAll(saveList);
    }

}

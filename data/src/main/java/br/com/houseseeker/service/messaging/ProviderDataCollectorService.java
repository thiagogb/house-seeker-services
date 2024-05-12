package br.com.houseseeker.service.messaging;

import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.entity.UrbanPropertyConvenience;
import br.com.houseseeker.entity.UrbanPropertyLocation;
import br.com.houseseeker.entity.UrbanPropertyMeasure;
import br.com.houseseeker.entity.UrbanPropertyMedia;
import br.com.houseseeker.entity.UrbanPropertyPriceVariation;
import br.com.houseseeker.service.UrbanPropertyConvenienceService;
import br.com.houseseeker.service.UrbanPropertyLocationService;
import br.com.houseseeker.service.UrbanPropertyMeasureService;
import br.com.houseseeker.service.UrbanPropertyMediaService;
import br.com.houseseeker.service.UrbanPropertyPriceVariationService;
import br.com.houseseeker.service.UrbanPropertyService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProviderDataCollectorService {

    private final UrbanPropertyService urbanPropertyService;
    private final UrbanPropertyConvenienceService urbanPropertyConvenienceService;
    private final UrbanPropertyLocationService urbanPropertyLocationService;
    private final UrbanPropertyMeasureService urbanPropertyMeasureService;
    private final UrbanPropertyMediaService urbanPropertyMediaService;
    private final UrbanPropertyPriceVariationService urbanPropertyPriceVariationService;

    @Transactional
    public Map<UrbanProperty, UrbanPropertyFullData> collect(@NotNull Provider provider) {
        log.info("Provider {}: collecting all properties ...", provider.getName());
        List<UrbanProperty> properties = urbanPropertyService.findAllByProvider(provider);

        log.info("Provider {}: collecting all properties conveniences  ...", provider.getName());
        Map<UrbanProperty, List<UrbanPropertyConvenience>> conveniencesMap = urbanPropertyConvenienceService
                .findAllByProvider(provider)
                .stream()
                .collect(Collectors.groupingBy(
                        UrbanPropertyConvenience::getUrbanProperty
                ));

        log.info("Provider {}: collecting all properties locations  ...", provider.getName());
        Map<UrbanProperty, UrbanPropertyLocation> locationMap = urbanPropertyLocationService
                .findAllByProvider(provider)
                .stream()
                .collect(Collectors.toMap(
                        UrbanPropertyLocation::getUrbanProperty,
                        Function.identity()
                ));

        log.info("Provider {}: collecting all properties measures  ...", provider.getName());
        Map<UrbanProperty, UrbanPropertyMeasure> measureMap = urbanPropertyMeasureService
                .findAllByProvider(provider)
                .stream()
                .collect(Collectors.toMap(
                        UrbanPropertyMeasure::getUrbanProperty,
                        Function.identity()
                ));

        log.info("Provider {}: collecting all properties medias  ...", provider.getName());
        Map<UrbanProperty, List<UrbanPropertyMedia>> mediasMap = urbanPropertyMediaService
                .findAllByProvider(provider)
                .stream()
                .collect(Collectors.groupingBy(
                        UrbanPropertyMedia::getUrbanProperty
                ));

        log.info("Provider {}: collecting all properties price variations  ...", provider.getName());
        Map<UrbanProperty, List<UrbanPropertyPriceVariation>> priceVariationsMap = urbanPropertyPriceVariationService
                .findAllByProvider(provider)
                .stream()
                .collect(Collectors.groupingBy(
                        UrbanPropertyPriceVariation::getUrbanProperty
                ));

        return properties.stream()
                         .collect(Collectors.toMap(
                                 Function.identity(),
                                 urbanProperty -> new UrbanPropertyFullData(
                                         locationMap.get(urbanProperty),
                                         measureMap.get(urbanProperty),
                                         conveniencesMap.getOrDefault(urbanProperty, Collections.emptyList()),
                                         mediasMap.getOrDefault(urbanProperty, Collections.emptyList()),
                                         priceVariationsMap.getOrDefault(urbanProperty, Collections.emptyList())
                                 )
                         ));
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static final class UrbanPropertyFullData {

        private final UrbanPropertyLocation location;
        private final UrbanPropertyMeasure measure;
        private final List<UrbanPropertyConvenience> conveniences;
        private final List<UrbanPropertyMedia> medias;
        private final List<UrbanPropertyPriceVariation> priceVariations;

    }

}

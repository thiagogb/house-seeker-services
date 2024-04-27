package br.com.houseseeker.service;

import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.entity.UrbanPropertyConvenience;
import br.com.houseseeker.entity.UrbanPropertyLocation;
import br.com.houseseeker.entity.UrbanPropertyMeasure;
import br.com.houseseeker.entity.UrbanPropertyMedia;
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

    @Transactional
    public Map<UrbanProperty, UrbanPropertyFullData> collect(@NotNull Provider provider) {
        log.info("Provider {}: collecting all properties ...", provider.getName());
        List<UrbanProperty> properties = urbanPropertyService.findAllByProvider(provider);

        log.info("Provider {}: collecting all properties conveniences  ...", provider.getName());
        Map<UrbanProperty, List<UrbanPropertyConvenience>> conveniencesMap = urbanPropertyConvenienceService.findAllByProvider(provider)
                                                                                                            .stream()
                                                                                                            .collect(Collectors.groupingBy(
                                                                                                                    UrbanPropertyConvenience::getUrbanProperty
                                                                                                            ));

        log.info("Provider {}: collecting all properties locations  ...", provider.getName());
        Map<UrbanProperty, UrbanPropertyLocation> locationMap = urbanPropertyLocationService.findAllByProvider(provider)
                                                                                            .stream()
                                                                                            .collect(Collectors.toMap(
                                                                                                    UrbanPropertyLocation::getUrbanProperty,
                                                                                                    Function.identity()
                                                                                            ));

        log.info("Provider {}: collecting all properties measures  ...", provider.getName());
        Map<UrbanProperty, UrbanPropertyMeasure> measureMap = urbanPropertyMeasureService.findAllByProvider(provider)
                                                                                         .stream()
                                                                                         .collect(Collectors.toMap(
                                                                                                 UrbanPropertyMeasure::getUrbanProperty,
                                                                                                 Function.identity()
                                                                                         ));

        log.info("Provider {}: collecting all properties medias  ...", provider.getName());
        Map<UrbanProperty, List<UrbanPropertyMedia>> mediasMap = urbanPropertyMediaService.findAllByProvider(provider)
                                                                                          .stream()
                                                                                          .collect(Collectors.groupingBy(
                                                                                                  UrbanPropertyMedia::getUrbanProperty
                                                                                          ));

        return properties.stream()
                         .collect(Collectors.toMap(
                                 Function.identity(),
                                 urbanProperty -> new UrbanPropertyFullData(
                                         locationMap.get(urbanProperty),
                                         measureMap.get(urbanProperty),
                                         conveniencesMap.getOrDefault(urbanProperty, Collections.emptyList()),
                                         mediasMap.getOrDefault(urbanProperty, Collections.emptyList())
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

    }

}
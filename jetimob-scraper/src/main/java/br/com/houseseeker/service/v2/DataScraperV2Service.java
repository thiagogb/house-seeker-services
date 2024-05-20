package br.com.houseseeker.service.v2;

import br.com.houseseeker.domain.jetimob.v2.FilterOptionsMetadata;
import br.com.houseseeker.domain.jetimob.v2.PropertyInfoMetadata;
import br.com.houseseeker.domain.jetimob.v2.ScraperAnalysisProperties;
import br.com.houseseeker.domain.jetimob.v2.SearchPageMetadata;
import br.com.houseseeker.domain.property.UrbanPropertyContract;
import br.com.houseseeker.domain.provider.ProviderMetadata;
import br.com.houseseeker.domain.provider.ProviderParameters;
import br.com.houseseeker.domain.provider.ProviderScraperResponse;
import br.com.houseseeker.service.AbstractDataScraperService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;

import java.time.Clock;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DataScraperV2Service extends AbstractDataScraperService {

    private static final Map<String, UrbanPropertyContract> FILTER_OPTIONS_SEGMENTS = Map.of(
            "imoveis-plus-comprar", UrbanPropertyContract.SELL,
            "imoveis-plus-alugar", UrbanPropertyContract.RENT,
            "imoveis-urbanos-comprar", UrbanPropertyContract.SELL,
            "imoveis-urbanos-alugar", UrbanPropertyContract.RENT
    );

    private final ObjectMapper objectMapper;
    private final DataScraperSegmentFilterOptionsV2Service dataScraperSegmentFilterOptionsV2Service;
    private final DataScraperSegmentSearchV2Service dataScraperSegmentSearchV2Service;
    private final DataScraperSegmentPropertyV2Service dataScraperSegmentPropertyV2Service;
    private final PropertyMetadataMergeV2Service propertyMetadataMergeV2Service;
    private final MetadataTransferV2Service metadataTransferV2Service;

    public DataScraperV2Service(
            Clock clock,
            ObjectMapper objectMapper,
            DataScraperSegmentFilterOptionsV2Service dataScraperSegmentFilterOptionsV2Service,
            DataScraperSegmentSearchV2Service dataScraperSegmentSearchV2Service,
            DataScraperSegmentPropertyV2Service dataScraperSegmentPropertyV2Service,
            PropertyMetadataMergeV2Service propertyMetadataMergeV2Service,
            MetadataTransferV2Service metadataTransferV2Service
    ) {
        super(clock);
        this.objectMapper = objectMapper;
        this.dataScraperSegmentFilterOptionsV2Service = dataScraperSegmentFilterOptionsV2Service;
        this.dataScraperSegmentSearchV2Service = dataScraperSegmentSearchV2Service;
        this.dataScraperSegmentPropertyV2Service = dataScraperSegmentPropertyV2Service;
        this.propertyMetadataMergeV2Service = propertyMetadataMergeV2Service;
        this.metadataTransferV2Service = metadataTransferV2Service;
    }

    @Override
    protected ProviderScraperResponse execute(
            ProviderMetadata providerMetadata,
            ProviderParameters providerParameters,
            Retrofit retrofit
    ) {
        ScraperAnalysisProperties scraperAnalysisProperties = getAnalysisScope(providerParameters);
        Map<String, FilterOptionsMetadata> filterOptionsBySegmentMap = fetchSegmentsFilterOptions(providerMetadata, scraperAnalysisProperties, retrofit);
        Map<String, List<SearchPageMetadata.Item>> searchResultsBySegmentMap = fetchSegmentsSearchResults(providerMetadata, filterOptionsBySegmentMap);
        Map<String, List<PropertyInfoMetadata>> propertiesBySegmentMap = fetchSegmentsProperties(retrofit, searchResultsBySegmentMap);
        List<PropertyInfoMetadata> mergedProperties = propertyMetadataMergeV2Service.merge(
                propertiesBySegmentMap.values().stream().flatMap(Collection::stream).toList()
        );
        return generateResponse(providerMetadata, mergedProperties, metadataTransferV2Service::transfer);
    }

    private ScraperAnalysisProperties getAnalysisScope(ProviderParameters providerParameters) {
        return providerParameters.getPropertyAs(objectMapper, "analysisScope", ScraperAnalysisProperties.class)
                                 .orElse(ScraperAnalysisProperties.DEFAULT);
    }

    private Map<String, FilterOptionsMetadata> fetchSegmentsFilterOptions(
            ProviderMetadata providerMetadata,
            ScraperAnalysisProperties analysisProperties,
            Retrofit retrofit
    ) {
        log.info("Fetching filter options for segments ...");
        return FILTER_OPTIONS_SEGMENTS.entrySet()
                                      .parallelStream()
                                      .collect(Collectors.toMap(
                                              Map.Entry::getKey,
                                              segment -> dataScraperSegmentFilterOptionsV2Service.fetch(
                                                      retrofit,
                                                      providerMetadata,
                                                      analysisProperties,
                                                      segment.getKey(),
                                                      segment.getValue()
                                              )
                                      ));
    }

    private Map<String, List<SearchPageMetadata.Item>> fetchSegmentsSearchResults(
            ProviderMetadata providerMetadata,
            Map<String, FilterOptionsMetadata> filterOptionsBySegmentMap
    ) {
        log.info("Fetching search results for segments ...");
        return filterOptionsBySegmentMap.entrySet()
                                        .parallelStream()
                                        .collect(Collectors.toMap(
                                                Map.Entry::getKey,
                                                e -> dataScraperSegmentSearchV2Service.fetch(providerMetadata, e.getKey(), e.getValue())
                                        ));
    }

    private Map<String, List<PropertyInfoMetadata>> fetchSegmentsProperties(
            Retrofit retrofit,
            Map<String, List<SearchPageMetadata.Item>> searchResultsBySegmentMap
    ) {
        return searchResultsBySegmentMap.entrySet()
                                        .parallelStream()
                                        .collect(Collectors.toMap(
                                                Map.Entry::getKey,
                                                e -> dataScraperSegmentPropertyV2Service.fetch(
                                                        retrofit,
                                                        FILTER_OPTIONS_SEGMENTS.get(e.getKey()),
                                                        e.getValue()
                                                )
                                        ));
    }

}

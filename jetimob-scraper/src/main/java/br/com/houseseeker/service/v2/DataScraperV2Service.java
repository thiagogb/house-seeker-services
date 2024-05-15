package br.com.houseseeker.service.v2;

import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import br.com.houseseeker.domain.jetimob.v2.FilterOptionsMetadata;
import br.com.houseseeker.domain.jetimob.v2.PropertyInfoMetadata;
import br.com.houseseeker.domain.jetimob.v2.ScraperAnalysisProperties;
import br.com.houseseeker.domain.jetimob.v2.SearchPageMetadata;
import br.com.houseseeker.domain.property.UrbanPropertyContract;
import br.com.houseseeker.domain.provider.ProviderMetadata;
import br.com.houseseeker.domain.provider.ProviderParameters;
import br.com.houseseeker.domain.provider.ProviderScraperResponse;
import br.com.houseseeker.service.AbstractDataScraperService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Url;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static br.com.houseseeker.util.RetrofitUtils.executeCall;

@Service
@Slf4j
public class DataScraperV2Service extends AbstractDataScraperService {

    private static final List<Pair<String, UrbanPropertyContract>> FILTER_OPTIONS_SEGMENTS = List.of(
            Pair.of("imoveis-plus-comprar", UrbanPropertyContract.SELL),
            Pair.of("imoveis-plus-alugar", UrbanPropertyContract.RENT),
            Pair.of("imoveis-urbanos-comprar", UrbanPropertyContract.SELL),
            Pair.of("imoveis-urbanos-alugar", UrbanPropertyContract.RENT)
    );

    private final FilterOptionsV2ScraperService filterOptionsV2ScraperService;
    private final SearchPageV2ScraperService searchPageV2ScraperService;
    private final PropertyPageV2ScraperService propertyPageV2ScraperService;
    private final PropertyMetadataMergeV2Service propertyMetadataMergeV2Service;
    private final MetadataTransferV2Service metadataTransferV2Service;

    public DataScraperV2Service(
            Clock clock,
            FilterOptionsV2ScraperService filterOptionsV2ScraperService,
            SearchPageV2ScraperService searchPageV2ScraperService,
            PropertyPageV2ScraperService propertyPageV2ScraperService,
            PropertyMetadataMergeV2Service propertyMetadataMergeV2Service, MetadataTransferV2Service metadataTransferV2Service
    ) {
        super(clock);
        this.filterOptionsV2ScraperService = filterOptionsV2ScraperService;
        this.searchPageV2ScraperService = searchPageV2ScraperService;
        this.propertyPageV2ScraperService = propertyPageV2ScraperService;
        this.propertyMetadataMergeV2Service = propertyMetadataMergeV2Service;
        this.metadataTransferV2Service = metadataTransferV2Service;
    }

    @Override
    protected ProviderScraperResponse execute(
            ProviderMetadata providerMetadata,
            ProviderParameters providerParameters,
            Retrofit retrofit
    ) {
        Api api = retrofit.create(Api.class);
        ScraperAnalysisProperties scraperAnalysisProperties = providerParameters.getPropertyAs("analysisScope", ScraperAnalysisProperties.class)
                                                                                .orElse(ScraperAnalysisProperties.DEFAULT);
        Map<String, FilterOptionsMetadata> filterOptionsBySegmentMap = fetchSegmentsFilterOptions(api, providerMetadata, scraperAnalysisProperties);
        Map<String, List<PropertyInfoMetadata>> propertiesBySegmentMap = fetchSegmentsProperties(api, providerMetadata, filterOptionsBySegmentMap);
        List<PropertyInfoMetadata> mergedProperties = propertyMetadataMergeV2Service.merge(
                propertiesBySegmentMap.values().stream().flatMap(Collection::stream).toList()
        );
        return generateResponse(providerMetadata, mergedProperties, metadataTransferV2Service::transfer);
    }

    private Map<String, FilterOptionsMetadata> fetchSegmentsFilterOptions(
            Api api,
            ProviderMetadata providerMetadata,
            ScraperAnalysisProperties analysisProperties
    ) {
        log.info("Fetching filter options for segments ...");
        return FILTER_OPTIONS_SEGMENTS.parallelStream()
                                      .collect(Collectors.toMap(
                                              Pair::getKey,
                                              segment -> fetchSegmentFilterOptions(
                                                      api,
                                                      analysisProperties,
                                                      providerMetadata.getSiteUrl(),
                                                      segment.getKey(),
                                                      segment.getValue()
                                              )
                                      ));
    }

    private FilterOptionsMetadata fetchSegmentFilterOptions(
            Api api,
            ScraperAnalysisProperties analysisProperties,
            String siteUrl,
            String segment,
            UrbanPropertyContract contract
    ) {
        try {
            String url = new URIBuilder(siteUrl).appendPathSegments(segment).build().toString();
            try (var response = executeCall(api.getFilterOptions(url))) {
                FilterOptionsMetadata filterOptionsMetadata = filterOptionsV2ScraperService.scrap(response.string());
                return filterOptionsMetadata.setContract(contract)
                                            .setCities(analysisProperties.applyCitiesFilter(filterOptionsMetadata.getCities()))
                                            .setTypes(analysisProperties.applySubTypesFilter(filterOptionsMetadata.getTypes()));
            }
        } catch (IOException | URISyntaxException e) {
            throw new ExtendedRuntimeException(e, "Failed to fetch filter options for segment %s", segment);
        }
    }

    private Map<String, List<PropertyInfoMetadata>> fetchSegmentsProperties(
            Api api,
            ProviderMetadata providerMetadata,
            Map<String, FilterOptionsMetadata> segmentFilterOptions
    ) {
        log.info("Fetching segments properties ...");
        return segmentFilterOptions.entrySet()
                                   .parallelStream()
                                   .collect(Collectors.toMap(
                                           Map.Entry::getKey,
                                           e -> fetchSegmentProperties(api, providerMetadata.getSiteUrl(), e.getKey(), e.getValue())
                                   ));
    }

    private List<PropertyInfoMetadata> fetchSegmentProperties(
            Api api,
            String siteUrl,
            String segment,
            FilterOptionsMetadata filterOptions
    ) {
        return filterOptions.getCities()
                            .stream()
                            .flatMap(city -> fetchSegmentCityProperties(
                                    api,
                                    siteUrl,
                                    segment,
                                    filterOptions.getContract(),
                                    city,
                                    filterOptions.getTypes()
                            ))
                            .collect(Collectors.toList());
    }

    private Stream<PropertyInfoMetadata> fetchSegmentCityProperties(
            Api api,
            String siteUrl,
            String segment,
            UrbanPropertyContract contract,
            String city,
            List<String> subTypes
    ) {
        return fetchSegmentCitySearchResults(siteUrl, segment, city, subTypes)
                .parallelStream()
                .map(searchResultItem -> fetchSegmentCityPropertiesData(api, contract, searchResultItem));
    }

    private List<SearchPageMetadata.Item> fetchSegmentCitySearchResults(
            String siteUrl,
            String segment,
            String city,
            List<String> subTypes
    ) {
        return subTypes.stream()
                       .flatMap(st -> fetchSegmentCityTypeSearchResults(siteUrl, segment, city, st))
                       .toList();
    }

    private Stream<SearchPageMetadata.Item> fetchSegmentCityTypeSearchResults(String siteUrl, String segment, String city, String subType) {
        Stream.Builder<SearchPageMetadata.Item> results = Stream.builder();
        int page = 1;
        try {
            do {
                String url = new URIBuilder(siteUrl).appendPathSegments(segment)
                                                    .addParameter("city", city)
                                                    .addParameter("subtypes[]", subType)
                                                    .addParameter("sort_by", "price_highest")
                                                    .addParameter("page", String.valueOf(page))
                                                    .build()
                                                    .toString();
                url = URLDecoder.decode(url, StandardCharsets.UTF_8);

                log.info("Fetching page {} of segment /{}?city={}&subTypes[]={} ...", page, segment, city, subType);
                SearchPageMetadata searchPageMetadata = searchPageV2ScraperService.scrap(url);
                searchPageMetadata.getItems().forEach(i -> results.add(i.setSubType(subType)));

                if (searchPageMetadata.getPagination().isLastPage() || searchPageMetadata.getItems().isEmpty())
                    break;

                page++;
            } while (true);
        } catch (URISyntaxException e) {
            throw new ExtendedRuntimeException(e, "Failed to fetch filter options for segment %s", segment);
        }
        return results.build();
    }

    private PropertyInfoMetadata fetchSegmentCityPropertiesData(
            Api api,
            UrbanPropertyContract contract,
            SearchPageMetadata.Item searchResultItem
    ) {
        try {
            try (var response = executeCall(api.getPropertyData(searchResultItem.getPageLink()))) {
                return propertyPageV2ScraperService.scrap(response.string())
                                                   .setProviderCode(searchResultItem.getProviderCode())
                                                   .setUrl(searchResultItem.getPageLink())
                                                   .setContract(contract)
                                                   .setSubType(searchResultItem.getSubType());
            }
        } catch (IOException e) {
            throw new ExtendedRuntimeException(e, "Failed to fetch property %s", searchResultItem.getProviderCode());
        }
    }

    private interface Api {

        @GET
        Call<ResponseBody> getFilterOptions(@Url String url);

        @GET
        Call<ResponseBody> getPropertyData(@Url String url);

    }

}

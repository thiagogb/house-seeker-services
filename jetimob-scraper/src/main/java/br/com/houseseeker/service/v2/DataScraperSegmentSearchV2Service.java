package br.com.houseseeker.service.v2;

import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import br.com.houseseeker.domain.jetimob.v2.FilterOptionsMetadata;
import br.com.houseseeker.domain.jetimob.v2.SearchPageMetadata;
import br.com.houseseeker.domain.provider.ProviderMetadata;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataScraperSegmentSearchV2Service {

    private final SearchPageV2ScraperService searchPageV2ScraperService;

    public List<SearchPageMetadata.Item> fetch(
            @NotNull ProviderMetadata providerMetadata,
            @NotNull String segment,
            @NotNull FilterOptionsMetadata filterOptions
    ) {
        return filterOptions.getCities()
                            .stream()
                            .flatMap(city -> fetchSearchResultsByCity(
                                    providerMetadata.getBaseUrl(),
                                    segment,
                                    city,
                                    filterOptions.getTypes()
                            ))
                            .collect(Collectors.toList());
    }

    private Stream<SearchPageMetadata.Item> fetchSearchResultsByCity(
            String siteUrl,
            String segment,
            String city,
            List<String> subTypes
    ) {
        return subTypes.stream().flatMap(subType -> fetchSearchResultsByCityAndSubType(siteUrl, segment, city, subType));
    }

    private Stream<SearchPageMetadata.Item> fetchSearchResultsByCityAndSubType(String baseUrl, String segment, String city, String subType) {
        Stream.Builder<SearchPageMetadata.Item> results = Stream.builder();
        int page = 1;
        do {
            String url = buildRequestUrl(baseUrl, segment, city, subType, page);

            log.info("Fetching page {} of segment /{}?city={}&subTypes[]={} ...", page, segment, city, subType);
            SearchPageMetadata searchPageMetadata = searchPageV2ScraperService.scrap(url);
            searchPageMetadata.getItems().forEach(i -> results.add(i.setSubType(subType)));

            if (searchPageMetadata.getPagination().isLastPage() || searchPageMetadata.getItems().isEmpty())
                break;

            page++;
        } while (true);
        return results.build();
    }

    private String buildRequestUrl(String baseUrl, String segment, String city, String subType, int page) {
        try {
            String url = new URIBuilder(baseUrl).appendPathSegments(segment)
                                                .addParameter("city", city)
                                                .addParameter("subtypes[]", subType)
                                                .addParameter("sort_by", "price_highest")
                                                .addParameter("page", String.valueOf(page))
                                                .build()
                                                .toString();
            return URLDecoder.decode(url, StandardCharsets.UTF_8);
        } catch (URISyntaxException e) {
            throw new ExtendedRuntimeException(e, "Failed to fetch filter options for segment %s", segment);
        }
    }

}

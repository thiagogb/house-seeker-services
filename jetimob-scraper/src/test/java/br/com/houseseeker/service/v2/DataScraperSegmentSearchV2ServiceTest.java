package br.com.houseseeker.service.v2;

import br.com.houseseeker.AbstractMockWebServerTest;
import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import br.com.houseseeker.domain.jetimob.v2.FilterOptionsMetadata;
import br.com.houseseeker.domain.provider.ProviderMetadata;
import br.com.houseseeker.service.WebDriverFactoryService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static br.com.houseseeker.TestUtils.getTextFromResources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(
        classes = {
                WebDriverFactoryService.class,
                SearchPageV2ScraperService.class,
                DataScraperSegmentSearchV2Service.class
        },
        properties = {"webdriver.retry-count=1"}
)
class DataScraperSegmentSearchV2ServiceTest extends AbstractMockWebServerTest {

    private static final String DEFAULT_SEGMENT = "imoveis-urbanos-venda";
    private static final FilterOptionsMetadata DEFAULT_FILTER_OPTIONS = FilterOptionsMetadata.builder()
                                                                                             .cities(List.of("Santa Maria"))
                                                                                             .types(List.of("Casa"))
                                                                                             .build();
    private static final String SAMPLE_WITH_ITEMS = "samples/v2/search/with-items.html";
    private static final String SAMPLE_WITHOUT_ITEMS = "samples/v2/search/without-items.html";

    @Autowired
    private DataScraperSegmentSearchV2Service dataScraperSegmentSearchV2Service;

    private ProviderMetadata providerMetadata;

    @BeforeEach
    @Override
    public void setup() throws IOException {
        super.setup();

        providerMetadata = ProviderMetadata.builder().siteUrl(getBaseUrl()).build();
    }

    @Test
    @DisplayName("given empty cities on filter options when calls fetch then expects")
    void givenEmptyCitiesOnFilterOptions_whenCallsFetch_thenExpects() {
        var filterOptions = FilterOptionsMetadata.builder().build();

        assertThat(dataScraperSegmentSearchV2Service.fetch(providerMetadata, DEFAULT_SEGMENT, filterOptions)).isEmpty();

        assertRecordedRequests(RecordedRequest::getRequestUrl).isEmpty();
    }

    @Test
    @DisplayName("given empty subtypes on filter options when calls fetch then expects")
    void givenEmptySubTypesOnFilterOptions_whenCallsFetch_thenExpects() {
        var filterOptions = FilterOptionsMetadata.builder()
                                                 .cities(List.of("Santa Maria", "Porto Alegre"))
                                                 .build();

        assertThat(dataScraperSegmentSearchV2Service.fetch(providerMetadata, DEFAULT_SEGMENT, filterOptions)).isEmpty();

        assertRecordedRequests(RecordedRequest::getRequestUrl).isEmpty();
    }

    @Test
    @DisplayName("given a invalid url when calls fetch then expects exception")
    void givenAInvalidUrl_whenCallsFetch_thenExpectsException() {
        providerMetadata.setDataUrl("h_t_t_p://invalid");

        assertThatThrownBy(() -> dataScraperSegmentSearchV2Service.fetch(providerMetadata, DEFAULT_SEGMENT, DEFAULT_FILTER_OPTIONS))
                .isInstanceOf(ExtendedRuntimeException.class)
                .hasMessage("Failed to fetch filter options for segment imoveis-urbanos-venda");

        assertRecordedRequests(RecordedRequest::getRequestUrl).isEmpty();
    }

    @Test
    @DisplayName("given a search response when calls fetch then expects")
    void givenASearchResponse_whenCallsFetch_thenExpects() {
        whenDispatch(recordedRequest -> {
            if (requestHasPath(recordedRequest, "/imoveis-urbanos-venda?city=Santa%20Maria&subtypes[]=Casa&sort_by=price_highest&page=1"))
                return new MockResponse().setResponseCode(200).setBody(getTextFromResources(SAMPLE_WITH_ITEMS));

            if (requestHasPath(recordedRequest, "/imoveis-urbanos-venda?city=Santa%20Maria&subtypes[]=Casa&sort_by=price_highest&page=2"))
                return new MockResponse().setResponseCode(200).setBody(getTextFromResources(SAMPLE_WITHOUT_ITEMS));

            return new MockResponse().setResponseCode(404);
        });

        assertThat(dataScraperSegmentSearchV2Service.fetch(providerMetadata, DEFAULT_SEGMENT, DEFAULT_FILTER_OPTIONS))
                .hasSize(1);

        assertRecordedRequests(RecordedRequest::getPath)
                .hasSize(2)
                .containsExactly(
                        "/imoveis-urbanos-venda?city=Santa%20Maria&subtypes[]=Casa&sort_by=price_highest&page=1",
                        "/imoveis-urbanos-venda?city=Santa%20Maria&subtypes[]=Casa&sort_by=price_highest&page=2"
                );
    }

}
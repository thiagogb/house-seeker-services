package br.com.houseseeker.service.v4;

import br.com.houseseeker.AbstractMockWebServerTest;
import br.com.houseseeker.configuration.ObjectMapperConfiguration;
import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.domain.provider.ProviderMetadata;
import br.com.houseseeker.service.OkHttpClientFactoryService;
import br.com.houseseeker.service.RetrofitFactoryService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.Collections;

import static br.com.houseseeker.TestUtils.getTextFromResources;
import static br.com.houseseeker.mock.ProviderMetadataMocks.withUrlAndMechanism;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = {
                ObjectMapperConfiguration.class,
                OkHttpClientFactoryService.class,
                RetrofitFactoryService.class,
                MetadataTransferV4Service.class,
                DataScraperV4Service.class
        },
        properties = "data.scraper.v4.page-size=1"
)
class DataScraperV4ServiceTest extends AbstractMockWebServerTest {

    private static final String SAMPLE_EMPTY = "samples/v4/search/empty.json";
    private static final String SAMPLE_TWO_ITEMS_PAGE_1 = "samples/v4/search/with-two-items-page-1.json";
    private static final String SAMPLE_TWO_ITEMS_PAGE_2 = "samples/v4/search/with-two-items-page-2.json";
    private static final String SAMPLE_PROPERTY_2367172 = "samples/v4/property/2367172.json";
    private static final String SAMPLE_PROPERTY_2360531 = "samples/v4/property/2360531.json";

    @Autowired
    private RetrofitFactoryService retrofitFactoryService;

    @Autowired
    private DataScraperV4Service dataScraperV4Service;

    private ProviderMetadata providerMetadata;
    private Retrofit retrofit;

    @BeforeEach
    @Override
    public void setup() throws IOException {
        super.setup();

        providerMetadata = withUrlAndMechanism(getBaseUrl(), ProviderMechanism.JETIMOB_V4);
        retrofit = retrofitFactoryService.configure(providerMetadata, DEFAULT_PROVIDER_PARAMETERS);
    }

    @Test
    @DisplayName("given search page without items when calls scrap then expects response with empty extracted data")
    void givenSearchPageWithoutItems_whenCallsScrap_thenExpectsResponseWithEmptyExtractedData() {
        whenDispatch(recordedRequest -> new MockResponse().setBody(getTextFromResources(SAMPLE_EMPTY)));

        assertThat(dataScraperV4Service.scrap(providerMetadata, DEFAULT_PROVIDER_PARAMETERS, retrofit))
                .hasFieldOrPropertyWithValue("providerMetadata", providerMetadata)
                .hasFieldOrPropertyWithValue("errorInfo", null)
                .hasFieldOrPropertyWithValue("extractedData", Collections.emptyList());

        assertRecordedRequests(RecordedRequest::getPath)
                .hasSize(1)
                .containsExactly("/api/frontend/real-estate-data/property/list?chunkSize=1&offset=0");
    }

    @Test
    @DisplayName("given error on search page when calls scrap then expects response to have error info")
    void givenErrorOnSearchPage_whenCallsScrap_thenExpectsResponseToHaveErrorInfo() {
        whenDispatch(recordedRequest -> new MockResponse().setResponseCode(500).setBody("sample error message"));

        assertThat(dataScraperV4Service.scrap(providerMetadata, DEFAULT_PROVIDER_PARAMETERS, retrofit))
                .hasFieldOrPropertyWithValue("providerMetadata", providerMetadata)
                .hasFieldOrPropertyWithValue("extractedData", Collections.emptyList())
                .extracting("errorInfo.message", InstanceOfAssertFactories.STRING)
                .contains("500 INTERNAL_SERVER_ERROR \"Request failed with error: sample error message\"");

        assertRecordedRequests(RecordedRequest::getPath)
                .hasSize(1)
                .containsExactly("/api/frontend/real-estate-data/property/list?chunkSize=1&offset=0");
    }

    @Test
    @DisplayName("given search pages with one item on each page when calls scrap then expects response with two properties extracted")
    void givenSearchPagesWithOneItemOnEachPage_whenCallsScrap_thenExpectsResponseWithTwoPropertiesExtracted() {
        whenDispatch(recordedRequest -> {
            if (requestHasPath(recordedRequest, "/api/frontend/real-estate-data/property/list?chunkSize=1&offset=0"))
                return new MockResponse().setBody(getTextFromResources(SAMPLE_TWO_ITEMS_PAGE_1));

            if (requestHasPath(recordedRequest, "/api/frontend/real-estate-data/property/list?chunkSize=1&offset=1"))
                return new MockResponse().setBody(getTextFromResources(SAMPLE_TWO_ITEMS_PAGE_2));

            if (requestHasPath(recordedRequest, "/api/frontend/real-estate-data/property/2367172"))
                return new MockResponse().setBody(getTextFromResources(SAMPLE_PROPERTY_2367172));

            if (requestHasPath(recordedRequest, "/api/frontend/real-estate-data/property/2360531"))
                return new MockResponse().setBody(getTextFromResources(SAMPLE_PROPERTY_2360531));

            return new MockResponse().setResponseCode(404);
        });

        assertThat(dataScraperV4Service.scrap(providerMetadata, DEFAULT_PROVIDER_PARAMETERS, retrofit))
                .hasFieldOrPropertyWithValue("providerMetadata", providerMetadata)
                .hasFieldOrPropertyWithValue("errorInfo", null)
                .extracting("extractedData", InstanceOfAssertFactories.LIST)
                .hasSize(2);

        assertRecordedRequests(RecordedRequest::getPath)
                .hasSize(4)
                .containsExactlyInAnyOrder(
                        "/api/frontend/real-estate-data/property/list?chunkSize=1&offset=0",
                        "/api/frontend/real-estate-data/property/list?chunkSize=1&offset=1",
                        "/api/frontend/real-estate-data/property/2367172",
                        "/api/frontend/real-estate-data/property/2360531"
                );
    }

}
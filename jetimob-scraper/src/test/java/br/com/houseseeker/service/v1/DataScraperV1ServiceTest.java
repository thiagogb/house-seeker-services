package br.com.houseseeker.service.v1;

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

@SpringBootTest(classes = {
        ObjectMapperConfiguration.class,
        OkHttpClientFactoryService.class,
        RetrofitFactoryService.class,
        SearchPageV1ScraperService.class,
        PropertyPageV1ScraperService.class,
        MetadataTransferV1Service.class,
        DataScraperV1Service.class
})
class DataScraperV1ServiceTest extends AbstractMockWebServerTest {

    private static final String SAMPLE_WITHOUT_ITEMS = "samples/v1/search/without-items.html";
    private static final String SAMPLE_WITH_ONE_ITEM_PAGE_1 = "samples/v1/search/with-one-item-page-1.html";
    private static final String SAMPLE_WITH_ONE_ITEM_PAGE_2 = "samples/v1/search/with-one-item-page-2.html";
    private static final String SAMPLE_WITH_SINGLE_PAGE = "samples/v1/search/with-single-page.html";
    private static final String SAMPLE_FULL_PROPERTY_DATA = "samples/v1/property/with-full-data.html";

    @Autowired
    private RetrofitFactoryService retrofitFactoryService;

    @Autowired
    private DataScraperV1Service dataScraperV1Service;

    private ProviderMetadata providerMetadata;
    private Retrofit retrofit;

    @BeforeEach
    @Override
    public void setup() throws IOException {
        super.setup();

        providerMetadata = withUrlAndMechanism(getBaseUrl(), ProviderMechanism.JETIMOB_V1);
        retrofit = retrofitFactoryService.configure(providerMetadata, DEFAULT_PROVIDER_PARAMETERS);
    }

    @Test
    @DisplayName("given search page without items when calls scrap then expects response with empty extracted data")
    void givenSearchPageWithoutItems_whenCallsScrap_thenExpectsResponseWithEmptyExtractedData() {
        whenDispatch(recordedRequest -> new MockResponse().setBody(getTextFromResources(SAMPLE_WITHOUT_ITEMS)));

        assertThat(dataScraperV1Service.scrap(providerMetadata, DEFAULT_PROVIDER_PARAMETERS, retrofit))
                .hasFieldOrPropertyWithValue("providerMetadata", providerMetadata)
                .hasFieldOrPropertyWithValue("errorInfo", null)
                .hasFieldOrPropertyWithValue("extractedData", Collections.emptyList());

        assertRecordedRequests(RecordedRequest::getPath)
                .hasSizeBetween(1, 2)
                .containsExactlyInAnyOrder(
                        "/imoveis/a-venda?ordem=valor_max&pagina=1",
                        "/imoveis/alugar?ordem=valor_max&pagina=1"
                );
    }

    @Test
    @DisplayName("given error on search page when calls scrap then expects response to have error info")
    void givenErrorOnSearchPage_whenCallsScrap_thenExpectsResponseToHaveErrorInfo() {
        whenDispatch(recordedRequest -> new MockResponse().setResponseCode(500).setBody("sample error message"));

        assertThat(dataScraperV1Service.scrap(providerMetadata, DEFAULT_PROVIDER_PARAMETERS, retrofit))
                .hasFieldOrPropertyWithValue("providerMetadata", providerMetadata)
                .hasFieldOrPropertyWithValue("extractedData", Collections.emptyList())
                .extracting("errorInfo.message", InstanceOfAssertFactories.STRING)
                .contains("500 INTERNAL_SERVER_ERROR \"Request failed with error: sample error message\"");

        assertRecordedRequests(RecordedRequest::getPath)
                .hasSizeBetween(1, 2)
                .containsExactlyInAnyOrder(
                        "/imoveis/a-venda?ordem=valor_max&pagina=1",
                        "/imoveis/alugar?ordem=valor_max&pagina=1"
                );
    }

    @Test
    @DisplayName("given sell search pages with one item on each when calls scrap then expects response with not empty extracted data")
    void givenSellSearchPagesWithOneItemOnEachPage_whenCallsScrap_thenExpectsResponseWithNotEmptyExtractedData() {
        whenDispatch(recordedRequest -> {
            if (requestHasPath(recordedRequest, "/imoveis/a-venda?ordem=valor_max&pagina=1"))
                return new MockResponse().setBody(replaceMockServerBaseUrlPlaceHolder(getTextFromResources(SAMPLE_WITH_ONE_ITEM_PAGE_1)));

            if (requestHasPath(recordedRequest, "/imoveis/a-venda?ordem=valor_max&pagina=2"))
                return new MockResponse().setBody(replaceMockServerBaseUrlPlaceHolder(getTextFromResources(SAMPLE_WITH_ONE_ITEM_PAGE_2)));

            if (requestHasPath(recordedRequest, "/imoveis/alugar?ordem=valor_max&pagina=1"))
                return new MockResponse().setBody(getTextFromResources(SAMPLE_WITHOUT_ITEMS));

            if (requestHasPath(recordedRequest, "/property/1") || requestHasPath(recordedRequest, "/property/2"))
                return new MockResponse().setBody(getTextFromResources(SAMPLE_FULL_PROPERTY_DATA));

            return new MockResponse().setResponseCode(404);
        });

        assertThat(dataScraperV1Service.scrap(providerMetadata, DEFAULT_PROVIDER_PARAMETERS, retrofit))
                .hasFieldOrPropertyWithValue("providerMetadata", providerMetadata)
                .hasFieldOrPropertyWithValue("errorInfo", null)
                .extracting("extractedData", InstanceOfAssertFactories.LIST)
                .hasSize(2);

        assertRecordedRequests(RecordedRequest::getPath)
                .hasSize(5)
                .containsExactlyInAnyOrder(
                        "/imoveis/alugar?ordem=valor_max&pagina=1",
                        "/imoveis/a-venda?ordem=valor_max&pagina=1",
                        "/imoveis/a-venda?ordem=valor_max&pagina=2",
                        "/property/1",
                        "/property/2"
                );
    }

    @Test
    @DisplayName("given error on property page request when calls scrap then expects response to have error info")
    void givenSellSearchPagesWithOneItemOnEachPage_whenCallsScrap_thenExpectsResponseToHaveErrorInfo() {
        whenDispatch(recordedRequest -> {
            if (requestHasPath(recordedRequest, "/imoveis/a-venda?ordem=valor_max&pagina=1"))
                return new MockResponse().setBody(replaceMockServerBaseUrlPlaceHolder(getTextFromResources(SAMPLE_WITH_SINGLE_PAGE)));

            if (requestHasPath(recordedRequest, "/imoveis/alugar?ordem=valor_max&pagina=1"))
                return new MockResponse().setBody(getTextFromResources(SAMPLE_WITHOUT_ITEMS));

            return new MockResponse().setResponseCode(500).setBody("failed on property page request");
        });

        assertThat(dataScraperV1Service.scrap(providerMetadata, DEFAULT_PROVIDER_PARAMETERS, retrofit))
                .hasFieldOrPropertyWithValue("providerMetadata", providerMetadata)
                .hasFieldOrPropertyWithValue("extractedData", Collections.emptyList())
                .extracting("errorInfo.message", InstanceOfAssertFactories.STRING)
                .contains("500 INTERNAL_SERVER_ERROR \"Request failed with error: failed on property page request\"");

        assertRecordedRequests(RecordedRequest::getPath)
                .hasSize(3)
                .containsExactlyInAnyOrder(
                        "/imoveis/alugar?ordem=valor_max&pagina=1",
                        "/imoveis/a-venda?ordem=valor_max&pagina=1",
                        "/property/1"
                );
    }

}
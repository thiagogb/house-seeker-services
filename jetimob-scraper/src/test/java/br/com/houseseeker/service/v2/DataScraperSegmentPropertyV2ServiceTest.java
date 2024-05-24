package br.com.houseseeker.service.v2;

import br.com.houseseeker.AbstractMockWebServerTest;
import br.com.houseseeker.configuration.ObjectMapperConfiguration;
import br.com.houseseeker.domain.jetimob.v2.SearchPageMetadata;
import br.com.houseseeker.domain.property.UrbanPropertyContract;
import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.domain.provider.ProviderParameters;
import br.com.houseseeker.mock.ProviderMetadataMocks;
import br.com.houseseeker.service.OkHttpClientFactoryService;
import br.com.houseseeker.service.RetrofitFactoryService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static br.com.houseseeker.TestUtils.getTextFromResources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = {
        ObjectMapperConfiguration.class,
        OkHttpClientFactoryService.class,
        RetrofitFactoryService.class,
        PropertyPageV2ScraperService.class,
        DataScraperSegmentPropertyV2Service.class
})
class DataScraperSegmentPropertyV2ServiceTest extends AbstractMockWebServerTest {

    private static final ProviderParameters PARAMETER_NO_RETRY = ProviderParameters.builder()
                                                                                   .connection(
                                                                                           ProviderParameters.Connection.builder()
                                                                                                                        .retryCount(0)
                                                                                                                        .build()
                                                                                   )
                                                                                   .build();
    private static final UrbanPropertyContract DEFAULT_CONTRACT = UrbanPropertyContract.SELL;
    private static final String SAMPLE_WITH_FULL_DATA = "samples/v2/property/with-full-data.html";

    @Autowired
    private DataScraperSegmentPropertyV2Service dataScraperSegmentPropertyV2Service;

    @Autowired
    private RetrofitFactoryService retrofitFactoryService;

    private Retrofit retrofit;

    @BeforeEach
    @Override
    public void setup() throws IOException {
        super.setup();

        retrofit = retrofitFactoryService.configure(ProviderMetadataMocks.withMechanism(ProviderMechanism.JETIMOB_V1), PARAMETER_NO_RETRY);
    }

    @Test
    @DisplayName("given a empty search items when calls fetch then expects")
    void givenAEmptySearchItems_whenCallsFetch_thenExpects() {
        assertThat(dataScraperSegmentPropertyV2Service.fetch(retrofit, DEFAULT_CONTRACT, Collections.emptyList())).isEmpty();

        assertRecordedRequests(RecordedRequest::getRequestUrl).isEmpty();
    }

    @Test
    @DisplayName("given a response error when calls fetch then expects exception")
    void givenAResponseError_whenCallsFetch_thenExpectsException() {
        var searchItems = List.of(
                SearchPageMetadata.Item.builder()
                                       .pageLink(getBaseUrl())
                                       .build()
        );

        whenDispatch(recordedRequest -> new MockResponse()
                .setResponseCode(500)
                .setBody("Server error")
        );

        assertThatThrownBy(() -> dataScraperSegmentPropertyV2Service.fetch(retrofit, DEFAULT_CONTRACT, searchItems))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("500 INTERNAL_SERVER_ERROR \"Request failed with error: Server error\"");

        assertRecordedRequests(RecordedRequest::getRequestUrl).hasSize(1);
    }

    @Test
    @DisplayName("given a property response when calls fetch then expects")
    void givenAPropertyResponse_whenCallsFetch_thenExpects() {
        var searchItems = List.of(
                SearchPageMetadata.Item.builder()
                                       .pageLink(getBaseUrl())
                                       .build()
        );

        whenDispatch(recordedRequest -> new MockResponse()
                .setResponseCode(200)
                .setBody(getTextFromResources(SAMPLE_WITH_FULL_DATA))
        );

        assertThat(dataScraperSegmentPropertyV2Service.fetch(retrofit, DEFAULT_CONTRACT, searchItems)).hasSize(1);

        assertRecordedRequests(RecordedRequest::getRequestUrl).hasSize(1);
    }

}
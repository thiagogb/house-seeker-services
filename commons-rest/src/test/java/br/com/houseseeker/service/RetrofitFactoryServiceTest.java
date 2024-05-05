package br.com.houseseeker.service;

import br.com.houseseeker.AbstractMockWebServerTest;
import br.com.houseseeker.domain.provider.ProviderMetadata;
import br.com.houseseeker.domain.provider.ProviderParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

import java.io.IOException;
import java.util.List;

import static br.com.houseseeker.TestUtils.getTextFromResources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = {
        ObjectMapper.class,
        OkHttpClientFactoryService.class,
        RetrofitFactoryService.class
})
class RetrofitFactoryServiceTest extends AbstractMockWebServerTest {

    private static final String SAMPLE_RESPONSE = "responses/sample-response.json";

    @Autowired
    private RetrofitFactoryService retrofitFactoryService;

    @Test
    @DisplayName("given a provider with invalid url when calls configure then expects exception")
    void givenAProviderWithInvalidUrl_whenCallsConfigure_thenExpectsException() {
        ProviderMetadata providerMetadata = ProviderMetadata.builder().siteUrl("<invalid-url>").build();

        assertThatThrownBy(() -> retrofitFactoryService.configure(providerMetadata, ProviderParameters.DEFAULT))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Expected URL scheme 'http' or 'https' but no scheme was found");
    }

    @Test
    @DisplayName("given a provider with url not normalized when calls configure then expects url with slash at the end")
    void givenAProviderWithUrlNotNormalized_whenCallsConfigure_thenExpectsUrlWithSlashAtTheEnd() {
        ProviderMetadata providerMetadata = ProviderMetadata.builder().siteUrl("http://localhost").build();

        assertThat(retrofitFactoryService.configure(providerMetadata, ProviderParameters.DEFAULT))
                .extracting("baseUrl.url", InstanceOfAssertFactories.STRING)
                .isEqualTo("http://localhost/");
    }

    @Test
    @DisplayName("given a provider with siteUrl and dataUrl when calls configure then expects to use dataUrl")
    void givenAProviderWithSiteUrlAndDataUrl_whenCallsConfigure_thenExpectsToUseDataUrl() {
        ProviderMetadata providerMetadata = ProviderMetadata.builder()
                                                            .siteUrl("http://localhost")
                                                            .dataUrl("http://localhost/api")
                                                            .build();

        assertThat(retrofitFactoryService.configure(providerMetadata, ProviderParameters.DEFAULT))
                .extracting("baseUrl.url", InstanceOfAssertFactories.STRING)
                .isEqualTo("http://localhost/api/");
    }

    @Test
    @DisplayName("given a valid provider when calls configure then expects to validate created adapter with a http request")
    void givenAValidProvider_whenCallsConfigure_thenExpectsToValidateCreatedAdapterWithAHttpRequest() throws IOException {
        ProviderMetadata providerMetadata = ProviderMetadata.builder().siteUrl("http://localhost").build();

        whenDispatch(recordedRequest -> new MockResponse().setBody(getTextFromResources(SAMPLE_RESPONSE)));

        assertThat(
                retrofitFactoryService.configure(providerMetadata, ProviderParameters.DEFAULT)
                                      .create(TestApi.class)
                                      .getList(getBaseUrl())
                                      .execute()
                                      .body()
        ).containsExactly("value1", "value2", "value3");
    }

    private interface TestApi {

        @GET
        Call<List<String>> getList(@Url String url);

    }

}
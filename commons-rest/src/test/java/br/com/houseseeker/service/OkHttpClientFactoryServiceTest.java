package br.com.houseseeker.service;

import br.com.houseseeker.AbstractMockWebServerTest;
import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.domain.provider.ProviderParameters;
import br.com.houseseeker.domain.provider.ProviderParameters.Connection;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static br.com.houseseeker.mock.ProviderMetadataMocks.withMechanism;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = OkHttpClientFactoryService.class)
class OkHttpClientFactoryServiceTest extends AbstractMockWebServerTest {

    @Autowired
    private OkHttpClientFactoryService okHttpClientFactoryService;

    @Test
    @DisplayName("given provider parameters when calls configure then expects to create client using parameters")
    void givenProviderParameters_whenCallsConfigure_thenExpectsToCreateClientUsingParameters() {
        ProviderParameters providerParameters = ProviderParameters.builder()
                                                                  .connection(
                                                                          Connection.builder()
                                                                                    .connectionTimeout(5)
                                                                                    .readTimeout(7)
                                                                                    .logLevels(List.of(
                                                                                            HttpLoggingInterceptor.Level.HEADERS,
                                                                                            HttpLoggingInterceptor.Level.BODY
                                                                                    ))
                                                                                    .retryCount(1)
                                                                                    .retryCount(1000)
                                                                                    .build()
                                                                  )
                                                                  .build();

        assertThat(okHttpClientFactoryService.configure(withMechanism(ProviderMechanism.JETIMOB_V1), providerParameters))
                .satisfies(okHttpClient -> assertThat(okHttpClient.interceptors())
                        .filteredOn(i -> i instanceof HttpLoggingInterceptor)
                        .extracting(i -> ((HttpLoggingInterceptor) i).getLevel())
                        .containsExactly(
                                HttpLoggingInterceptor.Level.HEADERS,
                                HttpLoggingInterceptor.Level.BODY
                        )
                )
                .extracting("connectTimeoutMillis", "readTimeoutMillis")
                .containsExactly(5000, 7000);
    }

    @Test
    @DisplayName("given provider parameters with retry parameters when calls configure then expects to create client and test retry interceptor")
    void givenProviderParametersWithRetryParameters_whenCallsConfigure_thenExpectsToCreateClientAndTestRetryInterceptor() throws IOException {
        ProviderParameters providerParameters = ProviderParameters.builder()
                                                                  .connection(
                                                                          Connection.builder()
                                                                                    .retryCount(2)
                                                                                    .retryWait(1000)
                                                                                    .build()
                                                                  )
                                                                  .build();

        AtomicInteger requestsMade = new AtomicInteger(0);
        whenDispatch(recordedRequest -> {
            if (requestsMade.getAndIncrement() < providerParameters.getConnection().getRetryCount())
                return new MockResponse().setResponseCode(500);

            return new MockResponse().setResponseCode(200);
        });

        OkHttpClient okHttpClient = okHttpClientFactoryService.configure(withMechanism(ProviderMechanism.JETIMOB_V1), providerParameters);

        Request request = new Request.Builder()
                .url(getBaseUrl())
                .method(HttpMethod.GET.name(), null)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            assertThat(response.isSuccessful()).isTrue();
            assertThat(requestsMade).hasValue(3);
        }
    }

}
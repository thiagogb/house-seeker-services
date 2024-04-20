package br.com.houseseeker;

import br.com.houseseeker.domain.provider.ProviderParameters;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.assertj.core.api.ListAssert;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractMockWebServerTest {

    protected static final ProviderParameters.Connection DEFAULT_CONNECTION = ProviderParameters.Connection.builder()
                                                                                                           .retryCount(0)
                                                                                                           .logLevels(List.of(HttpLoggingInterceptor.Level.BODY))
                                                                                                           .build();

    protected static final ProviderParameters DEFAULT_PROVIDER_PARAMETERS = ProviderParameters.builder()
                                                                                              .connection(DEFAULT_CONNECTION)
                                                                                              .build();

    private static final String MOCK_SERVER_BASE_URL_PLACEHOLDER = "%mockServerBaseUrl%";

    private MockWebServer mockWebServer;
    private final List<RecordedRequest> recordedRequests = new LinkedList<>();

    @BeforeEach
    public void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterEach
    public void finish() throws IOException {
        mockWebServer.shutdown();
        recordedRequests.clear();
    }

    protected final String getBaseUrl() {
        return String.format("http://%s:%s", mockWebServer.getHostName(), mockWebServer.getPort());
    }

    protected final void whenDispatch(Function<RecordedRequest, MockResponse> recordedRequestResponseCallback) {
        mockWebServer.setDispatcher(new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) {
                registerRequest(recordedRequest);
                return recordedRequestResponseCallback.apply(recordedRequest);
            }
        });
    }

    protected final boolean requestHasPath(@NotNull RecordedRequest recordedRequest, @NotNull String path) {
        return Optional.ofNullable(recordedRequest.getPath())
                       .map(p -> p.equals(path))
                       .orElse(false);
    }

    protected final String replaceMockServerBaseUrlPlaceHolder(@NotNull String content) {
        return content.replaceAll(MOCK_SERVER_BASE_URL_PLACEHOLDER, getBaseUrl());
    }

    protected final <T> ListAssert<T> assertRecordedRequests(@NotNull Function<RecordedRequest, T> extractor) {
        return assertThat(recordedRequests.stream().map(extractor));
    }

    private synchronized void registerRequest(RecordedRequest recordedRequest) {
        recordedRequests.add(recordedRequest);
    }

}

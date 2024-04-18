package br.com.houseseeker;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public abstract class AbstractMockWebServerTest {

    private static final String MOCK_SERVER_BASE_URL_PLACEHOLDER = "%mockServerBaseUrl%";

    private MockWebServer mockWebServer;

    @BeforeEach
    public void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterEach
    public void finish() throws IOException {
        mockWebServer.shutdown();
    }

    protected final String getBaseUrl() {
        return String.format("http://%s:%s", mockWebServer.getHostName(), mockWebServer.getPort());
    }

    protected final void whenDispatch(Function<RecordedRequest, MockResponse> recordedRequestResponseCallback) {
        mockWebServer.setDispatcher(new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) {
                return recordedRequestResponseCallback.apply(recordedRequest);
            }
        });
    }

    protected final boolean requestHasSegments(@NotNull RecordedRequest recordedRequest, @NotNull String segments) {
        return Optional.ofNullable(recordedRequest.getRequestUrl())
                       .map(url -> url.encodedPath().equals(segments))
                       .orElse(false);
    }

    protected final String getRequestQueryParameter(@NotNull RecordedRequest recordedRequest, @NotNull String name) {
        return Optional.ofNullable(recordedRequest.getRequestUrl())
                       .map(url -> url.queryParameter(name))
                       .orElse(EMPTY);
    }

    protected final String replaceMockServerBaseUrlPlaceHolder(@NotNull String content) {
        return content.replaceAll(MOCK_SERVER_BASE_URL_PLACEHOLDER, getBaseUrl());
    }

}

package br.com.houseseeker.domain.provider;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import okhttp3.logging.HttpLoggingInterceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@Builder
@ToString
public class ProviderParameters {

    public static final ProviderParameters DEFAULT = ProviderParameters.builder().build();

    private static final int DEFAULT_CONNECTION_TIMEOUT = 30;
    private static final int DEFAULT_READ_TIMEOUT = 60;
    private static final List<HttpLoggingInterceptor.Level> DEFAULT_LOG_LEVELS = List.of(HttpLoggingInterceptor.Level.BASIC);
    private static final int DEFAULT_RETRY_COUNT = 3;
    private static final int DEFAULT_RETRY_WAIT = 5000;

    @Builder.Default
    private Connection connection = new Connection();
    @Builder.Default
    private Map<String, ?> properties = new HashMap<>();

    @JsonCreator
    public ProviderParameters(
            @JsonProperty("connection") Connection connection,
            @JsonProperty("properties") Map<String, ?> properties
    ) {
        this.connection = connection;
        this.properties = properties;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static final class Connection {

        private Integer connectionTimeout;
        private Integer readTimeout;
        private List<HttpLoggingInterceptor.Level> logLevels;
        private Integer retryCount;
        private Integer retryWait;

        public Integer getConnectionTimeout() {
            return Optional.ofNullable(connectionTimeout).orElse(DEFAULT_CONNECTION_TIMEOUT);
        }

        public Integer getReadTimeout() {
            return Optional.ofNullable(readTimeout).orElse(DEFAULT_READ_TIMEOUT);
        }

        public List<HttpLoggingInterceptor.Level> getLogLevels() {
            return Optional.ofNullable(logLevels).orElse(DEFAULT_LOG_LEVELS);
        }

        public Integer getRetryCount() {
            return Optional.ofNullable(retryCount).orElse(DEFAULT_RETRY_COUNT);
        }

        public Integer getRetryWait() {
            return Optional.ofNullable(retryWait).orElse(DEFAULT_RETRY_WAIT);
        }
    }

}

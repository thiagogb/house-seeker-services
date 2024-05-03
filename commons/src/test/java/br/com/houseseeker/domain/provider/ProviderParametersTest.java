package br.com.houseseeker.domain.provider;

import br.com.houseseeker.configuration.ObjectMapperConfiguration;
import br.com.houseseeker.util.ObjectMapperUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ObjectMapperConfiguration.class)
class ProviderParametersTest {

    private static final String[] EXTRACTED_ATTRIBUTES = new String[]{
            "connection.connectionTimeout",
            "connection.readTimeout",
            "connection.logLevels",
            "connection.retryCount",
            "connection.retryWait",
            "properties"
    };

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("given a json object without connection and properties attributes when deserialize then expects")
    void givenAJsonObjectWithoutConnectionAndPropertiesAttributes_whenDeserialize_thenExpects() {
        String json = "{}";

        assertThat(ObjectMapperUtils.deserializeAs(objectMapper, json, ProviderParameters.class))
                .extracting(EXTRACTED_ATTRIBUTES)
                .containsExactly(30, 60, List.of(HttpLoggingInterceptor.Level.BASIC), 3, 5000, Collections.emptyMap());
    }

    @Test
    @DisplayName("given a json object with only connection attribute when deserialize then expects")
    void givenAJsonObjectWithOnlyConnectionAttribute_whenDeserialize_thenExpects() {
        String json = """
                {
                  "connection": {
                    "connectionTimeout": 60,
                    "readTimeout": 120,
                    "logLevels": [
                      "BODY"
                    ],
                    "retryCount": 5,
                    "retryWait": 10000
                  }
                }
                """;

        assertThat(ObjectMapperUtils.deserializeAs(objectMapper, json, ProviderParameters.class))
                .extracting(EXTRACTED_ATTRIBUTES)
                .containsExactly(60, 120, List.of(HttpLoggingInterceptor.Level.BODY), 5, 10000, Collections.emptyMap());
    }

    @Test
    @DisplayName("given a json object with only properties attribute when deserialize then expects")
    void givenAJsonObjectWithOnlPropertiesAttribute_whenDeserialize_thenExpects() {
        String json = """
                {
                  "properties": {
                    "prop1": "value",
                    "prop2": 1,
                    "prop3": true
                  }
                }
                """;

        assertThat(ObjectMapperUtils.deserializeAs(objectMapper, json, ProviderParameters.class))
                .extracting(EXTRACTED_ATTRIBUTES)
                .containsExactly(
                        30,
                        60,
                        List.of(HttpLoggingInterceptor.Level.BASIC),
                        3,
                        5000,
                        Map.of("prop1", "value", "prop2", 1, "prop3", true)
                );
    }

}
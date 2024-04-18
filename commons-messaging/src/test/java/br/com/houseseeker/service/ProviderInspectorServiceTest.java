package br.com.houseseeker.service;

import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import br.com.houseseeker.domain.provider.ProviderMetadata;
import br.com.houseseeker.domain.provider.ProviderParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static br.com.houseseeker.TestUtils.getTextFromResources;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = {
        ObjectMapper.class,
        ProviderInspectorService.class
})
class ProviderInspectorServiceTest {

    private static final String SAMPLE_PROVIDER_PARAMS = "samples/provider-params.json";

    @Autowired
    private ProviderInspectorService providerInspectorService;

    @ParameterizedTest
    @MethodSource("providerMetadataSamples")
    @DisplayName("given a provider with null or empty params when calls getParameters then expects default provider params")
    void givenAProviderWithNullOrEmptyParams_whenCallsGetParameters_thenExpectsDefaultProviderParams(ProviderMetadata providerMetadata) {
        assertThat(providerInspectorService.getParameters(providerMetadata)).isEqualTo(ProviderParameters.DEFAULT);
    }

    @Test
    @DisplayName("given a provider with valid params when calls getParameters then expects")
    void givenAProviderWithValidParams_whenCallsGetParameters_thenExpects() {
        ProviderMetadata providerMetadata = ProviderMetadata.builder().params(getTextFromResources(SAMPLE_PROVIDER_PARAMS)).build();
        assertThat(providerInspectorService.getParameters(providerMetadata))
                .extracting(
                        "connection.connectionTimeout",
                        "connection.readTimeout",
                        "connection.logLevels",
                        "connection.retryCount",
                        "connection.retryWait",
                        "properties.key1",
                        "properties.key2"
                )
                .containsExactly(5, 15, List.of(HttpLoggingInterceptor.Level.BODY), 2, 3000, "value1", "value2");
    }

    @Test
    @DisplayName("given a provider with invalid params when calls getParameters then expects exception")
    void givenAProviderWithInvalidParams_whenCallsGetParameters_thenExpectException() {
        ProviderMetadata providerMetadata = ProviderMetadata.builder().params("{invalid}").build();
        assertThatThrownBy(() -> providerInspectorService.getParameters(providerMetadata))
                .isInstanceOf(ExtendedRuntimeException.class)
                .hasMessage("Content read failed");
    }

    private static Stream<Arguments> providerMetadataSamples() {
        return Stream.of(
                Arguments.of(ProviderMetadata.builder().params(null).build()),
                Arguments.of(ProviderMetadata.builder().params(EMPTY).build())
        );
    }

}
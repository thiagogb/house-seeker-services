package br.com.houseseeker.domain.property;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;

class UrbanPropertyTypeTest {

    @ParameterizedTest
    @MethodSource("valueSamples")
    @DisplayName("given a string value when calls detect then expect")
    void givenAStringValue_whenCallsDetect_thenExpects(String input, UrbanPropertyType output) {
        assertThat(UrbanPropertyType.detect(input)).isEqualTo(Optional.ofNullable(output));
    }

    private static Stream<Arguments> valueSamples() {
        return Stream.of(
                Arguments.of(EMPTY, null),
                Arguments.of("Casa", UrbanPropertyType.RESIDENTIAL),
                Arguments.of("Apartamento", UrbanPropertyType.RESIDENTIAL),
                Arguments.of("Casa comercial", UrbanPropertyType.COMMERCIAL),
                Arguments.of("Sala comercial", UrbanPropertyType.COMMERCIAL),
                Arguments.of("Terreno", UrbanPropertyType.RESIDENTIAL)
        );
    }

}
package br.com.houseseeker.entity.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.assertj.core.api.Assertions.assertThat;

class BooleanToVarCharConverterTest {

    private final BooleanToVarCharConverter booleanToVarCharConverter = new BooleanToVarCharConverter();

    @ParameterizedTest
    @MethodSource("booleanSamples")
    @DisplayName("given a value when calls convertToDatabaseColumn then expects")
    void givenAValue_whenCallsConvertToDatabaseColumn_thenConvertsToDatabaseColumn(Boolean input, String output) {
        assertThat(booleanToVarCharConverter.convertToDatabaseColumn(input)).isEqualTo(output);
    }

    @ParameterizedTest
    @MethodSource("stringSamples")
    @DisplayName("given a value when calls convertToEntityAttribute then expects")
    void givenAValue_whenCallsConvertToEntityAttribute_thenConvertsToEntityAttribute(String input, Boolean output) {
        assertThat(booleanToVarCharConverter.convertToEntityAttribute(input)).isEqualTo(output);
    }

    private static Stream<Arguments> booleanSamples() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(Boolean.TRUE, "Y"),
                Arguments.of(Boolean.FALSE, "N")
        );
    }

    private static Stream<Arguments> stringSamples() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(EMPTY, null),
                Arguments.of(SPACE, null),
                Arguments.of("Y", Boolean.TRUE),
                Arguments.of("N", Boolean.FALSE),
                Arguments.of("<unknown>", Boolean.FALSE)
        );
    }

}
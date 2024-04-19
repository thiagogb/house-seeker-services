package br.com.houseseeker.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;

class ConverterUtilsTest {

    @ParameterizedTest
    @MethodSource("integerCandidateSamples")
    @DisplayName("given string as integer candidate when calls tryToInteger then expects")
    void givenStringAsIntegerCandidate_whenCallsTryToInteger_thenExpects(String input, Integer output) {
        assertThat(ConverterUtils.tryToInteger(input)).isEqualTo(Optional.ofNullable(output));
    }

    @ParameterizedTest
    @MethodSource("bigDecimalPtBrCandidateSamples")
    @DisplayName("given string as bigDecimal candidate in ptBr format when calls tryToBigDecimalPtBR then expects")
    void givenStringAsBigDecimalCandidateInPtBrFormat_whenCallsTryToBigDecimalPtBR_thenExpects(String input, BigDecimal output) {
        assertThat(ConverterUtils.tryToBigDecimalPtBR(input)).isEqualTo(Optional.ofNullable(output));
    }

    @ParameterizedTest
    @MethodSource("bigDecimalEnUsCandidateSamples")
    @DisplayName("given string as bigDecimal candidate in enUs format when calls tryToBigDecimalEnUs then expects")
    void givenStringAsBigDecimalCandidateInEnUsFormat_whenCallsTryToBigDecimalEnUs_thenExpects(String input, BigDecimal output) {
        assertThat(ConverterUtils.tryToBigDecimalEnUs(input)).isEqualTo(Optional.ofNullable(output));
    }

    private static Stream<Arguments> integerCandidateSamples() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(EMPTY, null),
                Arguments.of("abc", null),
                Arguments.of("1a2b", null),
                Arguments.of("123", 123),
                Arguments.of("1 2 3", null),
                Arguments.of(" 456 ", 456),
                Arguments.of("100.0", null)
        );
    }

    private static Stream<Arguments> bigDecimalPtBrCandidateSamples() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(EMPTY, null),
                Arguments.of("abc", null),
                Arguments.of("123", BigDecimal.valueOf(123.0)),
                Arguments.of("123.456,00", BigDecimal.valueOf(123456.0)),
                Arguments.of("123456,78", BigDecimal.valueOf(123456.78)),
                Arguments.of("+123456,78", null),
                Arguments.of("-123456,78", BigDecimal.valueOf(-123456.78)),
                Arguments.of("123.456.789,01", BigDecimal.valueOf(123456789.01)),
                Arguments.of("123,456,789.01", BigDecimal.valueOf(123.456))
        );
    }

    private static Stream<Arguments> bigDecimalEnUsCandidateSamples() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(EMPTY, null),
                Arguments.of("abc", null),
                Arguments.of("123", BigDecimal.valueOf(123.0)),
                Arguments.of("123,456.00", BigDecimal.valueOf(123456.0)),
                Arguments.of("123456.78", BigDecimal.valueOf(123456.78)),
                Arguments.of("+123456.78", null),
                Arguments.of("-123456.78", BigDecimal.valueOf(-123456.78)),
                Arguments.of("123,456,789.01", BigDecimal.valueOf(123456789.01)),
                Arguments.of("123.456.789,01", BigDecimal.valueOf(123.456))
        );
    }

}
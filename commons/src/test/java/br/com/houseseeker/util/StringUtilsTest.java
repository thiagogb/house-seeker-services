package br.com.houseseeker.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.assertj.core.api.Assertions.assertThat;

class StringUtilsTest {

    @ParameterizedTest
    @MethodSource("stringSamples")
    @DisplayName("given a string value when calls getNonBlank then expects")
    void givenAStringValue_whenCallsGetNonBlank_thenReturnsNonBlank(String input, String output) {
        assertThat(StringUtils.getNonBlank(input)).isEqualTo(Optional.ofNullable(output));
    }

    @ParameterizedTest
    @MethodSource("numericStringCandidateSamples")
    @DisplayName("given a numeric string candidate when calls keepOnlyNumericSymbols then expects")
    void givenANumericStringCandidate_whenCallsKeepOnlyNumericSymbols_thenExpects(String input, String output) {
        assertThat(StringUtils.keepOnlyNumericSymbols(input)).isEqualTo(output);
    }

    private static Stream<Arguments> stringSamples() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(EMPTY, null),
                Arguments.of(SPACE, null),
                Arguments.of("value", "value")
        );
    }

    private static Stream<Arguments> numericStringCandidateSamples() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(EMPTY, EMPTY),
                Arguments.of(SPACE, SPACE),
                Arguments.of("_1_", "1"),
                Arguments.of("_1&2@3", "123"),
                Arguments.of(" 3 2 1 ", "321"),
                Arguments.of(" 3 2, 1 ", "32,1"),
                Arguments.of("+  987.654,321 ", "+987.654,321"),
                Arguments.of("-987.654,321", "-987.654,321")
        );
    }

}
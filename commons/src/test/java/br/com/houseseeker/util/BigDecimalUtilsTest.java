package br.com.houseseeker.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static br.com.houseseeker.util.BigDecimalUtils.ONE_HUNDRED;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;

class BigDecimalUtilsTest {

    @ParameterizedTest
    @MethodSource("operatorAndDivisorSamples")
    @DisplayName("given a operator and a divisor when calls divideAndRoundByTwo then expects")
    void givenAOperatorAndADivisor_whenCallsDivideAndRoundByTwo_thenExpects(BigDecimal operator, BigDecimal divisor, BigDecimal expected) {
        assertThat(BigDecimalUtils.divideAndRoundByTwo(operator, divisor)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("zeroCandidatesSamples")
    @DisplayName("given a number when calls isZero then expects")
    void givenANumber_whenCallsIsZero_thenExpects(BigDecimal number, Boolean expected) {
        assertThat(BigDecimalUtils.isZero(number)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("equalityCandidateSamples")
    @DisplayName("given two numbers when calls isEqual then expects")
    void givenTwoNumbers_whenIsEqual_thenExpects(BigDecimal first, BigDecimal second, Boolean expected) {
        assertThat(BigDecimalUtils.isEqual(first, second)).isEqualTo(expected);
    }

    private static Stream<Arguments> operatorAndDivisorSamples() {
        return Stream.of(
                Arguments.of(ZERO, ONE_HUNDRED, new BigDecimal("0.00")),
                Arguments.of(ONE_HUNDRED, ONE_HUNDRED, new BigDecimal("1.00")),
                Arguments.of(BigDecimal.valueOf(10000), ONE_HUNDRED, new BigDecimal("100.00")),
                Arguments.of(BigDecimal.valueOf(123987), ONE_HUNDRED, BigDecimal.valueOf(1239.87)),
                Arguments.of(BigDecimal.valueOf(500), BigDecimal.TWO, new BigDecimal("250.00")),
                Arguments.of(BigDecimal.valueOf(3333.33), BigDecimal.TWO, BigDecimal.valueOf(1666.67))
        );
    }

    private static Stream<Arguments> zeroCandidatesSamples() {
        return Stream.of(
                Arguments.of(ZERO, true),
                Arguments.of(BigDecimal.valueOf(0.01), false),
                Arguments.of(BigDecimal.valueOf(-0.01), false)
        );
    }

    private static Stream<Arguments> equalityCandidateSamples() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(0.1), new BigDecimal("0.10"), true),
                Arguments.of(BigDecimal.valueOf(0.25), new BigDecimal("0.2500"), true),
                Arguments.of(BigDecimal.valueOf(100), BigDecimal.valueOf(100.0), true),
                Arguments.of(BigDecimal.valueOf(99), ONE_HUNDRED, false)
        );
    }

}
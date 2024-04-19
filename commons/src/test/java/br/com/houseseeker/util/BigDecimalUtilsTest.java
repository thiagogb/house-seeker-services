package br.com.houseseeker.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class BigDecimalUtilsTest {

    @ParameterizedTest
    @MethodSource("numberWithoutDecimalPartSample")
    @DisplayName("given a number without decimal part when calls divideBy100AndRoundByTwo then expects")
    void givenNumberWithoutDecimalPartSample_whenCallsDivideBy100AndRoundByTwo_thenExpects(BigDecimal input, BigDecimal output) {
        assertThat(BigDecimalUtils.divideBy100AndRoundByTwo(input)).isEqualTo(output);
    }

    private static Stream<Arguments> numberWithoutDecimalPartSample() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(100), new BigDecimal("1.00")),
                Arguments.of(BigDecimal.valueOf(10000), new BigDecimal("100.00")),
                Arguments.of(BigDecimal.valueOf(123987), BigDecimal.valueOf(1239.87))
        );
    }

}
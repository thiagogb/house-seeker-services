package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.input.FloatClauseInput;
import br.com.houseseeker.domain.input.FloatInput;
import br.com.houseseeker.domain.input.FloatIntervalInput;
import br.com.houseseeker.domain.proto.DoubleComparisonData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.normalizeSpace;
import static org.assertj.core.api.Assertions.assertThat;

class FloatComparisonBuilderTest {

    private static final FloatInput DEFAULT_SINGLE_INPUT = FloatInput.builder()
                                                                     .value(BigDecimal.valueOf(100.33))
                                                                     .build();

    private static final FloatIntervalInput DEFAULT_INTERVAL_INPUT = FloatIntervalInput.builder()
                                                                                       .start(BigDecimal.valueOf(100.33))
                                                                                       .end(BigDecimal.valueOf(200.33))
                                                                                       .build();

    @Test
    @DisplayName("given a null input when calls build then expects")
    void givenANullInput_whenCallsBuild_thenExpects() {
        assertThat(FloatComparisonBuilder.build(null))
                .isEqualTo(DoubleComparisonData.getDefaultInstance());
    }

    @ParameterizedTest
    @MethodSource("comparisonSamples")
    @DisplayName("given a input with clause when calls build then expects")
    void givenAInputWithClause_whenCallsBuild_thenExpects(FloatClauseInput input, String expected) {
        assertThat(FloatComparisonBuilder.build(input))
                .extracting(comparisonData -> normalizeSpace(comparisonData.toString()))
                .isEqualTo(expected);
    }

    private static Stream<Arguments> comparisonSamples() {
        return Stream.of(
                Arguments.of(FloatClauseInput.builder().isNull(true).build(), "is_null: true"),
                Arguments.of(FloatClauseInput.builder().isNotNull(true).build(), "is_not_null: true"),
                Arguments.of(FloatClauseInput.builder().isEqual(DEFAULT_SINGLE_INPUT).build(), "is_equal { value: 100.33 }"),
                Arguments.of(FloatClauseInput.builder().isNotEqual(DEFAULT_SINGLE_INPUT).build(), "is_not_equal { value: 100.33 }"),
                Arguments.of(FloatClauseInput.builder().isGreater(DEFAULT_SINGLE_INPUT).build(), "is_greater { value: 100.33 }"),
                Arguments.of(FloatClauseInput.builder().isGreaterOrEqual(DEFAULT_SINGLE_INPUT).build(), "is_greater_or_equal { value: 100.33 }"),
                Arguments.of(FloatClauseInput.builder().isLesser(DEFAULT_SINGLE_INPUT).build(), "is_lesser { value: 100.33 }"),
                Arguments.of(FloatClauseInput.builder().isLesserOrEqual(DEFAULT_SINGLE_INPUT).build(), "is_lesser_or_equal { value: 100.33 }"),
                Arguments.of(FloatClauseInput.builder().isBetween(DEFAULT_INTERVAL_INPUT).build(), "is_between { start: 100.33 end: 200.33 }"),
                Arguments.of(FloatClauseInput.builder().isNotBetween(DEFAULT_INTERVAL_INPUT).build(), "is_not_between { start: 100.33 end: 200.33 }")
        );
    }

}
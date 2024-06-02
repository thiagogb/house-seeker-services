package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.input.IntegerClauseInput;
import br.com.houseseeker.domain.input.IntegerInput;
import br.com.houseseeker.domain.input.IntegerIntervalInput;
import br.com.houseseeker.domain.input.IntegerListInput;
import br.com.houseseeker.domain.proto.Int32ComparisonData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.normalizeSpace;
import static org.assertj.core.api.Assertions.assertThat;

class IntegerComparisonBuilderTest {

    private static final IntegerInput DEFAULT_SINGLE_INPUT = IntegerInput.builder()
                                                                         .value(1)
                                                                         .build();

    private static final IntegerIntervalInput DEFAULT_INTERVAL_INPUT = IntegerIntervalInput.builder()
                                                                                           .start(1)
                                                                                           .end(10)
                                                                                           .build();

    private static final IntegerListInput DEFAULT_LIST_INPUT = IntegerListInput.builder()
                                                                               .values(List.of(1, 2, 3))
                                                                               .build();

    @Test
    @DisplayName("given a null input when calls build then expects")
    void givenANullInput_whenCallsBuild_thenExpects() {
        assertThat(IntegerComparisonBuilder.build(null))
                .isEqualTo(Int32ComparisonData.getDefaultInstance());
    }

    @ParameterizedTest
    @MethodSource("comparisonSamples")
    @DisplayName("given a input with clause when calls build then expects")
    void givenAInputWithClause_whenCallsBuild_thenExpects(IntegerClauseInput input, String expected) {
        assertThat(IntegerComparisonBuilder.build(input))
                .extracting(comparisonData -> normalizeSpace(comparisonData.toString()))
                .isEqualTo(expected);
    }

    private static Stream<Arguments> comparisonSamples() {
        return Stream.of(
                Arguments.of(IntegerClauseInput.builder().isNull(true).build(), "is_null: true"),
                Arguments.of(IntegerClauseInput.builder().isNotNull(true).build(), "is_not_null: true"),
                Arguments.of(IntegerClauseInput.builder().isEqual(DEFAULT_SINGLE_INPUT).build(), "is_equal { value: 1 }"),
                Arguments.of(IntegerClauseInput.builder().isNotEqual(DEFAULT_SINGLE_INPUT).build(), "is_not_equal { value: 1 }"),
                Arguments.of(IntegerClauseInput.builder().isGreater(DEFAULT_SINGLE_INPUT).build(), "is_greater { value: 1 }"),
                Arguments.of(IntegerClauseInput.builder().isGreaterOrEqual(DEFAULT_SINGLE_INPUT).build(), "is_greater_or_equal { value: 1 }"),
                Arguments.of(IntegerClauseInput.builder().isLesser(DEFAULT_SINGLE_INPUT).build(), "is_lesser { value: 1 }"),
                Arguments.of(IntegerClauseInput.builder().isLesserOrEqual(DEFAULT_SINGLE_INPUT).build(), "is_lesser_or_equal { value: 1 }"),
                Arguments.of(IntegerClauseInput.builder().isBetween(DEFAULT_INTERVAL_INPUT).build(), "is_between { start: 1 end: 10 }"),
                Arguments.of(IntegerClauseInput.builder().isNotBetween(DEFAULT_INTERVAL_INPUT).build(), "is_not_between { start: 1 end: 10 }"),
                Arguments.of(IntegerClauseInput.builder().isIn(DEFAULT_LIST_INPUT).build(), "is_in { values: 1 values: 2 values: 3 }"),
                Arguments.of(IntegerClauseInput.builder().isNotIn(DEFAULT_LIST_INPUT).build(), "is_not_in { values: 1 values: 2 values: 3 }")
        );
    }

}
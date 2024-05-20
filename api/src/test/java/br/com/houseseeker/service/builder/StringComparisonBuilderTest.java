package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.input.StringClauseInput;
import br.com.houseseeker.domain.input.StringInput;
import br.com.houseseeker.domain.input.StringListInput;
import br.com.houseseeker.domain.proto.StringComparisonData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class StringComparisonBuilderTest {

    @Test
    @DisplayName("given a null input when calls build then expects")
    void givenANullInput_whenCallsBuild_thenExpects() {
        assertThat(StringComparisonBuilder.build(null))
                .isEqualTo(StringComparisonData.getDefaultInstance());
    }

    @ParameterizedTest
    @MethodSource("comparisonSamples")
    @DisplayName("given a input with clause when calls build then expects")
    void givenAInputWithClause_whenCallsBuild_thenExpects(StringClauseInput input, StringComparisonData.ComparisonCase expected) {
        assertThat(StringComparisonBuilder.build(input))
                .extracting(StringComparisonData::getComparisonCase)
                .isEqualTo(expected);
    }

    private static Stream<Arguments> comparisonSamples() {
        return Stream.of(
                Arguments.of(StringClauseInput.builder().isNull(true).build(), StringComparisonData.ComparisonCase.IS_NULL),
                Arguments.of(StringClauseInput.builder().isNotNull(true).build(), StringComparisonData.ComparisonCase.IS_NOT_NULL),
                Arguments.of(StringClauseInput.builder().isBlank(true).build(), StringComparisonData.ComparisonCase.IS_BLANK),
                Arguments.of(StringClauseInput.builder().isNotBlank(true).build(), StringComparisonData.ComparisonCase.IS_NOT_BLANK),
                Arguments.of(
                        StringClauseInput.builder().isEqual(StringInput.builder().value("a").build()).build(),
                        StringComparisonData.ComparisonCase.IS_EQUAL
                ),
                Arguments.of(
                        StringClauseInput.builder().isNotEqual(StringInput.builder().value("a").build()).build(),
                        StringComparisonData.ComparisonCase.IS_NOT_EQUAL
                ),
                Arguments.of(
                        StringClauseInput.builder().isNotEqual(StringInput.builder().value("a").build()).build(),
                        StringComparisonData.ComparisonCase.IS_NOT_EQUAL
                ),
                Arguments.of(
                        StringClauseInput.builder().isStartingWith(StringInput.builder().value("a").build()).build(),
                        StringComparisonData.ComparisonCase.IS_STARTING_WITH
                ),
                Arguments.of(
                        StringClauseInput.builder().isNotStartingWith(StringInput.builder().value("a").build()).build(),
                        StringComparisonData.ComparisonCase.IS_NOT_STARTING_WITH
                ),
                Arguments.of(
                        StringClauseInput.builder().isEndingWith(StringInput.builder().value("a").build()).build(),
                        StringComparisonData.ComparisonCase.IS_ENDING_WITH
                ),
                Arguments.of(
                        StringClauseInput.builder().isNotEndingWith(StringInput.builder().value("a").build()).build(),
                        StringComparisonData.ComparisonCase.IS_NOT_ENDING_WITH
                ),
                Arguments.of(
                        StringClauseInput.builder().itContains(StringInput.builder().value("a").build()).build(),
                        StringComparisonData.ComparisonCase.IT_CONTAINS
                ),
                Arguments.of(
                        StringClauseInput.builder().itNotContains(StringInput.builder().value("a").build()).build(),
                        StringComparisonData.ComparisonCase.IT_NOT_CONTAINS
                ),
                Arguments.of(
                        StringClauseInput.builder().isIn(StringListInput.builder().values(List.of("a")).build()).build(),
                        StringComparisonData.ComparisonCase.IS_IN
                ),
                Arguments.of(
                        StringClauseInput.builder().isNotIn(StringListInput.builder().values(List.of("a")).build()).build(),
                        StringComparisonData.ComparisonCase.IS_NOT_IN
                )
        );
    }

}
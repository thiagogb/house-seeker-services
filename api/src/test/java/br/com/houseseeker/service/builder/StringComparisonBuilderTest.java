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

import static org.apache.commons.lang3.StringUtils.normalizeSpace;
import static org.assertj.core.api.Assertions.assertThat;

class StringComparisonBuilderTest {

    private static final StringInput DEFAULT_SINGLE_INPUT = StringInput.builder().value("a").build();

    private static final StringListInput DEFAULT_LIST_INPUT = StringListInput.builder().values(List.of("a")).build();

    @Test
    @DisplayName("given a null input when calls build then expects")
    void givenANullInput_whenCallsBuild_thenExpects() {
        assertThat(StringComparisonBuilder.build(null))
                .isEqualTo(StringComparisonData.getDefaultInstance());
    }

    @ParameterizedTest
    @MethodSource("comparisonSamples")
    @DisplayName("given a input with clause when calls build then expects")
    void givenAInputWithClause_whenCallsBuild_thenExpects(StringClauseInput input, String expected) {
        assertThat(StringComparisonBuilder.build(input))
                .extracting(comparisonData -> normalizeSpace(comparisonData.toString()))
                .isEqualTo(expected);
    }

    private static Stream<Arguments> comparisonSamples() {
        return Stream.of(
                Arguments.of(StringClauseInput.builder().isNull(true).build(), "is_null: true"),
                Arguments.of(StringClauseInput.builder().isNotNull(true).build(), "is_not_null: true"),
                Arguments.of(StringClauseInput.builder().isBlank(true).build(), "is_blank: true"),
                Arguments.of(StringClauseInput.builder().isNotBlank(true).build(), "is_not_blank: true"),
                Arguments.of(StringClauseInput.builder().isEqual(DEFAULT_SINGLE_INPUT).build(), "is_equal { value: \"a\" }"),
                Arguments.of(StringClauseInput.builder().isNotEqual(DEFAULT_SINGLE_INPUT).build(), "is_not_equal { value: \"a\" }"),
                Arguments.of(StringClauseInput.builder().isStartingWith(DEFAULT_SINGLE_INPUT).build(), "is_starting_with { value: \"a\" }"),
                Arguments.of(StringClauseInput.builder().isNotStartingWith(DEFAULT_SINGLE_INPUT).build(), "is_not_starting_with { value: \"a\" }"),
                Arguments.of(StringClauseInput.builder().isEndingWith(DEFAULT_SINGLE_INPUT).build(), "is_ending_with { value: \"a\" }"),
                Arguments.of(StringClauseInput.builder().isNotEndingWith(DEFAULT_SINGLE_INPUT).build(), "is_not_ending_with { value: \"a\" }"),
                Arguments.of(StringClauseInput.builder().itContains(DEFAULT_SINGLE_INPUT).build(), "it_contains { value: \"a\" }"),
                Arguments.of(StringClauseInput.builder().itNotContains(DEFAULT_SINGLE_INPUT).build(), "it_not_contains { value: \"a\" }"),
                Arguments.of(StringClauseInput.builder().isIn(DEFAULT_LIST_INPUT).build(), "is_in { values: \"a\" }"),
                Arguments.of(StringClauseInput.builder().isNotIn(DEFAULT_LIST_INPUT).build(), "is_not_in { values: \"a\" }")
        );
    }

}
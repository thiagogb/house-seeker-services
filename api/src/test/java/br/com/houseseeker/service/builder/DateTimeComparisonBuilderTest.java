package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.input.DateTimeClauseInput;
import br.com.houseseeker.domain.input.DateTimeInput;
import br.com.houseseeker.domain.input.DateTimeIntervalInput;
import br.com.houseseeker.domain.proto.DateTimeComparisonData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.normalizeSpace;
import static org.assertj.core.api.Assertions.assertThat;

class DateTimeComparisonBuilderTest {

    private static final DateTimeInput DEFAULT_SINGLE_INPUT = DateTimeInput.builder()
                                                                           .value("2024-05-01T17:21:15")
                                                                           .build();

    private static final DateTimeIntervalInput DEFAULT_INTERVAL_INPUT = DateTimeIntervalInput.builder()
                                                                                             .start("2024-05-01T17:21:15")
                                                                                             .end("2024-05-02T17:21:15")
                                                                                             .build();

    @Test
    @DisplayName("given a null input when calls build then expects")
    void givenANullInput_whenCallsBuild_thenExpects() {
        assertThat(DateTimeComparisonBuilder.build(null))
                .isEqualTo(DateTimeComparisonData.getDefaultInstance());
    }

    @ParameterizedTest
    @MethodSource("comparisonSamples")
    @DisplayName("given a input with clause when calls build then expects")
    void givenAInputWithClause_whenCallsBuild_thenExpects(DateTimeClauseInput input, String expected) {
        assertThat(DateTimeComparisonBuilder.build(input))
                .extracting(comparisonData -> normalizeSpace(comparisonData.toString()))
                .isEqualTo(expected);
    }

    private static Stream<Arguments> comparisonSamples() {
        return Stream.of(
                Arguments.of(DateTimeClauseInput.builder().isNull(true).build(), "is_null: true"),
                Arguments.of(DateTimeClauseInput.builder().isNotNull(true).build(), "is_not_null: true"),
                Arguments.of(
                        DateTimeClauseInput.builder().isEqual(DEFAULT_SINGLE_INPUT).build(),
                        "is_equal { value: \"2024-05-01T17:21:15\" }"
                ),
                Arguments.of(
                        DateTimeClauseInput.builder().isNotEqual(DEFAULT_SINGLE_INPUT).build(),
                        "is_not_equal { value: \"2024-05-01T17:21:15\" }"
                ),
                Arguments.of(
                        DateTimeClauseInput.builder().isGreater(DEFAULT_SINGLE_INPUT).build(),
                        "is_greater { value: \"2024-05-01T17:21:15\" }"
                ),
                Arguments.of(
                        DateTimeClauseInput.builder().isGreaterOrEqual(DEFAULT_SINGLE_INPUT).build(),
                        "is_greater_or_equal { value: \"2024-05-01T17:21:15\" }"
                ),
                Arguments.of(
                        DateTimeClauseInput.builder().isLesser(DEFAULT_SINGLE_INPUT).build(),
                        "is_lesser { value: \"2024-05-01T17:21:15\" }"
                ),
                Arguments.of(
                        DateTimeClauseInput.builder().isLesserOrEqual(DEFAULT_SINGLE_INPUT).build(),
                        "is_lesser_or_equal { value: \"2024-05-01T17:21:15\" }"
                ),
                Arguments.of(
                        DateTimeClauseInput.builder().isBetween(DEFAULT_INTERVAL_INPUT).build(),
                        "is_between { start: \"2024-05-01T17:21:15\" end: \"2024-05-02T17:21:15\" }"
                ),
                Arguments.of(
                        DateTimeClauseInput.builder().isNotBetween(DEFAULT_INTERVAL_INPUT).build(),
                        "is_not_between { start: \"2024-05-01T17:21:15\" end: \"2024-05-02T17:21:15\" }"
                )
        );
    }

}
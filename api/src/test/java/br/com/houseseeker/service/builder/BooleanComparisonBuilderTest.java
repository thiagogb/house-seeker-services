package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.input.BooleanClauseInput;
import br.com.houseseeker.domain.input.BooleanInput;
import br.com.houseseeker.domain.proto.BoolComparisonData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.normalizeSpace;
import static org.assertj.core.api.Assertions.assertThat;

class BooleanComparisonBuilderTest {

    private static final BooleanInput DEFAULT_SINGLE_INPUT = BooleanInput.builder()
                                                                         .value(true)
                                                                         .build();

    @Test
    @DisplayName("given a null input when calls build then expects")
    void givenANullInput_whenCallsBuild_thenExpects() {
        assertThat(BooleanComparisonBuilder.build(null))
                .isEqualTo(BoolComparisonData.getDefaultInstance());
    }

    @ParameterizedTest
    @MethodSource("comparisonSamples")
    @DisplayName("given a input with clause when calls build then expects")
    void givenAInputWithClause_whenCallsBuild_thenExpects(BooleanClauseInput input, String expected) {
        assertThat(BooleanComparisonBuilder.build(input))
                .extracting(comparisonData -> normalizeSpace(comparisonData.toString()))
                .isEqualTo(expected);
    }

    private static Stream<Arguments> comparisonSamples() {
        return Stream.of(
                Arguments.of(BooleanClauseInput.builder().isNull(true).build(), "is_null: true"),
                Arguments.of(BooleanClauseInput.builder().isNotNull(true).build(), "is_not_null: true"),
                Arguments.of(BooleanClauseInput.builder().isEqual(DEFAULT_SINGLE_INPUT).build(), "is_equal { value: true }"),
                Arguments.of(BooleanClauseInput.builder().isNotEqual(DEFAULT_SINGLE_INPUT).build(), "is_not_equal { value: true }")
        );
    }

}
package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.input.BytesClauseInput;
import br.com.houseseeker.domain.proto.BytesComparisonData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class BytesComparisonBuilderTest {

    @Test
    @DisplayName("given a null input when calls build then expects")
    void givenANullInput_whenCallsBuild_thenExpects() {
        assertThat(BytesComparisonBuilder.build(null))
                .isEqualTo(BytesComparisonData.getDefaultInstance());
    }

    @ParameterizedTest
    @MethodSource("comparisonSamples")
    @DisplayName("given a input with clause when calls build then expects")
    void givenAInputWithClause_whenCallsBuild_thenExpects(BytesClauseInput input, BytesComparisonData.ComparisonCase expected) {
        assertThat(BytesComparisonBuilder.build(input))
                .extracting(BytesComparisonData::getComparisonCase)
                .isEqualTo(expected);
    }

    private static Stream<Arguments> comparisonSamples() {
        return Stream.of(
                Arguments.of(BytesClauseInput.builder().isNull(true).build(), BytesComparisonData.ComparisonCase.IS_NULL),
                Arguments.of(BytesClauseInput.builder().isNotNull(true).build(), BytesComparisonData.ComparisonCase.IS_NOT_NULL)
        );
    }

}
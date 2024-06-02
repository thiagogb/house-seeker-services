package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.input.UrbanPropertyContractClauseInput;
import br.com.houseseeker.domain.input.UrbanPropertyContractInput;
import br.com.houseseeker.domain.input.UrbanPropertyContractListInput;
import br.com.houseseeker.domain.property.UrbanPropertyContract;
import br.com.houseseeker.domain.proto.EnumComparisonData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.normalizeSpace;
import static org.assertj.core.api.Assertions.assertThat;

class UrbanPropertyContractComparisonBuilderTest {

    private static final UrbanPropertyContractInput SINGLE_INPUT = UrbanPropertyContractInput.builder()
                                                                                             .value(UrbanPropertyContract.RENT)
                                                                                             .build();

    private static final UrbanPropertyContractListInput LIST_INPUT = UrbanPropertyContractListInput.builder()
                                                                                                   .values(List.of(
                                                                                                           UrbanPropertyContract.SELL,
                                                                                                           UrbanPropertyContract.RENT
                                                                                                   ))
                                                                                                   .build();

    @Test
    @DisplayName("given a null input when calls build then expects")
    void givenANullInput_whenCallsBuild_thenExpects() {
        assertThat(UrbanPropertyContractComparisonBuilder.build(null))
                .isEqualTo(EnumComparisonData.getDefaultInstance());
    }

    @ParameterizedTest
    @MethodSource("comparisonSamples")
    @DisplayName("given a input with clause when calls build then expects")
    void givenAInputWithClause_whenCallsBuild_thenExpects(UrbanPropertyContractClauseInput input, String expected) {
        assertThat(UrbanPropertyContractComparisonBuilder.build(input))
                .extracting(comparisonData -> normalizeSpace(comparisonData.toString()))
                .isEqualTo(expected);
    }

    private static Stream<Arguments> comparisonSamples() {
        return Stream.of(
                Arguments.of(UrbanPropertyContractClauseInput.builder().isNull(true).build(), "is_null: true"),
                Arguments.of(UrbanPropertyContractClauseInput.builder().isNotNull(true).build(), "is_not_null: true"),
                Arguments.of(UrbanPropertyContractClauseInput.builder().isEqual(SINGLE_INPUT).build(), "is_equal { value: \"RENT\" }"),
                Arguments.of(UrbanPropertyContractClauseInput.builder().isNotEqual(SINGLE_INPUT).build(), "is_not_equal { value: \"RENT\" }"),
                Arguments.of(UrbanPropertyContractClauseInput.builder().isIn(LIST_INPUT).build(), "is_in { values: \"SELL\" values: \"RENT\" }"),
                Arguments.of(UrbanPropertyContractClauseInput.builder().isNotIn(LIST_INPUT).build(), "is_not_in { values: \"SELL\" values: \"RENT\" }")
        );
    }

}
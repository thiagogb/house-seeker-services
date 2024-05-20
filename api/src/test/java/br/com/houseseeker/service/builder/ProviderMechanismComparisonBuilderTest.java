package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.input.ProviderMechanismClausesInput;
import br.com.houseseeker.domain.input.ProviderMechanismInput;
import br.com.houseseeker.domain.input.ProviderMechanismListInput;
import br.com.houseseeker.domain.proto.EnumComparisonData;
import br.com.houseseeker.domain.provider.ProviderMechanism;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ProviderMechanismComparisonBuilderTest {

    private static final ProviderMechanismInput DEFAULT_SINGLE_INPUT = ProviderMechanismInput.builder()
                                                                                             .value(ProviderMechanism.JETIMOB_V1)
                                                                                             .build();

    private static final ProviderMechanismListInput DEFAULT_LIST_INPUT = ProviderMechanismListInput.builder()
                                                                                                   .values(List.of(
                                                                                                           ProviderMechanism.JETIMOB_V1
                                                                                                   ))
                                                                                                   .build();

    @Test
    @DisplayName("given a null input when calls build then expects")
    void givenANullInput_whenCallsBuild_thenExpects() {
        assertThat(ProviderMechanismComparisonBuilder.build(null))
                .isEqualTo(EnumComparisonData.getDefaultInstance());
    }

    @ParameterizedTest
    @MethodSource("comparisonSamples")
    @DisplayName("given a input with clause when calls build then expects")
    void givenAInputWithClause_whenCallsBuild_thenExpects(
            ProviderMechanismClausesInput input,
            EnumComparisonData.ComparisonCase expected
    ) {
        assertThat(ProviderMechanismComparisonBuilder.build(input))
                .extracting(EnumComparisonData::getComparisonCase)
                .isEqualTo(expected);
    }

    private static Stream<Arguments> comparisonSamples() {
        return Stream.of(
                Arguments.of(
                        ProviderMechanismClausesInput.builder().isNull(true).build(),
                        EnumComparisonData.ComparisonCase.IS_NULL
                ),
                Arguments.of(
                        ProviderMechanismClausesInput.builder().isNotNull(true).build(),
                        EnumComparisonData.ComparisonCase.IS_NOT_NULL
                ),
                Arguments.of(
                        ProviderMechanismClausesInput.builder().isEqual(DEFAULT_SINGLE_INPUT).build(),
                        EnumComparisonData.ComparisonCase.IS_EQUAL
                ),
                Arguments.of(
                        ProviderMechanismClausesInput.builder().isNotEqual(DEFAULT_SINGLE_INPUT).build(),
                        EnumComparisonData.ComparisonCase.IS_NOT_EQUAL
                ),
                Arguments.of(
                        ProviderMechanismClausesInput.builder().isIn(DEFAULT_LIST_INPUT).build(),
                        EnumComparisonData.ComparisonCase.IS_IN
                ),
                Arguments.of(
                        ProviderMechanismClausesInput.builder().isNotIn(DEFAULT_LIST_INPUT).build(),
                        EnumComparisonData.ComparisonCase.IS_NOT_IN
                )
        );
    }

}
package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.input.UrbanPropertyStatusClauseInput;
import br.com.houseseeker.domain.input.UrbanPropertyStatusInput;
import br.com.houseseeker.domain.input.UrbanPropertyStatusListInput;
import br.com.houseseeker.domain.property.UrbanPropertyStatus;
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

class UrbanPropertyStatusComparisonBuilderTest {

    private static final UrbanPropertyStatusInput SINGLE_INPUT = UrbanPropertyStatusInput.builder()
                                                                                         .value(UrbanPropertyStatus.USED)
                                                                                         .build();

    private static final UrbanPropertyStatusListInput LIST_INPUT = UrbanPropertyStatusListInput.builder()
                                                                                               .values(List.of(
                                                                                                       UrbanPropertyStatus.USED,
                                                                                                       UrbanPropertyStatus.UNUSED
                                                                                               ))
                                                                                               .build();

    @Test
    @DisplayName("given a null input when calls build then expects")
    void givenANullInput_whenCallsBuild_thenExpects() {
        assertThat(UrbanPropertyStatusComparisonBuilder.build(null))
                .isEqualTo(EnumComparisonData.getDefaultInstance());
    }

    @ParameterizedTest
    @MethodSource("comparisonSamples")
    @DisplayName("given a input with clause when calls build then expects")
    void givenAInputWithClause_whenCallsBuild_thenExpects(UrbanPropertyStatusClauseInput input, String expected) {
        assertThat(UrbanPropertyStatusComparisonBuilder.build(input))
                .extracting(comparisonData -> normalizeSpace(comparisonData.toString()))
                .isEqualTo(expected);
    }

    private static Stream<Arguments> comparisonSamples() {
        return Stream.of(
                Arguments.of(UrbanPropertyStatusClauseInput.builder().isNull(true).build(), "is_null: true"),
                Arguments.of(UrbanPropertyStatusClauseInput.builder().isNotNull(true).build(), "is_not_null: true"),
                Arguments.of(UrbanPropertyStatusClauseInput.builder().isEqual(SINGLE_INPUT).build(), "is_equal { value: \"USED\" }"),
                Arguments.of(UrbanPropertyStatusClauseInput.builder().isNotEqual(SINGLE_INPUT).build(), "is_not_equal { value: \"USED\" }"),
                Arguments.of(UrbanPropertyStatusClauseInput.builder().isIn(LIST_INPUT).build(), "is_in { values: \"USED\" values: \"UNUSED\" }"),
                Arguments.of(UrbanPropertyStatusClauseInput.builder().isNotIn(LIST_INPUT).build(), "is_not_in { values: \"USED\" values: \"UNUSED\" }")
        );
    }

}
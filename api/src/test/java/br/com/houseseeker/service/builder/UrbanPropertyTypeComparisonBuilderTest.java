package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.input.UrbanPropertyTypeClauseInput;
import br.com.houseseeker.domain.input.UrbanPropertyTypeInput;
import br.com.houseseeker.domain.input.UrbanPropertyTypeListInput;
import br.com.houseseeker.domain.property.UrbanPropertyType;
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

class UrbanPropertyTypeComparisonBuilderTest {

    private static final UrbanPropertyTypeInput SINGLE_INPUT = UrbanPropertyTypeInput.builder()
                                                                                     .value(UrbanPropertyType.RESIDENTIAL)
                                                                                     .build();

    private static final UrbanPropertyTypeListInput LIST_INPUT = UrbanPropertyTypeListInput.builder()
                                                                                           .values(List.of(
                                                                                                   UrbanPropertyType.RESIDENTIAL,
                                                                                                   UrbanPropertyType.COMMERCIAL
                                                                                           ))
                                                                                           .build();

    @Test
    @DisplayName("given a null input when calls build then expects")
    void givenANullInput_whenCallsBuild_thenExpects() {
        assertThat(UrbanPropertyTypeComparisonBuilder.build(null))
                .isEqualTo(EnumComparisonData.getDefaultInstance());
    }

    @ParameterizedTest
    @MethodSource("comparisonSamples")
    @DisplayName("given a input with clause when calls build then expects")
    void givenAInputWithClause_whenCallsBuild_thenExpects(UrbanPropertyTypeClauseInput input, String expected) {
        assertThat(UrbanPropertyTypeComparisonBuilder.build(input))
                .extracting(comparisonData -> normalizeSpace(comparisonData.toString()))
                .isEqualTo(expected);
    }

    private static Stream<Arguments> comparisonSamples() {
        return Stream.of(
                Arguments.of(UrbanPropertyTypeClauseInput.builder().isNull(true).build(), "is_null: true"),
                Arguments.of(UrbanPropertyTypeClauseInput.builder().isNotNull(true).build(), "is_not_null: true"),
                Arguments.of(UrbanPropertyTypeClauseInput.builder().isEqual(SINGLE_INPUT).build(), "is_equal { value: \"RESIDENTIAL\" }"),
                Arguments.of(UrbanPropertyTypeClauseInput.builder().isNotEqual(SINGLE_INPUT).build(), "is_not_equal { value: \"RESIDENTIAL\" }"),
                Arguments.of(
                        UrbanPropertyTypeClauseInput.builder().isIn(LIST_INPUT).build(),
                        "is_in { values: \"RESIDENTIAL\" values: \"COMMERCIAL\" }"
                ),
                Arguments.of(
                        UrbanPropertyTypeClauseInput.builder().isNotIn(LIST_INPUT).build(),
                        "is_not_in { values: \"RESIDENTIAL\" values: \"COMMERCIAL\" }"
                )
        );
    }

}
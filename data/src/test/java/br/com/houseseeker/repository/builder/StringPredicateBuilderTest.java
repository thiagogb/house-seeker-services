package br.com.houseseeker.repository.builder;

import br.com.houseseeker.domain.exception.GrpcStatusException;
import br.com.houseseeker.domain.proto.StringComparisonData;
import br.com.houseseeker.domain.proto.StringListComparisonData;
import br.com.houseseeker.domain.proto.StringSingleComparisonData;
import io.grpc.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static br.com.houseseeker.entity.QDslUrbanProperty.urbanProperty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StringPredicateBuilderTest {

    private static final String DEFAULT_VALUE = "Casa";

    @Mock
    private StringComparisonData mockedStringComparisonData;

    @Mock
    private StringComparisonData.ComparisonCase mockedStringComparisonCase;

    @BeforeEach
    void setup() {
        when(mockedStringComparisonData.getComparisonCase()).thenReturn(mockedStringComparisonCase);
    }

    @Test
    @DisplayName("given a unknown case comparison when calls build then expects exception")
    void givenAUnknownCaseComparisonAppended_whenCallsBuild_thenExpectsException() {
        when(mockedStringComparisonCase.getNumber()).thenReturn(Integer.MAX_VALUE);

        assertThatThrownBy(() -> StringPredicateBuilder.build(urbanProperty.subType, mockedStringComparisonData))
                .isInstanceOf(GrpcStatusException.class)
                .hasFieldOrPropertyWithValue("status", Status.INVALID_ARGUMENT)
                .hasMessage("Unknown comparison case");
    }

    @Test
    @DisplayName("given a is null true case comparison when calls build then expects")
    void givenAIsNullTrueCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedStringComparisonCase.getNumber()).thenReturn(1);
        when(mockedStringComparisonData.getIsNull()).thenReturn(true);

        assertThat(StringPredicateBuilder.build(urbanProperty.subType, mockedStringComparisonData))
                .hasToString("Optional[urbanProperty.subType is null]");
    }

    @Test
    @DisplayName("given a is null false case comparison when calls build then expects")
    void givenAIsNullFalseCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedStringComparisonCase.getNumber()).thenReturn(1);
        when(mockedStringComparisonData.getIsNull()).thenReturn(false);

        assertThat(StringPredicateBuilder.build(urbanProperty.subType, mockedStringComparisonData))
                .isEmpty();
    }

    @Test
    @DisplayName("given a is not null true case comparison when calls build then expects")
    void givenAIsNotNullTrueCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedStringComparisonCase.getNumber()).thenReturn(2);
        when(mockedStringComparisonData.getIsNotNull()).thenReturn(true);

        assertThat(StringPredicateBuilder.build(urbanProperty.subType, mockedStringComparisonData))
                .hasToString("Optional[urbanProperty.subType is not null]");
    }

    @Test
    @DisplayName("given a is not null false case comparison when calls build then expects")
    void givenAIsNotNullFalseCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedStringComparisonCase.getNumber()).thenReturn(2);
        when(mockedStringComparisonData.getIsNotNull()).thenReturn(false);

        assertThat(StringPredicateBuilder.build(urbanProperty.subType, mockedStringComparisonData))
                .isEmpty();
    }

    @Test
    @DisplayName("given a is blank true case comparison when calls build then expects")
    void givenAIsBlankTrueCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedStringComparisonCase.getNumber()).thenReturn(3);
        when(mockedStringComparisonData.getIsBlank()).thenReturn(true);

        assertThat(StringPredicateBuilder.build(urbanProperty.subType, mockedStringComparisonData))
                .hasToString("Optional[empty(urbanProperty.subType)]");
    }

    @Test
    @DisplayName("given a is blank false case comparison when calls build then expects")
    void givenAIsBlankFalseCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedStringComparisonCase.getNumber()).thenReturn(3);
        when(mockedStringComparisonData.getIsBlank()).thenReturn(false);

        assertThat(StringPredicateBuilder.build(urbanProperty.subType, mockedStringComparisonData))
                .isEmpty();
    }

    @Test
    @DisplayName("given a is not blank true case comparison when calls build then expects")
    void givenAIsNotBlankTrueCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedStringComparisonCase.getNumber()).thenReturn(4);
        when(mockedStringComparisonData.getIsNotBlank()).thenReturn(true);

        assertThat(StringPredicateBuilder.build(urbanProperty.subType, mockedStringComparisonData))
                .hasToString("Optional[!empty(urbanProperty.subType)]");
    }

    @Test
    @DisplayName("given a is not blank false case comparison when calls build then expects")
    void givenAIsNotBlankFalseCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedStringComparisonCase.getNumber()).thenReturn(4);
        when(mockedStringComparisonData.getIsNotBlank()).thenReturn(false);

        assertThat(StringPredicateBuilder.build(urbanProperty.subType, mockedStringComparisonData))
                .isEmpty();
    }

    @Test
    @DisplayName("given a is equal case comparison when calls build then expects")
    void givenAIsEqualCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedStringComparisonCase.getNumber()).thenReturn(5);
        when(mockedStringComparisonData.getIsEqual()).thenReturn(
                StringSingleComparisonData.newBuilder().setValue(DEFAULT_VALUE).build()
        );

        assertThat(StringPredicateBuilder.build(urbanProperty.subType, mockedStringComparisonData))
                .hasToString("Optional[eqIc(urbanProperty.subType,Casa)]");
    }

    @Test
    @DisplayName("given a is not equal case comparison when calls build then expects")
    void givenAIsNotEqualCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedStringComparisonCase.getNumber()).thenReturn(6);
        when(mockedStringComparisonData.getIsNotEqual()).thenReturn(
                StringSingleComparisonData.newBuilder().setValue(DEFAULT_VALUE).build()
        );

        assertThat(StringPredicateBuilder.build(urbanProperty.subType, mockedStringComparisonData))
                .hasToString("Optional[!(eqIc(urbanProperty.subType,Casa))]");
    }

    @Test
    @DisplayName("given a is starting with case comparison when calls build then expects")
    void givenAIsStartingWithCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedStringComparisonCase.getNumber()).thenReturn(7);
        when(mockedStringComparisonData.getIsStartingWith()).thenReturn(
                StringSingleComparisonData.newBuilder().setValue(DEFAULT_VALUE).build()
        );

        assertThat(StringPredicateBuilder.build(urbanProperty.subType, mockedStringComparisonData))
                .hasToString("Optional[startsWithIgnoreCase(urbanProperty.subType,Casa)]");
    }

    @Test
    @DisplayName("given a is not starting with case comparison when calls build then expects")
    void givenAIsNotStartingWithCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedStringComparisonCase.getNumber()).thenReturn(8);
        when(mockedStringComparisonData.getIsNotStartingWith()).thenReturn(
                StringSingleComparisonData.newBuilder().setValue(DEFAULT_VALUE).build()
        );

        assertThat(StringPredicateBuilder.build(urbanProperty.subType, mockedStringComparisonData))
                .hasToString("Optional[!startsWithIgnoreCase(urbanProperty.subType,Casa)]");
    }

    @Test
    @DisplayName("given a is ending with case comparison when calls build then expects")
    void givenAIsEndingWithCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedStringComparisonCase.getNumber()).thenReturn(9);
        when(mockedStringComparisonData.getIsEndingWith()).thenReturn(
                StringSingleComparisonData.newBuilder().setValue(DEFAULT_VALUE).build()
        );

        assertThat(StringPredicateBuilder.build(urbanProperty.subType, mockedStringComparisonData))
                .hasToString("Optional[endsWithIgnoreCase(urbanProperty.subType,Casa)]");
    }

    @Test
    @DisplayName("given a is not ending with case comparison when calls build then expects")
    void givenAIsNotEndingWithCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedStringComparisonCase.getNumber()).thenReturn(10);
        when(mockedStringComparisonData.getIsNotEndingWith()).thenReturn(
                StringSingleComparisonData.newBuilder().setValue(DEFAULT_VALUE).build()
        );

        assertThat(StringPredicateBuilder.build(urbanProperty.subType, mockedStringComparisonData))
                .hasToString("Optional[!endsWithIgnoreCase(urbanProperty.subType,Casa)]");
    }

    @Test
    @DisplayName("given a it contains with case comparison when calls build then expects")
    void givenAItContainsCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedStringComparisonCase.getNumber()).thenReturn(11);
        when(mockedStringComparisonData.getItContains()).thenReturn(
                StringSingleComparisonData.newBuilder().setValue(DEFAULT_VALUE).build()
        );

        assertThat(StringPredicateBuilder.build(urbanProperty.subType, mockedStringComparisonData))
                .hasToString("Optional[containsIc(urbanProperty.subType,Casa)]");
    }

    @Test
    @DisplayName("given a it not contains with case comparison when calls build then expects")
    void givenAItNotContainsCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedStringComparisonCase.getNumber()).thenReturn(12);
        when(mockedStringComparisonData.getItNotContains()).thenReturn(
                StringSingleComparisonData.newBuilder().setValue(DEFAULT_VALUE).build()
        );

        assertThat(StringPredicateBuilder.build(urbanProperty.subType, mockedStringComparisonData))
                .hasToString("Optional[!containsIc(urbanProperty.subType,Casa)]");
    }

    @Test
    @DisplayName("given a is in with case comparison when calls build then expects")
    void givenAIsInCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedStringComparisonCase.getNumber()).thenReturn(13);
        when(mockedStringComparisonData.getIsIn()).thenReturn(
                StringListComparisonData.newBuilder().addAllValues(List.of("Casa", "Sobrado")).build()
        );

        assertThat(StringPredicateBuilder.build(urbanProperty.subType, mockedStringComparisonData))
                .hasToString("Optional[urbanProperty.subType in [Casa, Sobrado]]");
    }

    @Test
    @DisplayName("given a is not in with case comparison when calls build then expects")
    void givenAIsNotInCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedStringComparisonCase.getNumber()).thenReturn(14);
        when(mockedStringComparisonData.getIsNotIn()).thenReturn(
                StringListComparisonData.newBuilder().addAllValues(List.of("Casa", "Sobrado")).build()
        );

        assertThat(StringPredicateBuilder.build(urbanProperty.subType, mockedStringComparisonData))
                .hasToString("Optional[urbanProperty.subType not in [Casa, Sobrado]]");
    }

}
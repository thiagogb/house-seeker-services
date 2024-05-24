package br.com.houseseeker.repository.builder;

import br.com.houseseeker.domain.exception.GrpcStatusException;
import br.com.houseseeker.domain.proto.Int32ComparisonData;
import br.com.houseseeker.domain.proto.Int32IntervalComparisonData;
import br.com.houseseeker.domain.proto.Int32ListComparisonData;
import br.com.houseseeker.domain.proto.Int32SingleComparisonData;
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
class IntegerPredicateBuilderTest {

    private static final int DEFAULT_VALUE = 100;

    @Mock
    private Int32ComparisonData mockedInt32ComparisonData;

    @Mock
    private Int32ComparisonData.ComparisonCase mockedInt32ComparisonCase;

    @BeforeEach
    void setup() {
        when(mockedInt32ComparisonData.getComparisonCase()).thenReturn(mockedInt32ComparisonCase);
    }

    @Test
    @DisplayName("given a unknown case comparison when calls build then expects exception")
    void givenAUnknownCaseComparisonAppended_whenCallsBuild_thenExpectsException() {
        when(mockedInt32ComparisonCase.getNumber()).thenReturn(Integer.MAX_VALUE);

        assertThatThrownBy(() -> IntegerPredicateBuilder.build(urbanProperty.bathrooms, mockedInt32ComparisonData))
                .isInstanceOf(GrpcStatusException.class)
                .hasFieldOrPropertyWithValue("status", Status.INVALID_ARGUMENT)
                .hasMessage("Unknown comparison case");
    }

    @Test
    @DisplayName("given a is null true case comparison when calls build then expects")
    void givenAIsNullTrueCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedInt32ComparisonCase.getNumber()).thenReturn(1);
        when(mockedInt32ComparisonData.getIsNull()).thenReturn(true);

        assertThat(IntegerPredicateBuilder.build(urbanProperty.bathrooms, mockedInt32ComparisonData))
                .hasToString("Optional[urbanProperty.bathrooms is null]");
    }

    @Test
    @DisplayName("given a is null false case comparison when calls build then expects")
    void givenAIsNullFalseCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedInt32ComparisonCase.getNumber()).thenReturn(1);
        when(mockedInt32ComparisonData.getIsNull()).thenReturn(false);

        assertThat(IntegerPredicateBuilder.build(urbanProperty.bathrooms, mockedInt32ComparisonData))
                .isEmpty();
    }

    @Test
    @DisplayName("given a is not null true case comparison when calls build then expects")
    void givenAIsNotNullTrueCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedInt32ComparisonCase.getNumber()).thenReturn(2);
        when(mockedInt32ComparisonData.getIsNotNull()).thenReturn(true);

        assertThat(IntegerPredicateBuilder.build(urbanProperty.bathrooms, mockedInt32ComparisonData))
                .hasToString("Optional[urbanProperty.bathrooms is not null]");
    }

    @Test
    @DisplayName("given a is not null false case comparison when calls build then expects")
    void givenAIsNotNullFalseCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedInt32ComparisonCase.getNumber()).thenReturn(2);
        when(mockedInt32ComparisonData.getIsNotNull()).thenReturn(false);

        assertThat(IntegerPredicateBuilder.build(urbanProperty.bathrooms, mockedInt32ComparisonData))
                .isEmpty();
    }

    @Test
    @DisplayName("given a is equal case comparison when calls build then expects")
    void givenAIsEqualCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedInt32ComparisonCase.getNumber()).thenReturn(3);
        when(mockedInt32ComparisonData.getIsEqual()).thenReturn(
                Int32SingleComparisonData.newBuilder().setValue(DEFAULT_VALUE).build()
        );

        assertThat(IntegerPredicateBuilder.build(urbanProperty.bathrooms, mockedInt32ComparisonData))
                .hasToString("Optional[urbanProperty.bathrooms = 100]");
    }

    @Test
    @DisplayName("given a is not equal case comparison when calls build then expects")
    void givenAIsNotEqualCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedInt32ComparisonCase.getNumber()).thenReturn(4);
        when(mockedInt32ComparisonData.getIsNotEqual()).thenReturn(
                Int32SingleComparisonData.newBuilder().setValue(DEFAULT_VALUE).build()
        );

        assertThat(IntegerPredicateBuilder.build(urbanProperty.bathrooms, mockedInt32ComparisonData))
                .hasToString("Optional[urbanProperty.bathrooms != 100]");
    }

    @Test
    @DisplayName("given a is greater case comparison when calls build then expects")
    void givenAIsGreaterCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedInt32ComparisonCase.getNumber()).thenReturn(5);
        when(mockedInt32ComparisonData.getIsGreater()).thenReturn(
                Int32SingleComparisonData.newBuilder().setValue(DEFAULT_VALUE).build()
        );

        assertThat(IntegerPredicateBuilder.build(urbanProperty.bathrooms, mockedInt32ComparisonData))
                .hasToString("Optional[urbanProperty.bathrooms > 100]");
    }

    @Test
    @DisplayName("given a is greater or equal case comparison when calls build then expects")
    void givenAIsGreaterOrEqualCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedInt32ComparisonCase.getNumber()).thenReturn(6);
        when(mockedInt32ComparisonData.getIsGreaterOrEqual()).thenReturn(
                Int32SingleComparisonData.newBuilder().setValue(DEFAULT_VALUE).build()
        );

        assertThat(IntegerPredicateBuilder.build(urbanProperty.bathrooms, mockedInt32ComparisonData))
                .hasToString("Optional[urbanProperty.bathrooms >= 100]");
    }

    @Test
    @DisplayName("given a is lesser case comparison when calls build then expects")
    void givenAIsLesserCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedInt32ComparisonCase.getNumber()).thenReturn(7);
        when(mockedInt32ComparisonData.getIsLesser()).thenReturn(
                Int32SingleComparisonData.newBuilder().setValue(DEFAULT_VALUE).build()
        );

        assertThat(IntegerPredicateBuilder.build(urbanProperty.bathrooms, mockedInt32ComparisonData))
                .hasToString("Optional[urbanProperty.bathrooms < 100]");
    }

    @Test
    @DisplayName("given a is lesser or equal case comparison when calls build then expects")
    void givenAIsLesserOrEqualCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedInt32ComparisonCase.getNumber()).thenReturn(8);
        when(mockedInt32ComparisonData.getIsLesserOrEqual()).thenReturn(
                Int32SingleComparisonData.newBuilder().setValue(DEFAULT_VALUE).build()
        );

        assertThat(IntegerPredicateBuilder.build(urbanProperty.bathrooms, mockedInt32ComparisonData))
                .hasToString("Optional[urbanProperty.bathrooms <= 100]");
    }

    @Test
    @DisplayName("given a is between case comparison when calls build then expects")
    void givenAIsBetweenCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedInt32ComparisonCase.getNumber()).thenReturn(9);
        when(mockedInt32ComparisonData.getIsBetween()).thenReturn(
                Int32IntervalComparisonData.newBuilder()
                                           .setStart(DEFAULT_VALUE)
                                           .setEnd(DEFAULT_VALUE)
                                           .build()
        );

        assertThat(IntegerPredicateBuilder.build(urbanProperty.bathrooms, mockedInt32ComparisonData))
                .hasToString("Optional[urbanProperty.bathrooms between 100 and 100]");
    }

    @Test
    @DisplayName("given a is not between case comparison when calls build then expects")
    void givenAIsNotBetweenCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedInt32ComparisonCase.getNumber()).thenReturn(10);
        when(mockedInt32ComparisonData.getIsNotBetween()).thenReturn(
                Int32IntervalComparisonData.newBuilder()
                                           .setStart(DEFAULT_VALUE)
                                           .setEnd(DEFAULT_VALUE)
                                           .build()
        );

        assertThat(IntegerPredicateBuilder.build(urbanProperty.bathrooms, mockedInt32ComparisonData))
                .hasToString("Optional[!(urbanProperty.bathrooms between 100 and 100)]");
    }

    @Test
    @DisplayName("given a is in case comparison when calls build then expects")
    void givenAIsInCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedInt32ComparisonCase.getNumber()).thenReturn(11);
        when(mockedInt32ComparisonData.getIsIn()).thenReturn(
                Int32ListComparisonData.newBuilder().addAllValues(List.of(100, 101)).build()
        );

        assertThat(IntegerPredicateBuilder.build(urbanProperty.bathrooms, mockedInt32ComparisonData))
                .hasToString("Optional[urbanProperty.bathrooms in [100, 101]]");
    }

    @Test
    @DisplayName("given a is not in case comparison when calls build then expects")
    void givenAIsNotInCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedInt32ComparisonCase.getNumber()).thenReturn(12);
        when(mockedInt32ComparisonData.getIsNotIn()).thenReturn(
                Int32ListComparisonData.newBuilder().addAllValues(List.of(100, 101)).build()
        );

        assertThat(IntegerPredicateBuilder.build(urbanProperty.bathrooms, mockedInt32ComparisonData))
                .hasToString("Optional[urbanProperty.bathrooms not in [100, 101]]");
    }

}
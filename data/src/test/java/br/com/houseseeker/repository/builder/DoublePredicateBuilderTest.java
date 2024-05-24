package br.com.houseseeker.repository.builder;

import br.com.houseseeker.domain.exception.GrpcStatusException;
import br.com.houseseeker.domain.proto.DoubleComparisonData;
import br.com.houseseeker.domain.proto.DoubleIntervalComparisonData;
import br.com.houseseeker.domain.proto.DoubleSingleComparisonData;
import io.grpc.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static br.com.houseseeker.entity.QDslUrbanProperty.urbanProperty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DoublePredicateBuilderTest {

    private static final Double DEFAULT_VALUE = 100.33;

    @Mock
    private DoubleComparisonData mockedDoubleComparisonData;

    @Mock
    private DoubleComparisonData.ComparisonCase mockedDoubleComparisonCase;

    @BeforeEach
    void setup() {
        when(mockedDoubleComparisonData.getComparisonCase()).thenReturn(mockedDoubleComparisonCase);
    }

    @Test
    @DisplayName("given a unknown case comparison when calls build then expects exception")
    void givenAUnknownCaseComparisonAppended_whenCallsBuild_thenExpectsException() {
        when(mockedDoubleComparisonCase.getNumber()).thenReturn(Integer.MAX_VALUE);

        assertThatThrownBy(() -> DoublePredicateBuilder.build(urbanProperty.sellPrice, mockedDoubleComparisonData))
                .isInstanceOf(GrpcStatusException.class)
                .hasFieldOrPropertyWithValue("status", Status.INVALID_ARGUMENT)
                .hasMessage("Unknown comparison case");
    }

    @Test
    @DisplayName("given a is null true case comparison when calls build then expects")
    void givenAIsNullTrueCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedDoubleComparisonCase.getNumber()).thenReturn(1);
        when(mockedDoubleComparisonData.getIsNull()).thenReturn(true);

        assertThat(DoublePredicateBuilder.build(urbanProperty.sellPrice, mockedDoubleComparisonData))
                .hasToString("Optional[urbanProperty.sellPrice is null]");
    }

    @Test
    @DisplayName("given a is null false case comparison when calls build then expects")
    void givenAIsNullFalseCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedDoubleComparisonCase.getNumber()).thenReturn(1);
        when(mockedDoubleComparisonData.getIsNull()).thenReturn(false);

        assertThat(DoublePredicateBuilder.build(urbanProperty.sellPrice, mockedDoubleComparisonData)).isEmpty();
    }

    @Test
    @DisplayName("given a is not null true case comparison when calls build then expects")
    void givenAIsNotNullTrueCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedDoubleComparisonCase.getNumber()).thenReturn(2);
        when(mockedDoubleComparisonData.getIsNotNull()).thenReturn(true);

        assertThat(DoublePredicateBuilder.build(urbanProperty.sellPrice, mockedDoubleComparisonData))
                .hasToString("Optional[urbanProperty.sellPrice is not null]");
    }

    @Test
    @DisplayName("given a is not null false case comparison when calls build then expects")
    void givenAIsNotNullFalseCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedDoubleComparisonCase.getNumber()).thenReturn(2);
        when(mockedDoubleComparisonData.getIsNotNull()).thenReturn(false);

        assertThat(DoublePredicateBuilder.build(urbanProperty.sellPrice, mockedDoubleComparisonData)).isEmpty();
    }

    @Test
    @DisplayName("given a is equal case comparison when calls build then expects")
    void givenAIsEqualCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedDoubleComparisonCase.getNumber()).thenReturn(3);
        when(mockedDoubleComparisonData.getIsEqual()).thenReturn(
                DoubleSingleComparisonData.newBuilder().setValue(DEFAULT_VALUE).build()
        );

        assertThat(DoublePredicateBuilder.build(urbanProperty.sellPrice, mockedDoubleComparisonData))
                .hasToString("Optional[urbanProperty.sellPrice = 100.33]");
    }

    @Test
    @DisplayName("given a is not equal case comparison when calls build then expects")
    void givenAIsNotEqualCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedDoubleComparisonCase.getNumber()).thenReturn(4);
        when(mockedDoubleComparisonData.getIsNotEqual()).thenReturn(
                DoubleSingleComparisonData.newBuilder().setValue(DEFAULT_VALUE).build()
        );

        assertThat(DoublePredicateBuilder.build(urbanProperty.sellPrice, mockedDoubleComparisonData))
                .hasToString("Optional[urbanProperty.sellPrice != 100.33]");
    }

    @Test
    @DisplayName("given a is greater case comparison when calls build then expects")
    void givenAIsGreaterCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedDoubleComparisonCase.getNumber()).thenReturn(5);
        when(mockedDoubleComparisonData.getIsGreater()).thenReturn(
                DoubleSingleComparisonData.newBuilder().setValue(DEFAULT_VALUE).build()
        );

        assertThat(DoublePredicateBuilder.build(urbanProperty.sellPrice, mockedDoubleComparisonData))
                .hasToString("Optional[urbanProperty.sellPrice > 100.33]");
    }

    @Test
    @DisplayName("given a is greater or equal case comparison when calls build then expects")
    void givenAIsGreaterOrEqualCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedDoubleComparisonCase.getNumber()).thenReturn(6);
        when(mockedDoubleComparisonData.getIsGreaterOrEqual()).thenReturn(
                DoubleSingleComparisonData.newBuilder().setValue(DEFAULT_VALUE).build()
        );

        assertThat(DoublePredicateBuilder.build(urbanProperty.sellPrice, mockedDoubleComparisonData))
                .hasToString("Optional[urbanProperty.sellPrice >= 100.33]");
    }

    @Test
    @DisplayName("given a is lesser case comparison when calls build then expects")
    void givenAIsLesserCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedDoubleComparisonCase.getNumber()).thenReturn(7);
        when(mockedDoubleComparisonData.getIsLesser()).thenReturn(
                DoubleSingleComparisonData.newBuilder().setValue(DEFAULT_VALUE).build()
        );

        assertThat(DoublePredicateBuilder.build(urbanProperty.sellPrice, mockedDoubleComparisonData))
                .hasToString("Optional[urbanProperty.sellPrice < 100.33]");
    }

    @Test
    @DisplayName("given a is lesser or equal case comparison when calls build then expects")
    void givenAIsLesserOrEqualCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedDoubleComparisonCase.getNumber()).thenReturn(8);
        when(mockedDoubleComparisonData.getIsLesserOrEqual()).thenReturn(
                DoubleSingleComparisonData.newBuilder().setValue(DEFAULT_VALUE).build()
        );

        assertThat(DoublePredicateBuilder.build(urbanProperty.sellPrice, mockedDoubleComparisonData))
                .hasToString("Optional[urbanProperty.sellPrice <= 100.33]");
    }

    @Test
    @DisplayName("given a is between case comparison when calls build then expects")
    void givenAIsBetweenCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedDoubleComparisonCase.getNumber()).thenReturn(9);
        when(mockedDoubleComparisonData.getIsBetween()).thenReturn(
                DoubleIntervalComparisonData.newBuilder()
                                            .setStart(DEFAULT_VALUE)
                                            .setEnd(DEFAULT_VALUE)
                                            .build()
        );

        assertThat(DoublePredicateBuilder.build(urbanProperty.sellPrice, mockedDoubleComparisonData))
                .hasToString("Optional[urbanProperty.sellPrice between 100.33 and 100.33]");
    }

    @Test
    @DisplayName("given a is not between case comparison when calls build then expects")
    void givenAIsNotBetweenCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedDoubleComparisonCase.getNumber()).thenReturn(10);
        when(mockedDoubleComparisonData.getIsNotBetween()).thenReturn(
                DoubleIntervalComparisonData.newBuilder()
                                            .setStart(DEFAULT_VALUE)
                                            .setEnd(DEFAULT_VALUE)
                                            .build()
        );

        assertThat(DoublePredicateBuilder.build(urbanProperty.sellPrice, mockedDoubleComparisonData))
                .hasToString("Optional[!(urbanProperty.sellPrice between 100.33 and 100.33)]");
    }

}
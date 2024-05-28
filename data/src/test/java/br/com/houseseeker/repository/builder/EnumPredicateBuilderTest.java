package br.com.houseseeker.repository.builder;

import br.com.houseseeker.domain.exception.GrpcStatusException;
import br.com.houseseeker.domain.property.UrbanPropertyContract;
import br.com.houseseeker.domain.proto.EnumComparisonData;
import br.com.houseseeker.domain.proto.EnumListComparisonData;
import br.com.houseseeker.domain.proto.EnumSingleComparisonData;
import io.grpc.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static br.com.houseseeker.domain.property.UrbanPropertyContract.RENT;
import static br.com.houseseeker.domain.property.UrbanPropertyContract.SELL;
import static br.com.houseseeker.entity.QDslUrbanProperty.urbanProperty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnumPredicateBuilderTest {

    @Mock
    private EnumComparisonData mockedEnumComparisonData;

    @Mock
    private EnumComparisonData.ComparisonCase mockedEnumComparisonCase;

    @BeforeEach
    void setup() {
        when(mockedEnumComparisonData.getComparisonCase()).thenReturn(mockedEnumComparisonCase);
    }

    @Test
    @DisplayName("given a unknown case comparison when calls build then expects exception")
    void givenAUnknownCaseComparisonAppended_whenCallsBuild_thenExpectsException() {
        when(mockedEnumComparisonCase.getNumber()).thenReturn(Integer.MAX_VALUE);

        assertThatThrownBy(() -> EnumPredicateBuilder.build(urbanProperty.contract, mockedEnumComparisonData, UrbanPropertyContract::valueOf))
                .isInstanceOf(GrpcStatusException.class)
                .hasFieldOrPropertyWithValue("status", Status.INVALID_ARGUMENT)
                .hasMessage("Unknown comparison case");
    }

    @Test
    @DisplayName("given a is null true case comparison when calls build then expects")
    void givenAIsNullTrueCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedEnumComparisonCase.getNumber()).thenReturn(1);
        when(mockedEnumComparisonData.getIsNull()).thenReturn(true);

        assertThat(EnumPredicateBuilder.build(urbanProperty.contract, mockedEnumComparisonData, UrbanPropertyContract::valueOf))
                .hasToString("Optional[urbanProperty.contract is null]");
    }

    @Test
    @DisplayName("given a is null false case comparison when calls build then expects")
    void givenAIsNullFalseCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedEnumComparisonCase.getNumber()).thenReturn(1);
        when(mockedEnumComparisonData.getIsNull()).thenReturn(false);

        assertThat(EnumPredicateBuilder.build(urbanProperty.contract, mockedEnumComparisonData, UrbanPropertyContract::valueOf))
                .isEmpty();
    }

    @Test
    @DisplayName("given a is not null true case comparison when calls build then expects")
    void givenAIsNotNullTrueCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedEnumComparisonCase.getNumber()).thenReturn(2);
        when(mockedEnumComparisonData.getIsNotNull()).thenReturn(true);

        assertThat(EnumPredicateBuilder.build(urbanProperty.contract, mockedEnumComparisonData, UrbanPropertyContract::valueOf))
                .hasToString("Optional[urbanProperty.contract is not null]");
    }

    @Test
    @DisplayName("given a is not null false case comparison when calls build then expects")
    void givenAIsNotNullFalseCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedEnumComparisonCase.getNumber()).thenReturn(2);
        when(mockedEnumComparisonData.getIsNotNull()).thenReturn(false);

        assertThat(EnumPredicateBuilder.build(urbanProperty.contract, mockedEnumComparisonData, UrbanPropertyContract::valueOf))
                .isEmpty();
    }

    @Test
    @DisplayName("given a is equal case comparison when calls build then expects")
    void givenAIsEqualCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedEnumComparisonCase.getNumber()).thenReturn(3);
        when(mockedEnumComparisonData.getIsEqual()).thenReturn(
                EnumSingleComparisonData.newBuilder().setValue(SELL.name()).build()
        );

        assertThat(EnumPredicateBuilder.build(urbanProperty.contract, mockedEnumComparisonData, UrbanPropertyContract::valueOf))
                .hasToString("Optional[urbanProperty.contract = SELL]");
    }

    @Test
    @DisplayName("given a is not equal case comparison when calls build then expects")
    void givenAIsNotEqualCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedEnumComparisonCase.getNumber()).thenReturn(4);
        when(mockedEnumComparisonData.getIsNotEqual()).thenReturn(
                EnumSingleComparisonData.newBuilder().setValue(SELL.name()).build()
        );

        assertThat(EnumPredicateBuilder.build(urbanProperty.contract, mockedEnumComparisonData, UrbanPropertyContract::valueOf))
                .hasToString("Optional[urbanProperty.contract != SELL]");
    }

    @Test
    @DisplayName("given a is in case comparison when calls build then expects")
    void givenAIsInCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedEnumComparisonCase.getNumber()).thenReturn(5);
        when(mockedEnumComparisonData.getIsIn()).thenReturn(
                EnumListComparisonData.newBuilder().addAllValues(List.of(SELL.name(), RENT.name())).build()
        );

        assertThat(EnumPredicateBuilder.build(urbanProperty.contract, mockedEnumComparisonData, UrbanPropertyContract::valueOf))
                .hasToString("Optional[urbanProperty.contract in [SELL, RENT]]");
    }

    @Test
    @DisplayName("given a is not in case comparison when calls build then expects")
    void givenAIsNotInCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedEnumComparisonCase.getNumber()).thenReturn(6);
        when(mockedEnumComparisonData.getIsNotIn()).thenReturn(
                EnumListComparisonData.newBuilder().addAllValues(List.of(SELL.name(), RENT.name())).build()
        );

        assertThat(EnumPredicateBuilder.build(urbanProperty.contract, mockedEnumComparisonData, UrbanPropertyContract::valueOf))
                .hasToString("Optional[urbanProperty.contract not in [SELL, RENT]]");
    }

}
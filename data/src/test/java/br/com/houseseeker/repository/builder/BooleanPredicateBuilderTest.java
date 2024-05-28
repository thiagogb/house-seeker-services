package br.com.houseseeker.repository.builder;

import br.com.houseseeker.domain.exception.GrpcStatusException;
import br.com.houseseeker.domain.proto.BoolComparisonData;
import br.com.houseseeker.domain.proto.BoolSingleComparisonData;
import io.grpc.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static br.com.houseseeker.entity.QDslProvider.provider;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BooleanPredicateBuilderTest {

    @Mock
    private BoolComparisonData mockedBoolComparisonData;

    @Mock
    private BoolComparisonData.ComparisonCase mockedBoolComparisonCase;

    @BeforeEach
    void setup() {
        when(mockedBoolComparisonData.getComparisonCase()).thenReturn(mockedBoolComparisonCase);
    }

    @Test
    @DisplayName("given a unknown boolean case comparison when calls build then expects exception")
    void givenAUnknownCaseComparisonAppended_whenCallsBuild_thenExpectsException() {
        when(mockedBoolComparisonCase.getNumber()).thenReturn(Integer.MAX_VALUE);

        assertThatThrownBy(() -> BooleanPredicateBuilder.build(provider.active, mockedBoolComparisonData))
                .isInstanceOf(GrpcStatusException.class)
                .hasFieldOrPropertyWithValue("status", Status.INVALID_ARGUMENT)
                .hasMessage("Unknown comparison case");
    }

    @Test
    @DisplayName("given a is null true case comparison when calls build then expects")
    void givenAIsNullTrueCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedBoolComparisonCase.getNumber()).thenReturn(1);
        when(mockedBoolComparisonData.getIsNull()).thenReturn(true);

        assertThat(BooleanPredicateBuilder.build(provider.active, mockedBoolComparisonData))
                .hasToString("Optional[provider.active is null]");
    }

    @Test
    @DisplayName("given a is null false case comparison when calls build then expects")
    void givenAIsNullFalseCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedBoolComparisonCase.getNumber()).thenReturn(1);
        when(mockedBoolComparisonData.getIsNull()).thenReturn(false);

        assertThat(BooleanPredicateBuilder.build(provider.active, mockedBoolComparisonData)).isEmpty();
    }

    @Test
    @DisplayName("given a is not null true case comparison when calls build then expects")
    void givenAIsNotNullTrueCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedBoolComparisonCase.getNumber()).thenReturn(2);
        when(mockedBoolComparisonData.getIsNotNull()).thenReturn(true);

        assertThat(BooleanPredicateBuilder.build(provider.active, mockedBoolComparisonData))
                .hasToString("Optional[provider.active is not null]");
    }

    @Test
    @DisplayName("given a is not null false case comparison when calls build then expects")
    void givenAIsNotNullFalseCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedBoolComparisonCase.getNumber()).thenReturn(2);
        when(mockedBoolComparisonData.getIsNotNull()).thenReturn(false);

        assertThat(BooleanPredicateBuilder.build(provider.active, mockedBoolComparisonData)).isEmpty();
    }

    @Test
    @DisplayName("given a is equal case comparison when calls build then expects")
    void givenAIsEqualCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedBoolComparisonCase.getNumber()).thenReturn(3);
        when(mockedBoolComparisonData.getIsEqual()).thenReturn(
                BoolSingleComparisonData.newBuilder().setValue(true).build()
        );

        assertThat(BooleanPredicateBuilder.build(provider.active, mockedBoolComparisonData))
                .hasToString("Optional[provider.active = true]");
    }

    @Test
    @DisplayName("given a is not equal case comparison when calls build then expects")
    void givenAIsNotEqualCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedBoolComparisonCase.getNumber()).thenReturn(4);
        when(mockedBoolComparisonData.getIsNotEqual()).thenReturn(
                BoolSingleComparisonData.newBuilder().setValue(true).build()
        );

        assertThat(BooleanPredicateBuilder.build(provider.active, mockedBoolComparisonData))
                .hasToString("Optional[provider.active != true]");
    }

}
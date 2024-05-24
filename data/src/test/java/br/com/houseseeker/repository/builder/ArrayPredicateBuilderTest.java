package br.com.houseseeker.repository.builder;

import br.com.houseseeker.domain.exception.GrpcStatusException;
import br.com.houseseeker.domain.proto.BytesComparisonData;
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
class ArrayPredicateBuilderTest {

    @Mock
    private BytesComparisonData mockedBytesComparisonData;

    @Mock
    private BytesComparisonData.ComparisonCase mockedBytesComparisonCase;

    @BeforeEach
    void setup() {
        when(mockedBytesComparisonData.getComparisonCase()).thenReturn(mockedBytesComparisonCase);
    }

    @Test
    @DisplayName("given a unknown array case comparison when calls build then expects exception")
    void givenAUnknownCaseComparison_whenCallsBuild_thenExpectsException() {
        when(mockedBytesComparisonCase.getNumber()).thenReturn(Integer.MAX_VALUE);

        assertThatThrownBy(() -> ArrayPredicateBuilder.build(provider.logo, mockedBytesComparisonData))
                .isInstanceOf(GrpcStatusException.class)
                .hasFieldOrPropertyWithValue("status", Status.INVALID_ARGUMENT)
                .hasMessage("Unknown comparison case");
    }

    @Test
    @DisplayName("given a is null true case comparison when calls build then expects")
    void givenAIsNullTrueCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedBytesComparisonCase.getNumber()).thenReturn(1);
        when(mockedBytesComparisonData.getIsNull()).thenReturn(true);

        assertThat(ArrayPredicateBuilder.build(provider.logo, mockedBytesComparisonData))
                .hasToString("Optional[provider.logo is null]");
    }

    @Test
    @DisplayName("given a is null false case comparison when calls build then expects")
    void givenAIsNullFalseCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedBytesComparisonCase.getNumber()).thenReturn(1);
        when(mockedBytesComparisonData.getIsNull()).thenReturn(false);

        assertThat(ArrayPredicateBuilder.build(provider.logo, mockedBytesComparisonData)).isEmpty();
    }

    @Test
    @DisplayName("given a is not null true case comparison when calls build then expects")
    void givenAIsNotNullTrueCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedBytesComparisonCase.getNumber()).thenReturn(2);
        when(mockedBytesComparisonData.getIsNotNull()).thenReturn(true);

        assertThat(ArrayPredicateBuilder.build(provider.logo, mockedBytesComparisonData))
                .hasToString("Optional[provider.logo is not null]");
    }

    @Test
    @DisplayName("given a is not null false case comparison when calls build then expects")
    void givenAIsNotNullFalseCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedBytesComparisonCase.getNumber()).thenReturn(2);
        when(mockedBytesComparisonData.getIsNotNull()).thenReturn(false);

        assertThat(ArrayPredicateBuilder.build(provider.logo, mockedBytesComparisonData)).isEmpty();
    }

}
package br.com.houseseeker.repository.builder;

import br.com.houseseeker.domain.exception.GrpcStatusException;
import br.com.houseseeker.domain.proto.DateTimeComparisonData;
import br.com.houseseeker.domain.proto.DateTimeIntervalComparisonData;
import br.com.houseseeker.domain.proto.DateTimeSingleComparisonData;
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
class DateTimePredicateBuilderTest {

    private static final String DEFAULT_DATE_TIME = "2020-01-01T12:30:45";

    @Mock
    private DateTimeComparisonData mockedDateTimeComparisonData;

    @Mock
    private DateTimeComparisonData.ComparisonCase mockedDateTimeComparisonCase;

    @BeforeEach
    void setup() {
        when(mockedDateTimeComparisonData.getComparisonCase()).thenReturn(mockedDateTimeComparisonCase);
    }

    @Test
    @DisplayName("given a unknown case comparison when calls build then expects exception")
    void givenAUnknownCaseComparisonAppended_whenCallsBuild_thenExpectsException() {
        when(mockedDateTimeComparisonCase.getNumber()).thenReturn(Integer.MAX_VALUE);

        assertThatThrownBy(() -> DateTimePredicateBuilder.build(urbanProperty.creationDate, mockedDateTimeComparisonData))
                .isInstanceOf(GrpcStatusException.class)
                .hasFieldOrPropertyWithValue("status", Status.INVALID_ARGUMENT)
                .hasMessage("Unknown comparison case");
    }

    @Test
    @DisplayName("given a is null true case comparison when calls build then expects")
    void givenAIsNullTrueCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedDateTimeComparisonCase.getNumber()).thenReturn(1);
        when(mockedDateTimeComparisonData.getIsNull()).thenReturn(true);

        assertThat(DateTimePredicateBuilder.build(urbanProperty.creationDate, mockedDateTimeComparisonData))
                .hasToString("Optional[urbanProperty.creationDate is null]");
    }

    @Test
    @DisplayName("given a is null false case comparison when calls build then expects")
    void givenAIsNullFalseCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedDateTimeComparisonCase.getNumber()).thenReturn(1);
        when(mockedDateTimeComparisonData.getIsNull()).thenReturn(false);

        assertThat(DateTimePredicateBuilder.build(urbanProperty.creationDate, mockedDateTimeComparisonData)).isEmpty();
    }

    @Test
    @DisplayName("given a is not null true case comparison when calls build then expects")
    void givenAIsNotNullTrueCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedDateTimeComparisonCase.getNumber()).thenReturn(2);
        when(mockedDateTimeComparisonData.getIsNotNull()).thenReturn(true);

        assertThat(DateTimePredicateBuilder.build(urbanProperty.creationDate, mockedDateTimeComparisonData))
                .hasToString("Optional[urbanProperty.creationDate is not null]");
    }

    @Test
    @DisplayName("given a is not null false case comparison when calls build then expects")
    void givenAIsNotNullFalseCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedDateTimeComparisonCase.getNumber()).thenReturn(2);
        when(mockedDateTimeComparisonData.getIsNotNull()).thenReturn(false);

        assertThat(DateTimePredicateBuilder.build(urbanProperty.creationDate, mockedDateTimeComparisonData)).isEmpty();
    }

    @Test
    @DisplayName("given a is equal case comparison when calls build then expects")
    void givenAIsEqualCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedDateTimeComparisonCase.getNumber()).thenReturn(3);
        when(mockedDateTimeComparisonData.getIsEqual()).thenReturn(
                DateTimeSingleComparisonData.newBuilder().setValue(DEFAULT_DATE_TIME).build()
        );

        assertThat(DateTimePredicateBuilder.build(urbanProperty.creationDate, mockedDateTimeComparisonData))
                .hasToString("Optional[urbanProperty.creationDate = 2020-01-01T12:30:45]");
    }

    @Test
    @DisplayName("given a is not equal case comparison when calls build then expects")
    void givenAIsNotEqualCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedDateTimeComparisonCase.getNumber()).thenReturn(4);
        when(mockedDateTimeComparisonData.getIsNotEqual()).thenReturn(
                DateTimeSingleComparisonData.newBuilder().setValue(DEFAULT_DATE_TIME).build()
        );

        assertThat(DateTimePredicateBuilder.build(urbanProperty.creationDate, mockedDateTimeComparisonData))
                .hasToString("Optional[urbanProperty.creationDate != 2020-01-01T12:30:45]");
    }

    @Test
    @DisplayName("given a is greater case comparison when calls build then expects")
    void givenAIsGreaterCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedDateTimeComparisonCase.getNumber()).thenReturn(5);
        when(mockedDateTimeComparisonData.getIsGreater()).thenReturn(
                DateTimeSingleComparisonData.newBuilder().setValue(DEFAULT_DATE_TIME).build()
        );

        assertThat(DateTimePredicateBuilder.build(urbanProperty.creationDate, mockedDateTimeComparisonData))
                .hasToString("Optional[urbanProperty.creationDate > 2020-01-01T12:30:45]");
    }

    @Test
    @DisplayName("given a is greater or equal case comparison when calls build then expects")
    void givenAIsGreaterOrEqualCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedDateTimeComparisonCase.getNumber()).thenReturn(6);
        when(mockedDateTimeComparisonData.getIsGreaterOrEqual()).thenReturn(
                DateTimeSingleComparisonData.newBuilder().setValue(DEFAULT_DATE_TIME).build()
        );

        assertThat(DateTimePredicateBuilder.build(urbanProperty.creationDate, mockedDateTimeComparisonData))
                .hasToString("Optional[urbanProperty.creationDate >= 2020-01-01T12:30:45]");
    }

    @Test
    @DisplayName("given a is lesser case comparison when calls build then expects")
    void givenAIsLesserCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedDateTimeComparisonCase.getNumber()).thenReturn(7);
        when(mockedDateTimeComparisonData.getIsLesser()).thenReturn(
                DateTimeSingleComparisonData.newBuilder().setValue(DEFAULT_DATE_TIME).build()
        );

        assertThat(DateTimePredicateBuilder.build(urbanProperty.creationDate, mockedDateTimeComparisonData))
                .hasToString("Optional[urbanProperty.creationDate < 2020-01-01T12:30:45]");
    }

    @Test
    @DisplayName("given a is lesser or equal case comparison when calls build then expects")
    void givenAIsLesserOrEqualCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedDateTimeComparisonCase.getNumber()).thenReturn(8);
        when(mockedDateTimeComparisonData.getIsLesserOrEqual()).thenReturn(
                DateTimeSingleComparisonData.newBuilder().setValue(DEFAULT_DATE_TIME).build()
        );

        assertThat(DateTimePredicateBuilder.build(urbanProperty.creationDate, mockedDateTimeComparisonData))
                .hasToString("Optional[urbanProperty.creationDate <= 2020-01-01T12:30:45]");
    }

    @Test
    @DisplayName("given a is between case comparison when calls build then expects")
    void givenAIsBetweenCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedDateTimeComparisonCase.getNumber()).thenReturn(9);
        when(mockedDateTimeComparisonData.getIsBetween()).thenReturn(
                DateTimeIntervalComparisonData.newBuilder()
                                              .setStart(DEFAULT_DATE_TIME)
                                              .setEnd(DEFAULT_DATE_TIME)
                                              .build()
        );

        assertThat(DateTimePredicateBuilder.build(urbanProperty.creationDate, mockedDateTimeComparisonData))
                .hasToString("Optional[urbanProperty.creationDate between 2020-01-01T12:30:45 and 2020-01-01T12:30:45]");
    }

    @Test
    @DisplayName("given a is not between case comparison when calls build then expects")
    void givenAIsNotBetweenCaseComparison_whenCallsBuild_thenExpects() {
        when(mockedDateTimeComparisonCase.getNumber()).thenReturn(10);
        when(mockedDateTimeComparisonData.getIsNotBetween()).thenReturn(
                DateTimeIntervalComparisonData.newBuilder()
                                              .setStart(DEFAULT_DATE_TIME)
                                              .setEnd(DEFAULT_DATE_TIME)
                                              .build()
        );

        assertThat(DateTimePredicateBuilder.build(urbanProperty.creationDate, mockedDateTimeComparisonData))
                .hasToString("Optional[!(urbanProperty.creationDate between 2020-01-01T12:30:45 and 2020-01-01T12:30:45)]");
    }

}
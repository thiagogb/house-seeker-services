package br.com.houseseeker.repository.builder;

import br.com.houseseeker.domain.exception.GrpcStatusException;
import br.com.houseseeker.domain.proto.BoolComparisonData;
import br.com.houseseeker.domain.proto.BoolSingleComparisonData;
import br.com.houseseeker.domain.proto.BytesComparisonData;
import br.com.houseseeker.domain.proto.EnumComparisonData;
import br.com.houseseeker.domain.proto.EnumListComparisonData;
import br.com.houseseeker.domain.proto.EnumSingleComparisonData;
import br.com.houseseeker.domain.proto.Int32ComparisonData;
import br.com.houseseeker.domain.proto.Int32IntervalComparisonData;
import br.com.houseseeker.domain.proto.Int32ListComparisonData;
import br.com.houseseeker.domain.proto.Int32SingleComparisonData;
import br.com.houseseeker.domain.proto.StringComparisonData;
import br.com.houseseeker.domain.proto.StringListComparisonData;
import br.com.houseseeker.domain.proto.StringSingleComparisonData;
import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.entity.QProvider;
import com.querydsl.core.types.Predicate;
import io.grpc.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class PredicateBuilderTest {

    @Mock
    private BoolComparisonData mockedBoolComparisonData;

    @Mock
    private BoolComparisonData.ComparisonCase mockedBoolComparisonCase;

    @Mock
    private Int32ComparisonData mockedIntegerComparisonData;

    @Mock
    private Int32ComparisonData.ComparisonCase mockedIntegerComparisonCase;

    @Mock
    private StringComparisonData mockedStringComparisonData;

    @Mock
    private StringComparisonData.ComparisonCase mockedStringComparisonCase;

    @Mock
    private EnumComparisonData mockedEnumComparisonData;

    @Mock
    private EnumComparisonData.ComparisonCase mockedEnumComparisonCase;

    @Mock
    private BytesComparisonData mockedBytesComparisonData;

    @Mock
    private BytesComparisonData.ComparisonCase mockedBytesComparisonCase;

    @BeforeEach
    void setup() {
        lenient().when(mockedBoolComparisonData.getComparisonCase()).thenReturn(mockedBoolComparisonCase);
        lenient().when(mockedBoolComparisonCase.getNumber()).thenReturn(Integer.MAX_VALUE);

        lenient().when(mockedIntegerComparisonData.getComparisonCase()).thenReturn(mockedIntegerComparisonCase);
        lenient().when(mockedIntegerComparisonCase.getNumber()).thenReturn(Integer.MAX_VALUE);

        lenient().when(mockedStringComparisonData.getComparisonCase()).thenReturn(mockedStringComparisonCase);
        lenient().when(mockedStringComparisonCase.getNumber()).thenReturn(Integer.MAX_VALUE);

        lenient().when(mockedEnumComparisonData.getComparisonCase()).thenReturn(mockedEnumComparisonCase);
        lenient().when(mockedEnumComparisonCase.getNumber()).thenReturn(Integer.MAX_VALUE);

        lenient().when(mockedBytesComparisonData.getComparisonCase()).thenReturn(mockedBytesComparisonCase);
        lenient().when(mockedBytesComparisonCase.getNumber()).thenReturn(Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("given a builder without clauses appended when calls build then expects empty")
    void givenABuilderWithoutClassesAppended_whenCallsBuild_thenExpectsEmpty() {
        assertThat(PredicateBuilder.newInstance().build()).isEmpty();
    }

    @Test
    @DisplayName("given a builder with a boolean not chosen comparison appended when calls build then expects empty")
    void givenABuilderWithABooleanNotChosenComparisonAppended_whenCallsBuild_thenExpectsEmpty() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(QProvider.provider.active, BoolComparisonData.getDefaultInstance())
                                             .build();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("given a builder with a boolean is null comparison appended when calls build then expects")
    void givenABuilderWithABooleanIsNullComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(QProvider.provider.active, BoolComparisonData.newBuilder().setIsNull(true).build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.active is null");
    }

    @Test
    @DisplayName("given a builder with a boolean is not null comparison appended when calls build then expects")
    void givenABuilderWithABooleanIsNotNullComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(QProvider.provider.active, BoolComparisonData.newBuilder().setIsNotNull(true).build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.active is not null");
    }

    @Test
    @DisplayName("given a builder with a boolean is equal comparison appended when calls build then expects")
    void givenABuilderWithABooleanIsEqualComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     QProvider.provider.active,
                                                     BoolComparisonData.newBuilder()
                                                                       .setIsEqual(
                                                                               BoolSingleComparisonData.newBuilder()
                                                                                                       .setValue(true)
                                                                                                       .build()
                                                                       )
                                                                       .build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.active = true");
    }

    @Test
    @DisplayName("given a builder with a boolean is not equal comparison appended when calls build then expects")
    void givenABuilderWithABooleanIsNotEqualComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     QProvider.provider.active,
                                                     BoolComparisonData.newBuilder()
                                                                       .setIsNotEqual(
                                                                               BoolSingleComparisonData.newBuilder()
                                                                                                       .setValue(false)
                                                                                                       .build()
                                                                       )
                                                                       .build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.active != false");
    }

    @Test
    @DisplayName("given a builder with a unknown boolean case comparison appended when calls build then expects exception")
    void givenABuilderWithAUnknownBooleanCaseComparisonAppended_whenCallsBuild_thenExpectsException() {
        PredicateBuilder builder = PredicateBuilder.newInstance();

        assertThatThrownBy(() -> builder.append(QProvider.provider.active, mockedBoolComparisonData))
                .isInstanceOf(GrpcStatusException.class)
                .hasFieldOrPropertyWithValue("status", Status.INVALID_ARGUMENT)
                .hasMessage("Unknown comparison case");
    }

    @Test
    @DisplayName("given a builder with a integer not chosen comparison appended when calls build then expects empty")
    void givenABuilderWithAIntegerNotChosenComparisonAppended_whenCallsBuild_thenExpectsEmpty() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(QProvider.provider.id, Int32ComparisonData.getDefaultInstance())
                                             .build();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("given a builder with a integer is null comparison appended when calls build then expects")
    void givenABuilderWithAIntegerIsNullComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(QProvider.provider.id, Int32ComparisonData.newBuilder().setIsNull(true).build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.id is null");
    }

    @Test
    @DisplayName("given a builder with a integer is not null comparison appended when calls build then expects")
    void givenABuilderWithAIntegerIsNotNullComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(QProvider.provider.id, Int32ComparisonData.newBuilder().setIsNotNull(true).build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.id is not null");
    }

    @Test
    @DisplayName("given a builder with a integer is equal comparison appended when calls build then expects")
    void givenABuilderWithAIntegerIsEqualComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     QProvider.provider.id,
                                                     Int32ComparisonData.newBuilder()
                                                                        .setIsEqual(
                                                                                Int32SingleComparisonData.newBuilder()
                                                                                                         .setValue(1)
                                                                                                         .build()
                                                                        )
                                                                        .build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.id = 1");
    }

    @Test
    @DisplayName("given a builder with a integer is not equal comparison appended when calls build then expects")
    void givenABuilderWithAIntegerIsNotEqualComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     QProvider.provider.id,
                                                     Int32ComparisonData.newBuilder()
                                                                        .setIsNotEqual(
                                                                                Int32SingleComparisonData.newBuilder()
                                                                                                         .setValue(1)
                                                                                                         .build()
                                                                        )
                                                                        .build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.id != 1");
    }

    @Test
    @DisplayName("given a builder with a integer is greater comparison appended when calls build then expects")
    void givenABuilderWithAIntegerIsGreaterComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     QProvider.provider.id,
                                                     Int32ComparisonData.newBuilder()
                                                                        .setIsGreater(
                                                                                Int32SingleComparisonData.newBuilder()
                                                                                                         .setValue(1)
                                                                                                         .build()
                                                                        )
                                                                        .build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.id > 1");
    }

    @Test
    @DisplayName("given a builder with a integer is greater or equal comparison appended when calls build then expects")
    void givenABuilderWithAIntegerIsGreaterOrEqualComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     QProvider.provider.id,
                                                     Int32ComparisonData.newBuilder()
                                                                        .setIsGreaterOrEqual(
                                                                                Int32SingleComparisonData.newBuilder()
                                                                                                         .setValue(1)
                                                                                                         .build()
                                                                        )
                                                                        .build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.id >= 1");
    }

    @Test
    @DisplayName("given a builder with a integer is lesser comparison appended when calls build then expects")
    void givenABuilderWithAIntegerIsLesserComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     QProvider.provider.id,
                                                     Int32ComparisonData.newBuilder()
                                                                        .setIsLesser(
                                                                                Int32SingleComparisonData.newBuilder()
                                                                                                         .setValue(1)
                                                                                                         .build()
                                                                        )
                                                                        .build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.id < 1");
    }

    @Test
    @DisplayName("given a builder with a integer is lesser or equal comparison appended when calls build then expects")
    void givenABuilderWithAIntegerIsLesserOrEqualComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     QProvider.provider.id,
                                                     Int32ComparisonData.newBuilder()
                                                                        .setIsLesserOrEqual(
                                                                                Int32SingleComparisonData.newBuilder()
                                                                                                         .setValue(1)
                                                                                                         .build()
                                                                        )
                                                                        .build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.id <= 1");
    }

    @Test
    @DisplayName("given a builder with a integer is between comparison appended when calls build then expects")
    void givenABuilderWithAIntegerIsBetweenComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     QProvider.provider.id,
                                                     Int32ComparisonData.newBuilder()
                                                                        .setIsBetween(
                                                                                Int32IntervalComparisonData.newBuilder()
                                                                                                           .setStart(1)
                                                                                                           .setEnd(2)
                                                                                                           .build()
                                                                        )
                                                                        .build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.id between 1 and 2");
    }

    @Test
    @DisplayName("given a builder with a integer is not between comparison appended when calls build then expects")
    void givenABuilderWithAIntegerIsNotBetweenComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     QProvider.provider.id,
                                                     Int32ComparisonData.newBuilder()
                                                                        .setIsNotBetween(
                                                                                Int32IntervalComparisonData.newBuilder()
                                                                                                           .setStart(1)
                                                                                                           .setEnd(2)
                                                                                                           .build()
                                                                        )
                                                                        .build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("!(provider.id between 1 and 2)");
    }

    @Test
    @DisplayName("given a builder with a integer is in comparison appended when calls build then expects")
    void givenABuilderWithAIntegerIsInComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     QProvider.provider.id,
                                                     Int32ComparisonData.newBuilder()
                                                                        .setIsIn(
                                                                                Int32ListComparisonData.newBuilder()
                                                                                                       .addValues(1)
                                                                                                       .addValues(2)
                                                                                                       .build()
                                                                        )
                                                                        .build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.id in [1, 2]");
    }

    @Test
    @DisplayName("given a builder with a integer is not in comparison appended when calls build then expects")
    void givenABuilderWithAIntegerIsNotInComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     QProvider.provider.id,
                                                     Int32ComparisonData.newBuilder()
                                                                        .setIsNotIn(
                                                                                Int32ListComparisonData.newBuilder()
                                                                                                       .addValues(1)
                                                                                                       .addValues(2)
                                                                                                       .build()
                                                                        )
                                                                        .build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.id not in [1, 2]");
    }

    @Test
    @DisplayName("given a builder with a unknown integer case comparison appended when calls build then expects exception")
    void givenABuilderWithAUnknownIntegerCaseComparisonAppended_whenCallsBuild_thenExpectsException() {
        PredicateBuilder builder = PredicateBuilder.newInstance();

        assertThatThrownBy(() -> builder.append(QProvider.provider.id, mockedIntegerComparisonData))
                .isInstanceOf(GrpcStatusException.class)
                .hasFieldOrPropertyWithValue("status", Status.INVALID_ARGUMENT)
                .hasMessage("Unknown comparison case");
    }

    @Test
    @DisplayName("given a builder with a string not chosen comparison appended when calls build then expects empty")
    void givenABuilderWithAStringNotChosenComparisonAppended_whenCallsBuild_thenExpectsEmpty() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(QProvider.provider.name, StringComparisonData.getDefaultInstance())
                                             .build();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("given a builder with a string is null comparison appended when calls build then expects")
    void givenABuilderWithAStringIsNullComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(QProvider.provider.name, StringComparisonData.newBuilder().setIsNull(true).build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.name is null");
    }

    @Test
    @DisplayName("given a builder with a string is not null comparison appended when calls build then expects")
    void givenABuilderWithAStringIsNotNullComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(QProvider.provider.name, StringComparisonData.newBuilder().setIsNotNull(true).build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.name is not null");
    }

    @Test
    @DisplayName("given a builder with a string is blank comparison appended when calls build then expects")
    void givenABuilderWithAStringIsBlankComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(QProvider.provider.name, StringComparisonData.newBuilder().setIsBlank(true).build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("empty(provider.name)");
    }

    @Test
    @DisplayName("given a builder with a string is not blank comparison appended when calls build then expects")
    void givenABuilderWithAStringIsNotBlankComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(QProvider.provider.name, StringComparisonData.newBuilder().setIsNotBlank(true).build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("!empty(provider.name)");
    }

    @Test
    @DisplayName("given a builder with a string is equal comparison appended when calls build then expects")
    void givenABuilderWithAStringIsEqualComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     QProvider.provider.name,
                                                     StringComparisonData.newBuilder()
                                                                         .setIsEqual(
                                                                                 StringSingleComparisonData.newBuilder()
                                                                                                           .setValue("X")
                                                                                                           .build()
                                                                         )
                                                                         .build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("eqIc(provider.name,X)");
    }

    @Test
    @DisplayName("given a builder with a string is not equal comparison appended when calls build then expects")
    void givenABuilderWithAStringIsNotEqualComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     QProvider.provider.name,
                                                     StringComparisonData.newBuilder()
                                                                         .setIsNotEqual(
                                                                                 StringSingleComparisonData.newBuilder()
                                                                                                           .setValue("X")
                                                                                                           .build()
                                                                         )
                                                                         .build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("!(eqIc(provider.name,X))");
    }

    @Test
    @DisplayName("given a builder with a string is starting with comparison appended when calls build then expects")
    void givenABuilderWithAStringIsStartingWithComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     QProvider.provider.name,
                                                     StringComparisonData.newBuilder()
                                                                         .setIsStartingWith(
                                                                                 StringSingleComparisonData.newBuilder()
                                                                                                           .setValue("X")
                                                                                                           .build()
                                                                         )
                                                                         .build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("startsWithIgnoreCase(provider.name,X)");
    }

    @Test
    @DisplayName("given a builder with a string is not starting with comparison appended when calls build then expects")
    void givenABuilderWithAStringIsNotStartingWithComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     QProvider.provider.name,
                                                     StringComparisonData.newBuilder()
                                                                         .setIsNotStartingWith(
                                                                                 StringSingleComparisonData.newBuilder()
                                                                                                           .setValue("X")
                                                                                                           .build()
                                                                         )
                                                                         .build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("!startsWithIgnoreCase(provider.name,X)");
    }

    @Test
    @DisplayName("given a builder with a string is ending with comparison appended when calls build then expects")
    void givenABuilderWithAStringIsEndingWithComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     QProvider.provider.name,
                                                     StringComparisonData.newBuilder()
                                                                         .setIsEndingWith(
                                                                                 StringSingleComparisonData.newBuilder()
                                                                                                           .setValue("X")
                                                                                                           .build()
                                                                         )
                                                                         .build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("endsWithIgnoreCase(provider.name,X)");
    }

    @Test
    @DisplayName("given a builder with a string is not ending with comparison appended when calls build then expects")
    void givenABuilderWithAStringIsNotEndingWithComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     QProvider.provider.name,
                                                     StringComparisonData.newBuilder()
                                                                         .setIsNotEndingWith(
                                                                                 StringSingleComparisonData.newBuilder()
                                                                                                           .setValue("X")
                                                                                                           .build()
                                                                         )
                                                                         .build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("!endsWithIgnoreCase(provider.name,X)");
    }

    @Test
    @DisplayName("given a builder with a string it contains with comparison appended when calls build then expects")
    void givenABuilderWithAStringItContainsWithComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     QProvider.provider.name,
                                                     StringComparisonData.newBuilder()
                                                                         .setItContains(
                                                                                 StringSingleComparisonData.newBuilder()
                                                                                                           .setValue("X")
                                                                                                           .build()
                                                                         )
                                                                         .build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("containsIc(provider.name,X)");
    }

    @Test
    @DisplayName("given a builder with a string it not contains with comparison appended when calls build then expects")
    void givenABuilderWithAStringItNotContainsWithComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     QProvider.provider.name,
                                                     StringComparisonData.newBuilder()
                                                                         .setItNotContains(
                                                                                 StringSingleComparisonData.newBuilder()
                                                                                                           .setValue("X")
                                                                                                           .build()
                                                                         )
                                                                         .build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("!containsIc(provider.name,X)");
    }

    @Test
    @DisplayName("given a builder with a string is in with comparison appended when calls build then expects")
    void givenABuilderWithAStringIsInWithComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     QProvider.provider.name,
                                                     StringComparisonData.newBuilder()
                                                                         .setIsIn(
                                                                                 StringListComparisonData.newBuilder()
                                                                                                         .addValues("X")
                                                                                                         .addValues("Y")
                                                                                                         .build()
                                                                         )
                                                                         .build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.name in [X, Y]");
    }

    @Test
    @DisplayName("given a builder with a string is not in with comparison appended when calls build then expects")
    void givenABuilderWithAStringIsNotInWithComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     QProvider.provider.name,
                                                     StringComparisonData.newBuilder()
                                                                         .setIsNotIn(
                                                                                 StringListComparisonData.newBuilder()
                                                                                                         .addValues("X")
                                                                                                         .addValues("Y")
                                                                                                         .build()
                                                                         )
                                                                         .build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.name not in [X, Y]");
    }

    @Test
    @DisplayName("given a builder with a unknown string case comparison appended when calls build then expects exception")
    void givenABuilderWithAUnknownStringCaseComparisonAppended_whenCallsBuild_thenExpectsException() {
        PredicateBuilder builder = PredicateBuilder.newInstance();

        assertThatThrownBy(() -> builder.append(QProvider.provider.name, mockedStringComparisonData))
                .isInstanceOf(GrpcStatusException.class)
                .hasFieldOrPropertyWithValue("status", Status.INVALID_ARGUMENT)
                .hasMessage("Unknown comparison case");
    }

    @Test
    @DisplayName("given a builder with a enum not chosen comparison appended when calls build then expects empty")
    void givenABuilderWithAEnumNotChosenComparisonAppended_whenCallsBuild_thenExpectsEmpty() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(QProvider.provider.mechanism, EnumComparisonData.getDefaultInstance(), ProviderMechanism::valueOf)
                                             .build();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("given a builder with a enum is null comparison appended when calls build then expects")
    void givenABuilderWithAEnumIsNullComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     QProvider.provider.mechanism,
                                                     EnumComparisonData.newBuilder().setIsNull(true).build(),
                                                     ProviderMechanism::valueOf
                                             )
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.mechanism is null");
    }

    @Test
    @DisplayName("given a builder with a enum is not null comparison appended when calls build then expects")
    void givenABuilderWithAEnumIsNotNullComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     QProvider.provider.mechanism,
                                                     EnumComparisonData.newBuilder().setIsNotNull(true).build(),
                                                     ProviderMechanism::valueOf
                                             )
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.mechanism is not null");
    }

    @Test
    @DisplayName("given a builder with a enum is equal comparison appended when calls build then expects")
    void givenABuilderWithAEnumIsEqualComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     QProvider.provider.mechanism,
                                                     EnumComparisonData.newBuilder()
                                                                       .setIsEqual(
                                                                               EnumSingleComparisonData.newBuilder()
                                                                                                       .setValue("JETIMOB_V1")
                                                                                                       .build()
                                                                       )
                                                                       .build(),
                                                     ProviderMechanism::valueOf
                                             )
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.mechanism = JETIMOB_V1");
    }

    @Test
    @DisplayName("given a builder with a enum is not equal comparison appended when calls build then expects")
    void givenABuilderWithAEnumIsNotEqualComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     QProvider.provider.mechanism,
                                                     EnumComparisonData.newBuilder()
                                                                       .setIsNotEqual(
                                                                               EnumSingleComparisonData.newBuilder()
                                                                                                       .setValue("JETIMOB_V1")
                                                                                                       .build()
                                                                       )
                                                                       .build(),
                                                     ProviderMechanism::valueOf
                                             )
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.mechanism != JETIMOB_V1");
    }

    @Test
    @DisplayName("given a builder with a enum is in comparison appended when calls build then expects")
    void givenABuilderWithAEnumIsInComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     QProvider.provider.mechanism,
                                                     EnumComparisonData.newBuilder()
                                                                       .setIsIn(
                                                                               EnumListComparisonData.newBuilder()
                                                                                                     .addValues("JETIMOB_V1")
                                                                                                     .addValues("JETIMOB_V2")
                                                                                                     .build()
                                                                       )
                                                                       .build(),
                                                     ProviderMechanism::valueOf
                                             )
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.mechanism in [JETIMOB_V1, JETIMOB_V2]");
    }

    @Test
    @DisplayName("given a builder with a enum is not in comparison appended when calls build then expects")
    void givenABuilderWithAEnumIsNotInComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     QProvider.provider.mechanism,
                                                     EnumComparisonData.newBuilder()
                                                                       .setIsNotIn(
                                                                               EnumListComparisonData.newBuilder()
                                                                                                     .addValues("JETIMOB_V1")
                                                                                                     .addValues("JETIMOB_V2")
                                                                                                     .build()
                                                                       )
                                                                       .build(),
                                                     ProviderMechanism::valueOf
                                             )
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.mechanism not in [JETIMOB_V1, JETIMOB_V2]");
    }

    @Test
    @DisplayName("given a builder with a unknown enum case comparison appended when calls build then expects exception")
    void givenABuilderWithAUnknownEnumCaseComparisonAppended_whenCallsBuild_thenExpectsException() {
        PredicateBuilder builder = PredicateBuilder.newInstance();

        assertThatThrownBy(() -> builder.append(QProvider.provider.mechanism, mockedEnumComparisonData, ProviderMechanism::valueOf))
                .isInstanceOf(GrpcStatusException.class)
                .hasFieldOrPropertyWithValue("status", Status.INVALID_ARGUMENT)
                .hasMessage("Unknown comparison case");
    }

    @Test
    @DisplayName("given a builder with a array not chosen comparison appended when calls build then expects empty")
    void givenABuilderWithAArrayNotChosenComparisonAppended_whenCallsBuild_thenExpectsEmpty() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(QProvider.provider.logo, BytesComparisonData.newBuilder().build())
                                             .build();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("given a builder with a array is null comparison appended when calls build then expects")
    void givenABuilderWithAArrayIsNullComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     QProvider.provider.logo,
                                                     BytesComparisonData.newBuilder().setIsNull(true).build()
                                             )
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.logo is null");
    }

    @Test
    @DisplayName("given a builder with a array is not null comparison appended when calls build then expects")
    void givenABuilderWithAArrayIsNotNullComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     QProvider.provider.logo,
                                                     BytesComparisonData.newBuilder().setIsNotNull(true).build()
                                             )
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.logo is not null");
    }

    @Test
    @DisplayName("given a builder with a unknown array case comparison appended when calls build then expects exception")
    void givenABuilderWithAUnknownArrayCaseComparisonAppended_whenCallsBuild_thenExpectsException() {
        PredicateBuilder builder = PredicateBuilder.newInstance();

        assertThatThrownBy(() -> builder.append(QProvider.provider.logo, mockedBytesComparisonData))
                .isInstanceOf(GrpcStatusException.class)
                .hasFieldOrPropertyWithValue("status", Status.INVALID_ARGUMENT)
                .hasMessage("Unknown comparison case");
    }

}
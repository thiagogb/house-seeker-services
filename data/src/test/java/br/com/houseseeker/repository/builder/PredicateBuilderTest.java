package br.com.houseseeker.repository.builder;

import br.com.houseseeker.domain.proto.BoolComparisonData;
import br.com.houseseeker.domain.proto.BytesComparisonData;
import br.com.houseseeker.domain.proto.DateTimeComparisonData;
import br.com.houseseeker.domain.proto.DoubleComparisonData;
import br.com.houseseeker.domain.proto.EnumComparisonData;
import br.com.houseseeker.domain.proto.Int32ComparisonData;
import br.com.houseseeker.domain.proto.StringComparisonData;
import br.com.houseseeker.domain.provider.ProviderMechanism;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static br.com.houseseeker.entity.QDslProvider.provider;
import static br.com.houseseeker.entity.QDslUrbanProperty.urbanProperty;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PredicateBuilderTest {

    @Test
    @DisplayName("given a builder without clauses appended when calls build then expects empty")
    void givenABuilderWithoutClassesAppended_whenCallsBuild_thenExpectsEmpty() {
        assertThat(PredicateBuilder.newInstance().build()).isEmpty();
    }

    @Test
    @DisplayName("given a builder with a boolean not chosen comparison appended when calls build then expects empty")
    void givenABuilderWithABooleanNotChosenComparisonAppended_whenCallsBuild_thenExpectsEmpty() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(provider.active, BoolComparisonData.getDefaultInstance())
                                             .build();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("given a builder with a boolean comparison appended when calls build then expects")
    void givenABuilderWithABooleanIsNullComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(provider.active, BoolComparisonData.newBuilder().setIsNull(true).build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.active is null");
    }

    @Test
    @DisplayName("given a builder with a integer not chosen comparison appended when calls build then expects empty")
    void givenABuilderWithAIntegerNotChosenComparisonAppended_whenCallsBuild_thenExpectsEmpty() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(provider.id, Int32ComparisonData.getDefaultInstance())
                                             .build();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("given a builder with a integer is null comparison appended when calls build then expects")
    void givenABuilderWithAIntegerIsNullComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(provider.id, Int32ComparisonData.newBuilder().setIsNull(true).build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.id is null");
    }

    @Test
    @DisplayName("given a builder with a double not chosen comparison appended when calls build then expects empty")
    void givenABuilderWithADoubleNotChosenComparisonAppended_whenCallsBuild_thenExpectsEmpty() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(urbanProperty.sellPrice, DoubleComparisonData.getDefaultInstance())
                                             .build();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("given a builder with a double is null comparison appended when calls build then expects")
    void givenABuilderWithADoubleIsNullComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(urbanProperty.sellPrice, DoubleComparisonData.newBuilder().setIsNull(true).build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("urbanProperty.sellPrice is null");
    }

    @Test
    @DisplayName("given a builder with a string not chosen comparison appended when calls build then expects empty")
    void givenABuilderWithAStringNotChosenComparisonAppended_whenCallsBuild_thenExpectsEmpty() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(provider.name, StringComparisonData.getDefaultInstance())
                                             .build();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("given a builder with a string is null comparison appended when calls build then expects")
    void givenABuilderWithAStringIsNullComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(provider.name, StringComparisonData.newBuilder().setIsNull(true).build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.name is null");
    }

    @Test
    @DisplayName("given a builder with a enum not chosen comparison appended when calls build then expects empty")
    void givenABuilderWithAEnumNotChosenComparisonAppended_whenCallsBuild_thenExpectsEmpty() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(provider.mechanism, EnumComparisonData.getDefaultInstance(), ProviderMechanism::valueOf)
                                             .build();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("given a builder with a enum is null comparison appended when calls build then expects")
    void givenABuilderWithAEnumIsNullComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     provider.mechanism,
                                                     EnumComparisonData.newBuilder().setIsNull(true).build(),
                                                     ProviderMechanism::valueOf
                                             )
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.mechanism is null");
    }

    @Test
    @DisplayName("given a builder with a datetime not chosen comparison appended when calls build then expects empty")
    void givenABuilderWithADateTimeNotChosenComparisonAppended_whenCallsBuild_thenExpectsEmpty() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(urbanProperty.creationDate, DateTimeComparisonData.getDefaultInstance())
                                             .build();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("given a builder with a datetime is null comparison appended when calls build then expects")
    void givenABuilderWithADateTimeIsNullComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(urbanProperty.creationDate, DateTimeComparisonData.newBuilder().setIsNull(true).build())
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("urbanProperty.creationDate is null");
    }

    @Test
    @DisplayName("given a builder with a array not chosen comparison appended when calls build then expects empty")
    void givenABuilderWithAArrayNotChosenComparisonAppended_whenCallsBuild_thenExpectsEmpty() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(provider.logo, BytesComparisonData.newBuilder().build())
                                             .build();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("given a builder with a array is null comparison appended when calls build then expects")
    void givenABuilderWithAArrayIsNullComparisonAppended_whenCallsBuild_thenExpects() {
        Predicate[] result = PredicateBuilder.newInstance()
                                             .append(
                                                     provider.logo,
                                                     BytesComparisonData.newBuilder().setIsNull(true).build()
                                             )
                                             .build();

        assertThat(result).extracting(Objects::toString).containsExactly("provider.logo is null");
    }

}
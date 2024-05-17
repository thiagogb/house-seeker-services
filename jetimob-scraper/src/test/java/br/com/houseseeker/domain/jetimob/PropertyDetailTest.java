package br.com.houseseeker.domain.jetimob;

import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import br.com.houseseeker.domain.jetimob.PropertyDetail.Type;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PropertyDetailTest {

    @ParameterizedTest
    @MethodSource("valueSamples")
    @DisplayName("given a accepted value when calls Type.typeOf then expects")
    void givenAAcceptedValue_whenCallsTypeOf_thenExpects(String value, Type expected) {
        assertThat(Type.typeOf(value)).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {EMPTY, "outros"})
    @DisplayName("given a unacceptable value when calls Type.typeOf then expects exception")
    void givenAUnacceptableValue_whenCallsTypeOf_thenExpectsException(String value) {
        assertThatThrownBy(() -> Type.typeOf(value))
                .isInstanceOf(ExtendedRuntimeException.class)
                .hasMessage(String.format("Undefined type for detail with value: %s", value));
    }

    @Test
    @DisplayName("given a exiting type in list when calls findType then expects to be present")
    void givenAExitingTypeInList_whenCallsFindType_thenExpectToBePresent() {
        List<PropertyDetail> propertyDetails = List.of(
                PropertyDetail.builder().type(Type.GENERAL_INFO).items(Collections.emptyList()).build()
        );

        assertThat(PropertyDetail.findType(propertyDetails, Type.GENERAL_INFO)).isPresent();
    }

    @Test
    @DisplayName("given a non exiting type in list when calls findType then expects to be empty")
    void givenANonExitingTypeInList_whenCallsFindType_thenExpectToBeEmpty() {
        List<PropertyDetail> propertyDetails = List.of(
                PropertyDetail.builder().type(Type.GENERAL_INFO).items(Collections.emptyList()).build()
        );

        assertThat(PropertyDetail.findType(propertyDetails, Type.POSITION)).isEmpty();
    }

    private static Stream<Arguments> valueSamples() {
        return Stream.of(
                Arguments.of("tipo de piso", Type.FLOOR_TYPE),
                Arguments.of("posição solar", Type.SOLAR_POSITION),
                Arguments.of("posição", Type.POSITION),
                Arguments.of("medidas", Type.MEASURES),
                Arguments.of("informações gerais", Type.GENERAL_INFO)
        );
    }

}
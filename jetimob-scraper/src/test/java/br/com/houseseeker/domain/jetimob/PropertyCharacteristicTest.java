package br.com.houseseeker.domain.jetimob;

import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import br.com.houseseeker.domain.jetimob.PropertyCharacteristic.Type;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PropertyCharacteristicTest {

    @ParameterizedTest
    @MethodSource("valueSamples")
    @DisplayName("given a accepted value when calls Type.typeOf then expects")
    void givenAAcceptedValue_whenCallsTypeOf_thenExpects(String value, Type expected) {
        assertThat(Type.typeOf(value)).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {EMPTY, "dormítorio", "suite", "mº"})
    @DisplayName("given a unacceptable value when calls Type.typeOf then expects exception")
    void givenAUnacceptableValue_whenCallsTypeOf_thenExpectsException(String value) {
        assertThatThrownBy(() -> Type.typeOf(value))
                .isInstanceOf(ExtendedRuntimeException.class)
                .hasMessage(String.format("Undefined type for characteristic with value: %s", value));
    }

    @Test
    @DisplayName("given a existing type in list when calls findType then expects to be present")
    void givenAExistingType_whenCallsFindType_thenExpectsToBePresent() {
        List<PropertyCharacteristic> propertyCharacteristics = List.of(
                PropertyCharacteristic.builder().type(Type.ROOMS).build()
        );

        assertThat(PropertyCharacteristic.findType(propertyCharacteristics, Type.ROOMS)).isPresent();
    }

    @Test
    @DisplayName("given a non existing type in list when calls findType then expects to be empty")
    void givenANonExistingType_whenCallsFindType_thenExpectsToBeEmpty() {
        List<PropertyCharacteristic> propertyCharacteristics = List.of(
                PropertyCharacteristic.builder().type(Type.ROOMS).build()
        );

        assertThat(PropertyCharacteristic.findType(propertyCharacteristics, Type.DORMITORIES)).isEmpty();
    }

    private static Stream<Arguments> valueSamples() {
        return Stream.of(
                Arguments.of("1 dormitório", Type.DORMITORIES),
                Arguments.of("dormitório", Type.DORMITORIES),
                Arguments.of("2 dormitórios", Type.DORMITORIES),
                Arguments.of("dormitórios", Type.DORMITORIES),
                Arguments.of("1 sala", Type.ROOMS),
                Arguments.of("sala", Type.ROOMS),
                Arguments.of("3 salas", Type.ROOMS),
                Arguments.of("salas", Type.ROOMS),
                Arguments.of("1 suíte", Type.SUITES),
                Arguments.of("suíte", Type.SUITES),
                Arguments.of("2 suítes", Type.SUITES),
                Arguments.of("suítes", Type.SUITES),
                Arguments.of("m² total", Type.TOTAL_AREA),
                Arguments.of("100 m² total", Type.TOTAL_AREA),
                Arguments.of("m² privativa", Type.PRIVATE_AREA),
                Arguments.of("100 m² privativa", Type.PRIVATE_AREA),
                Arguments.of("banheiro", Type.BATHROOMS),
                Arguments.of("1 banheiro", Type.BATHROOMS),
                Arguments.of("banheiros", Type.BATHROOMS),
                Arguments.of("4 banheiros", Type.BATHROOMS),
                Arguments.of("vaga", Type.GARAGES),
                Arguments.of("1 vaga", Type.GARAGES),
                Arguments.of("vagas", Type.GARAGES),
                Arguments.of("3 vagas", Type.GARAGES)
        );
    }

}
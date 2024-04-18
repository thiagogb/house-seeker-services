package br.com.houseseeker.domain.jetimob.v1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PropertyCharacteristicTypeTest {

    @ParameterizedTest
    @MethodSource("characteristicTypeSamples")
    @DisplayName("given icon class when calls getByClasses then expects")
    void givenIconClass_whenCallsGetByClasses_thenExpects(String input, PropertyCharacteristicType output) {
        assertThat(PropertyCharacteristicType.getByClasses(Set.of(input))).isEqualTo(Optional.ofNullable(output));
    }

    private static Stream<Arguments> characteristicTypeSamples() {
        return Stream.of(
                Arguments.of("fa-house-window", PropertyCharacteristicType.BUILD_AREA),
                Arguments.of("fa-house-user", PropertyCharacteristicType.PRIVATE_AREA),
                Arguments.of("fa-vector-square", PropertyCharacteristicType.TERRAIN_AREA),
                Arguments.of("fa-ruler-combined", PropertyCharacteristicType.USABLE_AREA),
                Arguments.of("fa-tractor", PropertyCharacteristicType.ARABLE_AREA),
                Arguments.of("fa-bed", PropertyCharacteristicType.DORMITORIES),
                Arguments.of("fa-door-closed", PropertyCharacteristicType.ROOMS),
                Arguments.of("fa-bath", PropertyCharacteristicType.BATHROOM),
                Arguments.of("fa-car", PropertyCharacteristicType.GARAGE),
                Arguments.of("fa-unknown", null)
        );
    }

}
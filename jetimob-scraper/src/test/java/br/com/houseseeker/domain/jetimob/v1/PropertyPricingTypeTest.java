package br.com.houseseeker.domain.jetimob.v1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PropertyPricingTypeTest {

    @ParameterizedTest
    @MethodSource("pricingTypeSamples")
    @DisplayName("given pricing type names when calls getByName then expects")
    void givenPricingTypeNames_whenCallsGetByName_thenExpects(String input, PropertyPricingType output) {
        assertThat(PropertyPricingType.getByName(input)).isEqualTo(Optional.ofNullable(output));
    }

    private static Stream<Arguments> pricingTypeSamples() {
        return Stream.of(
                Arguments.of("venda", PropertyPricingType.SELL_PRICE),
                Arguments.of("locação", PropertyPricingType.RENT_PRICE),
                Arguments.of("condomínio", PropertyPricingType.CONDOMINIUM_PRICE),
                Arguments.of("iptu", PropertyPricingType.IPTU),
                Arguments.of("temporada", PropertyPricingType.SEASON),
                Arguments.of("unknown", null)
        );
    }

}
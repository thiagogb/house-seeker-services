package br.com.houseseeker.domain.jetimob.v2;

import br.com.houseseeker.domain.jetimob.PropertyCharacteristic;
import br.com.houseseeker.domain.jetimob.v2.PropertyInfoMetadata.Location;
import br.com.houseseeker.domain.jetimob.v2.PropertyInfoMetadata.Pricing;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PropertyInfoMetadataTest {

    @Test
    @DisplayName("given a non existing characteristic in list when calls extractCharacteristicsByTypes then expects null")
    void givenANonExistingCharacteristicInList_whenCallsExtractCharacteristicsByTypes_thenExpectsNull() {
        var propertyInfoMetadata = PropertyInfoMetadata.builder()
                                                       .characteristics(List.of(
                                                               PropertyCharacteristic.builder()
                                                                                     .type(PropertyCharacteristic.Type.DORMITORIES)
                                                                                     .build(),
                                                               PropertyCharacteristic.builder()
                                                                                     .type(PropertyCharacteristic.Type.GARAGES)
                                                                                     .build()
                                                       ))
                                                       .build();

        assertThat(propertyInfoMetadata.extractCharacteristicsByTypes(this::defaultSupplier, PropertyCharacteristic.Type.SUITES))
                .isNull();
    }

    @Test
    @DisplayName("given a existing characteristic in list when calls extractCharacteristicsByTypes then expects not null")
    void givenAExistingCharacteristicInList_whenCallsExtractCharacteristicsByTypes_thenExpectsNotNull() {
        var propertyInfoMetadata = PropertyInfoMetadata.builder()
                                                       .characteristics(List.of(
                                                               PropertyCharacteristic.builder()
                                                                                     .type(PropertyCharacteristic.Type.DORMITORIES)
                                                                                     .value("3")
                                                                                     .build()
                                                       ))
                                                       .build();

        assertThat(propertyInfoMetadata.extractCharacteristicsByTypes(this::defaultSupplier, PropertyCharacteristic.Type.DORMITORIES))
                .isEqualTo("3");
    }

    @Test
    @DisplayName("given a location with latitude when calls getLatitudeAsBigDecimal then expects")
    void givenALocationWithLatitude_whenCallsGetLatitudeAsBigDecimal_thenExpects() {
        var location = Location.builder().latitude("-29.695085").build();

        assertThat(location.getLatitudeAsBigDecimal()).isEqualTo(BigDecimal.valueOf(-29.695085));
    }

    @Test
    @DisplayName("given a location with longitude when calls getLongitudeAsBigDecimal then expects")
    void givenALocationWithLongitude_whenCallsGetLongitudeAsBigDecimal_thenExpects() {
        var location = Location.builder().longitude("-53.848229").build();

        assertThat(location.getLongitudeAsBigDecimal()).isEqualTo(BigDecimal.valueOf(-53.848229));
    }

    @Test
    @DisplayName("given a pricing with sellPrice when calls getSellPriceAsBigDecimal then expects")
    void givenAPricingWithSellPrice_whenCallsGetSellPriceAsBigDecimal_thenExpects() {
        var pricing = Pricing.builder().sellPrice("R$ 10.500,25").build();

        assertThat(pricing.getSellPriceAsBigDecimal()).isEqualTo(BigDecimal.valueOf(10500.25));
    }

    @Test
    @DisplayName("given a pricing with sellPrice when calls getRentPriceAsBigDecimal then expects")
    void givenAPricingWithSellPrice_whenCallsGetRentPriceAsBigDecimal_thenExpects() {
        var pricing = Pricing.builder().rentPrice("R$ 1.500,25").build();

        assertThat(pricing.getRentPriceAsBigDecimal()).isEqualTo(BigDecimal.valueOf(1500.25));
    }

    @Test
    @DisplayName("given a pricing with sellPrice when calls getCondominiumPriceAsBigDecimal then expects")
    void givenAPricingWithSellPrice_whenCallsGetCondominiumPriceAsBigDecimal_thenExpects() {
        var pricing = Pricing.builder().condominiumPrice("R$ 500,25").build();

        assertThat(pricing.getCondominiumPriceAsBigDecimal()).isEqualTo(BigDecimal.valueOf(500.25));
    }

    private Optional<String> defaultSupplier(PropertyCharacteristic propertyCharacteristic) {
        return Optional.ofNullable(propertyCharacteristic.getValue());
    }

}
package br.com.houseseeker.domain.jetimob.v4;

import br.com.houseseeker.domain.jetimob.v4.PropertyInfoResponse.Address;
import br.com.houseseeker.domain.jetimob.v4.PropertyInfoResponse.Address.Coordinate;
import br.com.houseseeker.domain.jetimob.v4.PropertyInfoResponse.Contract;
import br.com.houseseeker.domain.jetimob.v4.PropertyInfoResponse.Measure;
import br.com.houseseeker.domain.jetimob.v4.PropertyInfoResponse.Price;
import br.com.houseseeker.domain.property.UrbanPropertyContract;
import br.com.houseseeker.domain.property.UrbanPropertyType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.assertj.core.api.Assertions.assertThat;

class PropertyInfoResponseTest {

    @ParameterizedTest
    @MethodSource("contractSamples")
    @DisplayName("given a property with contracts when calls getMainContract then expects")
    void givenAPropertyWithContracts_whenCallsGetMainContract_thenExpects(List<Contract> contracts, UrbanPropertyContract expected) {
        PropertyInfoResponse propertyInfoResponse = PropertyInfoResponse.builder().contracts(contracts).build();
        assertThat(propertyInfoResponse.getMainContract()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("typeSamples")
    @DisplayName("given a property with type when calls getType then expects")
    void givenAPropertyWithType_whenCallsGetType_thenExpects(String type, UrbanPropertyType expected) {
        PropertyInfoResponse propertyInfoResponse = PropertyInfoResponse.builder().type(type).build();
        assertThat(propertyInfoResponse.getType()).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {EMPTY, SPACE, "Casa", "Apartamento", "Sobrado"})
    @DisplayName("given a property with type when calls getSubType then expects")
    void givenAPropertyWithType_whenCallsGetSubType_thenExpects(String type) {
        PropertyInfoResponse propertyInfoResponse = PropertyInfoResponse.builder().type(type).build();
        assertThat(propertyInfoResponse.getSubType()).isEqualTo(type);
    }

    @ParameterizedTest
    @MethodSource("contractSellPriceSamples")
    @DisplayName("given a property with sellPrice when calls getSellPrice then expects")
    void givenAPropertyWithSellPrice_whenCallsGetSellPrice_thenExpects(List<Contract> contracts, BigDecimal expected) {
        PropertyInfoResponse propertyInfoResponse = PropertyInfoResponse.builder().contracts(contracts).build();
        assertThat(propertyInfoResponse.getSellPrice()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("contractRentPriceSamples")
    @DisplayName("given a property with rentPrice when calls getRentPrice then expects")
    void givenAPropertyWithRentPrice_whenCallsGetRentPrice_thenExpects(List<Contract> contracts, BigDecimal expected) {
        PropertyInfoResponse propertyInfoResponse = PropertyInfoResponse.builder().contracts(contracts).build();
        assertThat(propertyInfoResponse.getRentPrice()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("condominiumPriceSamples")
    @DisplayName("given a property with condominiumPrice when calls getCondominiumPrice then expects")
    void givenAPropertyWithCondominiumPrice_whenCallsGetCondominiumPrice_thenExpects(Price price, BigDecimal expected) {
        PropertyInfoResponse propertyInfoResponse = PropertyInfoResponse.builder().condominiumPrice(price).build();
        assertThat(propertyInfoResponse.getCondominiumPrice()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("addressStringFieldSamples")
    @DisplayName("given a property with state when calls getState then expects")
    void givenAPropertyWithState_whenCallsGetState_thenExpects(String value, String expected) {
        PropertyInfoResponse propertyInfoResponse = PropertyInfoResponse.builder()
                                                                        .address(Address.builder().state(value).build())
                                                                        .build();
        assertThat(propertyInfoResponse.getState()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("addressStringFieldSamples")
    @DisplayName("given a property with city when calls getCity then expects")
    void givenAPropertyWithCity_whenCallsGetCity_thenExpects(String value, String expected) {
        PropertyInfoResponse propertyInfoResponse = PropertyInfoResponse.builder()
                                                                        .address(Address.builder().city(value).build())
                                                                        .build();
        assertThat(propertyInfoResponse.getCity()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("addressStringFieldSamples")
    @DisplayName("given a property with district when calls getDistrict then expects")
    void givenAPropertyWithDistrict_whenCallsGetDistrict_thenExpects(String value, String expected) {
        PropertyInfoResponse propertyInfoResponse = PropertyInfoResponse.builder()
                                                                        .address(Address.builder().district(value).build())
                                                                        .build();
        assertThat(propertyInfoResponse.getDistrict()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("addressStringFieldSamples")
    @DisplayName("given a property with zipCode when calls getZipCode then expects")
    void givenAPropertyWithZipCode_whenCallsGetZipCode_thenExpects(String value, String expected) {
        PropertyInfoResponse propertyInfoResponse = PropertyInfoResponse.builder()
                                                                        .address(Address.builder().zipCode(value).build())
                                                                        .build();
        assertThat(propertyInfoResponse.getZipCode()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("addressStringFieldSamples")
    @DisplayName("given a property with streetName when calls getStreetName then expects")
    void givenAPropertyWithStreetName_whenCallsGetStreetName_thenExpects(String value, String expected) {
        PropertyInfoResponse propertyInfoResponse = PropertyInfoResponse.builder()
                                                                        .address(Address.builder().streetName(value).build())
                                                                        .build();
        assertThat(propertyInfoResponse.getStreetName()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("addressIntegerFieldSamples")
    @DisplayName("given a property with streetNumber when calls getStreetNumber then expects")
    void givenAPropertyWithStreetNumber_whenCallsGetStreetNumber_thenExpects(String value, Integer expected) {
        PropertyInfoResponse propertyInfoResponse = PropertyInfoResponse.builder()
                                                                        .address(Address.builder().streetNumber(value).build())
                                                                        .build();
        assertThat(propertyInfoResponse.getStreetNumber()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("addressStringFieldSamples")
    @DisplayName("given a property with complement when calls getComplement then expects")
    void givenAPropertyWithComplement_whenCallsGetComplement_thenExpects(String value, String expected) {
        PropertyInfoResponse propertyInfoResponse = PropertyInfoResponse.builder()
                                                                        .address(Address.builder().complement(value).build())
                                                                        .build();
        assertThat(propertyInfoResponse.getComplement()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("coordsSamples")
    @DisplayName("given a property with latitude when calls getLatitude then expects")
    void givenAPropertyWithLatitude_whenCallsGetLatitude_thenExpects(String coordinate, BigDecimal expected) {
        PropertyInfoResponse propertyInfoResponse = PropertyInfoResponse.builder()
                                                                        .address(
                                                                                Address.builder()
                                                                                       .coordinate(Coordinate.builder().latitude(coordinate).build())
                                                                                       .build()
                                                                        )
                                                                        .build();
        assertThat(propertyInfoResponse.getLatitude()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("coordsSamples")
    @DisplayName("given a property with longitude when calls getLongitude then expects")
    void givenAPropertyWithLongitude_whenCallsGetLongitude_thenExpects(String coordinate, BigDecimal expected) {
        PropertyInfoResponse propertyInfoResponse = PropertyInfoResponse.builder()
                                                                        .address(
                                                                                Address.builder()
                                                                                       .coordinate(Coordinate.builder().longitude(coordinate).build())
                                                                                       .build()
                                                                        )
                                                                        .build();
        assertThat(propertyInfoResponse.getLongitude()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("measureSamples")
    @DisplayName("given a property with totalArea when calls getTotalArea then expects")
    void givenAPropertyWithTotalArea_whenCallsGetTotalArea_thenExpects(Measure measure, BigDecimal expected) {
        PropertyInfoResponse propertyInfoResponse = PropertyInfoResponse.builder().totalArea(measure).build();
        assertThat(propertyInfoResponse.getTotalArea()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("measureSamples")
    @DisplayName("given a property with privateArea when calls getPrivateArea then expects")
    void givenAPropertyWithPrivateArea_whenCallsGetPrivateArea_thenExpects(Measure measure, BigDecimal expected) {
        PropertyInfoResponse propertyInfoResponse = PropertyInfoResponse.builder().privateArea(measure).build();
        assertThat(propertyInfoResponse.getPrivateArea()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("measureSamples")
    @DisplayName("given a property with usableArea when calls getUsableArea then expects")
    void givenAPropertyWithUsableArea_whenCallsGetUsableArea_thenExpects(Measure measure, BigDecimal expected) {
        PropertyInfoResponse propertyInfoResponse = PropertyInfoResponse.builder().usableArea(measure).build();
        assertThat(propertyInfoResponse.getUsableArea()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("measureSamples")
    @DisplayName("given a property with terrainTotalArea when calls getTerrainTotalArea then expects")
    void givenAPropertyWithTerrainTotalArea_whenCallsGetTerrainTotalArea_thenExpects(Measure measure, BigDecimal expected) {
        PropertyInfoResponse propertyInfoResponse = PropertyInfoResponse.builder().terrainTotalArea(measure).build();
        assertThat(propertyInfoResponse.getTerrainTotalArea()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("areaUnitSamples")
    @DisplayName("given a property with areaUnit when calls getAreaUnit then expects")
    void givenAPropertyWithAreaUnit_whenCallsGetAreaUnit_thenExpects(
            Measure totalArea,
            Measure privateArea,
            Measure usableArea,
            Measure terrainTotalArea,
            String expected
    ) {
        PropertyInfoResponse propertyInfoResponse = PropertyInfoResponse.builder()
                                                                        .totalArea(totalArea)
                                                                        .privateArea(privateArea)
                                                                        .usableArea(usableArea)
                                                                        .terrainTotalArea(terrainTotalArea)
                                                                        .build();
        assertThat(propertyInfoResponse.getAreaUnit()).isEqualTo(expected);
    }

    private static Stream<Arguments> contractSamples() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(Collections.emptyList(), null),
                Arguments.of(List.of(Contract.builder().id(1).build()), UrbanPropertyContract.SELL),
                Arguments.of(List.of(Contract.builder().id(2).build()), UrbanPropertyContract.RENT),
                Arguments.of(List.of(Contract.builder().id(1).build(), Contract.builder().id(2).build()), UrbanPropertyContract.SELL),
                Arguments.of(List.of(Contract.builder().id(3).build()), null)
        );
    }

    private static Stream<Arguments> typeSamples() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(EMPTY, null),
                Arguments.of(SPACE, null),
                Arguments.of("Casa", UrbanPropertyType.RESIDENTIAL),
                Arguments.of("Comercial Casa", UrbanPropertyType.RESIDENTIAL),
                Arguments.of("Apartamento", UrbanPropertyType.RESIDENTIAL),
                Arguments.of("Sobrado", UrbanPropertyType.RESIDENTIAL),
                Arguments.of("Terrano", UrbanPropertyType.RESIDENTIAL),
                Arguments.of("Casa Comercial", UrbanPropertyType.COMMERCIAL),
                Arguments.of("Apartamento COMERCIAL", UrbanPropertyType.COMMERCIAL)
        );
    }

    private static Stream<Arguments> contractSellPriceSamples() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(Collections.emptyList(), null),
                Arguments.of(List.of(Contract.builder().id(1).price(Price.builder().build()).build()), null),
                Arguments.of(
                        List.of(Contract.builder().id(1).price(Price.builder().value(BigDecimal.valueOf(50000000)).build()).build()),
                        new BigDecimal("500000.00")
                ),
                Arguments.of(List.of(Contract.builder().id(2).build()), null)
        );
    }

    private static Stream<Arguments> contractRentPriceSamples() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(Collections.emptyList(), null),
                Arguments.of(List.of(Contract.builder().id(2).price(Price.builder().build()).build()), null),
                Arguments.of(
                        List.of(Contract.builder().id(2).price(Price.builder().value(BigDecimal.valueOf(500000)).build()).build()),
                        new BigDecimal("5000.00")
                ),
                Arguments.of(List.of(Contract.builder().id(1).build()), null)
        );
    }

    private static Stream<Arguments> condominiumPriceSamples() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(Price.builder().build(), null),
                Arguments.of(Price.builder().value(BigDecimal.valueOf(50000)).build(), new BigDecimal("500.00"))
        );
    }

    private static Stream<Arguments> addressStringFieldSamples() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(EMPTY, null),
                Arguments.of(SPACE, null),
                Arguments.of("value", "value"),
                Arguments.of(" value ", " value ")
        );
    }

    private static Stream<Arguments> addressIntegerFieldSamples() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(EMPTY, null),
                Arguments.of(SPACE, null),
                Arguments.of("a", null),
                Arguments.of("1", 1),
                Arguments.of("123456", 123456),
                Arguments.of("1.1", null)
        );
    }

    private static Stream<Arguments> coordsSamples() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(EMPTY, null),
                Arguments.of(SPACE, null),
                Arguments.of("abc", null),
                Arguments.of("-99.9999", BigDecimal.valueOf(-99.9999)),
                Arguments.of("99.9999", BigDecimal.valueOf(99.9999))
        );
    }

    private static Stream<Arguments> measureSamples() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(Measure.builder().value(null).build(), null),
                Arguments.of(Measure.builder().value(BigDecimal.valueOf(250.50)).build(), BigDecimal.valueOf(250.50))
        );
    }

    private static Stream<Arguments> areaUnitSamples() {
        return Stream.of(
                Arguments.of(null, null, null, null, null),
                Arguments.of(Measure.builder().measureUnit("mº").build(), null, null, null, "mº"),
                Arguments.of(null, Measure.builder().measureUnit("mº").build(), null, null, "mº"),
                Arguments.of(null, null, Measure.builder().measureUnit("mº").build(), null, "mº"),
                Arguments.of(null, null, null, Measure.builder().measureUnit("mº").build(), "mº")
        );
    }

}
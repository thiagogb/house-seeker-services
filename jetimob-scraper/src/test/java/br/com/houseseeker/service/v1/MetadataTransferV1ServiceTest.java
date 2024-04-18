package br.com.houseseeker.service.v1;

import br.com.houseseeker.TestUtils;
import br.com.houseseeker.domain.jetimob.v1.PropertyCharacteristicType;
import br.com.houseseeker.domain.jetimob.v1.PropertyInfoMetadata;
import br.com.houseseeker.domain.jetimob.v1.PropertyInfoMetadata.Characteristics;
import br.com.houseseeker.domain.jetimob.v1.PropertyInfoMetadata.Convenience;
import br.com.houseseeker.domain.jetimob.v1.PropertyInfoMetadata.Location;
import br.com.houseseeker.domain.jetimob.v1.PropertyInfoMetadata.Media;
import br.com.houseseeker.domain.jetimob.v1.PropertyInfoMetadata.Pricing;
import br.com.houseseeker.domain.jetimob.v1.PropertyPricingType;
import br.com.houseseeker.domain.jetimob.v1.SearchPageMetadata;
import br.com.houseseeker.domain.urbanProperty.UrbanPropertyContract;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
        ObjectMapper.class,
        MetadataTransferV1Service.class
})
class MetadataTransferV1ServiceTest {

    private static final SearchPageMetadata.Item ITEM_METADATA = SearchPageMetadata.Item.builder()
                                                                                        .subType("Residencial")
                                                                                        .subType("Casa")
                                                                                        .providerCode("PC1")
                                                                                        .pageLink("http://example.com/property/1")
                                                                                        .build();

    private static final String RESPONSE_WITH_MINIMUM_INFO = "responses/v1/transfer/minimum-info.json";
    private static final String RESPONSE_WITH_FULL_INFO = "responses/v1/transfer/full-info.json";

    @Autowired
    private MetadataTransferV1Service metadataTransferV1Service;

    @Test
    @DisplayName("given metadata with minimum info when calls transfer then expects")
    void givenMetadataWithMinimumInfo_whenCallsTransfer_thenExpects() {
        PropertyInfoMetadata pageMetadata = PropertyInfoMetadata.builder().build();

        assertThat(metadataTransferV1Service.transfer(Pair.of(ITEM_METADATA, pageMetadata)))
                .extracting(Object::toString)
                .satisfies(expected -> JSONAssert.assertEquals(
                        expected,
                        TestUtils.getTextFromResources(RESPONSE_WITH_MINIMUM_INFO),
                        true
                ));
    }

    @Test
    @DisplayName("given metadata with full info when calls transfer then expects")
    void givenMetadataWithFullInfo_whenCallsTransfer_thenExpects() {
        PropertyInfoMetadata pageMetadata = PropertyInfoMetadata.builder()
                                                                .contract(UrbanPropertyContract.RENT)
                                                                .location(
                                                                        Location.builder()
                                                                                .streetName("Street name 1")
                                                                                .city("City 1")
                                                                                .state("State 1")
                                                                                .district("District 1")
                                                                                .condominiumName("Condominium 1")
                                                                                .build()
                                                                )
                                                                .characteristics(List.of(
                                                                        Characteristics.builder()
                                                                                       .type(PropertyCharacteristicType.DORMITORIES)
                                                                                       .additional("3")
                                                                                       .build(),
                                                                        Characteristics.builder()
                                                                                       .type(PropertyCharacteristicType.BATHROOM)
                                                                                       .value("3")
                                                                                       .build(),
                                                                        Characteristics.builder()
                                                                                       .type(PropertyCharacteristicType.GARAGE)
                                                                                       .value("1")
                                                                                       .build(),
                                                                        Characteristics.builder()
                                                                                       .type(PropertyCharacteristicType.BUILD_AREA)
                                                                                       .value("350 m²")
                                                                                       .build(),
                                                                        Characteristics.builder()
                                                                                       .type(PropertyCharacteristicType.PRIVATE_AREA)
                                                                                       .value("275 m²")
                                                                                       .build(),
                                                                        Characteristics.builder()
                                                                                       .type(PropertyCharacteristicType.USABLE_AREA)
                                                                                       .value("265 m²")
                                                                                       .build(),
                                                                        Characteristics.builder()
                                                                                       .type(PropertyCharacteristicType.TERRAIN_AREA)
                                                                                       .value("500 m²")
                                                                                       .build()
                                                                ))
                                                                .pricing(List.of(
                                                                        Pricing.builder()
                                                                               .type(PropertyPricingType.SELL_PRICE)
                                                                               .name("financiável")
                                                                               .value("1.500.900,99")
                                                                               .build(),
                                                                        Pricing.builder()
                                                                               .type(PropertyPricingType.RENT_PRICE)
                                                                               .value("7.350,00")
                                                                               .build(),
                                                                        Pricing.builder()
                                                                               .type(PropertyPricingType.CONDOMINIUM_PRICE)
                                                                               .value("1.000,00")
                                                                               .build()
                                                                ))
                                                                .convenience(
                                                                        Convenience.builder()
                                                                                   .description("Description 1")
                                                                                   .items(List.of("Item 1", "Item 2", "Item 3"))
                                                                                   .build()
                                                                )
                                                                .medias(List.of(
                                                                        Media.builder()
                                                                             .link("http://image.link/1")
                                                                             .linkThumb("http://image.link/1/thumb")
                                                                             .type("images")
                                                                             .extension("jpeg")
                                                                             .build(),
                                                                        Media.builder()
                                                                             .link("http://image.link/2")
                                                                             .linkThumb("http://image.link/2/thumb")
                                                                             .type("images")
                                                                             .extension("jpeg")
                                                                             .build()
                                                                ))
                                                                .build();

        assertThat(metadataTransferV1Service.transfer(Pair.of(ITEM_METADATA, pageMetadata)))
                .extracting(Object::toString)
                .satisfies(expected -> JSONAssert.assertEquals(
                        expected,
                        TestUtils.getTextFromResources(RESPONSE_WITH_FULL_INFO),
                        true
                ));
    }

}
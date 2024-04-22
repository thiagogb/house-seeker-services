package br.com.houseseeker.service.v1;

import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import br.com.houseseeker.domain.jetimob.v1.PropertyCharacteristicType;
import br.com.houseseeker.domain.jetimob.v1.PropertyInfoMetadata;
import br.com.houseseeker.domain.jetimob.v1.PropertyPricingType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static br.com.houseseeker.TestUtils.getTextFromResources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = {PropertyPageV1ScraperService.class, ObjectMapper.class})
class PropertyPageV1ScraperServiceTest {

    private static final String SAMPLE_EMPTY_DOCUMENT = "samples/empty-document.html";
    private static final String SAMPLE_WITHOUT_CHARACTERISTICS = "samples/v1/property/without-characteristics.html";
    private static final String SAMPLE_WITH_UNKNOWN_CHARACTERISTIC = "samples/v1/property/with-unknown-characteristic.html";
    private static final String SAMPLE_WITH_BLANK_CHARACTERISTIC_NAME = "samples/v1/property/with-blank-characteristic-name.html";
    private static final String SAMPLE_WITH_BLANK_CHARACTERISTIC_VALUE = "samples/v1/property/with-blank-characteristic-value.html";
    private static final String SAMPLE_WITHOUT_PRICING = "samples/v1/property/without-pricing.html";
    private static final String SAMPLE_WITH_BLANK_PRICING_NAME = "samples/v1/property/with-blank-pricing-name.html";
    private static final String SAMPLE_WITH_BLANK_PRICING_VALUE = "samples/v1/property/with-blank-pricing-value.html";
    private static final String SAMPLE_WITH_UNKNOWN_PRICING_TYPE = "samples/v1/property/with-unknown-pricing-type.html";
    private static final String SAMPLE_WITH_INVALID_DATA_MEDIA = "samples/v1/property/with-invalid-data-media.html";
    private static final String SAMPLE_WITH_MINIMUM_DATA = "samples/v1/property/with-minimum-data.html";
    private static final String SAMPLE_WITH_FULL_DATA = "samples/v1/property/with-full-data.html";

    @Autowired
    private PropertyPageV1ScraperService propertyPageV1ScraperService;

    @Test
    @DisplayName("given invalid property page when calls scrap then expects exception")
    void givenInvalidPropertyPage_whenCallsScrap_thenExpectsException() {
        assertThatThrownBy(() -> scrapWithSample(SAMPLE_EMPTY_DOCUMENT))
                .isInstanceOf(ExtendedRuntimeException.class)
                .hasMessage("Location root element not found");
    }

    @Test
    @DisplayName("given property page without characteristics container when calls scrap then expects exception")
    void givenPropertyPageWithoutCharacteristicsContainer_whenCallsScrap_thenExpectsException() {
        assertThatThrownBy(() -> scrapWithSample(SAMPLE_WITHOUT_CHARACTERISTICS))
                .isInstanceOf(ExtendedRuntimeException.class)
                .hasMessage("Characteristics root element not found");
    }

    @Test
    @DisplayName("given property page with unknown characteristic when calls scrap then expects exception")
    void givenPropertyPageWithUnknownCharacteristic_whenCallsScrap_thenExpectsException() {
        assertThatThrownBy(() -> scrapWithSample(SAMPLE_WITH_UNKNOWN_CHARACTERISTIC))
                .isInstanceOf(ExtendedRuntimeException.class)
                .hasMessage("Unknown characteristic class");
    }

    @Test
    @DisplayName("given property page with blank characteristic name when calls scrap then expects exception")
    void givenPropertyPageWithBlankCharacteristicName_whenCallsScrap_thenExpectsException() {
        assertThatThrownBy(() -> scrapWithSample(SAMPLE_WITH_BLANK_CHARACTERISTIC_NAME))
                .isInstanceOf(ExtendedRuntimeException.class)
                .hasMessage("Failed to extract characteristic name");
    }

    @Test
    @DisplayName("given property page with blank characteristic value when calls scrap then expects exception")
    void givenPropertyPageWithBlankCharacteristicValue_whenCallsScrap_thenExpectsException() {
        assertThatThrownBy(() -> scrapWithSample(SAMPLE_WITH_BLANK_CHARACTERISTIC_VALUE))
                .isInstanceOf(ExtendedRuntimeException.class)
                .hasMessage("Failed to extract characteristic value");
    }

    @Test
    @DisplayName("given property page without pricing container when calls scrap then expects exception")
    void givenPropertyPageWithoutPricingContainer_whenCallsScrap_thenExpectsException() {
        assertThatThrownBy(() -> scrapWithSample(SAMPLE_WITHOUT_PRICING))
                .isInstanceOf(ExtendedRuntimeException.class)
                .hasMessage("Pricing root element not found");
    }

    @Test
    @DisplayName("given property page with blank pricing name when calls scrap then expects exception")
    void givenPropertyPageWithBlankPricingName_whenCallsScrap_thenExpectsException() {
        assertThatThrownBy(() -> scrapWithSample(SAMPLE_WITH_BLANK_PRICING_NAME))
                .isInstanceOf(ExtendedRuntimeException.class)
                .hasMessage("Failed to extract pricing name");
    }

    @Test
    @DisplayName("given property page with blank pricing value when calls scrap then expects exception")
    void givenPropertyPageWithBlankPricingValue_whenCallsScrap_thenExpectsException() {
        assertThatThrownBy(() -> scrapWithSample(SAMPLE_WITH_BLANK_PRICING_VALUE))
                .isInstanceOf(ExtendedRuntimeException.class)
                .hasMessage("Failed to extract pricing value");
    }

    @Test
    @DisplayName("given property page with invalid pricing type when calls scrap then expects exception")
    void givenPropertyPageWithInvalidPricingType_whenCallsScrap_thenExpectsException() {
        assertThatThrownBy(() -> scrapWithSample(SAMPLE_WITH_UNKNOWN_PRICING_TYPE))
                .isInstanceOf(ExtendedRuntimeException.class)
                .hasMessage("Unknown pricing type");
    }

    @Test
    @DisplayName("given property page with invalid data media when calls scrap then expects exception")
    void givenPropertyPageWithInvalidDataMedia_whenCallsScrap_thenExpectsException() {
        assertThatThrownBy(() -> scrapWithSample(SAMPLE_WITH_INVALID_DATA_MEDIA))
                .isInstanceOf(ExtendedRuntimeException.class)
                .hasMessage("Content read failed");
    }

    @Test
    @DisplayName("given property page with minimum data when calls scrap then expects property info metadata")
    void givenPropertyPageWithMinimumData_whenCallsScrap_thenExpectsPropertyInfoMetadata() {
        assertThat(propertyPageV1ScraperService.scrap(getTextFromResources(SAMPLE_WITH_MINIMUM_DATA)))
                .hasFieldOrPropertyWithValue("contract", null)
                .hasFieldOrPropertyWithValue("location", PropertyInfoMetadata.Location.builder().build())
                .hasFieldOrPropertyWithValue("characteristics", Collections.emptyList())
                .hasFieldOrPropertyWithValue("pricing", Collections.emptyList())
                .hasFieldOrPropertyWithValue("medias", Collections.emptyList())
                .hasFieldOrPropertyWithValue("convenience", PropertyInfoMetadata.Convenience.builder().build());
    }

    @Test
    @DisplayName("given property page with full data when calls scrap then expects property info metadata")
    void givenPropertyPageWithFullData_whenCallsScrap_thenExpectsPropertyInfoMetadata() {
        assertThat(propertyPageV1ScraperService.scrap(getTextFromResources(SAMPLE_WITH_FULL_DATA)))
                .hasFieldOrPropertyWithValue("contract", null)
                .extracting("location", "characteristics", "pricing", "medias", "convenience")
                .containsExactly(
                        PropertyInfoMetadata.Location.builder()
                                                     .state("State 1")
                                                     .city("City 1")
                                                     .streetName("Street 1")
                                                     .district("District 1")
                                                     .condominiumName("Condominium 1")
                                                     .build(),
                        List.of(
                                PropertyInfoMetadata.Characteristics.builder()
                                                                    .type(PropertyCharacteristicType.BUILD_AREA)
                                                                    .name("Characteristic 1")
                                                                    .value("Value 1")
                                                                    .additional("Extra 1")
                                                                    .build()
                        ),
                        List.of(
                                PropertyInfoMetadata.Pricing.builder()
                                                            .name("Venda")
                                                            .value("Value 1")
                                                            .type(PropertyPricingType.SELL_PRICE)
                                                            .build()
                        ),
                        List.of(
                                PropertyInfoMetadata.Media.builder()
                                                          .link("https://sample.com/image-1.jpg")
                                                          .linkThumb("https://sample.com/image-1-thumb.jpg")
                                                          .type("images")
                                                          .extension("jpg")
                                                          .build(),
                                PropertyInfoMetadata.Media.builder()
                                                          .link("https://sample.com/image-2.jpg")
                                                          .linkThumb("https://sample.com/image-2-thumb.jpg")
                                                          .type("images")
                                                          .extension("jpg")
                                                          .build()
                        ),
                        PropertyInfoMetadata.Convenience.builder()
                                                        .description("Description Value")
                                                        .items(List.of("Value 1", "Value 2"))
                                                        .build()
                );
    }

    private PropertyInfoMetadata scrapWithSample(String sample) {
        return propertyPageV1ScraperService.scrap(getTextFromResources(sample));
    }

}
package br.com.houseseeker.service;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.entity.UrbanPropertyPriceVariation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class UrbanPropertyPriceVariationServiceIntegrationTest extends AbstractJpaIntegrationTest {

    private static final int TEST_PROVIDER_10000 = 10000;
    private static final int TEST_PROVIDER_10001 = 10001;
    private static final int TEST_PROPERTY_10000 = 10000;

    @Autowired
    private UrbanPropertyPriceVariationService urbanPropertyPriceVariationService;

    @Test
    @DisplayName("given a provider with price variations when calls findAllByProvider then expects")
    void givenAProviderWithPriceVariations_whenCallsFindAllByProvider_thenExpects() {
        var provider = findProviderById(TEST_PROVIDER_10000);

        assertThat(urbanPropertyPriceVariationService.findAllByProvider(provider)).hasSize(13);
    }

    @Test
    @DisplayName("given a provider without price variations when calls findAllByProvider then expects")
    void givenAProviderWithoutPriceVariations_whenCallsFindAllByProvider_thenExpects() {
        var provider = findProviderById(TEST_PROVIDER_10001);

        assertThat(urbanPropertyPriceVariationService.findAllByProvider(provider)).isEmpty();
    }

    @Test
    @DisplayName("given a batch of price variations when calls saveAll then expects")
    void givenABatchOfPriceVariations_whenCallsSaveAll_thenExpects() {
        var urbanProperty = findUrbanPropertyById(TEST_PROPERTY_10000);
        var urbanPropertyPriceVariations = IntStream.range(0, 100)
                                                    .mapToObj(i -> UrbanPropertyPriceVariation.builder()
                                                                                              .urbanProperty(urbanProperty)
                                                                                              .type(UrbanPropertyPriceVariation.Type.SELL)
                                                                                              .price(BigDecimal.valueOf(i))
                                                                                              .variation(BigDecimal.valueOf(i))
                                                                                              .build()
                                                    )
                                                    .toList();

        assertThat(urbanPropertyPriceVariationService.saveAll(urbanPropertyPriceVariations)).hasSize(100);
    }

    @Test
    @DisplayName("given a provider with price variations when calls deleteAllByProvider then expects")
    void givenAProviderWithPriceVariations_whenCallsDeleteAllByProvider_thenExpects() {
        var provider = findProviderById(TEST_PROVIDER_10000);

        assertThat(urbanPropertyPriceVariationService.deleteAllByProvider(provider)).isEqualTo(13);
    }

}
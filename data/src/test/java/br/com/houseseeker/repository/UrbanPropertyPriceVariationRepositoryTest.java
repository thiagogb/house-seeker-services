package br.com.houseseeker.repository;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class UrbanPropertyPriceVariationRepositoryTest extends AbstractJpaIntegrationTest {

    private static final int TEST_PROVIDER_10000 = 10000;
    private static final int TEST_PROVIDER_10001 = 10001;

    @Autowired
    private UrbanPropertyPriceVariationRepository urbanPropertyPriceVariationRepository;

    @Test
    @DisplayName("given a provider with existing property price variations when calls findAllByProvider then expects 13 rows")
    void givenAProviderWithExistingPropertyPriceVariations_whenCallsFindAllByProvider_thenReturn13Rows() {
        var provider = findProviderById(TEST_PROVIDER_10000);

        assertThat(urbanPropertyPriceVariationRepository.findAllByProvider(provider)).hasSize(13);
    }

    @Test
    @DisplayName("given a provider with price variations when calls deleteAllByProvider then expects")
    void givenAProviderWithPriceVariations_whenCallsDeleteAllByProvider_thenExpects() {
        var provider = findProviderById(TEST_PROVIDER_10000);

        assertThat(urbanPropertyPriceVariationRepository.deleteAllByProvider(provider)).isEqualTo(13);
    }

    @Test
    @DisplayName("given a provider without price variations when calls deleteAllByProvider then expects")
    void givenAProviderWithoutPriceVariations_whenCallsDeleteAllByProvider_thenExpects() {
        var provider = findProviderById(TEST_PROVIDER_10001);

        assertThat(urbanPropertyPriceVariationRepository.deleteAllByProvider(provider)).isZero();
    }

}
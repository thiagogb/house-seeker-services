package br.com.houseseeker.repository;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.entity.Provider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class UrbanPropertyPriceVariationRepositoryTest extends AbstractJpaIntegrationTest {

    private static final int TEST_PROVIDER = 10000;

    @Autowired
    private UrbanPropertyPriceVariationRepository urbanPropertyPriceVariationRepository;

    @Test
    @DisplayName("given a provider with existing property price variations when calls findAllByProvider then expects 13 rows")
    void givenAProviderWithExistingPropertyPriceVariations_whenCallsFindAllByProvider_thenReturn13Rows() {
        Provider provider = findProviderById(TEST_PROVIDER);

        assertThat(urbanPropertyPriceVariationRepository.findAllByProvider(provider)).hasSize(13);
    }

}
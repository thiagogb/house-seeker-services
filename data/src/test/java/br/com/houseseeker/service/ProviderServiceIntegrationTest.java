package br.com.houseseeker.service;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class ProviderServiceIntegrationTest extends AbstractJpaIntegrationTest {

    private static final int TEST_PROVIDER = 10002;

    @Autowired
    private ProviderService providerService;

    @Test
    @DisplayName("given a existing provider when calls findById then expects present")
    void givenAExistingProvider_whenCallsFindById_thenExpectsPresent() {
        assertThat(providerService.findById(TEST_PROVIDER)).isPresent();
    }

    @Test
    @DisplayName("given a non existing provider when calls findById then expects empty")
    void givenANonExistingProvider_whenCallsFindById_thenExpectsEmpty() {
        assertThat(providerService.findById(99999)).isEmpty();
    }

}
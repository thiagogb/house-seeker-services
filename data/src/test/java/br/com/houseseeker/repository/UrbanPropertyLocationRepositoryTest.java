package br.com.houseseeker.repository;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class UrbanPropertyLocationRepositoryTest extends AbstractJpaIntegrationTest {

    private static final int TEST_PROVIDER = 10000;

    @Autowired
    private UrbanPropertyLocationRepository urbanPropertyLocationRepository;

    @Test
    @DisplayName("given a provider with existing property locations when calls findAllByProvider then expects five rows")
    void givenAProviderWithExistingPropertyLocations_whenCallsFindAllByProvider_thenReturnFiveRows() {
        var provider = findProviderById(TEST_PROVIDER);

        assertThat(urbanPropertyLocationRepository.findAllByProvider(provider))
                .extracting("urbanProperty.providerCode", "state", "city", "district")
                .containsExactly(
                        tuple("500489", "RS", "Santa Maria", "Tomazetti"),
                        tuple("2302", "RS", "Santa Maria", "Nossa Senhora Medianeira"),
                        tuple("98297", "RS", "Santa Maria", "Nossa Senhora de Fátima"),
                        tuple("145687", "RS", "Santa Maria", "Camobi"),
                        tuple("3272", "RS", "Santa Maria", "Patronato")
                );
    }

    @Test
    @DisplayName("given a provider with property locations when calls deleteAllByProvider then expects")
    void givenAProviderWithPropertyLocations_whenCallsDeleteAllByProvider_thenExpects() {
        var provider = findProviderById(TEST_PROVIDER);

        assertThat(urbanPropertyLocationRepository.deleteAllByProvider(provider)).isEqualTo(5);
    }

}
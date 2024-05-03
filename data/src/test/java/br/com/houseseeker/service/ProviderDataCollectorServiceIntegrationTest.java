package br.com.houseseeker.service;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.service.ProviderDataCollectorService.UrbanPropertyFullData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ProviderDataCollectorServiceIntegrationTest extends AbstractJpaIntegrationTest {

    private static final int TEST_PROVIDER = 10000;

    @Autowired
    private ProviderDataCollectorService providerDataCollectorService;

    @Test
    @DisplayName("given a provider with associated data when calls collect then expects")
    void givenAProviderWithAssociatedData_whenCallsCollect_thenExpects() {
        Provider provider = findProviderById(TEST_PROVIDER);

        Map<UrbanProperty, UrbanPropertyFullData> response = providerDataCollectorService.collect(provider);

        assertThat(response.keySet())
                .extracting("providerCode")
                .containsExactly("145687", "2302", "3272", "500489", "98297");

        assertThat(response.values())
                .extracting("location", "measure", "priceVariations")
                .doesNotContainNull();

        assertThat(response.values()).extracting(data -> data.getMedias().size())
                                     .containsExactly(11, 4, 11, 62, 11);

        assertThat(response.values()).extracting(data -> data.getConveniences().size())
                                     .containsExactly(14, 7, 15, 22, 15);

        assertThat(response.values()).extracting(data -> data.getPriceVariations().size())
                                     .containsExactly(2, 2, 2, 5, 2);
    }

}
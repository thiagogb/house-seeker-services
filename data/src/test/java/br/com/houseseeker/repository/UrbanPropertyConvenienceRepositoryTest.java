package br.com.houseseeker.repository;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.entity.Provider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class UrbanPropertyConvenienceRepositoryTest extends AbstractJpaIntegrationTest {

    private static final int TEST_PROVIDER = 10000;

    @Autowired
    private UrbanPropertyConvenienceRepository urbanPropertyConvenienceRepository;

    @Test
    @DisplayName("given a provider with existing property conveniences when calls findAllByProvider then expects 73 rows")
    void givenAProviderWithExistingPropertyConveniences_whenCallsFindAllByProvider_thenReturn73Rows() {
        Provider provider = findProviderById(TEST_PROVIDER);

        assertThat(urbanPropertyConvenienceRepository.findAllByProvider(provider)).hasSize(73);
    }

}
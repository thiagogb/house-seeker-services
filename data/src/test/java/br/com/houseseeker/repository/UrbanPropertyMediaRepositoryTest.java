package br.com.houseseeker.repository;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.entity.Provider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class UrbanPropertyMediaRepositoryTest extends AbstractJpaIntegrationTest {

    private static final int TEST_PROVIDER = 10000;

    @Autowired
    private UrbanPropertyMediaRepository urbanPropertyMediaRepository;

    @Test
    @DisplayName("given a provider with existing property medias when calls findAllByProvider then expects 99 rows")
    void givenAProviderWithExistingPropertyMedias_whenCallsFindAllByProvider_thenReturn99Rows() {
        Provider provider = findProviderById(TEST_PROVIDER);

        assertThat(urbanPropertyMediaRepository.findAllByProvider(provider)).hasSize(99);
    }

}
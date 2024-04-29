package br.com.houseseeker.repository;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.entity.Provider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static br.com.houseseeker.domain.property.UrbanPropertyContract.SELL;
import static br.com.houseseeker.domain.property.UrbanPropertyType.RESIDENTIAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class UrbanPropertyRepositoryTest extends AbstractJpaIntegrationTest {

    private static final int TEST_PROVIDER = 10000;

    @Autowired
    private UrbanPropertyRepository urbanPropertyRepository;

    @Test
    @DisplayName("given a provider with existing properties when calls findAllByProvider then expects five rows")
    void givenAProviderWithExistingProperties_whenCallsFindAllByProvider_thenExpectFiveRows() {
        Provider provider = findProviderById(TEST_PROVIDER);

        assertThat(urbanPropertyRepository.findAllByProvider(provider))
                .extracting("providerCode", "contract", "type", "subType", "sellPrice", "rentPrice")
                .containsExactly(
                        tuple("145687", SELL, RESIDENTIAL, "Casa de Condomínio", new BigDecimal("1180000.00"), new BigDecimal("1180000.00")),
                        tuple("2302", SELL, RESIDENTIAL, "Apartamento", new BigDecimal("1166000.00"), new BigDecimal("1166000.00")),
                        tuple("3272", SELL, RESIDENTIAL, "Casa", new BigDecimal("1590000.00"), new BigDecimal("1590000.00")),
                        tuple("500489", SELL, RESIDENTIAL, "Casa de Condomínio", new BigDecimal("1800000.00"), new BigDecimal("1800000.00")),
                        tuple("98297", SELL, RESIDENTIAL, "Cobertura", new BigDecimal("960000.00"), new BigDecimal("960000.00"))
                );
    }

}
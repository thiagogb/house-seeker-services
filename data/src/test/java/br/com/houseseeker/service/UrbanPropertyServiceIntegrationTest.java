package br.com.houseseeker.service;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanProperty;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static br.com.houseseeker.domain.property.UrbanPropertyContract.SELL;
import static br.com.houseseeker.domain.property.UrbanPropertyType.RESIDENTIAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class UrbanPropertyServiceIntegrationTest extends AbstractJpaIntegrationTest {

    private static final int TEST_PROVIDER = 10000;

    @Autowired
    private UrbanPropertyService urbanPropertyService;

    @Test
    @DisplayName("given a provider with existing properties when calls findAllByProvider then expects five rows")
    void givenAProviderWithExistingProperties_whenCallsFindAllByProvider_thenExpectFiveRows() {
        Provider provider = findProviderById(TEST_PROVIDER);

        assertThat(urbanPropertyService.findAllByProvider(provider))
                .extracting("providerCode", "contract", "type", "subType", "sellPrice", "rentPrice")
                .containsExactly(
                        tuple("145687", SELL, RESIDENTIAL, "Casa de Condomínio", new BigDecimal("1180000.00"), new BigDecimal("1180.00")),
                        tuple("2302", SELL, RESIDENTIAL, "Apartamento", new BigDecimal("1166000.00"), new BigDecimal("1166.00")),
                        tuple("3272", SELL, RESIDENTIAL, "Casa", new BigDecimal("1590000.00"), new BigDecimal("1590.00")),
                        tuple("500489", SELL, RESIDENTIAL, "Casa de Condomínio", new BigDecimal("1800000.00"), new BigDecimal("1800.00")),
                        tuple("98297", SELL, RESIDENTIAL, "Cobertura", new BigDecimal("960000.00"), new BigDecimal("960.00"))
                );
    }

    @Test
    @DisplayName("given a batch of properties to save when calls saveAll then expects to save all then return")
    void givenABatchOfPropertiesToSave_whenCallsSaveAll_thenExpectsToSaveAllAndReturn() {
        Provider provider = findProviderById(TEST_PROVIDER);

        List<UrbanProperty> batchOfProperties = IntStream.range(0, 1000)
                                                         .mapToObj(i -> UrbanProperty.builder()
                                                                                     .provider(provider)
                                                                                     .providerCode(String.format("PC%d", i + 1))
                                                                                     .url(String.format("http://test.com/property/PC%d", i + 1))
                                                                                     .contract(SELL)
                                                                                     .creationDate(LocalDateTime.now())
                                                                                     .analyzable(true)
                                                                                     .build()
                                                         )
                                                         .toList();

        assertThat(urbanPropertyService.saveAll(batchOfProperties)).hasSize(1000);
    }

}
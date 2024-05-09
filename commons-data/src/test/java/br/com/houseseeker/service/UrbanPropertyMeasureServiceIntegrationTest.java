package br.com.houseseeker.service;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanPropertyMeasure;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class UrbanPropertyMeasureServiceIntegrationTest extends AbstractJpaIntegrationTest {

    private static final int TEST_PROVIDER = 10000;

    @Autowired
    private UrbanPropertyMeasureService urbanPropertyMeasureService;

    @Test
    @DisplayName("given a provider with existing property measures when calls findAllByProvider then expects five rows")
    void givenAProviderWithExistingPropertyMeasures_whenCallsFindAllByProvider_thenReturnFiveRows() {
        Provider provider = findProviderById(TEST_PROVIDER);

        assertThat(urbanPropertyMeasureService.findAllByProvider(provider))
                .extracting(
                        "urbanProperty.providerCode",
                        "totalArea",
                        "privateArea",
                        "usableArea",
                        "terrainTotalArea",
                        "terrainFront",
                        "terrainBack",
                        "terrainLeft",
                        "terrainRight",
                        "areaUnit"
                )
                .containsExactly(
                        tuple("500489", new BigDecimal("262.00"), null, null, null, null, null, null, null, "m²"),
                        tuple("2302", new BigDecimal("226.00"), new BigDecimal("149.00"), null, null, null, null, null, null, "m²"),
                        tuple("98297", new BigDecimal("312.00"), new BigDecimal("284.00"), new BigDecimal("272.00"), null, null,
                              null, null, null, "m²"),
                        tuple("145687", new BigDecimal("250.00"), new BigDecimal("182.00"), null, null, null, null, null, null, "m²"),
                        tuple("3272", null, new BigDecimal("347.00"), null, new BigDecimal("500.00"), new BigDecimal("25.00"),
                              new BigDecimal("25.00"), new BigDecimal("25.00"), new BigDecimal("25.00"), "m²")
                );
    }

    @Test
    @DisplayName("given a batch of property measures to save when calls saveAll then expects to save all and return")
    void givenABatchOfPropertyMeasuresToSave_whenCallsSaveAll_thenExpectsToSaveAllAndReturn() {
        List<UrbanPropertyMeasure> urbanPropertyMeasures = IntStream.rangeClosed(10000, 10004)
                                                                    .mapToObj(id -> UrbanPropertyMeasure.builder()
                                                                                                        .urbanProperty(findUrbanPropertyById(id))
                                                                                                        .build()
                                                                    )
                                                                    .toList();

        assertThat(urbanPropertyMeasureService.saveAll(urbanPropertyMeasures)).hasSize(5);
    }

}
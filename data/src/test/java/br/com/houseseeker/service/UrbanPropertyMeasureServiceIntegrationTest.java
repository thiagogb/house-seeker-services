package br.com.houseseeker.service;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.domain.proto.DoubleComparisonData;
import br.com.houseseeker.domain.proto.Int32ComparisonData;
import br.com.houseseeker.domain.proto.Int32SingleComparisonData;
import br.com.houseseeker.domain.proto.OrderDetailsData;
import br.com.houseseeker.domain.proto.OrderDirectionData;
import br.com.houseseeker.entity.UrbanPropertyMeasure;
import br.com.houseseeker.service.proto.GetUrbanPropertyMeasuresRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertyMeasuresRequest.ClausesData;
import br.com.houseseeker.service.proto.GetUrbanPropertyMeasuresRequest.OrdersData;
import br.com.houseseeker.service.proto.GetUrbanPropertyMeasuresRequest.ProjectionsData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
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
        var provider = findProviderById(TEST_PROVIDER);

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
    @DisplayName("given a proto request when calls findBy then expects")
    void givenAProtoRequest_whenCallsFindBy_thenExpects() {
        var request = GetUrbanPropertyMeasuresRequest.newBuilder()
                                                     .setProjections(
                                                             ProjectionsData.newBuilder()
                                                                            .setId(true)
                                                                            .setTotalArea(true)
                                                                            .setPrivateArea(true)
                                                                            .setUsableArea(true)
                                                                            .build()
                                                     )
                                                     .setClauses(
                                                             ClausesData.newBuilder()
                                                                        .setUrbanPropertyId(
                                                                                Int32ComparisonData.newBuilder()
                                                                                                   .setIsGreaterOrEqual(
                                                                                                           Int32SingleComparisonData.newBuilder()
                                                                                                                                    .setValue(10000)
                                                                                                                                    .build()
                                                                                                   )
                                                                                                   .build()
                                                                        )
                                                                        .setTotalArea(
                                                                                DoubleComparisonData.newBuilder()
                                                                                                    .setIsNotNull(true)
                                                                                                    .build()
                                                                        )
                                                                        .setPrivateArea(
                                                                                DoubleComparisonData.newBuilder()
                                                                                                    .setIsNotNull(true)
                                                                                                    .build()
                                                                        )
                                                                        .build()
                                                     )
                                                     .setOrders(
                                                             OrdersData.newBuilder()
                                                                       .setTotalArea(
                                                                               OrderDetailsData.newBuilder()
                                                                                               .setIndex(1)
                                                                                               .setDirection(OrderDirectionData.ASC)
                                                                                               .build()
                                                                       )
                                                                       .build()
                                                     )
                                                     .build();

        assertThat(urbanPropertyMeasureService.findBy(request))
                .extracting("id", "totalArea", "privateArea", "usableArea")
                .containsExactly(
                        tuple(1375, new BigDecimal("226.00"), new BigDecimal("149.00"), null),
                        tuple(1452, new BigDecimal("250.00"), new BigDecimal("182.00"), null),
                        tuple(1430, new BigDecimal("312.00"), new BigDecimal("284.00"), new BigDecimal("272.00"))
                );
    }

    @Test
    @DisplayName("given a batch of property measures to save when calls saveAll then expects to save all and return")
    void givenABatchOfPropertyMeasuresToSave_whenCallsSaveAll_thenExpectsToSaveAllAndReturn() {
        var urbanPropertyMeasures = IntStream.rangeClosed(10000, 10004)
                                             .mapToObj(id -> UrbanPropertyMeasure.builder()
                                                                                 .urbanProperty(findUrbanPropertyById(id))
                                                                                 .build()
                                             )
                                             .toList();

        assertThat(urbanPropertyMeasureService.saveAll(urbanPropertyMeasures)).hasSize(5);
    }

    @Test
    @DisplayName("given a provider with property measures when calls deleteAllByProvider then expects")
    void givenAProviderWithPropertyMeasures_whenCallsDeleteAllByProvider_thenExpects() {
        var provider = findProviderById(TEST_PROVIDER);

        assertThat(urbanPropertyMeasureService.deleteAllByProvider(provider)).isEqualTo(5);
    }

}
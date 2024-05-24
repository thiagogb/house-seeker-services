package br.com.houseseeker.service;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.domain.proto.Int32ComparisonData;
import br.com.houseseeker.domain.proto.Int32IntervalComparisonData;
import br.com.houseseeker.domain.proto.OrderDetailsData;
import br.com.houseseeker.domain.proto.OrderDirectionData;
import br.com.houseseeker.domain.proto.StringComparisonData;
import br.com.houseseeker.domain.proto.StringSingleComparisonData;
import br.com.houseseeker.entity.UrbanPropertyLocation;
import br.com.houseseeker.service.proto.GetUrbanPropertyLocationsRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertyLocationsRequest.ClausesData;
import br.com.houseseeker.service.proto.GetUrbanPropertyLocationsRequest.OrdersData;
import br.com.houseseeker.service.proto.GetUrbanPropertyLocationsRequest.ProjectionsData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class UrbanPropertyLocationServiceIntegrationTest extends AbstractJpaIntegrationTest {

    private static final int TEST_PROVIDER = 10000;

    @Autowired
    private UrbanPropertyLocationService urbanPropertyLocationService;

    @Test
    @DisplayName("given a provider with existing property locations when calls findAllByProvider then expects five rows")
    void givenAProviderWithExistingPropertyLocations_whenCallsFindAllByProvider_thenReturnFiveRows() {
        var provider = findProviderById(TEST_PROVIDER);

        assertThat(urbanPropertyLocationService.findAllByProvider(provider))
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
    @DisplayName("given a proto request when calls findBy then expects")
    void givenAProtoRequest_whenCallsFindBy_thenExpects() {
        var request = GetUrbanPropertyLocationsRequest.newBuilder()
                                                      .setProjections(
                                                              ProjectionsData.newBuilder()
                                                                             .setId(true)
                                                                             .setState(true)
                                                                             .setCity(true)
                                                                             .setDistrict(true)
                                                                             .build()
                                                      )
                                                      .setClauses(
                                                              ClausesData.newBuilder()
                                                                         .setUrbanPropertyId(
                                                                                 Int32ComparisonData.newBuilder()
                                                                                                    .setIsBetween(
                                                                                                            Int32IntervalComparisonData.newBuilder()
                                                                                                                                       .setStart(10000)
                                                                                                                                       .setEnd(10002)
                                                                                                                                       .build()
                                                                                                    )
                                                                                                    .build()
                                                                         )
                                                                         .setState(
                                                                                 StringComparisonData.newBuilder()
                                                                                                     .setIsEqual(
                                                                                                             StringSingleComparisonData.newBuilder()
                                                                                                                                       .setValue("RS")
                                                                                                                                       .build()
                                                                                                     )
                                                                                                     .build()
                                                                         )
                                                                         .setStreetName(
                                                                                 StringComparisonData.newBuilder()
                                                                                                     .setIsNull(true)
                                                                                                     .build()
                                                                         )
                                                                         .build()
                                                      )
                                                      .setOrders(
                                                              OrdersData.newBuilder()
                                                                        .setId(
                                                                                OrderDetailsData.newBuilder()
                                                                                                .setIndex(1)
                                                                                                .setDirection(OrderDirectionData.DESC)
                                                                                                .build()
                                                                        )
                                                                        .build()
                                                      )
                                                      .build();

        assertThat(urbanPropertyLocationService.findBy(request))
                .extracting("id", "state", "city", "district")
                .containsExactly(
                        tuple(1430, "RS", "Santa Maria", "Nossa Senhora de Fátima"),
                        tuple(1375, "RS", "Santa Maria", "Nossa Senhora Medianeira"),
                        tuple(1369, "RS", "Santa Maria", "Tomazetti")
                );
    }

    @Test
    @DisplayName("given a batch of property locations to save when calls saveAll then expects to save all and return")
    void givenABatchOfPropertyLocationsToSave_whenCallsSaveAll_thenExpectsToSaveAllAndReturn() {
        var urbanPropertyLocations = IntStream.rangeClosed(10000, 10004)
                                              .mapToObj(id -> UrbanPropertyLocation.builder()
                                                                                   .urbanProperty(findUrbanPropertyById(id))
                                                                                   .build()
                                              )
                                              .toList();

        assertThat(urbanPropertyLocationService.saveAll(urbanPropertyLocations)).hasSize(5);
    }

    @Test
    @DisplayName("given a provider with property locations when calls deleteAllByProvider then expects")
    void givenAProviderWithPropertyLocations_whenCallsDeleteAllByProvider_thenExpects() {
        var provider = findProviderById(TEST_PROVIDER);

        assertThat(urbanPropertyLocationService.deleteAllByProvider(provider)).isEqualTo(5);
    }

}
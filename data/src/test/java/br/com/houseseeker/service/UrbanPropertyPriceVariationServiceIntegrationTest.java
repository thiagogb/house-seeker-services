package br.com.houseseeker.service;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.domain.proto.DateTimeComparisonData;
import br.com.houseseeker.domain.proto.DateTimeSingleComparisonData;
import br.com.houseseeker.domain.proto.EnumComparisonData;
import br.com.houseseeker.domain.proto.EnumSingleComparisonData;
import br.com.houseseeker.domain.proto.OrderDetailsData;
import br.com.houseseeker.domain.proto.OrderDirectionData;
import br.com.houseseeker.domain.proto.PaginationRequestData;
import br.com.houseseeker.entity.UrbanPropertyPriceVariation;
import br.com.houseseeker.service.proto.GetUrbanPropertyPriceVariationsRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertyPriceVariationsRequest.ClausesData;
import br.com.houseseeker.service.proto.GetUrbanPropertyPriceVariationsRequest.OrdersData;
import br.com.houseseeker.service.proto.GetUrbanPropertyPriceVariationsRequest.ProjectionsData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static br.com.houseseeker.domain.property.UrbanPropertyPriceVariationType.SELL;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class UrbanPropertyPriceVariationServiceIntegrationTest extends AbstractJpaIntegrationTest {

    private static final int TEST_PROVIDER_10000 = 10000;
    private static final int TEST_PROVIDER_10001 = 10001;
    private static final int TEST_PROPERTY_10000 = 10000;

    @Autowired
    private UrbanPropertyPriceVariationService urbanPropertyPriceVariationService;

    @Test
    @DisplayName("given a provider with price variations when calls findAllByProvider then expects")
    void givenAProviderWithPriceVariations_whenCallsFindAllByProvider_thenExpects() {
        var provider = findProviderById(TEST_PROVIDER_10000);

        assertThat(urbanPropertyPriceVariationService.findAllByProvider(provider)).hasSize(13);
    }

    @Test
    @DisplayName("given a provider without price variations when calls findAllByProvider then expects")
    void givenAProviderWithoutPriceVariations_whenCallsFindAllByProvider_thenExpects() {
        var provider = findProviderById(TEST_PROVIDER_10001);

        assertThat(urbanPropertyPriceVariationService.findAllByProvider(provider)).isEmpty();
    }

    @Test
    @DisplayName("given a proto request when calls findBy then expects")
    void givenAProtoRequest_whenCallsFindBy_thenExpects() {
        var clauses = ClausesData.newBuilder()
                                 .setAnalysisDate(
                                         DateTimeComparisonData.newBuilder()
                                                               .setIsLesser(
                                                                       DateTimeSingleComparisonData.newBuilder()
                                                                                                   .setValue("2024-04-27T00:00:00")
                                                                                                   .build()
                                                               )
                                                               .build()
                                 )
                                 .setType(
                                         EnumComparisonData.newBuilder()
                                                           .setIsEqual(
                                                                   EnumSingleComparisonData.newBuilder()
                                                                                           .setValue(SELL.name())
                                                                                           .build()
                                                           )
                                                           .build()
                                 )
                                 .build();
        var request = GetUrbanPropertyPriceVariationsRequest.newBuilder()
                                                            .setProjections(
                                                                    ProjectionsData.newBuilder()
                                                                                   .setId(true)
                                                                                   .setAnalysisDate(true)
                                                                                   .setPrice(true)
                                                                                   .setVariation(true)
                                                                                   .build()
                                                            )
                                                            .setClauses(clauses)
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
                                                            .setPagination(
                                                                    PaginationRequestData.newBuilder()
                                                                                         .setPageSize(3)
                                                                                         .build()
                                                            )
                                                            .build();

        assertThat(urbanPropertyPriceVariationService.findBy(request))
                .extracting("id", "analysisDate", "price", "variation")
                .containsExactly(
                        tuple(
                                10011,
                                LocalDateTime.parse("2024-04-26T11:10:25", ISO_LOCAL_DATE_TIME),
                                new BigDecimal("1590000.00"),
                                new BigDecimal("0.00")
                        ),
                        tuple(
                                10009,
                                LocalDateTime.parse("2024-04-26T11:10:25", ISO_LOCAL_DATE_TIME),
                                new BigDecimal("1180000.00"),
                                new BigDecimal("0.00")
                        ),
                        tuple(
                                10007,
                                LocalDateTime.parse("2024-04-26T11:10:25", ISO_LOCAL_DATE_TIME),
                                new BigDecimal("960000.00"),
                                new BigDecimal("0.00")
                        )
                );
    }

    @Test
    @DisplayName("given a batch of price variations when calls saveAll then expects")
    void givenABatchOfPriceVariations_whenCallsSaveAll_thenExpects() {
        var urbanProperty = findUrbanPropertyById(TEST_PROPERTY_10000);
        var urbanPropertyPriceVariations = IntStream.range(0, 100)
                                                    .mapToObj(i -> UrbanPropertyPriceVariation.builder()
                                                                                              .urbanProperty(urbanProperty)
                                                                                              .type(SELL)
                                                                                              .price(BigDecimal.valueOf(i))
                                                                                              .variation(BigDecimal.valueOf(i))
                                                                                              .build()
                                                    )
                                                    .toList();

        assertThat(urbanPropertyPriceVariationService.saveAll(urbanPropertyPriceVariations)).hasSize(100);
    }

    @Test
    @DisplayName("given a provider with price variations when calls deleteAllByProvider then expects")
    void givenAProviderWithPriceVariations_whenCallsDeleteAllByProvider_thenExpects() {
        var provider = findProviderById(TEST_PROVIDER_10000);

        assertThat(urbanPropertyPriceVariationService.deleteAllByProvider(provider)).isEqualTo(13);
    }

}
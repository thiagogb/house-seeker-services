package br.com.houseseeker.service.grpc;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.TestStreamObserver;
import br.com.houseseeker.domain.proto.DoubleComparisonData;
import br.com.houseseeker.domain.proto.DoubleSingleComparisonData;
import br.com.houseseeker.domain.proto.EnumComparisonData;
import br.com.houseseeker.domain.proto.EnumListComparisonData;
import br.com.houseseeker.domain.proto.OrderDetailsData;
import br.com.houseseeker.domain.proto.OrderDirectionData;
import br.com.houseseeker.domain.proto.PaginationRequestData;
import br.com.houseseeker.service.proto.GetUrbanPropertyPriceVariationsRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertyPriceVariationsRequest.ClausesData;
import br.com.houseseeker.service.proto.GetUrbanPropertyPriceVariationsRequest.OrdersData;
import br.com.houseseeker.service.proto.GetUrbanPropertyPriceVariationsRequest.ProjectionsData;
import br.com.houseseeker.service.proto.GetUrbanPropertyPriceVariationsResponse;
import com.google.protobuf.DoubleValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static br.com.houseseeker.domain.property.UrbanPropertyPriceVariationType.RENT;
import static br.com.houseseeker.domain.property.UrbanPropertyPriceVariationType.SELL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class UrbanPropertyPriceVariationGrpcDataServiceTest extends AbstractJpaIntegrationTest {

    @Autowired
    private UrbanPropertyPriceVariationGrpcDataService urbanPropertyPriceVariationGrpcDataService;

    @Test
    @DisplayName("given a request when calls getUrbanPropertyPriceVariations then expects")
    void givenARequest_whenCallsGetUrbanPropertyPriceVariations_thenExpects() {
        var request = GetUrbanPropertyPriceVariationsRequest.newBuilder()
                                                            .setProjections(
                                                                    ProjectionsData.newBuilder()
                                                                                   .setId(true)
                                                                                   .setUrbanProperty(true)
                                                                                   .setType(true)
                                                                                   .setPrice(true)
                                                                                   .setVariation(true)
                                                                                   .build()
                                                            )
                                                            .setClauses(
                                                                    ClausesData.newBuilder()
                                                                               .setType(
                                                                                       EnumComparisonData.newBuilder()
                                                                                                         .setIsIn(
                                                                                                                 EnumListComparisonData.newBuilder()
                                                                                                                                       .addValues(SELL.name())
                                                                                                                                       .addValues(RENT.name())
                                                                                                                                       .build()
                                                                                                         )
                                                                                                         .build()
                                                                               )
                                                                               .setVariation(
                                                                                       DoubleComparisonData.newBuilder()
                                                                                                           .setIsGreater(
                                                                                                                   DoubleSingleComparisonData.newBuilder()
                                                                                                                                             .setValue(0)
                                                                                                                                             .build()
                                                                                                           )
                                                                                                           .build()
                                                                               )
                                                                               .build()
                                                            )
                                                            .setOrders(
                                                                    OrdersData.newBuilder()
                                                                              .setAnalysisDate(
                                                                                      OrderDetailsData.newBuilder()
                                                                                                      .setIndex(1)
                                                                                                      .setDirection(OrderDirectionData.ASC)
                                                                                                      .build()
                                                                              )
                                                                              .setId(
                                                                                      OrderDetailsData.newBuilder()
                                                                                                      .setIndex(2)
                                                                                                      .setDirection(OrderDirectionData.DESC)
                                                                                                      .build()
                                                                              )
                                                                              .build()
                                                            )
                                                            .setPagination(
                                                                    PaginationRequestData.newBuilder()
                                                                                         .setPageSize(2)
                                                                                         .setPageNumber(1)
                                                                                         .build()
                                                            )
                                                            .build();
        var responseObserver = new TestStreamObserver<GetUrbanPropertyPriceVariationsResponse>();

        urbanPropertyPriceVariationGrpcDataService.getUrbanPropertyPriceVariations(request, responseObserver);

        assertThat(responseObserver)
                .satisfies(ro -> assertThat(ro.getValue())
                        .satisfies(r -> assertThat(r.getUrbanPropertyPriceVariationsList())
                                .extracting("id", "urbanProperty.providerCode", "type", "price", "variation")
                                .containsExactly(
                                        tuple(
                                                Int32Value.of(10003),
                                                StringValue.of("500489"),
                                                StringValue.of(RENT.name()),
                                                DoubleValue.of(1800),
                                                DoubleValue.of(6.45)
                                        ),
                                        tuple(
                                                Int32Value.of(10001),
                                                StringValue.of("500489"),
                                                StringValue.of(SELL.name()),
                                                DoubleValue.of(1800000),
                                                DoubleValue.of(6.45)
                                        )
                                )
                        )
                        .satisfies(r -> assertThat(r.getPagination())
                                .extracting("pageSize", "pageNumber", "totalPages", "totalRows")
                                .containsExactly(2, 1, 1, 2L)
                        )
                )
                .satisfies(ro -> assertThat(ro.getThrowable()).isNull());
    }

}
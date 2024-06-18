package br.com.houseseeker.service.grpc;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.TestStreamObserver;
import br.com.houseseeker.domain.proto.OrderDetailsData;
import br.com.houseseeker.domain.proto.OrderDirectionData;
import br.com.houseseeker.domain.proto.StringComparisonData;
import br.com.houseseeker.domain.proto.StringSingleComparisonData;
import br.com.houseseeker.service.proto.GetConveniencesRequest;
import br.com.houseseeker.service.proto.GetConveniencesRequest.ClausesData;
import br.com.houseseeker.service.proto.GetConveniencesRequest.OrdersData;
import br.com.houseseeker.service.proto.GetConveniencesResponse;
import com.google.protobuf.StringValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class ConvenienceGrpcDataServiceTest extends AbstractJpaIntegrationTest {

    @Autowired
    private ConvenienceGrpcDataService convenienceGrpcDataService;

    @Test
    @DisplayName("given a conveniences request when calls getConveniences then expects observer to catch response")
    void givenAConvenienceRequest_whenCallsGetConveniencesWithoutErrors_thenExpectsObserverToCatchResponse() {
        var request = GetConveniencesRequest.newBuilder()
                                            .addClauses(
                                                    ClausesData.newBuilder()
                                                               .setDescription(
                                                                       StringComparisonData.newBuilder()
                                                                                           .setIsStartingWith(
                                                                                                   StringSingleComparisonData.newBuilder()
                                                                                                                             .setValue("SALA")
                                                                                                                             .build()
                                                                                           )
                                                                                           .build()
                                                               )
                                                               .build()
                                            )
                                            .setOrders(
                                                    OrdersData.newBuilder()
                                                              .setDescription(
                                                                      OrderDetailsData.newBuilder()
                                                                                      .setIndex(1)
                                                                                      .setDirection(OrderDirectionData.ASC)
                                                                                      .build()
                                                              )
                                                              .build()
                                            )
                                            .build();
        var responseObserver = new TestStreamObserver<GetConveniencesResponse>();

        convenienceGrpcDataService.getConveniences(request, responseObserver);

        assertThat(responseObserver)
                .satisfies(ro -> assertThat(ro.getValue())
                        .satisfies(r -> assertThat(r.getConveniencesList())
                                .extracting(StringValue::getValue)
                                .containsExactly("SALA DE ESTAR", "SALA DE JANTAR")
                        )
                        .satisfies(r -> assertThat(r.getPagination())
                                .extracting("pageSize", "pageNumber", "totalPages", "totalRows")
                                .containsExactly(50, 1, 1, 2L)
                        )
                )
                .satisfies(ro -> assertThat(ro.getThrowable()).isNull());
    }

}
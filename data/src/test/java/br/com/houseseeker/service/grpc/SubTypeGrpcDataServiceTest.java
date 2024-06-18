package br.com.houseseeker.service.grpc;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.TestStreamObserver;
import br.com.houseseeker.domain.proto.OrderDetailsData;
import br.com.houseseeker.domain.proto.OrderDirectionData;
import br.com.houseseeker.domain.proto.StringComparisonData;
import br.com.houseseeker.service.proto.GetSubTypesRequest;
import br.com.houseseeker.service.proto.GetSubTypesRequest.ClausesData;
import br.com.houseseeker.service.proto.GetSubTypesRequest.OrdersData;
import br.com.houseseeker.service.proto.GetSubTypesResponse;
import com.google.protobuf.StringValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class SubTypeGrpcDataServiceTest extends AbstractJpaIntegrationTest {

    @Autowired
    private SubTypeGrpcDataService subTypeGrpcDataService;

    @Test
    @DisplayName("given a subType request when calls getSubTypes then expects observer to catch response")
    void givenASubTypeRequest_whenCallsGetSubTypesWithoutErrors_thenExpectsObserverToCatchResponse() {
        var request = GetSubTypesRequest.newBuilder()
                                        .addClauses(
                                                ClausesData.newBuilder()
                                                           .setSubType(
                                                                   StringComparisonData.newBuilder()
                                                                                       .setIsNotBlank(true)
                                                                                       .build()
                                                           )
                                                           .build()
                                        )
                                        .setOrders(
                                                OrdersData.newBuilder()
                                                          .setSubType(
                                                                  OrderDetailsData.newBuilder()
                                                                                  .setIndex(1)
                                                                                  .setDirection(OrderDirectionData.ASC)
                                                                                  .build()
                                                          )
                                                          .build()
                                        )
                                        .build();
        var responseObserver = new TestStreamObserver<GetSubTypesResponse>();

        subTypeGrpcDataService.getSubTypes(request, responseObserver);

        assertThat(responseObserver)
                .satisfies(ro -> assertThat(ro.getValue())
                        .satisfies(r -> assertThat(r.getSubTypesList())
                                .extracting(StringValue::getValue)
                                .containsExactly("Apartamento", "Casa", "Casa de Condomínio", "Cobertura")
                        )
                        .satisfies(r -> assertThat(r.getPagination())
                                .extracting("pageSize", "pageNumber", "totalPages", "totalRows")
                                .containsExactly(50, 1, 1, 4L)
                        )
                )
                .satisfies(ro -> assertThat(ro.getThrowable()).isNull());
    }

}
package br.com.houseseeker.service.grpc;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.TestStreamObserver;
import br.com.houseseeker.domain.proto.OrderDetailsData;
import br.com.houseseeker.domain.proto.OrderDirectionData;
import br.com.houseseeker.domain.proto.PaginationRequestData;
import br.com.houseseeker.service.proto.GetUrbanPropertyConveniencesRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertyConveniencesRequest.OrdersData;
import br.com.houseseeker.service.proto.GetUrbanPropertyConveniencesRequest.ProjectionsData;
import br.com.houseseeker.service.proto.GetUrbanPropertyConveniencesResponse;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class UrbanPropertyConvenienceGrpcDataServiceTest extends AbstractJpaIntegrationTest {

    @Autowired
    private UrbanPropertyConvenienceGrpcDataService urbanPropertyConvenienceGrpcDataService;

    @Test
    @DisplayName("given a provider request when calls getProviders then expects observer to catch response")
    void givenARequest_whenCallsGetUrbanPropertyConveniencesWithoutErrors_thenExpectsObserverToCatchResponse() {
        var request = GetUrbanPropertyConveniencesRequest.newBuilder()
                                                         .setProjections(
                                                                 ProjectionsData.newBuilder()
                                                                                .setId(true)
                                                                                .setUrbanProperty(true)
                                                                                .setDescription(true)
                                                                                .build()
                                                         )
                                                         .setOrders(
                                                                 OrdersData.newBuilder()
                                                                           .setId(
                                                                                   OrderDetailsData.newBuilder()
                                                                                                   .setIndex(2)
                                                                                                   .setDirection(OrderDirectionData.ASC)
                                                                                                   .build()
                                                                           )
                                                                           .setUrbanPropertyId(
                                                                                   OrderDetailsData.newBuilder()
                                                                                                   .setIndex(1)
                                                                                                   .setDirection(OrderDirectionData.ASC)
                                                                                                   .build()
                                                                           )
                                                                           .build()
                                                         )
                                                         .setPagination(
                                                                 PaginationRequestData.newBuilder()
                                                                                      .setPageSize(5)
                                                                                      .setPageNumber(3)
                                                                                      .build()
                                                         )
                                                         .build();
        var responseObserver = new TestStreamObserver<GetUrbanPropertyConveniencesResponse>();

        urbanPropertyConvenienceGrpcDataService.getUrbanPropertyConveniences(request, responseObserver);

        assertThat(responseObserver)
                .satisfies(ro -> assertThat(ro.getValue())
                        .satisfies(r -> assertThat(r.getUrbanPropertyConveniencesList())
                                .extracting("id", "urbanProperty.providerCode", "description")
                                .containsExactly(
                                        tuple(Int32Value.of(10061), StringValue.of("500489"), StringValue.of("ESCRITÓRIO")),
                                        tuple(Int32Value.of(10062), StringValue.of("500489"), StringValue.of("ESPAÇO GOURMET")),
                                        tuple(Int32Value.of(10063), StringValue.of("500489"), StringValue.of("ESTAR SOCIAL")),
                                        tuple(Int32Value.of(10064), StringValue.of("500489"), StringValue.of("INTERFONE")),
                                        tuple(Int32Value.of(10065), StringValue.of("500489"), StringValue.of("JARDIM"))
                                )
                        )
                        .satisfies(r -> assertThat(r.getPagination())
                                .extracting("pageSize", "pageNumber", "totalPages", "totalRows")
                                .containsExactly(5, 3, 15, 73L)
                        )
                );
    }

}
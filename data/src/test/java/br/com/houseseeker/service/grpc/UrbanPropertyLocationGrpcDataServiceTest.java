package br.com.houseseeker.service.grpc;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.TestStreamObserver;
import br.com.houseseeker.domain.proto.OrderDetailsData;
import br.com.houseseeker.domain.proto.OrderDirectionData;
import br.com.houseseeker.domain.proto.PaginationRequestData;
import br.com.houseseeker.domain.proto.StringComparisonData;
import br.com.houseseeker.domain.proto.StringSingleComparisonData;
import br.com.houseseeker.service.proto.GetUrbanPropertyLocationsRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertyLocationsRequest.ClausesData;
import br.com.houseseeker.service.proto.GetUrbanPropertyLocationsRequest.OrdersData;
import br.com.houseseeker.service.proto.GetUrbanPropertyLocationsRequest.ProjectionsData;
import br.com.houseseeker.service.proto.GetUrbanPropertyLocationsResponse;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class UrbanPropertyLocationGrpcDataServiceTest extends AbstractJpaIntegrationTest {

    @Autowired
    private UrbanPropertyLocationGrpcDataService urbanPropertyLocationGrpcDataService;

    @Test
    @DisplayName("given a request when calls getUrbanPropertyLocations then expects")
    void givenARequest_whenCallsGetUrbanPropertyLocations_thenExpects() {
        var request = GetUrbanPropertyLocationsRequest.newBuilder()
                                                      .setProjections(
                                                              ProjectionsData.newBuilder()
                                                                             .setId(true)
                                                                             .setUrbanProperty(true)
                                                                             .setState(true)
                                                                             .setCity(true)
                                                                             .setDistrict(true)
                                                                             .build()
                                                      )
                                                      .addClauses(
                                                              ClausesData.newBuilder()
                                                                         .setState(
                                                                                 StringComparisonData.newBuilder()
                                                                                                     .setIsEqual(
                                                                                                             StringSingleComparisonData.newBuilder()
                                                                                                                                       .setValue("RS")
                                                                                                                                       .build()
                                                                                                     )
                                                                                                     .build()
                                                                         )
                                                                         .build()
                                                      )
                                                      .setOrders(
                                                              OrdersData.newBuilder()
                                                                        .setUrbanPropertyId(
                                                                                OrderDetailsData.newBuilder()
                                                                                                .setIndex(1)
                                                                                                .setDirection(OrderDirectionData.DESC)
                                                                                                .build()
                                                                        )
                                                                        .build()
                                                      )
                                                      .setPagination(
                                                              PaginationRequestData.newBuilder()
                                                                                   .setPageSize(2)
                                                                                   .setPageNumber(2)
                                                                                   .build()
                                                      )
                                                      .build();
        var responseObserver = new TestStreamObserver<GetUrbanPropertyLocationsResponse>();

        urbanPropertyLocationGrpcDataService.getUrbanPropertyLocations(request, responseObserver);

        assertThat(responseObserver)
                .satisfies(ro -> assertThat(ro.getValue())
                        .satisfies(r -> assertThat(r.getUrbanPropertyLocationsList())
                                .extracting("id", "urbanProperty.providerCode", "state", "city", "district")
                                .containsExactly(
                                        tuple(
                                                Int32Value.of(1430),
                                                StringValue.of("98297"),
                                                StringValue.of("RS"),
                                                StringValue.of("Santa Maria"),
                                                StringValue.of("Nossa Senhora de Fátima")
                                        ),
                                        tuple(
                                                Int32Value.of(1375),
                                                StringValue.of("2302"),
                                                StringValue.of("RS"),
                                                StringValue.of("Santa Maria"),
                                                StringValue.of("Nossa Senhora Medianeira")
                                        )
                                )
                        )
                        .satisfies(r -> assertThat(r.getPagination())
                                .extracting("pageSize", "pageNumber", "totalPages", "totalRows")
                                .containsExactly(2, 2, 3, 5L)
                        )
                )
                .satisfies(ro -> assertThat(ro.getThrowable()).isNull());
    }

}
package br.com.houseseeker.service.grpc;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.TestStreamObserver;
import br.com.houseseeker.domain.proto.DoubleComparisonData;
import br.com.houseseeker.domain.proto.DoubleIntervalComparisonData;
import br.com.houseseeker.domain.proto.OrderDetailsData;
import br.com.houseseeker.domain.proto.OrderDirectionData;
import br.com.houseseeker.domain.proto.PaginationRequestData;
import br.com.houseseeker.service.proto.GetUrbanPropertyMeasuresRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertyMeasuresRequest.ClausesData;
import br.com.houseseeker.service.proto.GetUrbanPropertyMeasuresRequest.OrdersData;
import br.com.houseseeker.service.proto.GetUrbanPropertyMeasuresRequest.ProjectionsData;
import br.com.houseseeker.service.proto.GetUrbanPropertyMeasuresResponse;
import com.google.protobuf.DoubleValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class UrbanPropertyMeasureGrpcDataServiceTest extends AbstractJpaIntegrationTest {

    @Autowired
    private UrbanPropertyMeasureGrpcDataService urbanPropertyMeasureGrpcDataService;

    @Test
    @DisplayName("given a request when calls getUrbanPropertyMeasures then expects")
    void givenARequest_whenCallsGetUrbanPropertyMeasures_thenExpects() {
        var request = GetUrbanPropertyMeasuresRequest.newBuilder()
                                                     .setProjections(
                                                             ProjectionsData.newBuilder()
                                                                            .setId(true)
                                                                            .setUrbanProperty(true)
                                                                            .setTotalArea(true)
                                                                            .setUsableArea(true)
                                                                            .setPrivateArea(true)
                                                                            .build()
                                                     )
                                                     .setClauses(
                                                             ClausesData.newBuilder()
                                                                        .setTotalArea(
                                                                                DoubleComparisonData.newBuilder()
                                                                                                    .setIsBetween(
                                                                                                            DoubleIntervalComparisonData.newBuilder()
                                                                                                                                        .setStart(250)
                                                                                                                                        .setEnd(350)
                                                                                                                                        .build()
                                                                                                    )
                                                                                                    .build()
                                                                        )
                                                                        .build()
                                                     )
                                                     .setOrders(
                                                             OrdersData.newBuilder()
                                                                       .setTotalArea(
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
                                                                                  .setPageNumber(1)
                                                                                  .build()
                                                     )
                                                     .build();
        var responseObserver = new TestStreamObserver<GetUrbanPropertyMeasuresResponse>();

        urbanPropertyMeasureGrpcDataService.getUrbanPropertyMeasures(request, responseObserver);

        assertThat(responseObserver)
                .satisfies(ro -> assertThat(ro.getValue())
                        .satisfies(r -> assertThat(r.getUrbanPropertyMeasuresList())
                                .extracting("id", "urbanProperty.providerCode", "totalArea", "usableArea", "privateArea")
                                .containsExactly(
                                        tuple(
                                                Int32Value.of(1430),
                                                StringValue.of("98297"),
                                                DoubleValue.of(312),
                                                DoubleValue.of(272),
                                                DoubleValue.of(284)
                                        ),
                                        tuple(
                                                Int32Value.of(1369),
                                                StringValue.of("500489"),
                                                DoubleValue.of(262),
                                                DoubleValue.getDefaultInstance(),
                                                DoubleValue.getDefaultInstance()
                                        )
                                )
                        )
                        .satisfies(r -> assertThat(r.getPagination())
                                .extracting("pageSize", "pageNumber", "totalPages", "totalRows")
                                .containsExactly(2, 1, 2, 3L)
                        )
                )
                .satisfies(ro -> assertThat(ro.getThrowable()).isNull());
    }

}
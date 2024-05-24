package br.com.houseseeker.service.grpc;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.TestStreamObserver;
import br.com.houseseeker.domain.proto.Int32ComparisonData;
import br.com.houseseeker.domain.proto.Int32ListComparisonData;
import br.com.houseseeker.domain.proto.OrderDetailsData;
import br.com.houseseeker.domain.proto.OrderDirectionData;
import br.com.houseseeker.domain.proto.PaginationRequestData;
import br.com.houseseeker.service.proto.GetUrbanPropertyMediasRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertyMediasRequest.ClausesData;
import br.com.houseseeker.service.proto.GetUrbanPropertyMediasRequest.OrdersData;
import br.com.houseseeker.service.proto.GetUrbanPropertyMediasRequest.ProjectionsData;
import br.com.houseseeker.service.proto.GetUrbanPropertyMediasResponse;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static br.com.houseseeker.domain.property.UrbanPropertyMediaType.IMAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class UrbanPropertyMediaGrpcDataServiceTest extends AbstractJpaIntegrationTest {

    @Autowired
    private UrbanPropertyMediaGrpcDataService urbanPropertyMediaGrpcDataService;

    @Test
    @DisplayName("given a request when calls getUrbanPropertyMedias then expects")
    void givenARequest_whenCallsGetUrbanPropertyMedias_thenExpects() {
        var request = GetUrbanPropertyMediasRequest.newBuilder()
                                                   .setProjections(
                                                           ProjectionsData.newBuilder()
                                                                          .setId(true)
                                                                          .setUrbanProperty(true)
                                                                          .setMediaType(true)
                                                                          .setExtension(true)
                                                                          .build()
                                                   )
                                                   .setClauses(
                                                           ClausesData.newBuilder()
                                                                      .setUrbanPropertyId(
                                                                              Int32ComparisonData.newBuilder()
                                                                                                 .setIsIn(
                                                                                                         Int32ListComparisonData.newBuilder()
                                                                                                                                .addAllValues(List.of(
                                                                                                                                        10000,
                                                                                                                                        10001
                                                                                                                                ))
                                                                                                                                .build()
                                                                                                 )
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
                                                   .setPagination(
                                                           PaginationRequestData.newBuilder()
                                                                                .setPageSize(3)
                                                                                .setPageNumber(3)
                                                                                .build()
                                                   )
                                                   .build();
        var responseObserver = new TestStreamObserver<GetUrbanPropertyMediasResponse>();

        urbanPropertyMediaGrpcDataService.getUrbanPropertyMedias(request, responseObserver);

        assertThat(responseObserver)
                .satisfies(ro -> assertThat(ro.getValue())
                        .satisfies(r -> assertThat(r.getUrbanPropertyMediasList())
                                .extracting("id", "urbanProperty.providerCode", "mediaType", "extension")
                                .containsExactly(
                                        tuple(
                                                Int32Value.of(10092),
                                                StringValue.of("500489"),
                                                StringValue.of(IMAGE.name()),
                                                StringValue.of("webp")
                                        ),
                                        tuple(
                                                Int32Value.of(10091),
                                                StringValue.of("500489"),
                                                StringValue.of(IMAGE.name()),
                                                StringValue.of("webp")
                                        ),
                                        tuple(
                                                Int32Value.of(10090),
                                                StringValue.of("500489"),
                                                StringValue.of(IMAGE.name()),
                                                StringValue.of("webp")
                                        )
                                )
                        )
                        .satisfies(r -> assertThat(r.getPagination())
                                .extracting("pageSize", "pageNumber", "totalPages", "totalRows")
                                .containsExactly(3, 3, 22, 66L)
                        )
                )
                .satisfies(ro -> assertThat(ro.getThrowable()).isNull());
    }

}
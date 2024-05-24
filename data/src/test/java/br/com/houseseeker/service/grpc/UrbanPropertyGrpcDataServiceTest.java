package br.com.houseseeker.service.grpc;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.TestStreamObserver;
import br.com.houseseeker.domain.proto.Int32ComparisonData;
import br.com.houseseeker.domain.proto.Int32SingleComparisonData;
import br.com.houseseeker.domain.proto.OrderDetailsData;
import br.com.houseseeker.domain.proto.OrderDirectionData;
import br.com.houseseeker.domain.proto.PaginationRequestData;
import br.com.houseseeker.service.proto.GetUrbanPropertiesRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertiesRequest.ClausesData;
import br.com.houseseeker.service.proto.GetUrbanPropertiesRequest.OrdersData;
import br.com.houseseeker.service.proto.GetUrbanPropertiesRequest.ProjectionsData;
import br.com.houseseeker.service.proto.GetUrbanPropertiesResponse;
import com.google.protobuf.DoubleValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class UrbanPropertyGrpcDataServiceTest extends AbstractJpaIntegrationTest {

    @Autowired
    private UrbanPropertyGrpcDataService urbanPropertyGrpcDataService;

    @Test
    @DisplayName("given a request when calls getUrbanProperties then expects")
    void givenARequest_whenCallsGetUrbanProperties_thenExpects() {
        var request = GetUrbanPropertiesRequest.newBuilder()
                                               .setProjections(
                                                       ProjectionsData.newBuilder()
                                                                      .setId(true)
                                                                      .setProvider(true)
                                                                      .setProviderCode(true)
                                                                      .setSubType(true)
                                                                      .setSellPrice(true)
                                                                      .build()
                                               )
                                               .setClauses(
                                                       ClausesData.newBuilder()
                                                                  .setDormitories(
                                                                          Int32ComparisonData.newBuilder()
                                                                                             .setIsGreaterOrEqual(
                                                                                                     Int32SingleComparisonData.newBuilder()
                                                                                                                              .setValue(2)
                                                                                                                              .build()
                                                                                             )
                                                                                             .build()
                                                                  )
                                                                  .build()
                                               )
                                               .setOrders(
                                                       OrdersData.newBuilder()
                                                                 .setSellPrice(
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
        var responseObserver = new TestStreamObserver<GetUrbanPropertiesResponse>();

        urbanPropertyGrpcDataService.getUrbanProperties(request, responseObserver);

        assertThat(responseObserver)
                .satisfies(ro -> assertThat(ro.getValue())
                        .satisfies(r -> assertThat(r.getUrbanPropertiesList())
                                .extracting("id", "provider.name", "providerCode", "subType", "sellPrice")
                                .containsExactly(
                                        tuple(
                                                Int32Value.of(10000),
                                                StringValue.of("Oliveira Imóveis"),
                                                StringValue.of("500489"),
                                                StringValue.of("Casa de Condomínio"),
                                                DoubleValue.of(1800000)
                                        ),
                                        tuple(
                                                Int32Value.of(10004),
                                                StringValue.of("Oliveira Imóveis"),
                                                StringValue.of("3272"),
                                                StringValue.of("Casa"),
                                                DoubleValue.of(1590000)
                                        )
                                )
                        )
                        .satisfies(r -> assertThat(r.getPagination())
                                .extracting("pageSize", "pageNumber", "totalPages", "totalRows")
                                .containsExactly(2, 1, 3, 5L)
                        )
                )
                .satisfies(ro -> assertThat(ro.getThrowable()).isNull());
    }

}
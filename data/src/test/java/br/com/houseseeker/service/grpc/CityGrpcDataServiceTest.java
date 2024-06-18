package br.com.houseseeker.service.grpc;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.TestStreamObserver;
import br.com.houseseeker.domain.proto.StringComparisonData;
import br.com.houseseeker.domain.proto.StringSingleComparisonData;
import br.com.houseseeker.service.proto.GetCitiesRequest;
import br.com.houseseeker.service.proto.GetCitiesRequest.ClausesData;
import br.com.houseseeker.service.proto.GetCitiesResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class CityGrpcDataServiceTest extends AbstractJpaIntegrationTest {

    @Autowired
    private CityGrpcDataService cityGrpcDataService;

    @Test
    @DisplayName("given a city request when calls getCities then expects observer to catch response")
    void givenACityRequest_whenCallsGetCitiesWithoutErrors_thenExpectsObserverToCatchResponse() {
        var request = GetCitiesRequest.newBuilder()
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
                                                         .setCity(
                                                                 StringComparisonData.newBuilder()
                                                                                     .setIsEqual(
                                                                                             StringSingleComparisonData.newBuilder()
                                                                                                                       .setValue("Santa Maria")
                                                                                                                       .build()
                                                                                     )
                                                                                     .build()
                                                         )
                                                         .build()
                                      )
                                      .build();
        var responseObserver = new TestStreamObserver<GetCitiesResponse>();

        cityGrpcDataService.getCities(request, responseObserver);

        assertThat(responseObserver)
                .satisfies(ro -> assertThat(ro.getValue())
                        .satisfies(r -> assertThat(r.getCitiesList())
                                .extracting("state.value", "city.value")
                                .containsExactly(tuple("RS", "Santa Maria"))
                        )
                        .satisfies(r -> assertThat(r.getPagination())
                                .extracting("pageSize", "pageNumber", "totalPages", "totalRows")
                                .containsExactly(50, 1, 1, 1L)
                        )
                )
                .satisfies(ro -> assertThat(ro.getThrowable()).isNull());
    }

}
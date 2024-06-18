package br.com.houseseeker.service.grpc;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.TestStreamObserver;
import br.com.houseseeker.domain.proto.StringComparisonData;
import br.com.houseseeker.domain.proto.StringSingleComparisonData;
import br.com.houseseeker.service.proto.GetStatesRequest;
import br.com.houseseeker.service.proto.GetStatesRequest.ClausesData;
import br.com.houseseeker.service.proto.GetStatesResponse;
import com.google.protobuf.StringValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class StateGrpcDataServiceTest extends AbstractJpaIntegrationTest {

    @Autowired
    private StateGrpcDataService stateGrpcDataService;

    @Test
    @DisplayName("given a state request when calls getProviders then expects observer to catch response")
    void givenAStateRequest_whenCallsGetStatesWithoutErrors_thenExpectsObserverToCatchResponse() {
        var request = GetStatesRequest.newBuilder()
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
                                      .build();
        var responseObserver = new TestStreamObserver<GetStatesResponse>();

        stateGrpcDataService.getStates(request, responseObserver);

        assertThat(responseObserver)
                .satisfies(ro -> assertThat(ro.getValue())
                        .satisfies(r -> assertThat(r.getStatesList())
                                .extracting(StringValue::getValue)
                                .containsExactly("RS")
                        )
                        .satisfies(r -> assertThat(r.getPagination())
                                .extracting("pageSize", "pageNumber", "totalPages", "totalRows")
                                .containsExactly(50, 1, 1, 1L)
                        )
                )
                .satisfies(ro -> assertThat(ro.getThrowable()).isNull());
    }

}
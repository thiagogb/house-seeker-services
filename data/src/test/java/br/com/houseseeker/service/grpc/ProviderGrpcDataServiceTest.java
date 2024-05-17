package br.com.houseseeker.service.grpc;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.TestStreamObserver;
import br.com.houseseeker.domain.proto.OrderDetailsData;
import br.com.houseseeker.domain.proto.OrderDirectionData;
import br.com.houseseeker.domain.proto.PaginationRequestData;
import br.com.houseseeker.domain.proto.ProviderData;
import br.com.houseseeker.mock.ProviderDataMocks;
import br.com.houseseeker.service.proto.GetProviderMechanismsResponse;
import br.com.houseseeker.service.proto.GetProvidersDataRequest;
import br.com.houseseeker.service.proto.GetProvidersDataRequest.OrdersData;
import br.com.houseseeker.service.proto.GetProvidersDataRequest.ProjectionsData;
import br.com.houseseeker.service.proto.GetProvidersDataResponse;
import com.google.protobuf.BoolValue;
import com.google.protobuf.Empty;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import io.grpc.Status;
import io.grpc.StatusException;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

class ProviderGrpcDataServiceTest extends AbstractJpaIntegrationTest {

    private static final String[] EXTRACTED_ATTRIBUTES = new String[]{
            "name", "siteUrl", "dataUrl", "mechanism", "params", "cronExpression", "logo", "active"
    };

    @Autowired
    private ProviderGrpcDataService providerGrpcDataService;

    @Test
    @DisplayName("given a provider request when calls getProviders then expects observer to catch response")
    void givenAProviderRequest_whenCallsGetProvidersWithoutErrors_thenExpectsObserverToCatchResponse() {
        var request = GetProvidersDataRequest.newBuilder()
                                             .setProjections(
                                                     ProjectionsData.newBuilder()
                                                                    .setId(true)
                                                                    .setName(true)
                                                                    .setActive(true)
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
                                                                          .setPageSize(2)
                                                                          .build()
                                             )
                                             .build();
        var responseObserver = new TestStreamObserver<GetProvidersDataResponse>();

        providerGrpcDataService.getProviders(request, responseObserver);

        assertThat(responseObserver)
                .satisfies(ro -> assertThat(ro.getValue())
                        .satisfies(r -> assertThat(r.getProvidersList())
                                .extracting("id", "name", "active")
                                .containsExactly(
                                        tuple(Int32Value.of(10006), StringValue.of("Cotrel Imóveis"), BoolValue.of(true)),
                                        tuple(Int32Value.of(10005), StringValue.of("InVista Imóveis"), BoolValue.of(true))
                                )
                        )
                        .satisfies(r -> assertThat(r.getPagination())
                                .extracting("pageSize", "pageNumber", "totalPages", "totalRows")
                                .containsExactly(2, 1, 4, 7L)
                        )
                )
                .satisfies(ro -> assertThat(ro.getThrowable()).isNull());
    }

    @Test
    @DisplayName("given existing provider mechanisms when calls getProviderMechanisms then expects")
    void givenExistingProviderMechanisms_whenCallsGetProviderMechanisms_thenExpects() {
        var request = Empty.getDefaultInstance();
        var responseObserver = new TestStreamObserver<GetProviderMechanismsResponse>();

        providerGrpcDataService.getProviderMechanisms(request, responseObserver);

        assertThat(responseObserver)
                .satisfies(ro -> assertThat(ro.getValue())
                        .extracting(GetProviderMechanismsResponse::getProviderMechanismsList, InstanceOfAssertFactories.LIST)
                        .containsExactly(
                                "UNIVERSAL_SOFTWARE",
                                "JETIMOB_V1",
                                "JETIMOB_V2",
                                "JETIMOB_V3",
                                "JETIMOB_V4",
                                "SUPER_LOGICA",
                                "ALAN_WGT"
                        )
                )
                .satisfies(ro -> assertThat(ro.getThrowable()).isNull());
    }

    @Test
    @DisplayName("given a invalid provider data when calls insertProvider then expects observer to catch error")
    void givenAInvalidProviderData_whenCallsInsertProvider_thenExpectsObserverToCatchErrors() {
        var providerData = ProviderData.getDefaultInstance();
        var responseObserver = new TestStreamObserver<ProviderData>();

        providerGrpcDataService.insertProvider(providerData, responseObserver);

        assertThat(responseObserver)
                .satisfies(ro -> assertThat(ro.getValue()).isNull())
                .satisfies(ro -> assertThat(ro.getThrowable())
                        .isInstanceOf(StatusException.class)
                        .hasFieldOrPropertyWithValue("status.code", Status.INVALID_ARGUMENT.getCode())
                );
    }

    @Test
    @DisplayName("given a valid provider data when calls insertProvider then expects observer to catch response")
    void givenAValidProviderData_whenCallsInsertProvider_thenExpectsObserverToCatchResponse() {
        var providerData = ProviderDataMocks.testProviderWithId(1)
                                            .toBuilder()
                                            .setId(Int32Value.getDefaultInstance())
                                            .build();
        var responseObserver = new TestStreamObserver<ProviderData>();

        providerGrpcDataService.insertProvider(providerData, responseObserver);

        assertThat(responseObserver)
                .satisfies(ro -> assertThat(ro.getValue())
                        .hasNoNullFieldsOrProperties()
                        .extracting(EXTRACTED_ATTRIBUTES)
                        .containsExactly(
                                providerData.getName(),
                                providerData.getSiteUrl(),
                                providerData.getDataUrl(),
                                providerData.getMechanism(),
                                providerData.getParams(),
                                providerData.getCronExpression(),
                                providerData.getLogo(),
                                providerData.getActive()
                        )
                )
                .satisfies(ro -> assertThat(ro.getThrowable()).isNull());
    }

    @Test
    @DisplayName("given a provider data with invalid id reference when calls updateProvider then expects observer to catch error")
    void givenAProviderDataWithInvalidIdReference_whenCallsUpdateProvider_thenExpectsObserverToCatchErrors() {
        var providerData = ProviderData.newBuilder().setId(Int32Value.of(999999)).build();
        var responseObserver = new TestStreamObserver<ProviderData>();

        providerGrpcDataService.updateProvider(providerData, responseObserver);

        assertThat(responseObserver)
                .satisfies(ro -> assertThat(ro.getValue()).isNull())
                .satisfies(ro -> assertThat(ro.getThrowable())
                        .isInstanceOf(StatusException.class)
                        .hasFieldOrPropertyWithValue("status.code", Status.NOT_FOUND.getCode())
                );
    }

    @Test
    @DisplayName("given a invalid provider data when calls updateProvider then expects observer to catch error")
    void givenAInvalidProviderData_whenCallsUpdateProvider_thenExpectsObserverToCatchErrors() {
        var providerData = ProviderData.newBuilder().setId(Int32Value.of(10000)).build();
        var responseObserver = new TestStreamObserver<ProviderData>();

        providerGrpcDataService.updateProvider(providerData, responseObserver);

        assertThat(responseObserver)
                .satisfies(ro -> assertThat(ro.getValue()).isNull())
                .satisfies(ro -> assertThat(ro.getThrowable())
                        .isInstanceOf(StatusException.class)
                        .hasFieldOrPropertyWithValue("status.code", Status.INVALID_ARGUMENT.getCode())
                );
    }

    @Test
    @DisplayName("given a valid provider data when calls updateProvider then expects observer to catch response")
    void givenAValidProviderData_whenCallsUpdateProvider_thenExpectsObserverToCatchResponse() {
        var providerData = ProviderDataMocks.testProviderWithId(10000);
        var responseObserver = new TestStreamObserver<ProviderData>();

        providerGrpcDataService.updateProvider(providerData, responseObserver);

        assertThat(responseObserver)
                .satisfies(ro -> assertThat(ro.getValue())
                        .hasNoNullFieldsOrProperties()
                        .extracting(EXTRACTED_ATTRIBUTES)
                        .containsExactly(
                                providerData.getName(),
                                providerData.getSiteUrl(),
                                providerData.getDataUrl(),
                                providerData.getMechanism(),
                                providerData.getParams(),
                                providerData.getCronExpression(),
                                providerData.getLogo(),
                                providerData.getActive()
                        )
                )
                .satisfies(ro -> assertThat(ro.getThrowable()).isNull());
    }

    @Test
    @DisplayName("given a existing provider with associated data when calls wipeProvider then expects")
    void givenAExistingProviderWithAssociatedData_whenCallsWipeProvider_thenExpects() {
        var request = Int32Value.of(10000);
        var responseObserver = new TestStreamObserver<Empty>();

        providerGrpcDataService.wipeProvider(request, responseObserver);

        assertThat(responseObserver.getThrowable()).isNull();
    }

    @Test
    @DisplayName("given a non existing provider when calls wipeProvider then expects")
    void givenANonExistingProvider_whenCallsWipeProvider_thenExpects() {
        var request = Int32Value.of(999999);
        var responseObserver = new TestStreamObserver<Empty>();

        providerGrpcDataService.wipeProvider(request, responseObserver);

        assertThat(responseObserver.getThrowable())
                .isInstanceOf(StatusException.class)
                .hasFieldOrPropertyWithValue("status.code", Status.NOT_FOUND.getCode());
    }

}
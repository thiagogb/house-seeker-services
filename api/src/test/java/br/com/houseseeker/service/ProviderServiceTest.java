package br.com.houseseeker.service;

import br.com.houseseeker.domain.argument.ProviderInput;
import br.com.houseseeker.domain.argument.ProviderInput.Clauses;
import br.com.houseseeker.domain.argument.ProviderInput.Orders;
import br.com.houseseeker.domain.input.IntegerClauseInput;
import br.com.houseseeker.domain.input.IntegerInput;
import br.com.houseseeker.domain.input.OrderInput;
import br.com.houseseeker.domain.input.PaginationInput;
import br.com.houseseeker.domain.input.ProviderCreationInput;
import br.com.houseseeker.domain.input.ProviderEditionInput;
import br.com.houseseeker.domain.proto.OrderDirectionData;
import br.com.houseseeker.domain.proto.PaginationResponseData;
import br.com.houseseeker.domain.proto.ProviderData;
import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.mapper.ProviderMapperImpl;
import br.com.houseseeker.service.proto.GetProvidersDataRequest.OrdersData;
import br.com.houseseeker.service.proto.GetProvidersDataResponse;
import br.com.houseseeker.service.proto.ProviderDataServiceGrpc.ProviderDataServiceBlockingStub;
import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import com.google.protobuf.BytesValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import graphql.schema.DataFetchingEnvironment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static br.com.houseseeker.service.dsl.GetProvidersDataRequestDsl.assertThis;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {
        ProviderMapperImpl.class,
        ProviderService.class
})
@ExtendWith(MockitoExtension.class)
class ProviderServiceTest {

    private static final ProviderData PROVIDER_DATA_1 = ProviderData.newBuilder()
                                                                    .setId(Int32Value.of(1))
                                                                    .setName(StringValue.of("Test provider"))
                                                                    .setSiteUrl(StringValue.of("http://localhost"))
                                                                    .setMechanism(StringValue.of(ProviderMechanism.SUPER_LOGICA.name()))
                                                                    .setActive(BoolValue.of(true))
                                                                    .build();

    private static final PaginationResponseData PAGINATION_RESPONSE_DATA = PaginationResponseData.newBuilder()
                                                                                                 .setPageNumber(1)
                                                                                                 .setPageSize(1)
                                                                                                 .setTotalPages(1)
                                                                                                 .setTotalRows(1)
                                                                                                 .build();

    private static final GetProvidersDataResponse DEFAULT_RESPONSE = GetProvidersDataResponse.newBuilder()
                                                                                             .addAllProviders(List.of(PROVIDER_DATA_1))
                                                                                             .setPagination(PAGINATION_RESPONSE_DATA)
                                                                                             .build();

    private static final ProviderCreationInput PROVIDER_CREATION_INPUT = ProviderCreationInput.builder()
                                                                                              .name("Test provider")
                                                                                              .siteUrl("http://localhost")
                                                                                              .dataUrl("http://localhost/api")
                                                                                              .mechanism(ProviderMechanism.JETIMOB_V1)
                                                                                              .params("{}")
                                                                                              .cronExpression("0 0 0 ? * * *")
                                                                                              .logo("Y29udGVudA==")
                                                                                              .active(true)
                                                                                              .build();

    private static final ProviderEditionInput PROVIDER_EDITION_INPUT = ProviderEditionInput.builder()
                                                                                           .name("Test provider")
                                                                                           .siteUrl("http://localhost")
                                                                                           .dataUrl("http://localhost/api")
                                                                                           .mechanism(ProviderMechanism.JETIMOB_V1)
                                                                                           .params("{}")
                                                                                           .cronExpression("0 0 0 ? * * *")
                                                                                           .logo("Y29udGVudA==")
                                                                                           .active(true)
                                                                                           .build();

    private static final String[] EXTRACTED_PROJECTIONS_ATTRIBUTES = new String[]{
            "id", "name", "siteUrl", "dataUrl", "mechanism", "params", "cronExpression", "logo", "active"
    };

    @Autowired
    private ProviderService providerService;

    @Mock
    private ProviderDataServiceBlockingStub mockedProviderDataServiceBlockingStub;

    @Mock
    private DataFetchingEnvironment mockedDataFetchingEnvironment;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(providerService, "providerDataServiceBlockingStub", mockedProviderDataServiceBlockingStub);
    }

    @Test
    @DisplayName("given a empty projections and null input when calls findBy then expects")
    void givenAEmptyProjectionsAndNullInput_whenCallsFindBy_thenExpects() {
        when(mockedProviderDataServiceBlockingStub.getProviders(any())).thenReturn(DEFAULT_RESPONSE);

        assertThat(providerService.findBy(Collections.emptySet(), null)).isEqualTo(DEFAULT_RESPONSE);

        verify(mockedProviderDataServiceBlockingStub, times(1)).getProviders(
                assertArg(a -> assertThis(a).isProjectingAll()
                                            .hasNotChangedClauses()
                                            .hasNotChangedOrders()
                                            .hasNotChangedPagination()
                )
        );
        verifyNoMoreInteractions(mockedProviderDataServiceBlockingStub);
    }

    @Test
    @DisplayName("given a filled projections and null input when calls findBy then expects")
    void givenAFilledProjectionsAndNullInput_whenCallsFindBy_thenExpects() {
        var projections = Set.of("rows/id", "rows/name", "rows/active");

        when(mockedProviderDataServiceBlockingStub.getProviders(any())).thenReturn(DEFAULT_RESPONSE);

        assertThat(providerService.findBy(projections, null)).isEqualTo(DEFAULT_RESPONSE);

        verify(mockedProviderDataServiceBlockingStub, times(1)).getProviders(
                assertArg(a -> assertThis(a).isProjectingOnly("id", "name", "active")
                                            .hasNotChangedClauses()
                                            .hasNotChangedOrders()
                                            .hasNotChangedPagination()
                )
        );
        verifyNoMoreInteractions(mockedProviderDataServiceBlockingStub);
    }

    @Test
    @DisplayName("given a empty projections and input with clauses when calls findBy then expects")
    void givenAEmptyProjectionsAndInputWithClauses_whenCallsFindBy_thenExpects() {
        var input = ProviderInput.builder()
                                 .clauses(
                                         Clauses.builder()
                                                .id(
                                                        IntegerClauseInput.builder()
                                                                          .isGreater(
                                                                                  IntegerInput.builder()
                                                                                              .value(1)
                                                                                              .build()
                                                                          )
                                                                          .build()
                                                )
                                                .build()
                                 )
                                 .build();

        when(mockedProviderDataServiceBlockingStub.getProviders(any())).thenReturn(DEFAULT_RESPONSE);

        assertThat(providerService.findBy(Collections.emptySet(), input)).isEqualTo(DEFAULT_RESPONSE);

        verify(mockedProviderDataServiceBlockingStub, times(1)).getProviders(
                assertArg(a -> assertThis(a).isProjectingAll()
                                            .hasClauseWithValue(c -> c.getId().getIsGreater(), 1)
                                            .hasNotChangedOrders()
                                            .hasNotChangedPagination()
                )
        );
        verifyNoMoreInteractions(mockedProviderDataServiceBlockingStub);
    }

    @Test
    @DisplayName("given a empty projections and input with orders when calls findBy then expects")
    void givenAEmptyProjectionsAndInputWithOrders_whenCallsFindBy_thenExpects() {
        var input = ProviderInput.builder()
                                 .orders(
                                         Orders.builder()
                                               .id(
                                                       OrderInput.builder()
                                                                 .index(1)
                                                                 .direction(OrderInput.Direction.ASC)
                                                                 .build()
                                               )
                                               .build()
                                 )
                                 .build();

        when(mockedProviderDataServiceBlockingStub.getProviders(any())).thenReturn(DEFAULT_RESPONSE);

        assertThat(providerService.findBy(Collections.emptySet(), input)).isEqualTo(DEFAULT_RESPONSE);

        verify(mockedProviderDataServiceBlockingStub, times(1)).getProviders(
                assertArg(a -> assertThis(a).isProjectingAll()
                                            .hasNotChangedClauses()
                                            .hasOrderWith(OrdersData::getId, 1, OrderDirectionData.ASC)
                                            .hasNotChangedPagination()
                )
        );
        verifyNoMoreInteractions(mockedProviderDataServiceBlockingStub);
    }

    @Test
    @DisplayName("given a empty projections and input with pagination when calls findBy then expects")
    void givenAEmptyProjectionsAndInputWithPagination_whenCallsFindBy_thenExpects() {
        var input = ProviderInput.builder()
                                 .pagination(
                                         PaginationInput.builder()
                                                        .pageNumber(2)
                                                        .pageSize(10)
                                                        .build()
                                 )
                                 .build();

        when(mockedProviderDataServiceBlockingStub.getProviders(any())).thenReturn(DEFAULT_RESPONSE);

        assertThat(providerService.findBy(Collections.emptySet(), input)).isEqualTo(DEFAULT_RESPONSE);

        verify(mockedProviderDataServiceBlockingStub, times(1)).getProviders(
                assertArg(a -> assertThis(a).isProjectingAll()
                                            .hasNotChangedClauses()
                                            .hasNotChangedOrders()
                                            .hasPaginationWith(2, 10)
                )
        );
        verifyNoMoreInteractions(mockedProviderDataServiceBlockingStub);
    }

    @Test
    @DisplayName("given a non existing provider when calls getLogo then expects exception")
    void givenANonExistingProvider_whenCallsGetLogo_thenExpectsException() {
        when(mockedProviderDataServiceBlockingStub.getProviders(any()))
                .thenReturn(GetProvidersDataResponse.getDefaultInstance());

        assertThatThrownBy(() -> providerService.getLogo(1))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("404 NOT_FOUND \"Provider with id 1 not found\"");

        verify(mockedProviderDataServiceBlockingStub, times(1)).getProviders(
                assertArg(a -> assertThis(a).isProjectingAll()
                                            .hasClauseWithValue(c -> c.getId().getIsEqual(), 1)
                                            .hasNotChangedOrders()
                                            .hasNotChangedPagination()
                )
        );
        verifyNoMoreInteractions(mockedProviderDataServiceBlockingStub);
    }

    @Test
    @DisplayName("given a existing provider when calls getLogo then expects")
    void givenAExistingProvider_whenCallsGetLogo_thenExpects() {
        when(mockedProviderDataServiceBlockingStub.getProviders(any())).thenReturn(
                GetProvidersDataResponse.newBuilder()
                                        .addAllProviders(List.of(
                                                ProviderData.newBuilder()
                                                            .setLogo(
                                                                    BytesValue.of(
                                                                            ByteString.copyFrom("content", StandardCharsets.UTF_8)
                                                                    )
                                                            )
                                                            .build()
                                        ))
                                        .build()
        );

        assertThat(providerService.getLogo(1))
                .asString(StandardCharsets.UTF_8)
                .isEqualTo("content");

        verify(mockedProviderDataServiceBlockingStub, times(1)).getProviders(
                assertArg(a -> assertThis(a).isProjectingAll()
                                            .hasClauseWithValue(c -> c.getId().getIsEqual(), 1)
                                            .hasNotChangedOrders()
                                            .hasNotChangedPagination()
                )
        );
        verifyNoMoreInteractions(mockedProviderDataServiceBlockingStub);
    }

    @Test
    @DisplayName("given a provider input when calls insert then expects")
    void givenAProviderInput_whenCallsInsert_thenExpects() {
        when(mockedProviderDataServiceBlockingStub.insertProvider(any()))
                .thenAnswer(a -> a.getArgument(0, ProviderData.class)
                                  .toBuilder()
                                  .setId(Int32Value.of(1))
                                  .build()
                );

        assertThat(providerService.insert(PROVIDER_CREATION_INPUT))
                .extracting(EXTRACTED_PROJECTIONS_ATTRIBUTES)
                .containsExactly(
                        Int32Value.of(1),
                        StringValue.of(PROVIDER_CREATION_INPUT.getName()),
                        StringValue.of(PROVIDER_CREATION_INPUT.getSiteUrl()),
                        StringValue.of(PROVIDER_CREATION_INPUT.getDataUrl()),
                        StringValue.of(PROVIDER_CREATION_INPUT.getMechanism().name()),
                        StringValue.of(PROVIDER_CREATION_INPUT.getParams()),
                        StringValue.of(PROVIDER_CREATION_INPUT.getCronExpression()),
                        BytesValue.of(ByteString.copyFrom(Base64.getDecoder().decode(PROVIDER_CREATION_INPUT.getLogo().getBytes()))),
                        BoolValue.of(PROVIDER_CREATION_INPUT.getActive())
                );

        verify(mockedProviderDataServiceBlockingStub, times(1)).insertProvider(any());
        verifyNoMoreInteractions(mockedProviderDataServiceBlockingStub);
    }

    @Test
    @DisplayName("given a non existing provider when calls update then expects exception")
    void givenANonExistingProvider_whenCallsUpdate_thenExpectsException() {
        when(mockedProviderDataServiceBlockingStub.getProviders(any()))
                .thenReturn(GetProvidersDataResponse.getDefaultInstance());

        assertThatThrownBy(() -> providerService.update(1, PROVIDER_EDITION_INPUT))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("404 NOT_FOUND \"Provider with id 1 not found\"");

        verify(mockedProviderDataServiceBlockingStub, times(1)).getProviders(
                assertArg(a -> assertThis(a)
                        .isProjectingAll()
                        .hasClauseWithValue(c -> c.getId().getIsEqual(), 1)
                        .hasNotChangedOrders()
                        .hasNotChangedPagination()
                )
        );
        verifyNoMoreInteractions(mockedProviderDataServiceBlockingStub);
    }

    @Test
    @DisplayName("given a existing provider when calls update then expects")
    void givenExistingProvider_whenCallsUpdate_thenExpects() {
        var input = PROVIDER_EDITION_INPUT.toBuilder().build();

        when(mockedProviderDataServiceBlockingStub.getProviders(any()))
                .thenReturn(DEFAULT_RESPONSE);

        when(mockedProviderDataServiceBlockingStub.updateProvider(any()))
                .thenAnswer(a -> a.getArgument(0, ProviderData.class));

        when(mockedDataFetchingEnvironment.getArgument("input")).thenReturn(
                Map.of(
                        "name", true,
                        "siteUrl", true,
                        "dataUrl", true,
                        "mechanism", true,
                        "params", true,
                        "cronExpression", true,
                        "logo", true,
                        "active", true
                )
        );

        input.detectedChangedArguments(mockedDataFetchingEnvironment);

        assertThat(providerService.update(1, input))
                .extracting(EXTRACTED_PROJECTIONS_ATTRIBUTES)
                .containsExactly(
                        Int32Value.of(1),
                        StringValue.of(PROVIDER_EDITION_INPUT.getName()),
                        StringValue.of(PROVIDER_EDITION_INPUT.getSiteUrl()),
                        StringValue.of(PROVIDER_EDITION_INPUT.getDataUrl()),
                        StringValue.of(PROVIDER_EDITION_INPUT.getMechanism().name()),
                        StringValue.of(PROVIDER_EDITION_INPUT.getParams()),
                        StringValue.of(PROVIDER_EDITION_INPUT.getCronExpression()),
                        BytesValue.of(ByteString.copyFrom(Base64.getDecoder().decode(PROVIDER_EDITION_INPUT.getLogo().getBytes()))),
                        BoolValue.of(PROVIDER_EDITION_INPUT.getActive())
                );

        verify(mockedProviderDataServiceBlockingStub, times(1)).getProviders(
                assertArg(a -> assertThis(a).isProjectingAll()
                                            .hasClauseWithValue(c -> c.getId().getIsEqual(), 1)
                                            .hasNotChangedOrders()
                                            .hasNotChangedPagination()
                )
        );
        verify(mockedProviderDataServiceBlockingStub, times(1)).updateProvider(any());
        verifyNoMoreInteractions(mockedProviderDataServiceBlockingStub);
    }

    @Test
    @DisplayName("given a provider id when calls wipe then expects")
    void givenAProviderId_whenCalsWipe_thenExpects() {
        providerService.wipe(1);

        verify(mockedProviderDataServiceBlockingStub, times(1)).wipeProvider(Int32Value.of(1));
        verifyNoMoreInteractions(mockedProviderDataServiceBlockingStub);
    }

}
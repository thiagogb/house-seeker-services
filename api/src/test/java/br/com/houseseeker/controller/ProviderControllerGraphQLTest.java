package br.com.houseseeker.controller;

import br.com.houseseeker.domain.argument.ProviderInput;
import br.com.houseseeker.domain.input.OrderInput;
import br.com.houseseeker.domain.input.PaginationInput;
import br.com.houseseeker.domain.input.ProviderCreationInput;
import br.com.houseseeker.domain.input.ProviderEditionInput;
import br.com.houseseeker.domain.proto.PaginationResponseData;
import br.com.houseseeker.domain.proto.ProviderData;
import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.mapper.PaginationMapperImpl;
import br.com.houseseeker.mapper.ProviderMapper;
import br.com.houseseeker.mapper.ProviderMapperImpl;
import br.com.houseseeker.service.ProviderService;
import br.com.houseseeker.service.proto.GetProvidersDataResponse;
import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import com.google.protobuf.BytesValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import graphql.ErrorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@GraphQlTest(controllers = ProviderController.class)
@Import({ProviderMapperImpl.class, PaginationMapperImpl.class})
@ExtendWith(MockitoExtension.class)
class ProviderControllerGraphQLTest {

    private static final ProviderData DEFAULT_PROVIDER_DATA = ProviderData.newBuilder()
                                                                          .setId(Int32Value.of(1))
                                                                          .setName(StringValue.of("Test provider"))
                                                                          .setSiteUrl(StringValue.of("http://localhost"))
                                                                          .setDataUrl(StringValue.of("http://localhost/api"))
                                                                          .setMechanism(StringValue.of(ProviderMechanism.ALAN_WGT.name()))
                                                                          .setParams(StringValue.of("{}"))
                                                                          .setCronExpression(StringValue.of("* * *"))
                                                                          .setLogo(BytesValue.of(ByteString.copyFrom("content", StandardCharsets.UTF_8)))
                                                                          .setActive(BoolValue.of(true))
                                                                          .build();

    private static final PaginationResponseData DEFAULT_PAGINATION_DATA = PaginationResponseData.newBuilder()
                                                                                                .setPageNumber(1)
                                                                                                .setPageSize(1)
                                                                                                .setTotalPages(1)
                                                                                                .setTotalRows(1)
                                                                                                .build();

    @Autowired
    private GraphQlTester graphQlTester;

    @Autowired
    private ProviderMapper providerMapper;

    @MockBean
    private ProviderService providerService;

    @BeforeEach
    void setup() {
        Locale.setDefault(Locale.of("pt", "BR"));

        lenient().when(providerService.findBy(anySet(), any())).thenReturn(
                GetProvidersDataResponse.newBuilder()
                                        .addAllProviders(List.of(DEFAULT_PROVIDER_DATA))
                                        .setPagination(DEFAULT_PAGINATION_DATA)
                                        .build()
        );

        lenient().when(providerService.insert(any()))
                 .thenAnswer(a -> providerMapper.toData(a.getArgument(0, ProviderCreationInput.class))
                                                .toBuilder()
                                                .setId(Int32Value.of(1))
                                                .build()
                 );

        lenient().when(providerService.update(anyInt(), any()))
                 .thenAnswer(a -> {
                     var id = a.getArgument(0, Integer.class);
                     var input = a.getArgument(1, ProviderEditionInput.class);
                     var data = DEFAULT_PROVIDER_DATA.toBuilder();
                     providerMapper.copyToData(input, data);
                     return data.setId(Int32Value.of(id)).build();
                 });
    }

    @Test
    @DisplayName("given a payload without input and all fields when calls providers then expects")
    void givenAPayloadWithoutInputAndAllFields_whenCallsProviders_thenExpects() {
        graphQlTester.documentName("get-providers-sample-1")
                     .execute()
                     .path("providers")
                     .matchesJsonStrictly("""
                                                  {
                                                    "rows": [
                                                      {
                                                        "id": "1",
                                                        "name": "Test provider",
                                                        "siteUrl": "http://localhost",
                                                        "dataUrl": "http://localhost/api",
                                                        "mechanism": "ALAN_WGT",
                                                        "params": "{}",
                                                        "cronExpression": "* * *",
                                                        "logoUrl": "/api/rest/providers/1/logo",
                                                        "active": true
                                                      }
                                                    ],
                                                    "pagination": {
                                                      "pageNumber": 1,
                                                      "pageSize": 1,
                                                      "totalPages": 1,
                                                      "totalRows": 1
                                                    }
                                                  }
                                                  """);

        verify(providerService, times(1)).findBy(
                assertArg(arg -> assertThat(arg).contains(
                        "rows/id",
                        "rows/name",
                        "rows/siteUrl",
                        "rows/dataUrl",
                        "rows/mechanism",
                        "rows/params",
                        "rows/cronExpression",
                        "rows/logoUrl",
                        "rows/active",
                        "pagination/pageNumber",
                        "pagination/pageSize",
                        "pagination/totalPages",
                        "pagination/totalRows"
                )),
                isNull()
        );
        verifyNoMoreInteractions(providerService);
    }

    @Test
    @DisplayName("given a payload without input and some fields when calls providers then expects")
    void givenAPayloadWithoutInputAndSomeFields_whenCallsProviders_thenExpects() {
        graphQlTester.documentName("get-providers-sample-2")
                     .execute()
                     .path("providers")
                     .matchesJsonStrictly("""
                                                  {
                                                    "rows": [
                                                      {
                                                        "id": "1",
                                                        "name": "Test provider",
                                                        "active": true
                                                      }
                                                    ],
                                                    "pagination": {
                                                      "pageNumber": 1,
                                                      "pageSize": 1,
                                                      "totalPages": 1
                                                    }
                                                  }
                                                  """);

        verify(providerService, times(1)).findBy(
                assertArg(arg -> assertThat(arg).contains(
                        "rows/id",
                        "rows/name",
                        "rows/active",
                        "pagination/pageNumber",
                        "pagination/pageSize",
                        "pagination/totalPages"
                )),
                isNull()
        );
        verifyNoMoreInteractions(providerService);
    }

    @Test
    @DisplayName("given a payload with input containing clauses when calls providers then expects")
    void givenAPayloadWithInputContainingClauses_whenCallsProviders_thenExpects() {
        graphQlTester.documentName("get-providers-sample-3")
                     .execute()
                     .path("providers")
                     .matchesJsonStrictly("""
                                                  {
                                                    "rows": [
                                                      {
                                                        "id": "1"
                                                      }
                                                    ]
                                                  }
                                                  """);

        verify(providerService, times(1)).findBy(
                assertArg(arg -> assertThat(arg).contains("rows/id")),
                assertArg(arg -> assertThat(arg)
                        .hasAllNullFieldsOrPropertiesExcept("clauses")
                        .extracting(ProviderInput::getClauses)
                        .extracting(
                                a -> a.getId().getIsGreaterOrEqual().getValue(),
                                a -> a.getName().getItContains().getValue(),
                                a -> a.getSiteUrl().getIsStartingWith().getValue(),
                                a -> a.getDataUrl().getIsNotBlank(),
                                a -> a.getMechanism().getIsIn().getValues(),
                                a -> a.getParams().getIsNotNull(),
                                a -> a.getCronExpression().getIsNotStartingWith().getValue(),
                                a -> a.getLogo().getIsNotNull(),
                                a -> a.getActive().getIsNotEqual().getValue()
                        )
                        .containsExactly(
                                1,
                                "Imóveis",
                                "http://",
                                true,
                                List.of(ProviderMechanism.JETIMOB_V1, ProviderMechanism.JETIMOB_V2),
                                true,
                                "*",
                                true,
                                false
                        )
                )
        );
        verifyNoMoreInteractions(providerService);
    }

    @Test
    @DisplayName("given a payload with input containing orders when calls providers then expects")
    void givenAPayloadWithInputContainingOrders_whenCallsProviders_thenExpects() {
        graphQlTester.documentName("get-providers-sample-4")
                     .execute()
                     .path("providers")
                     .matchesJsonStrictly("""
                                                  {
                                                    "rows": [
                                                      {
                                                        "id": "1"
                                                      }
                                                    ]
                                                  }
                                                  """);

        verify(providerService, times(1)).findBy(
                assertArg(arg -> assertThat(arg).contains("rows/id")),
                assertArg(arg -> assertThat(arg)
                        .hasAllNullFieldsOrPropertiesExcept("orders")
                        .extracting(ProviderInput::getOrders)
                        .extracting(
                                ProviderInput.Orders::getId,
                                ProviderInput.Orders::getName,
                                ProviderInput.Orders::getSiteUrl,
                                ProviderInput.Orders::getDataUrl,
                                ProviderInput.Orders::getMechanism,
                                ProviderInput.Orders::getActive
                        )
                        .containsExactly(
                                OrderInput.builder().index(4).direction(OrderInput.Direction.DESC).build(),
                                OrderInput.builder().index(2).direction(OrderInput.Direction.ASC).build(),
                                OrderInput.builder().index(0).build(),
                                OrderInput.builder().index(0).build(),
                                OrderInput.builder().index(1).direction(OrderInput.Direction.ASC).build(),
                                OrderInput.builder().index(3).direction(OrderInput.Direction.ASC).build()
                        )
                )
        );
        verifyNoMoreInteractions(providerService);
    }

    @Test
    @DisplayName("given a payload with input containing pagination when calls providers then expects")
    void givenAPayloadWithInputContainingPagination_whenCallsProviders_thenExpects() {
        graphQlTester.documentName("get-providers-sample-5")
                     .execute()
                     .path("providers")
                     .matchesJsonStrictly("""
                                                  {
                                                    "rows": [
                                                      {
                                                        "id": "1"
                                                      }
                                                    ]
                                                  }
                                                  """);

        verify(providerService, times(1)).findBy(
                assertArg(arg -> assertThat(arg).contains("rows/id")),
                assertArg(arg -> assertThat(arg)
                        .hasAllNullFieldsOrPropertiesExcept("pagination")
                        .extracting(ProviderInput::getPagination)
                        .extracting(PaginationInput::getPageNumber, PaginationInput::getPageSize)
                        .containsExactly(2, 10)
                )
        );
        verifyNoMoreInteractions(providerService);
    }

    @Test
    @DisplayName("given a payload with valid input when calls insertProvider then expects")
    void givenAPayloadWithValidInput_whenCallsInsertProvider_thenExpects() {
        graphQlTester.documentName("insert-provider-sample-1")
                     .execute()
                     .path("insertProvider")
                     .matchesJsonStrictly("""
                                                  {
                                                    "id": "1",
                                                    "name": "Test provider",
                                                    "siteUrl": "http://localhost",
                                                    "mechanism": "JETIMOB_V1",
                                                    "cronExpression": "* * *",
                                                    "active": true
                                                  }
                                                  """);

        verify(providerService, times(1)).insert(
                assertArg(arg -> assertThat(arg)
                        .hasAllNullFieldsOrPropertiesExcept("name", "siteUrl", "mechanism", "cronExpression", "active")
                        .extracting(
                                ProviderCreationInput::getName,
                                ProviderCreationInput::getSiteUrl,
                                ProviderCreationInput::getMechanism,
                                ProviderCreationInput::getCronExpression,
                                ProviderCreationInput::getActive
                        )
                        .containsExactly(
                                "Test provider",
                                "http://localhost",
                                ProviderMechanism.JETIMOB_V1,
                                "* * *",
                                true
                        )
                )
        );
        verifyNoMoreInteractions(providerService);
    }

    @Test
    @DisplayName("given a payload with invalid input when calls insertProvider then expects")
    void givenAPayloadWithInvalidInput_whenCallsInsertProvider_thenExpects() {
        graphQlTester.documentName("insert-provider-sample-2")
                     .execute()
                     .errors()
                     .expect(e -> (
                             Objects.equals(e.getErrorType(), ErrorType.ValidationError) &&
                                     Arrays.stream(Objects.requireNonNull(e.getMessage()).split(","))
                                           .map(String::trim)
                                           .toList()
                                           .containsAll(List.of(
                                                   "insertProvider.input.dataUrl: deve ser uma URL válida",
                                                   "insertProvider.input.name: não deve estar em branco",
                                                   "insertProvider.input.siteUrl: não deve estar em branco",
                                                   "insertProvider.input.cronExpression: não deve estar em branco"
                                           ))
                     ));

        verifyNoInteractions(providerService);
    }

    @Test
    @DisplayName("given a payload with valid input with full update when calls updateProvider then expects")
    void givenAPayloadWithValidInputWithFullUpdate_whenCallsUpdateProvider_thenExpects() {
        graphQlTester.documentName("update-provider-sample-1")
                     .execute()
                     .path("updateProvider")
                     .matchesJsonStrictly("""
                                                  {
                                                    "id": "1",
                                                    "name": "Test provider edited",
                                                    "siteUrl": "http://127.0.0.1",
                                                    "dataUrl": "http://127.0.0.1/api",
                                                    "mechanism": "JETIMOB_V2",
                                                    "params": "{\\"connection\\":{}}",
                                                    "cronExpression": "0 0 0",
                                                    "logoUrl": "/api/rest/providers/1/logo",
                                                    "active": false
                                                  }
                                                  """);

        verify(providerService, times(1)).update(
                eq(1),
                assertArg(arg -> assertThat(arg)
                        .extracting(
                                ProviderEditionInput::getName,
                                ProviderEditionInput::getSiteUrl,
                                ProviderEditionInput::getDataUrl,
                                ProviderEditionInput::getMechanism,
                                ProviderEditionInput::getParams,
                                ProviderEditionInput::getCronExpression,
                                ProviderEditionInput::getLogo,
                                ProviderEditionInput::getActive
                        )
                        .containsExactly(
                                "Test provider edited",
                                "http://127.0.0.1",
                                "http://127.0.0.1/api",
                                ProviderMechanism.JETIMOB_V2,
                                "{\"connection\":{}}",
                                "0 0 0",
                                "Y29udGVudCBlZGl0ZWQ=",
                                false
                        )
                )
        );
        verifyNoMoreInteractions(providerService);
    }

    @Test
    @DisplayName("given a payload with valid input with partial update when calls updateProvider then expects")
    void givenAPayloadWithValidInputWithPartialUpdate_whenCallsUpdateProvider_thenExpects() {
        graphQlTester.documentName("update-provider-sample-2")
                     .execute()
                     .path("updateProvider")
                     .matchesJsonStrictly("""
                                                  {
                                                    "id": "1",
                                                    "name": "Test provider edited",
                                                    "siteUrl": "http://localhost",
                                                    "dataUrl": "",
                                                    "mechanism": "ALAN_WGT",
                                                    "params": "{}",
                                                    "cronExpression": "* * *",
                                                    "logoUrl": "/api/rest/providers/1/logo",
                                                    "active": true
                                                  }
                                                  """);

        verify(providerService, times(1)).update(
                eq(1),
                assertArg(arg -> assertThat(arg)
                        .hasAllNullFieldsOrPropertiesExcept("changedArguments", "name")
                        .hasFieldOrPropertyWithValue("changedArguments", Set.of("name", "dataUrl"))
                        .hasFieldOrPropertyWithValue("name", "Test provider edited")
                )
        );
        verifyNoMoreInteractions(providerService);
    }

    @Test
    @DisplayName("given a payload with invalid input when calls updateProvider then expects")
    void givenAPayloadWithInvalidInput_whenCallsUpdateProvider_thenExpects() {
        graphQlTester.documentName("update-provider-sample-3")
                     .execute()
                     .errors()
                     .expect(e -> (
                             Objects.equals(e.getErrorType(), ErrorType.ValidationError) &&
                                     Arrays.stream(Objects.requireNonNull(e.getMessage()).split(","))
                                           .map(String::trim)
                                           .toList()
                                           .containsAll(List.of(
                                                   "updateProvider.input.dataUrl: deve ser uma URL válida",
                                                   "updateProvider.input.name: tamanho deve ser entre 0 e 255",
                                                   "updateProvider.input.siteUrl: deve ser uma URL válida"
                                           ))
                     ));

        verifyNoInteractions(providerService);
    }

}
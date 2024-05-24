package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.argument.ProviderInput;
import br.com.houseseeker.domain.argument.ProviderInput.Clauses;
import br.com.houseseeker.domain.argument.ProviderInput.Orders;
import br.com.houseseeker.domain.input.OrderInput;
import br.com.houseseeker.domain.input.PaginationInput;
import br.com.houseseeker.domain.input.StringClauseInput;
import br.com.houseseeker.domain.input.StringInput;
import br.com.houseseeker.domain.proto.OrderDirectionData;
import br.com.houseseeker.service.proto.GetProvidersDataRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static br.com.houseseeker.service.dsl.GetProvidersDataRequestDsl.assertThis;

class ProvidersDataRequestBuilderTest {

    @Test
    @DisplayName("given only a id when calls build then expects to apply clause isEqual")
    void givenOnlyAId_whenCallsBuild_thenExpectsToApplyClauseIsEqual() {
        var response = ProvidersDataRequestBuilder.newInstance()
                                                  .byId(1)
                                                  .build();

        assertThis(response).isProjectingAll()
                            .hasClauseWithValue(c -> c.getId().getIsEqual(), 1)
                            .hasNotChangedOrders()
                            .hasNotChangedPagination();
    }

    @Test
    @DisplayName("given a empty projections when calls build then expects to project all attributes")
    void givenAEmptyProjections_whenCallsBuild_thenExpectsToProjectAllAttributes() {
        var response = ProvidersDataRequestBuilder.newInstance()
                                                  .withProjections(Collections.emptySet())
                                                  .build();

        assertThis(response).isProjectingAll()
                            .hasNotChangedClauses()
                            .hasNotChangedOrders()
                            .hasNotChangedPagination();
    }

    @Test
    @DisplayName("given a projection list with wrong prefix when calls build then expects none projections")
    void givenAProjectionListWithWrongPrefix_whenCallsBuild_thenExpectsNoneProjections() {
        var response = ProvidersDataRequestBuilder.newInstance()
                                                  .withProjections(Set.of("id", "name"))
                                                  .build();

        assertThis(response).isProjectingNone()
                            .hasNotChangedClauses()
                            .hasNotChangedOrders()
                            .hasNotChangedPagination();
    }

    @Test
    @DisplayName("given a projection list with correct prefix when calls build then expects to project only specified attributes")
    void givenAProjectionListWithCorrectPrefix_whenCallsBuild_thenExpectsToProjectOnlySpecifiedAttributes() {
        var response = ProvidersDataRequestBuilder.newInstance()
                                                  .withProjections(Set.of("rows/id", "rows/name"))
                                                  .build();

        assertThis(response).isProjectingOnly("id", "name")
                            .hasNotChangedClauses()
                            .hasNotChangedOrders()
                            .hasNotChangedPagination();
    }

    @Test
    @DisplayName("given a input without clauses, orders and pagination when calls build then expects")
    void givenAInputWithoutClausesOrdersAndPagination_whenCallsBuild_thenExpects() {
        var input = ProviderInput.builder().build();

        var response = ProvidersDataRequestBuilder.newInstance()
                                                  .withInput(input)
                                                  .build();

        assertThis(response).isProjectingNone()
                            .hasNotChangedClauses()
                            .hasNotChangedOrders()
                            .hasNotChangedPagination();
    }

    @Test
    @DisplayName("given a input with only clauses when calls build then expects")
    void givenAInputWithOnlyClauses_whenCallsBuild_thenExpects() {
        var input = ProviderInput.builder()
                                 .clauses(
                                         Clauses.builder()
                                                .name(
                                                        StringClauseInput.builder()
                                                                         .itContains(
                                                                                 StringInput.builder()
                                                                                            .value("Imobiliária")
                                                                                            .build()
                                                                         )
                                                                         .build()
                                                )
                                                .build()
                                 )
                                 .build();

        var response = ProvidersDataRequestBuilder.newInstance()
                                                  .withInput(input)
                                                  .build();

        assertThis(response).isProjectingNone()
                            .hasClauseWith(c -> c.getName().getItContains(), "Imobiliária")
                            .hasNotChangedOrders()
                            .hasNotChangedPagination();
    }

    @Test
    @DisplayName("given a input with only order when calls build then expects")
    void givenAInputWithOnlyOrder_whenCallsBuild_thenExpects() {
        var input = ProviderInput.builder()
                                 .orders(
                                         Orders.builder()
                                               .id(
                                                       OrderInput.builder()
                                                                 .index(2)
                                                                 .direction(OrderInput.Direction.DESC)
                                                                 .build()
                                               )
                                               .mechanism(
                                                       OrderInput.builder()
                                                                 .index(1)
                                                                 .direction(OrderInput.Direction.ASC)
                                                                 .build()
                                               )
                                               .build()
                                 )
                                 .build();

        var response = ProvidersDataRequestBuilder.newInstance()
                                                  .withInput(input)
                                                  .build();

        assertThis(response).isProjectingNone()
                            .hasNotChangedClauses()
                            .hasOrderWith(GetProvidersDataRequest.OrdersData::getMechanism, 1, OrderDirectionData.ASC)
                            .hasOrderWith(GetProvidersDataRequest.OrdersData::getId, 2, OrderDirectionData.DESC)
                            .hasNotChangedPagination();
    }

    @Test
    @DisplayName("given a input with only pagination when calls build then expects")
    void givenAInputWithOnlyPagination_whenCallsBuild_thenExpects() {
        var input = ProviderInput.builder()
                                 .pagination(
                                         PaginationInput.builder()
                                                        .pageNumber(1)
                                                        .pageSize(10)
                                                        .build()
                                 )
                                 .build();

        var response = ProvidersDataRequestBuilder.newInstance()
                                                  .withInput(input)
                                                  .build();

        assertThis(response).isProjectingNone()
                            .hasNotChangedClauses()
                            .hasNotChangedOrders()
                            .hasPaginationWith(1, 10);
    }

}
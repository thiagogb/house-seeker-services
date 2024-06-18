package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.argument.ProviderInput;
import br.com.houseseeker.domain.argument.ProviderInput.Clauses;
import br.com.houseseeker.domain.argument.ProviderInput.Orders;
import br.com.houseseeker.domain.input.BooleanClauseInput;
import br.com.houseseeker.domain.input.BytesClauseInput;
import br.com.houseseeker.domain.input.IntegerClauseInput;
import br.com.houseseeker.domain.input.OrderInput;
import br.com.houseseeker.domain.input.PaginationInput;
import br.com.houseseeker.domain.input.ProviderMechanismClausesInput;
import br.com.houseseeker.domain.input.StringClauseInput;
import br.com.houseseeker.domain.proto.PaginationRequestData;
import br.com.houseseeker.service.proto.GetProvidersDataRequest.OrdersData;
import br.com.houseseeker.service.proto.GetProvidersDataRequest.ProjectionsData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.normalizeSpace;
import static org.assertj.core.api.Assertions.assertThat;

class ProvidersDataRequestBuilderTest {

    @Test
    @DisplayName("given only a id when calls build then expects to apply clause isEqual")
    void givenOnlyAId_whenCallsBuild_thenExpectsToApplyClauseIsEqual() {
        var response = ProvidersDataRequestBuilder.newInstance()
                                                  .byId(1)
                                                  .build();

        assertThat(response)
                .satisfies(r -> assertThat(r.getProjections())
                        .extracting(p -> normalizeSpace(p.toString()))
                        .isEqualTo(
                                "id: true name: true site_url: true data_url: true mechanism: true " +
                                        "params: true cron_expression: true logo: true active: true"
                        )
                )
                .satisfies(r -> assertThat(r.getClausesList())
                        .extracting(c -> normalizeSpace(c.toString()))
                        .containsExactly("id { is_equal { value: 1 } }")
                )
                .satisfies(r -> assertThat(r.getOrders()).isEqualTo(OrdersData.getDefaultInstance()))
                .satisfies(r -> assertThat(r.getPagination()).isEqualTo(PaginationRequestData.getDefaultInstance()));
    }

    @Test
    @DisplayName("given a empty projections when calls build then expects to project all attributes")
    void givenAEmptyProjections_whenCallsBuild_thenExpectsToProjectAllAttributes() {
        var response = ProvidersDataRequestBuilder.newInstance()
                                                  .withProjections(Collections.emptySet())
                                                  .build();

        assertThat(response)
                .satisfies(r -> assertThat(r.getProjections())
                        .extracting(p -> normalizeSpace(p.toString()))
                        .isEqualTo(
                                "id: true name: true site_url: true data_url: true mechanism: true " +
                                        "params: true cron_expression: true logo: true active: true"
                        )
                )
                .satisfies(r -> assertThat(r.getClausesList()).isEqualTo(Collections.emptyList()))
                .satisfies(r -> assertThat(r.getOrders()).isEqualTo(OrdersData.getDefaultInstance()))
                .satisfies(r -> assertThat(r.getPagination()).isEqualTo(PaginationRequestData.getDefaultInstance()));
    }

    @Test
    @DisplayName("given a projection list with wrong prefix when calls build then expects none projections")
    void givenAProjectionListWithWrongPrefix_whenCallsBuild_thenExpectsNoneProjections() {
        var response = ProvidersDataRequestBuilder.newInstance()
                                                  .withProjections(Set.of("id", "name"))
                                                  .build();

        assertThat(response)
                .satisfies(r -> assertThat(r.getProjections())
                        .extracting(p -> normalizeSpace(p.toString()))
                        .isEqualTo(
                                "id: false name: false site_url: false data_url: false mechanism: false " +
                                        "params: false cron_expression: false logo: false active: false"
                        )
                )
                .satisfies(r -> assertThat(r.getClausesList()).isEqualTo(Collections.emptyList()))
                .satisfies(r -> assertThat(r.getOrders()).isEqualTo(OrdersData.getDefaultInstance()))
                .satisfies(r -> assertThat(r.getPagination()).isEqualTo(PaginationRequestData.getDefaultInstance()));
    }

    @Test
    @DisplayName("given a projection list with correct prefix when calls build then expects to project only specified attributes")
    void givenAProjectionListWithCorrectPrefix_whenCallsBuild_thenExpectsToProjectOnlySpecifiedAttributes() {
        var response = ProvidersDataRequestBuilder.newInstance()
                                                  .withProjections(Set.of("rows/id", "rows/name"))
                                                  .build();

        assertThat(response)
                .satisfies(r -> assertThat(r.getProjections())
                        .extracting(p -> normalizeSpace(p.toString()))
                        .isEqualTo(
                                "id: true name: true site_url: false data_url: false mechanism: false " +
                                        "params: false cron_expression: false logo: false active: false"
                        )
                )
                .satisfies(r -> assertThat(r.getClausesList()).isEqualTo(Collections.emptyList()))
                .satisfies(r -> assertThat(r.getOrders()).isEqualTo(OrdersData.getDefaultInstance()))
                .satisfies(r -> assertThat(r.getPagination()).isEqualTo(PaginationRequestData.getDefaultInstance()));
    }

    @Test
    @DisplayName("given a input without clauses, orders and pagination when calls build then expects")
    void givenAInputWithoutClausesOrdersAndPagination_whenCallsBuild_thenExpects() {
        var input = ProviderInput.builder().build();

        var response = ProvidersDataRequestBuilder.newInstance()
                                                  .withInput(input)
                                                  .build();

        assertThat(response)
                .satisfies(r -> assertThat(r.getProjections()).isEqualTo(ProjectionsData.getDefaultInstance()))
                .satisfies(r -> assertThat(r.getClausesList()).isEqualTo(Collections.emptyList()))
                .satisfies(r -> assertThat(r.getOrders()).isEqualTo(OrdersData.getDefaultInstance()))
                .satisfies(r -> assertThat(r.getPagination()).isEqualTo(PaginationRequestData.getDefaultInstance()));
    }

    @Test
    @DisplayName("given a input with only clauses when calls build then expects")
    void givenAInputWithOnlyClauses_whenCallsBuild_thenExpects() {
        var input = ProviderInput.builder()
                                 .clauses(
                                         Clauses.builder()
                                                .id(IntegerClauseInput.builder().isNotNull(true).build())
                                                .name(StringClauseInput.builder().isNotNull(true).build())
                                                .siteUrl(StringClauseInput.builder().isNotNull(true).build())
                                                .dataUrl(StringClauseInput.builder().isNotNull(true).build())
                                                .mechanism(ProviderMechanismClausesInput.builder().isNotNull(true).build())
                                                .params(StringClauseInput.builder().isNotNull(true).build())
                                                .cronExpression(StringClauseInput.builder().isNotNull(true).build())
                                                .logo(BytesClauseInput.builder().isNotNull(true).build())
                                                .active(BooleanClauseInput.builder().isNotNull(true).build())
                                                .build()
                                 )
                                 .build();

        var response = ProvidersDataRequestBuilder.newInstance()
                                                  .withInput(input)
                                                  .build();

        assertThat(response)
                .satisfies(r -> assertThat(r.getProjections()).isEqualTo(ProjectionsData.getDefaultInstance()))
                .satisfies(r -> assertThat(r.getClausesList())
                        .extracting(c -> normalizeSpace(c.toString()))
                        .containsExactly(
                                "id { is_not_null: true } " +
                                        "name { is_not_null: true } " +
                                        "site_url { is_not_null: true } " +
                                        "data_url { is_not_null: true } " +
                                        "mechanism { is_not_null: true } " +
                                        "params { is_not_null: true } " +
                                        "cron_expression { is_not_null: true } " +
                                        "logo { is_not_null: true } " +
                                        "active { is_not_null: true }"
                        )
                )
                .satisfies(r -> assertThat(r.getOrders()).isEqualTo(OrdersData.getDefaultInstance()))
                .satisfies(r -> assertThat(r.getPagination()).isEqualTo(PaginationRequestData.getDefaultInstance()));
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

        assertThat(response)
                .satisfies(r -> assertThat(r.getProjections()).isEqualTo(ProjectionsData.getDefaultInstance()))
                .satisfies(r -> assertThat(r.getClausesList()).isEqualTo(Collections.emptyList()))
                .satisfies(r -> assertThat(r.getOrders())
                        .extracting(c -> normalizeSpace(c.toString()))
                        .isEqualTo("id { index: 2 direction: DESC } name { } site_url { } data_url { } mechanism { index: 1 } active { }")
                )
                .satisfies(r -> assertThat(r.getPagination()).isEqualTo(PaginationRequestData.getDefaultInstance()));
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

        assertThat(response)
                .satisfies(r -> assertThat(r.getProjections()).isEqualTo(ProjectionsData.getDefaultInstance()))
                .satisfies(r -> assertThat(r.getClausesList()).isEqualTo(Collections.emptyList()))
                .satisfies(r -> assertThat(r.getOrders()).isEqualTo(OrdersData.getDefaultInstance()))
                .satisfies(r -> assertThat(r.getPagination())
                        .extracting(c -> normalizeSpace(c.toString()))
                        .isEqualTo("pageSize: 10 pageNumber: 1")
                );
    }

}
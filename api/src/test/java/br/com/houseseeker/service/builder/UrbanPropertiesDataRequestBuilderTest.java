package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.argument.UrbanPropertyInput;
import br.com.houseseeker.domain.argument.UrbanPropertyInput.Clauses;
import br.com.houseseeker.domain.argument.UrbanPropertyInput.Orders;
import br.com.houseseeker.domain.input.BooleanClauseInput;
import br.com.houseseeker.domain.input.DateTimeClauseInput;
import br.com.houseseeker.domain.input.FloatClauseInput;
import br.com.houseseeker.domain.input.IntegerClauseInput;
import br.com.houseseeker.domain.input.OrderInput;
import br.com.houseseeker.domain.input.PaginationInput;
import br.com.houseseeker.domain.input.StringClauseInput;
import br.com.houseseeker.domain.input.UrbanPropertyContractClauseInput;
import br.com.houseseeker.domain.input.UrbanPropertyStatusClauseInput;
import br.com.houseseeker.domain.input.UrbanPropertyTypeClauseInput;
import br.com.houseseeker.domain.proto.PaginationRequestData;
import br.com.houseseeker.service.proto.GetUrbanPropertiesRequest.ClausesData;
import br.com.houseseeker.service.proto.GetUrbanPropertiesRequest.OrdersData;
import br.com.houseseeker.service.proto.GetUrbanPropertiesRequest.ProjectionsData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.normalizeSpace;
import static org.assertj.core.api.Assertions.assertThat;

class UrbanPropertiesDataRequestBuilderTest {

    private static final String ALL_PROJECTIONS = "id: true provider: true provider_code: true url: true contract: true type: true " +
            "sub_type: true dormitories: true suites: true bathrooms: true garages: true sell_price: true rent_price: true " +
            "condominium_price: true condominium_name: true exchangeable: true status: true financeable: true occupied: true notes: true " +
            "creation_date: true last_analysis_date: true exclusion_date: true analyzable: true";

    @Test
    @DisplayName("given a empty projections when calls build then expects to project all attributes")
    void givenAEmptyProjections_whenCallsBuild_thenExpectsToProjectAllAttributes() {
        var response = UrbanPropertiesDataRequestBuilder.newInstance()
                                                        .withProjections(Collections.emptySet())
                                                        .build();

        assertThat(response)
                .satisfies(r -> assertThat(r.getProjections())
                        .extracting(p -> normalizeSpace(p.toString()))
                        .isEqualTo(ALL_PROJECTIONS)
                )
                .satisfies(r -> assertThat(r.getClauses()).isEqualTo(ClausesData.getDefaultInstance()))
                .satisfies(r -> assertThat(r.getOrders()).isEqualTo(OrdersData.getDefaultInstance()))
                .satisfies(r -> assertThat(r.getPagination()).isEqualTo(PaginationRequestData.getDefaultInstance()));
    }

    @Test
    @DisplayName("given a projection list with correct prefix when calls build then expects to project only specified attributes")
    void givenAProjectionListWithCorrectPrefix_whenCallsBuild_thenExpectsToProjectOnlySpecifiedAttributes() {
        var response = UrbanPropertiesDataRequestBuilder.newInstance()
                                                        .withProjections(Set.of(
                                                                "rows/id", "rows/provider", "rows/providerCode", "rows/url", "rows/contract",
                                                                "rows/type", "rows/subType", "rows/dormitories", "rows/suites", "rows/bathrooms",
                                                                "rows/garages", "rows/sellPrice", "rows/rentPrice", "rows/condominiumPrice",
                                                                "rows/condominiumName", "rows/exchangeable", "rows/status", "rows/financeable",
                                                                "rows/occupied", "rows/notes", "rows/creationDate", "rows/lastAnalysisDate",
                                                                "rows/exclusionDate", "rows/analyzable"
                                                        ))
                                                        .build();

        assertThat(response)
                .satisfies(r -> assertThat(r.getProjections())
                        .extracting(p -> normalizeSpace(p.toString()))
                        .isEqualTo(ALL_PROJECTIONS)
                )
                .satisfies(r -> assertThat(r.getClauses()).isEqualTo(ClausesData.getDefaultInstance()))
                .satisfies(r -> assertThat(r.getOrders()).isEqualTo(OrdersData.getDefaultInstance()))
                .satisfies(r -> assertThat(r.getPagination()).isEqualTo(PaginationRequestData.getDefaultInstance()));
    }

    @Test
    @DisplayName("given a input without clauses, orders and pagination when calls build then expects")
    void givenAInputWithoutClausesOrdersAndPagination_whenCallsBuild_thenExpects() {
        var input = UrbanPropertyInput.builder().build();
        var response = UrbanPropertiesDataRequestBuilder.newInstance().withInput(input).build();

        assertThat(response)
                .satisfies(r -> assertThat(r.getProjections()).isEqualTo(ProjectionsData.getDefaultInstance()))
                .satisfies(r -> assertThat(r.getClauses()).isEqualTo(ClausesData.getDefaultInstance()))
                .satisfies(r -> assertThat(r.getOrders()).isEqualTo(OrdersData.getDefaultInstance()))
                .satisfies(r -> assertThat(r.getPagination()).isEqualTo(PaginationRequestData.getDefaultInstance()));
    }

    @Test
    @DisplayName("given a input with only clauses when calls build then expects")
    void givenAInputWithOnlyClauses_whenCallsBuild_thenExpects() {
        var input = UrbanPropertyInput.builder()
                                      .clauses(
                                              Clauses.builder()
                                                     .id(IntegerClauseInput.builder().isNotNull(true).build())
                                                     .providerId(IntegerClauseInput.builder().isNotNull(true).build())
                                                     .providerCode(StringClauseInput.builder().isNotNull(true).build())
                                                     .url(StringClauseInput.builder().isNotNull(true).build())
                                                     .contract(UrbanPropertyContractClauseInput.builder().isNotNull(true).build())
                                                     .type(UrbanPropertyTypeClauseInput.builder().isNotNull(true).build())
                                                     .subType(StringClauseInput.builder().isNotNull(true).build())
                                                     .dormitories(IntegerClauseInput.builder().isNotNull(true).build())
                                                     .suites(IntegerClauseInput.builder().isNotNull(true).build())
                                                     .bathrooms(IntegerClauseInput.builder().isNotNull(true).build())
                                                     .garages(IntegerClauseInput.builder().isNotNull(true).build())
                                                     .sellPrice(FloatClauseInput.builder().isNotNull(true).build())
                                                     .rentPrice(FloatClauseInput.builder().isNotNull(true).build())
                                                     .condominiumPrice(FloatClauseInput.builder().isNotNull(true).build())
                                                     .condominiumName(StringClauseInput.builder().isNotNull(true).build())
                                                     .exchangeable(BooleanClauseInput.builder().isNotNull(true).build())
                                                     .status(UrbanPropertyStatusClauseInput.builder().isNotNull(true).build())
                                                     .financeable(BooleanClauseInput.builder().isNotNull(true).build())
                                                     .occupied(BooleanClauseInput.builder().isNotNull(true).build())
                                                     .notes(StringClauseInput.builder().isNotNull(true).build())
                                                     .creationDate(DateTimeClauseInput.builder().isNotNull(true).build())
                                                     .lastAnalysisDate(DateTimeClauseInput.builder().isNotNull(true).build())
                                                     .exclusionDate(DateTimeClauseInput.builder().isNotNull(true).build())
                                                     .analyzable(BooleanClauseInput.builder().isNotNull(true).build())
                                                     .build()
                                      )
                                      .build();
        var response = UrbanPropertiesDataRequestBuilder.newInstance().withInput(input).build();

        assertThat(response)
                .satisfies(r -> assertThat(r.getProjections()).isEqualTo(ProjectionsData.getDefaultInstance()))
                .satisfies(r -> assertThat(r.getClauses())
                        .extracting(c -> normalizeSpace(c.toString()))
                        .isEqualTo(
                                "id { is_not_null: true } " +
                                        "provider_id { is_not_null: true } " +
                                        "provider_code { is_not_null: true } " +
                                        "url { is_not_null: true } " +
                                        "contract { is_not_null: true } " +
                                        "type { is_not_null: true } " +
                                        "sub_type { is_not_null: true } " +
                                        "dormitories { is_not_null: true } " +
                                        "suites { is_not_null: true } " +
                                        "bathrooms { is_not_null: true } " +
                                        "garages { is_not_null: true } " +
                                        "sell_price { is_not_null: true } " +
                                        "rent_price { is_not_null: true } " +
                                        "condominium_price { is_not_null: true } " +
                                        "condominium_name { is_not_null: true } " +
                                        "exchangeable { is_not_null: true } " +
                                        "status { is_not_null: true } " +
                                        "financeable { is_not_null: true } " +
                                        "occupied { is_not_null: true } " +
                                        "notes { is_not_null: true } " +
                                        "creation_date { is_not_null: true } " +
                                        "last_analysis_date { is_not_null: true } " +
                                        "exclusion_date { is_not_null: true } " +
                                        "analyzable { is_not_null: true }"
                        )
                )
                .satisfies(r -> assertThat(r.getOrders()).isEqualTo(OrdersData.getDefaultInstance()))
                .satisfies(r -> assertThat(r.getPagination()).isEqualTo(PaginationRequestData.getDefaultInstance()));
    }

    @Test
    @DisplayName("given a input with only order when calls build then expects")
    void givenAInputWithOnlyOrder_whenCallsBuild_thenExpects() {
        var input = UrbanPropertyInput.builder()
                                      .orders(
                                              Orders.builder()
                                                    .id(
                                                            OrderInput.builder()
                                                                      .index(2)
                                                                      .direction(OrderInput.Direction.DESC)
                                                                      .build()
                                                    )
                                                    .providerCode(
                                                            OrderInput.builder()
                                                                      .index(1)
                                                                      .direction(OrderInput.Direction.ASC)
                                                                      .build()
                                                    )
                                                    .build()
                                      )
                                      .build();
        var response = UrbanPropertiesDataRequestBuilder.newInstance().withInput(input).build();

        assertThat(response)
                .satisfies(r -> assertThat(r.getProjections()).isEqualTo(ProjectionsData.getDefaultInstance()))
                .satisfies(r -> assertThat(r.getClauses()).isEqualTo(ClausesData.getDefaultInstance()))
                .satisfies(r -> assertThat(r.getOrders())
                        .extracting(c -> normalizeSpace(c.toString()))
                        .isEqualTo(
                                "id { index: 2 direction: DESC } provider_id { } provider_code { index: 1 } url { } contract { } " +
                                        "type { } sub_type { } dormitories { } suites { } bathrooms { } garages { } sell_price { } " +
                                        "rent_price { } condominium_price { } condominium_name { } exchangeable { } status { } " +
                                        "financeable { } occupied { } notes { } creation_date { } last_analysis_date { } exclusion_date { } " +
                                        "analyzable { }"
                        )
                )
                .satisfies(r -> assertThat(r.getPagination()).isEqualTo(PaginationRequestData.getDefaultInstance()));
    }

    @Test
    @DisplayName("given a input with only pagination when calls build then expects")
    void givenAInputWithOnlyPagination_whenCallsBuild_thenExpects() {
        var input = UrbanPropertyInput.builder()
                                      .pagination(
                                              PaginationInput.builder()
                                                             .pageNumber(1)
                                                             .pageSize(10)
                                                             .build()
                                      )
                                      .build();
        var response = UrbanPropertiesDataRequestBuilder.newInstance().withInput(input).build();

        assertThat(response)
                .satisfies(r -> assertThat(r.getProjections()).isEqualTo(ProjectionsData.getDefaultInstance()))
                .satisfies(r -> assertThat(r.getClauses()).isEqualTo(ClausesData.getDefaultInstance()))
                .satisfies(r -> assertThat(r.getOrders()).isEqualTo(OrdersData.getDefaultInstance()))
                .satisfies(r -> assertThat(r.getPagination())
                        .extracting(c -> normalizeSpace(c.toString()))
                        .isEqualTo("pageSize: 10 pageNumber: 1")
                );
    }

}
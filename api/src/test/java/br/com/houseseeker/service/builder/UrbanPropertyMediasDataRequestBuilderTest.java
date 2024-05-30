package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.proto.PaginationRequestData;
import br.com.houseseeker.service.proto.GetUrbanPropertyMediasRequest.ClausesData;
import br.com.houseseeker.service.proto.GetUrbanPropertyMediasRequest.OrdersData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.normalizeSpace;
import static org.assertj.core.api.Assertions.assertThat;

class UrbanPropertyMediasDataRequestBuilderTest {

    private static final String ALL_PROJECTIONS = "id: true urban_property: true link: true link_thumb: true media_type: true extension: true";

    @Test
    @DisplayName("given a empty projections when calls build then expects to project all attributes")
    void givenAEmptyProjections_whenCallsBuild_thenExpectsToProjectAllAttributes() {
        var response = UrbanPropertyMediasDataRequestBuilder.newInstance()
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
        var response = UrbanPropertyMediasDataRequestBuilder.newInstance()
                                                            .withProjections(Set.of(
                                                                    "rows/id", "rows/urbanProperty", "rows/link", "rows/linkThumb",
                                                                    "rows/mediaType", "rows/extension"
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
    @DisplayName("given a list of urban property ids when calls build then expects")
    void givenAListOfUrbanPropertyIds_whenCallsBuild_thenExpects() {
        var response = UrbanPropertyMediasDataRequestBuilder.newInstance()
                                                            .byUrbanProperties(List.of(1, 2, 3))
                                                            .build();

        assertThat(response)
                .satisfies(r -> assertThat(r.getProjections())
                        .extracting(p -> normalizeSpace(p.toString()))
                        .isEqualTo(ALL_PROJECTIONS)
                )
                .satisfies(r -> assertThat(r.getClauses())
                        .extracting(p -> normalizeSpace(p.toString()))
                        .isEqualTo("urban_property_id { is_in { values: 1 values: 2 values: 3 } }")
                )
                .satisfies(r -> assertThat(r.getOrders()).isEqualTo(OrdersData.getDefaultInstance()))
                .satisfies(r -> assertThat(r.getPagination())
                        .extracting(p -> normalizeSpace(p.toString()))
                        .isEqualTo("pageSize: 2147483647 pageNumber: 1")
                );
    }

}
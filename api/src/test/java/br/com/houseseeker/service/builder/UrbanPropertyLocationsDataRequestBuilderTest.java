package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.proto.PaginationRequestData;
import br.com.houseseeker.service.proto.GetUrbanPropertyLocationsRequest.ClausesData;
import br.com.houseseeker.service.proto.GetUrbanPropertyLocationsRequest.OrdersData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.normalizeSpace;
import static org.assertj.core.api.Assertions.assertThat;

class UrbanPropertyLocationsDataRequestBuilderTest {

    private static final String ALL_PROJECTIONS = "id: true urban_property: true state: true city: true district: true zip_code: true " +
            "street_name: true street_number: true complement: true latitude: true longitude: true";

    @Test
    @DisplayName("given a empty projections when calls build then expects to project all attributes")
    void givenAEmptyProjections_whenCallsBuild_thenExpectsToProjectAllAttributes() {
        var response = UrbanPropertyLocationsDataRequestBuilder.newInstance()
                                                               .withProjections(Collections.emptySet())
                                                               .build();

        assertThat(response)
                .satisfies(r -> assertThat(r.getProjections())
                        .extracting(p -> normalizeSpace(p.toString()))
                        .isEqualTo(ALL_PROJECTIONS)
                )
                .satisfies(r -> assertThat(r.getClausesList()).isEqualTo(Collections.emptyList()))
                .satisfies(r -> assertThat(r.getOrders()).isEqualTo(OrdersData.getDefaultInstance()))
                .satisfies(r -> assertThat(r.getPagination()).isEqualTo(PaginationRequestData.getDefaultInstance()));
    }

    @Test
    @DisplayName("given a projection list with correct prefix when calls build then expects to project only specified attributes")
    void givenAProjectionListWithCorrectPrefix_whenCallsBuild_thenExpectsToProjectOnlySpecifiedAttributes() {
        var response = UrbanPropertyLocationsDataRequestBuilder.newInstance()
                                                               .withProjections(Set.of(
                                                                       "rows/id", "rows/urbanProperty", "rows/state", "rows/city",
                                                                       "rows/district", "rows/zipCode", "rows/streetName", "rows/streetNumber",
                                                                       "rows/complement", "rows/latitude", "rows/longitude"
                                                               ))
                                                               .build();

        assertThat(response)
                .satisfies(r -> assertThat(r.getProjections())
                        .extracting(p -> normalizeSpace(p.toString()))
                        .isEqualTo(ALL_PROJECTIONS)
                )
                .satisfies(r -> assertThat(r.getClausesList()).isEqualTo(Collections.emptyList()))
                .satisfies(r -> assertThat(r.getOrders()).isEqualTo(OrdersData.getDefaultInstance()))
                .satisfies(r -> assertThat(r.getPagination()).isEqualTo(PaginationRequestData.getDefaultInstance()));
    }

    @Test
    @DisplayName("given a list of urban property ids when calls build then expects")
    void givenAListOfUrbanPropertyIds_whenCallsBuild_thenExpects() {
        var response = UrbanPropertyLocationsDataRequestBuilder.newInstance()
                                                               .byUrbanProperties(List.of(1, 2, 3))
                                                               .build();

        assertThat(response)
                .satisfies(r -> assertThat(r.getProjections())
                        .extracting(p -> normalizeSpace(p.toString()))
                        .isEqualTo(ALL_PROJECTIONS)
                )
                .satisfies(r -> assertThat(r.getClausesList())
                        .extracting(p -> normalizeSpace(p.toString()))
                        .containsExactly("urban_property_id { is_in { values: 1 values: 2 values: 3 } }")
                )
                .satisfies(r -> assertThat(r.getOrders()).isEqualTo(OrdersData.getDefaultInstance()))
                .satisfies(r -> assertThat(r.getPagination())
                        .extracting(p -> normalizeSpace(p.toString()))
                        .isEqualTo("pageSize: 3 pageNumber: 1")
                );
    }

}
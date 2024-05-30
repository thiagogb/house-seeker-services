package br.com.houseseeker.service;

import br.com.houseseeker.domain.argument.UrbanPropertyInput;
import br.com.houseseeker.domain.argument.UrbanPropertyInput.Clauses;
import br.com.houseseeker.domain.argument.UrbanPropertyInput.Orders;
import br.com.houseseeker.domain.input.IntegerClauseInput;
import br.com.houseseeker.domain.input.IntegerInput;
import br.com.houseseeker.domain.input.OrderInput;
import br.com.houseseeker.domain.input.PaginationInput;
import br.com.houseseeker.domain.proto.PaginationRequestData;
import br.com.houseseeker.domain.proto.UrbanPropertyData;
import br.com.houseseeker.service.proto.GetUrbanPropertiesRequest.ClausesData;
import br.com.houseseeker.service.proto.GetUrbanPropertiesRequest.OrdersData;
import br.com.houseseeker.service.proto.GetUrbanPropertiesResponse;
import br.com.houseseeker.service.proto.UrbanPropertyDataServiceGrpc.UrbanPropertyDataServiceBlockingStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.normalizeSpace;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = UrbanPropertyService.class)
@ExtendWith(MockitoExtension.class)
class UrbanPropertyServiceTest {

    private static final GetUrbanPropertiesResponse DEFAULT_RESPONSE = GetUrbanPropertiesResponse.newBuilder()
                                                                                                 .addAllUrbanProperties(List.of(
                                                                                                         UrbanPropertyData.getDefaultInstance()
                                                                                                 ))
                                                                                                 .buildPartial();

    @Autowired
    private UrbanPropertyService urbanPropertyService;

    @Mock
    private UrbanPropertyDataServiceBlockingStub mockedUrbanPropertyDataServiceBlockingStub;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(
                urbanPropertyService,
                "urbanPropertyDataServiceBlockingStub",
                mockedUrbanPropertyDataServiceBlockingStub
        );
    }

    @Test
    @DisplayName("given a empty projections and null input when calls findBy then expects")
    void givenAEmptyProjectionsAndNullInput_whenCallsFindBy_thenExpects() {
        when(mockedUrbanPropertyDataServiceBlockingStub.getUrbanProperties(any())).thenReturn(DEFAULT_RESPONSE);

        assertThat(urbanPropertyService.findBy(Collections.emptySet(), null)).isEqualTo(DEFAULT_RESPONSE);

        verify(mockedUrbanPropertyDataServiceBlockingStub, times(1)).getUrbanProperties(
                assertArg(a -> assertThat(a)
                        .satisfies(r -> assertThat(r.getProjections())
                                .extracting(p -> normalizeSpace(p.toString()))
                                .isEqualTo(
                                        "id: true provider: true provider_code: true url: true contract: true " +
                                                "type: true sub_type: true dormitories: true suites: true bathrooms: true " +
                                                "garages: true sell_price: true rent_price: true condominium_price: true " +
                                                "condominium_name: true exchangeable: true status: true financeable: true " +
                                                "occupied: true notes: true creation_date: true last_analysis_date: true " +
                                                "exclusion_date: true analyzable: true"
                                )
                        )
                        .satisfies(r -> assertThat(r.getClauses()).isEqualTo(ClausesData.getDefaultInstance()))
                        .satisfies(r -> assertThat(r.getOrders()).isEqualTo(OrdersData.getDefaultInstance()))
                        .satisfies(r -> assertThat(r.getPagination()).isEqualTo(PaginationRequestData.getDefaultInstance()))
                )
        );
        verifyNoMoreInteractions(mockedUrbanPropertyDataServiceBlockingStub);
    }

    @Test
    @DisplayName("given a filled projections and null input when calls findBy then expects")
    void givenAFilledProjectionsAndNullInput_whenCallsFindBy_thenExpects() {
        var projections = Set.of("rows/id", "rows/name", "rows/active");

        when(mockedUrbanPropertyDataServiceBlockingStub.getUrbanProperties(any())).thenReturn(DEFAULT_RESPONSE);

        assertThat(urbanPropertyService.findBy(projections, null)).isEqualTo(DEFAULT_RESPONSE);

        verify(mockedUrbanPropertyDataServiceBlockingStub, times(1)).getUrbanProperties(
                assertArg(a -> assertThat(a)
                        .satisfies(r -> assertThat(r.getProjections())
                                .extracting(p -> normalizeSpace(p.toString()))
                                .isEqualTo(
                                        "id: true provider: false provider_code: false url: false contract: false " +
                                                "type: false sub_type: false dormitories: false suites: false bathrooms: false " +
                                                "garages: false sell_price: false rent_price: false condominium_price: false " +
                                                "condominium_name: false exchangeable: false status: false financeable: false " +
                                                "occupied: false notes: false creation_date: false last_analysis_date: false " +
                                                "exclusion_date: false analyzable: false"
                                )
                        )
                        .satisfies(r -> assertThat(r.getClauses()).isEqualTo(ClausesData.getDefaultInstance()))
                        .satisfies(r -> assertThat(r.getOrders()).isEqualTo(OrdersData.getDefaultInstance()))
                        .satisfies(r -> assertThat(r.getPagination()).isEqualTo(PaginationRequestData.getDefaultInstance()))
                )
        );
        verifyNoMoreInteractions(mockedUrbanPropertyDataServiceBlockingStub);
    }

    @Test
    @DisplayName("given a empty projections and input with clauses when calls findBy then expects")
    void givenAEmptyProjectionsAndInputWithClauses_whenCallsFindBy_thenExpects() {
        var input = UrbanPropertyInput.builder()
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

        when(mockedUrbanPropertyDataServiceBlockingStub.getUrbanProperties(any())).thenReturn(DEFAULT_RESPONSE);

        assertThat(urbanPropertyService.findBy(Collections.emptySet(), input)).isEqualTo(DEFAULT_RESPONSE);

        verify(mockedUrbanPropertyDataServiceBlockingStub, times(1)).getUrbanProperties(
                assertArg(a -> assertThat(a)
                        .satisfies(r -> assertThat(r.getProjections())
                                .extracting(p -> normalizeSpace(p.toString()))
                                .isEqualTo(
                                        "id: true provider: true provider_code: true url: true contract: true " +
                                                "type: true sub_type: true dormitories: true suites: true bathrooms: true " +
                                                "garages: true sell_price: true rent_price: true condominium_price: true " +
                                                "condominium_name: true exchangeable: true status: true financeable: true " +
                                                "occupied: true notes: true creation_date: true last_analysis_date: true " +
                                                "exclusion_date: true analyzable: true"
                                )
                        )
                        .satisfies(r -> assertThat(r.getClauses())
                                .extracting(p -> normalizeSpace(p.toString()))
                                .isEqualTo(
                                        "id { is_greater { value: 1 } } provider_id { } provider_code { } url { } " +
                                                "contract { } type { } sub_type { } dormitories { } suites { } bathrooms { } " +
                                                "garages { } sell_price { } rent_price { } condominium_price { } " +
                                                "condominium_name { } exchangeable { } status { } financeable { } occupied { } " +
                                                "notes { } creation_date { } last_analysis_date { } exclusion_date { } analyzable { }"
                                )
                        )
                        .satisfies(r -> assertThat(r.getOrders()).isEqualTo(OrdersData.getDefaultInstance()))
                        .satisfies(r -> assertThat(r.getPagination()).isEqualTo(PaginationRequestData.getDefaultInstance()))
                )
        );
        verifyNoMoreInteractions(mockedUrbanPropertyDataServiceBlockingStub);
    }

    @Test
    @DisplayName("given a empty projections and input with orders when calls findBy then expects")
    void givenAEmptyProjectionsAndInputWithOrders_whenCallsFindBy_thenExpects() {
        var input = UrbanPropertyInput.builder()
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

        when(mockedUrbanPropertyDataServiceBlockingStub.getUrbanProperties(any())).thenReturn(DEFAULT_RESPONSE);

        assertThat(urbanPropertyService.findBy(Collections.emptySet(), input)).isEqualTo(DEFAULT_RESPONSE);

        verify(mockedUrbanPropertyDataServiceBlockingStub, times(1)).getUrbanProperties(
                assertArg(a -> assertThat(a)
                        .satisfies(r -> assertThat(r.getProjections())
                                .extracting(p -> normalizeSpace(p.toString()))
                                .isEqualTo(
                                        "id: true provider: true provider_code: true url: true contract: true " +
                                                "type: true sub_type: true dormitories: true suites: true bathrooms: true " +
                                                "garages: true sell_price: true rent_price: true condominium_price: true " +
                                                "condominium_name: true exchangeable: true status: true financeable: true " +
                                                "occupied: true notes: true creation_date: true last_analysis_date: true " +
                                                "exclusion_date: true analyzable: true"
                                )
                        )
                        .satisfies(r -> assertThat(r.getClauses()).isEqualTo(ClausesData.getDefaultInstance()))
                        .satisfies(r -> assertThat(r.getOrders())
                                .extracting(p -> normalizeSpace(p.toString()))
                                .isEqualTo(
                                        "id { index: 1 } provider_id { } provider_code { } url { } " +
                                                "contract { } type { } sub_type { } dormitories { } suites { } " +
                                                "bathrooms { } garages { } sell_price { } rent_price { } " +
                                                "condominium_price { } condominium_name { } exchangeable { } " +
                                                "status { } financeable { } occupied { } notes { } creation_date { } " +
                                                "last_analysis_date { } exclusion_date { } analyzable { }"
                                )
                        )
                        .satisfies(r -> assertThat(r.getPagination()).isEqualTo(PaginationRequestData.getDefaultInstance()))
                )
        );
        verifyNoMoreInteractions(mockedUrbanPropertyDataServiceBlockingStub);
    }

    @Test
    @DisplayName("given a empty projections and input with pagination when calls findBy then expects")
    void givenAEmptyProjectionsAndInputWithPagination_whenCallsFindBy_thenExpects() {
        var input = UrbanPropertyInput.builder()
                                      .pagination(
                                              PaginationInput.builder()
                                                             .pageNumber(2)
                                                             .pageSize(10)
                                                             .build()
                                      )
                                      .build();

        when(mockedUrbanPropertyDataServiceBlockingStub.getUrbanProperties(any())).thenReturn(DEFAULT_RESPONSE);

        assertThat(urbanPropertyService.findBy(Collections.emptySet(), input)).isEqualTo(DEFAULT_RESPONSE);

        verify(mockedUrbanPropertyDataServiceBlockingStub, times(1)).getUrbanProperties(
                assertArg(a -> assertThat(a)
                        .satisfies(r -> assertThat(r.getProjections())
                                .extracting(p -> normalizeSpace(p.toString()))
                                .isEqualTo(
                                        "id: true provider: true provider_code: true url: true contract: true " +
                                                "type: true sub_type: true dormitories: true suites: true bathrooms: true " +
                                                "garages: true sell_price: true rent_price: true condominium_price: true " +
                                                "condominium_name: true exchangeable: true status: true financeable: true " +
                                                "occupied: true notes: true creation_date: true last_analysis_date: true " +
                                                "exclusion_date: true analyzable: true"
                                )
                        )
                        .satisfies(r -> assertThat(r.getClauses()).isEqualTo(ClausesData.getDefaultInstance()))
                        .satisfies(r -> assertThat(r.getOrders()).isEqualTo(OrdersData.getDefaultInstance()))
                        .satisfies(r -> assertThat(r.getPagination())
                                .extracting(p -> normalizeSpace(p.toString()))
                                .isEqualTo("pageSize: 10 pageNumber: 2")
                        )
                )
        );
        verifyNoMoreInteractions(mockedUrbanPropertyDataServiceBlockingStub);
    }

}
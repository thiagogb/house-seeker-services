package br.com.houseseeker.util;

import br.com.houseseeker.domain.exception.GrpcStatusException;
import br.com.houseseeker.domain.proto.PaginationRequestData;
import br.com.houseseeker.entity.Provider;
import com.querydsl.jpa.impl.JPAQuery;
import io.grpc.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.LongSupplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaginationUtilsTest {

    @Mock
    private JPAQuery<Provider> mockedJpaQuery;

    @Mock
    private Page<String> mockedPage;

    @Test
    @DisplayName("given a invalid page number when calls paginateQuery then expects exception")
    void givenAInvalidPageNumber_whenCallsPaginateQuery_thenExpectsException() {
        PaginationRequestData paginationData = PaginationRequestData.newBuilder().setPageNumber(-1).build();

        assertThatThrownBy(() -> PaginationUtils.paginateQuery(mockedJpaQuery, paginationData))
                .isInstanceOf(GrpcStatusException.class)
                .hasFieldOrPropertyWithValue("status", Status.INVALID_ARGUMENT)
                .hasMessage("Page number must be greater than zero");

        verifyNoInteractions(mockedJpaQuery);
    }

    @Test
    @DisplayName("given a invalid page size when calls paginateQuery then expects exception")
    void givenAInvalidPageSize_whenCallsPaginateQuery_thenExpectsException() {
        PaginationRequestData paginationData = PaginationRequestData.newBuilder()
                                                                    .setPageNumber(1)
                                                                    .setPageSize(-1)
                                                                    .build();

        assertThatThrownBy(() -> PaginationUtils.paginateQuery(mockedJpaQuery, paginationData))
                .isInstanceOf(GrpcStatusException.class)
                .hasFieldOrPropertyWithValue("status", Status.INVALID_ARGUMENT)
                .hasMessage("Page size must be greater than zero");

        verifyNoInteractions(mockedJpaQuery);
    }

    @Test
    @DisplayName("given a unset pagination data when calls paginateQuery then expects to apply default pagination")
    void givenAUnsetPaginationData_whenCallsPaginateQuery_thenExpectsToApplyDefaultPagination() {
        PaginationRequestData paginationData = PaginationRequestData.getDefaultInstance();

        when(mockedJpaQuery.offset(anyLong())).thenReturn(mockedJpaQuery);

        PaginationUtils.paginateQuery(mockedJpaQuery, paginationData);

        verify(mockedJpaQuery, times(1)).offset(0L);
        verify(mockedJpaQuery, times(1)).limit(50L);
        verifyNoMoreInteractions(mockedJpaQuery);
    }

    @Test
    @DisplayName("given a custom pagination data when calls paginateQuery then expects")
    void givenACustomPaginationData_whenCallsPaginateQuery_thenExpects() {
        PaginationRequestData paginationData = PaginationRequestData.newBuilder()
                                                                    .setPageNumber(3)
                                                                    .setPageSize(25)
                                                                    .build();

        when(mockedJpaQuery.offset(anyLong())).thenReturn(mockedJpaQuery);

        PaginationUtils.paginateQuery(mockedJpaQuery, paginationData);

        verify(mockedJpaQuery, times(1)).offset(50);
        verify(mockedJpaQuery, times(1)).limit(25L);
        verifyNoMoreInteractions(mockedJpaQuery);
    }

    @Test
    @DisplayName("given a execution result for first page when calls collectPaginationMetadata then expects")
    void givenAExecutionResultForFirstPage_whenCallsCollectPaginationMetadata_thenExpects() {
        List<String> rows = List.of("A", "B", "C");
        PaginationRequestData paginationData = PaginationRequestData.newBuilder()
                                                                    .setPageNumber(1)
                                                                    .setPageSize(50)
                                                                    .build();
        LongSupplier totalSupplier = () -> 3L;

        Page<String> results = PaginationUtils.collectPaginationMetadata(rows, paginationData, totalSupplier);

        assertThat(results.getContent()).isEqualTo(rows);
        assertThat(results.getTotalPages()).isEqualTo(1);
        assertThat(results.getTotalElements()).isEqualTo(3);
        assertThat(results.getNumber()).isZero();
        assertThat(results.getSize()).isEqualTo(50);
    }

    @Test
    @DisplayName("given a execution result for last page when calls collectPaginationMetadata then expects")
    void givenAExecutionResultForLastPage_whenCallsCollectPaginationMetadata_thenExpects() {
        List<String> rows = List.of("C");
        PaginationRequestData paginationData = PaginationRequestData.newBuilder()
                                                                    .setPageNumber(2)
                                                                    .setPageSize(2)
                                                                    .build();
        LongSupplier totalSupplier = () -> 3L;

        Page<String> results = PaginationUtils.collectPaginationMetadata(rows, paginationData, totalSupplier);

        assertThat(results.getContent()).isEqualTo(rows);
        assertThat(results.getTotalPages()).isEqualTo(2);
        assertThat(results.getTotalElements()).isEqualTo(3);
        assertThat(results.getNumber()).isEqualTo(1);
        assertThat(results.getSize()).isEqualTo(2);
    }

    @Test
    @DisplayName("given a page when calls toPaginationResponseData then expects")
    void givenAPage_whenCallsToPaginationResponseData_thenExpects() {
        when(mockedPage.getNumber()).thenReturn(1);
        when(mockedPage.getSize()).thenReturn(10);
        when(mockedPage.getTotalPages()).thenReturn(5);
        when(mockedPage.getTotalElements()).thenReturn(50L);

        assertThat(PaginationUtils.toPaginationResponseData(mockedPage))
                .extracting("pageNumber", "pageSize", "totalPages", "totalRows")
                .containsExactly(2, 10, 5, 50L);
    }

}
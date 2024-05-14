package br.com.houseseeker.util;

import br.com.houseseeker.domain.exception.GrpcStatusException;
import br.com.houseseeker.domain.proto.PaginationRequestData;
import br.com.houseseeker.domain.proto.PaginationResponseData;
import com.querydsl.jpa.impl.JPAQuery;
import io.grpc.Status;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.function.LongSupplier;

import static org.springframework.data.support.PageableExecutionUtils.getPage;

@UtilityClass
public class PaginationUtils {

    private static final int DEFAULT_OFFSET = 0;
    private static final int DEFAULT_OFFSET_DEVIATION = 1;
    private static final int DEFAULT_PAGE_SIZE = 50;

    public <T> void paginateQuery(@NotNull JPAQuery<T> jpaQuery, @NotNull PaginationRequestData paginationData) {
        validatePagination(paginationData);
        PageRequest pageRequest = calculatePageRequest(paginationData);
        jpaQuery.offset(pageRequest.getPageNumber())
                .limit(pageRequest.getPageSize());
    }

    public <T> Page<T> collectPaginationMetadata(
            @NotNull List<T> rows,
            @NotNull PaginationRequestData paginationData,
            @NotNull LongSupplier totalSupplier
    ) {
        return getPage(rows, calculatePageResponse(paginationData), totalSupplier);
    }

    public <T> PaginationResponseData toPaginationResponseData(@NotNull Page<T> page) {
        return PaginationResponseData.newBuilder()
                                     .setPageNumber(page.getNumber() + DEFAULT_OFFSET_DEVIATION)
                                     .setPageSize(page.getSize())
                                     .setTotalPages(page.getTotalPages())
                                     .setTotalRows(page.getTotalElements())
                                     .build();
    }

    private void validatePagination(PaginationRequestData paginationData) {
        if (paginationData.getPageNumber() < 0)
            throw new GrpcStatusException(Status.INVALID_ARGUMENT, "Page number must be greater than zero");

        if (paginationData.getPageSize() < 0)
            throw new GrpcStatusException(Status.INVALID_ARGUMENT, "Page size must be greater than zero");
    }

    private PageRequest calculatePageRequest(PaginationRequestData paginationData) {
        int pageSize = paginationData.getPageSize() > 0
                ? paginationData.getPageSize()
                : DEFAULT_PAGE_SIZE;

        int pageNumber = paginationData.getPageNumber() > 0
                ? calculateOffset(paginationData.getPageNumber(), pageSize)
                : DEFAULT_OFFSET;

        return PageRequest.of(pageNumber, pageSize);
    }

    private PageRequest calculatePageResponse(PaginationRequestData paginationData) {
        int pageSize = paginationData.getPageSize() > 0
                ? paginationData.getPageSize()
                : DEFAULT_PAGE_SIZE;

        return PageRequest.of(Math.max(0, paginationData.getPageNumber() - DEFAULT_OFFSET_DEVIATION), pageSize);
    }

    private int calculateOffset(int pageNumber, int pageSize) {
        return Math.max(0, pageNumber - DEFAULT_OFFSET_DEVIATION) * pageSize;
    }

}

package br.com.houseseeker.service.dsl;

import br.com.houseseeker.domain.proto.Int32SingleComparisonData;
import br.com.houseseeker.domain.proto.OrderDetailsData;
import br.com.houseseeker.domain.proto.OrderDirectionData;
import br.com.houseseeker.domain.proto.PaginationRequestData;
import br.com.houseseeker.domain.proto.StringSingleComparisonData;
import br.com.houseseeker.service.proto.GetProvidersDataRequest;
import br.com.houseseeker.service.proto.GetProvidersDataRequest.ClausesData;
import br.com.houseseeker.service.proto.GetProvidersDataRequest.OrdersData;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GetProvidersDataRequestDsl {

    private static final String[] EXTRACTED_PROJECTIONS = new String[]{
            "id", "name", "siteUrl", "dataUrl", "mechanism", "params", "cronExpression", "logo", "active"
    };

    private final GetProvidersDataRequest subject;

    public static GetProvidersDataRequestDsl assertThis(GetProvidersDataRequest subject) {
        return new GetProvidersDataRequestDsl(subject);
    }

    public GetProvidersDataRequestDsl isProjectingAll() {
        assertThat(subject.getProjections())
                .extracting(EXTRACTED_PROJECTIONS)
                .containsOnly(true);

        return this;
    }

    public GetProvidersDataRequestDsl isProjectingNone() {
        assertThat(subject.getProjections())
                .extracting(EXTRACTED_PROJECTIONS)
                .containsOnly(false);

        return this;
    }

    public GetProvidersDataRequestDsl isProjectingOnly(String... attributes) {
        assertThat(subject.getProjections())
                .extracting(attributes)
                .containsOnly(true);

        assertThat(subject.getProjections())
                .extracting(ArrayUtils.removeElements(EXTRACTED_PROJECTIONS, attributes))
                .containsOnly(false);

        return this;
    }

    public GetProvidersDataRequestDsl hasNotChangedClauses() {
        assertThat(subject.getClauses()).isEqualTo(ClausesData.getDefaultInstance());

        return this;
    }

    public GetProvidersDataRequestDsl hasNotChangedOrders() {
        assertThat(subject.getOrders()).isEqualTo(OrdersData.getDefaultInstance());

        return this;
    }

    public GetProvidersDataRequestDsl hasNotChangedPagination() {
        assertThat(subject.getPagination()).isEqualTo(PaginationRequestData.getDefaultInstance());

        return this;
    }

    public GetProvidersDataRequestDsl hasClauseWithValue(
            Function<ClausesData, Int32SingleComparisonData> clauseSupplier,
            Integer value
    ) {
        assertThat(subject.getClauses())
                .extracting(clauseSupplier)
                .extracting(Int32SingleComparisonData::getValue)
                .isEqualTo(value);

        return this;
    }

    public GetProvidersDataRequestDsl hasClauseWith(
            Function<ClausesData, StringSingleComparisonData> clauseSupplier,
            String value
    ) {
        assertThat(subject.getClauses())
                .extracting(clauseSupplier)
                .extracting(StringSingleComparisonData::getValue)
                .isEqualTo(value);

        return this;
    }

    public GetProvidersDataRequestDsl hasOrderWith(
            Function<OrdersData, OrderDetailsData> orderSupplier,
            Integer index,
            OrderDirectionData direction
    ) {
        assertThat(subject.getOrders())
                .extracting(orderSupplier)
                .extracting(OrderDetailsData::getIndex, OrderDetailsData::getDirection)
                .containsExactly(index, direction);

        return this;
    }

    public GetProvidersDataRequestDsl hasPaginationWith(Integer pageNumber, Integer pageSize) {
        assertThat(subject.getPagination())
                .extracting(PaginationRequestData::getPageNumber, PaginationRequestData::getPageSize)
                .containsExactly(pageNumber, pageSize);

        return this;
    }

}

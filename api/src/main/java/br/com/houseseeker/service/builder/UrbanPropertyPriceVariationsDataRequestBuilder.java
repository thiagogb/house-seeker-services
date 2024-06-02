package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.proto.Int32ComparisonData;
import br.com.houseseeker.domain.proto.Int32ListComparisonData;
import br.com.houseseeker.domain.proto.PaginationRequestData;
import br.com.houseseeker.service.proto.GetUrbanPropertyPriceVariationsRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertyPriceVariationsRequest.ClausesData;
import br.com.houseseeker.service.proto.GetUrbanPropertyPriceVariationsRequest.ProjectionsData;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UrbanPropertyPriceVariationsDataRequestBuilder {

    private final GetUrbanPropertyPriceVariationsRequest.Builder builder = GetUrbanPropertyPriceVariationsRequest.newBuilder();

    public static UrbanPropertyPriceVariationsDataRequestBuilder newInstance() {
        return new UrbanPropertyPriceVariationsDataRequestBuilder();
    }

    public UrbanPropertyPriceVariationsDataRequestBuilder byUrbanProperties(@NotNull List<Integer> ids) {
        withProjections(Collections.emptySet());

        builder.setClauses(
                       ClausesData.newBuilder()
                                  .setUrbanPropertyId(
                                          Int32ComparisonData.newBuilder()
                                                             .setIsIn(
                                                                     Int32ListComparisonData.newBuilder()
                                                                                            .addAllValues(ids)
                                                                                            .build()
                                                             )
                                                             .build()
                                  )
                                  .build()
               )
               .setPagination(
                       PaginationRequestData.newBuilder()
                                            .setPageSize(Integer.MAX_VALUE)
                                            .setPageNumber(1)
                                            .build()
               );

        return this;
    }

    public UrbanPropertyPriceVariationsDataRequestBuilder withProjections(@NotNull Set<String> projections) {
        boolean allProjectionsSelected = CollectionUtils.isEmpty(projections);

        builder.setProjections(
                ProjectionsData.newBuilder()
                               .setId(allProjectionsSelected || projections.contains("rows/id"))
                               .setUrbanProperty(allProjectionsSelected || projections.stream().anyMatch(p -> p.startsWith("rows/urbanProperty")))
                               .setAnalysisDate(allProjectionsSelected || projections.contains("rows/analysisDate"))
                               .setType(allProjectionsSelected || projections.contains("rows/type"))
                               .setPrice(allProjectionsSelected || projections.contains("rows/price"))
                               .setVariation(allProjectionsSelected || projections.contains("rows/variation"))
                               .build()
        );

        return this;
    }

    public GetUrbanPropertyPriceVariationsRequest build() {
        return builder.build();
    }

}

package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.proto.Int32ComparisonData;
import br.com.houseseeker.domain.proto.Int32ListComparisonData;
import br.com.houseseeker.domain.proto.PaginationRequestData;
import br.com.houseseeker.service.proto.GetUrbanPropertyConveniencesRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertyConveniencesRequest.ClausesData;
import br.com.houseseeker.service.proto.GetUrbanPropertyConveniencesRequest.ProjectionsData;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UrbanPropertyConveniencesDataRequestBuilder {

    private final GetUrbanPropertyConveniencesRequest.Builder builder = GetUrbanPropertyConveniencesRequest.newBuilder();

    public static UrbanPropertyConveniencesDataRequestBuilder newInstance() {
        return new UrbanPropertyConveniencesDataRequestBuilder();
    }

    public UrbanPropertyConveniencesDataRequestBuilder byUrbanProperties(@NotNull List<Integer> ids) {
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

    public UrbanPropertyConveniencesDataRequestBuilder withProjections(@NotNull Set<String> projections) {
        boolean allProjectionsSelected = CollectionUtils.isEmpty(projections);

        builder.setProjections(
                ProjectionsData.newBuilder()
                               .setId(allProjectionsSelected || projections.contains("rows/id"))
                               .setUrbanProperty(allProjectionsSelected || projections.stream().anyMatch(p -> p.startsWith("rows/urbanProperty")))
                               .setDescription(allProjectionsSelected || projections.contains("rows/description"))
                               .build()
        );

        return this;
    }

    public GetUrbanPropertyConveniencesRequest build() {
        return builder.build();
    }

}

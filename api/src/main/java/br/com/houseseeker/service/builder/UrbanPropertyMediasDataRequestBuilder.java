package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.proto.Int32ComparisonData;
import br.com.houseseeker.domain.proto.Int32ListComparisonData;
import br.com.houseseeker.domain.proto.PaginationRequestData;
import br.com.houseseeker.service.proto.GetUrbanPropertyMediasRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertyMediasRequest.ClausesData;
import br.com.houseseeker.service.proto.GetUrbanPropertyMediasRequest.ProjectionsData;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UrbanPropertyMediasDataRequestBuilder {

    private final GetUrbanPropertyMediasRequest.Builder builder = GetUrbanPropertyMediasRequest.newBuilder();

    public static UrbanPropertyMediasDataRequestBuilder newInstance() {
        return new UrbanPropertyMediasDataRequestBuilder();
    }

    public UrbanPropertyMediasDataRequestBuilder byUrbanProperties(@NotNull List<Integer> ids) {
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

    public UrbanPropertyMediasDataRequestBuilder withProjections(@NotNull Set<String> projections) {
        boolean allProjectionsSelected = CollectionUtils.isEmpty(projections);

        builder.setProjections(
                ProjectionsData.newBuilder()
                               .setId(allProjectionsSelected || projections.contains("rows/id"))
                               .setUrbanProperty(allProjectionsSelected || projections.stream().anyMatch(p -> p.startsWith("rows/urbanProperty")))
                               .setLink(allProjectionsSelected || projections.contains("rows/link"))
                               .setLinkThumb(allProjectionsSelected || projections.contains("rows/linkThumb"))
                               .setMediaType(allProjectionsSelected || projections.contains("rows/mediaType"))
                               .setExtension(allProjectionsSelected || projections.contains("rows/extension"))
                               .build()
        );

        return this;
    }

    public GetUrbanPropertyMediasRequest build() {
        return builder.build();
    }

}

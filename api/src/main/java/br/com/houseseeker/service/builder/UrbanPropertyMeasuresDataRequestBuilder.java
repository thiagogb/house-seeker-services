package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.proto.Int32ComparisonData;
import br.com.houseseeker.domain.proto.Int32ListComparisonData;
import br.com.houseseeker.domain.proto.PaginationRequestData;
import br.com.houseseeker.service.proto.GetUrbanPropertyMeasuresRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertyMeasuresRequest.ClausesData;
import br.com.houseseeker.service.proto.GetUrbanPropertyMeasuresRequest.ProjectionsData;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UrbanPropertyMeasuresDataRequestBuilder {

    private final GetUrbanPropertyMeasuresRequest.Builder builder = GetUrbanPropertyMeasuresRequest.newBuilder();

    public static UrbanPropertyMeasuresDataRequestBuilder newInstance() {
        return new UrbanPropertyMeasuresDataRequestBuilder();
    }

    public UrbanPropertyMeasuresDataRequestBuilder byUrbanProperties(@NotNull List<Integer> ids) {
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
                                            .setPageSize(ids.size())
                                            .setPageNumber(1)
                                            .build()
               );

        return this;
    }

    public UrbanPropertyMeasuresDataRequestBuilder withProjections(@NotNull Set<String> projections) {
        boolean allProjectionsSelected = CollectionUtils.isEmpty(projections);

        builder.setProjections(
                ProjectionsData.newBuilder()
                               .setId(allProjectionsSelected || projections.contains("rows/id"))
                               .setUrbanProperty(allProjectionsSelected || projections.stream().anyMatch(p -> p.startsWith("rows/urbanProperty")))
                               .setTotalArea(allProjectionsSelected || projections.contains("rows/totalArea"))
                               .setPrivateArea(allProjectionsSelected || projections.contains("rows/privateArea"))
                               .setUsableArea(allProjectionsSelected || projections.contains("rows/usableArea"))
                               .setTerrainTotalArea(allProjectionsSelected || projections.contains("rows/terrainTotalArea"))
                               .setTerrainFront(allProjectionsSelected || projections.contains("rows/terrainFront"))
                               .setTerrainBack(allProjectionsSelected || projections.contains("rows/terrainBack"))
                               .setTerrainLeft(allProjectionsSelected || projections.contains("rows/terrainLeft"))
                               .setTerrainRight(allProjectionsSelected || projections.contains("rows/terrainRight"))
                               .setAreaUnit(allProjectionsSelected || projections.contains("rows/areaUnit"))
                               .build()
        );

        return this;
    }

    public GetUrbanPropertyMeasuresRequest build() {
        return builder.build();
    }

}

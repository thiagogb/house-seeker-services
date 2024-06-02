package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.proto.Int32ComparisonData;
import br.com.houseseeker.domain.proto.Int32ListComparisonData;
import br.com.houseseeker.domain.proto.PaginationRequestData;
import br.com.houseseeker.service.proto.GetUrbanPropertyLocationsRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertyLocationsRequest.ClausesData;
import br.com.houseseeker.service.proto.GetUrbanPropertyLocationsRequest.ProjectionsData;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UrbanPropertyLocationsDataRequestBuilder {

    private final GetUrbanPropertyLocationsRequest.Builder builder = GetUrbanPropertyLocationsRequest.newBuilder();

    public static UrbanPropertyLocationsDataRequestBuilder newInstance() {
        return new UrbanPropertyLocationsDataRequestBuilder();
    }

    public UrbanPropertyLocationsDataRequestBuilder byUrbanProperties(@NotNull List<Integer> ids) {
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

    public UrbanPropertyLocationsDataRequestBuilder withProjections(@NotNull Set<String> projections) {
        boolean allProjectionsSelected = CollectionUtils.isEmpty(projections);

        builder.setProjections(
                ProjectionsData.newBuilder()
                               .setId(allProjectionsSelected || projections.contains("rows/id"))
                               .setUrbanProperty(allProjectionsSelected || projections.stream().anyMatch(p -> p.startsWith("rows/urbanProperty")))
                               .setState(allProjectionsSelected || projections.contains("rows/state"))
                               .setCity(allProjectionsSelected || projections.contains("rows/city"))
                               .setDistrict(allProjectionsSelected || projections.contains("rows/district"))
                               .setZipCode(allProjectionsSelected || projections.contains("rows/zipCode"))
                               .setStreetName(allProjectionsSelected || projections.contains("rows/streetName"))
                               .setStreetNumber(allProjectionsSelected || projections.contains("rows/streetNumber"))
                               .setComplement(allProjectionsSelected || projections.contains("rows/complement"))
                               .setLatitude(allProjectionsSelected || projections.contains("rows/latitude"))
                               .setLongitude(allProjectionsSelected || projections.contains("rows/longitude"))
                               .build()
        );

        return this;
    }

    public GetUrbanPropertyLocationsRequest build() {
        return builder.build();
    }

}

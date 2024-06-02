package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.argument.UrbanPropertyInput;
import br.com.houseseeker.domain.input.PaginationInput;
import br.com.houseseeker.service.proto.GetUrbanPropertiesRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertiesRequest.ClausesData;
import br.com.houseseeker.service.proto.GetUrbanPropertiesRequest.OrdersData;
import br.com.houseseeker.service.proto.GetUrbanPropertiesRequest.ProjectionsData;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.Optional;
import java.util.Set;

import static java.util.Objects.nonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UrbanPropertiesDataRequestBuilder
        implements DataRequestBuildable<UrbanPropertiesDataRequestBuilder, UrbanPropertyInput, GetUrbanPropertiesRequest> {

    private final GetUrbanPropertiesRequest.Builder builder = GetUrbanPropertiesRequest.newBuilder();

    public static UrbanPropertiesDataRequestBuilder newInstance() {
        return new UrbanPropertiesDataRequestBuilder();
    }

    @Override
    public UrbanPropertiesDataRequestBuilder withProjections(@NotNull Set<String> projections) {
        boolean allProjectionsSelected = CollectionUtils.isEmpty(projections);

        builder.setProjections(
                ProjectionsData.newBuilder()
                               .setId(allProjectionsSelected || projections.contains("rows/id"))
                               .setProvider(allProjectionsSelected || projections.stream().anyMatch(p -> p.startsWith("rows/provider")))
                               .setProviderCode(allProjectionsSelected || projections.contains("rows/providerCode"))
                               .setUrl(allProjectionsSelected || projections.contains("rows/url"))
                               .setContract(allProjectionsSelected || projections.contains("rows/contract"))
                               .setType(allProjectionsSelected || projections.contains("rows/type"))
                               .setSubType(allProjectionsSelected || projections.contains("rows/subType"))
                               .setDormitories(allProjectionsSelected || projections.contains("rows/dormitories"))
                               .setSuites(allProjectionsSelected || projections.contains("rows/suites"))
                               .setBathrooms(allProjectionsSelected || projections.contains("rows/bathrooms"))
                               .setGarages(allProjectionsSelected || projections.contains("rows/garages"))
                               .setSellPrice(allProjectionsSelected || projections.contains("rows/sellPrice"))
                               .setRentPrice(allProjectionsSelected || projections.contains("rows/rentPrice"))
                               .setCondominiumPrice(allProjectionsSelected || projections.contains("rows/condominiumPrice"))
                               .setCondominiumName(allProjectionsSelected || projections.contains("rows/condominiumName"))
                               .setExchangeable(allProjectionsSelected || projections.contains("rows/exchangeable"))
                               .setStatus(allProjectionsSelected || projections.contains("rows/status"))
                               .setFinanceable(allProjectionsSelected || projections.contains("rows/financeable"))
                               .setOccupied(allProjectionsSelected || projections.contains("rows/occupied"))
                               .setNotes(allProjectionsSelected || projections.contains("rows/notes"))
                               .setCreationDate(allProjectionsSelected || projections.contains("rows/creationDate"))
                               .setLastAnalysisDate(allProjectionsSelected || projections.contains("rows/lastAnalysisDate"))
                               .setExclusionDate(allProjectionsSelected || projections.contains("rows/exclusionDate"))
                               .setAnalyzable(allProjectionsSelected || projections.contains("rows/analyzable"))
                               .build()
        );

        return this;
    }

    @Override
    public UrbanPropertiesDataRequestBuilder withInput(UrbanPropertyInput input) {
        if (nonNull(input)) {
            Optional.ofNullable(input.getClauses()).ifPresent(this::configureClauses);
            Optional.ofNullable(input.getOrders()).ifPresent(this::configureOrders);
            Optional.ofNullable(input.getPagination()).ifPresent(this::configurePagination);
        }

        return this;
    }

    @Override
    public GetUrbanPropertiesRequest build() {
        return builder.build();
    }

    private void configureClauses(UrbanPropertyInput.Clauses clauses) {
        builder.setClauses(
                ClausesData.newBuilder()
                           .setId(IntegerComparisonBuilder.build(clauses.getId()))
                           .setProviderId(IntegerComparisonBuilder.build(clauses.getProviderId()))
                           .setProviderCode(StringComparisonBuilder.build(clauses.getProviderCode()))
                           .setUrl(StringComparisonBuilder.build(clauses.getUrl()))
                           .setContract(UrbanPropertyContractComparisonBuilder.build(clauses.getContract()))
                           .setType(UrbanPropertyTypeComparisonBuilder.build(clauses.getType()))
                           .setSubType(StringComparisonBuilder.build(clauses.getSubType()))
                           .setDormitories(IntegerComparisonBuilder.build(clauses.getDormitories()))
                           .setSuites(IntegerComparisonBuilder.build(clauses.getSuites()))
                           .setBathrooms(IntegerComparisonBuilder.build(clauses.getBathrooms()))
                           .setGarages(IntegerComparisonBuilder.build(clauses.getGarages()))
                           .setSellPrice(FloatComparisonBuilder.build(clauses.getSellPrice()))
                           .setRentPrice(FloatComparisonBuilder.build(clauses.getRentPrice()))
                           .setCondominiumPrice(FloatComparisonBuilder.build(clauses.getCondominiumPrice()))
                           .setCondominiumName(StringComparisonBuilder.build(clauses.getCondominiumName()))
                           .setExchangeable(BooleanComparisonBuilder.build(clauses.getExchangeable()))
                           .setStatus(UrbanPropertyStatusComparisonBuilder.build(clauses.getStatus()))
                           .setFinanceable(BooleanComparisonBuilder.build(clauses.getFinanceable()))
                           .setOccupied(BooleanComparisonBuilder.build(clauses.getOccupied()))
                           .setNotes(StringComparisonBuilder.build(clauses.getNotes()))
                           .setCreationDate(DateTimeComparisonBuilder.build(clauses.getCreationDate()))
                           .setLastAnalysisDate(DateTimeComparisonBuilder.build(clauses.getLastAnalysisDate()))
                           .setExclusionDate(DateTimeComparisonBuilder.build(clauses.getExclusionDate()))
                           .setAnalyzable(BooleanComparisonBuilder.build(clauses.getAnalyzable()))
                           .build()
        );
    }

    private void configureOrders(UrbanPropertyInput.Orders orders) {
        builder.setOrders(
                OrdersData.newBuilder()
                          .setId(OrderDetailBuilder.build(orders.getId()))
                          .setProviderId(OrderDetailBuilder.build(orders.getProviderId()))
                          .setProviderCode(OrderDetailBuilder.build(orders.getProviderCode()))
                          .setUrl(OrderDetailBuilder.build(orders.getUrl()))
                          .setContract(OrderDetailBuilder.build(orders.getContract()))
                          .setType(OrderDetailBuilder.build(orders.getType()))
                          .setSubType(OrderDetailBuilder.build(orders.getSubType()))
                          .setDormitories(OrderDetailBuilder.build(orders.getDormitories()))
                          .setSuites(OrderDetailBuilder.build(orders.getSuites()))
                          .setBathrooms(OrderDetailBuilder.build(orders.getBathrooms()))
                          .setGarages(OrderDetailBuilder.build(orders.getGarages()))
                          .setSellPrice(OrderDetailBuilder.build(orders.getSellPrice()))
                          .setRentPrice(OrderDetailBuilder.build(orders.getRentPrice()))
                          .setCondominiumPrice(OrderDetailBuilder.build(orders.getCondominiumPrice()))
                          .setCondominiumName(OrderDetailBuilder.build(orders.getCondominiumName()))
                          .setExchangeable(OrderDetailBuilder.build(orders.getExchangeable()))
                          .setStatus(OrderDetailBuilder.build(orders.getStatus()))
                          .setFinanceable(OrderDetailBuilder.build(orders.getFinanceable()))
                          .setOccupied(OrderDetailBuilder.build(orders.getOccupied()))
                          .setNotes(OrderDetailBuilder.build(orders.getNotes()))
                          .setCreationDate(OrderDetailBuilder.build(orders.getCreationDate()))
                          .setLastAnalysisDate(OrderDetailBuilder.build(orders.getLastAnalysisDate()))
                          .setExclusionDate(OrderDetailBuilder.build(orders.getExclusionDate()))
                          .setAnalyzable(OrderDetailBuilder.build(orders.getAnalyzable()))
                          .build()
        );
    }

    private void configurePagination(PaginationInput pagination) {
        builder.setPagination(PaginationBuilder.build(pagination));
    }

}

package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.argument.ProviderInput;
import br.com.houseseeker.domain.input.PaginationInput;
import br.com.houseseeker.domain.proto.Int32ComparisonData;
import br.com.houseseeker.domain.proto.Int32SingleComparisonData;
import br.com.houseseeker.service.proto.GetProvidersDataRequest;
import br.com.houseseeker.service.proto.GetProvidersDataRequest.ClausesData;
import br.com.houseseeker.service.proto.GetProvidersDataRequest.OrdersData;
import br.com.houseseeker.service.proto.GetProvidersDataRequest.ProjectionsData;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.nonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProvidersDataRequestBuilder implements DataRequestBuildable<ProvidersDataRequestBuilder, ProviderInput, GetProvidersDataRequest> {

    private final GetProvidersDataRequest.Builder builder = GetProvidersDataRequest.newBuilder();

    public static ProvidersDataRequestBuilder newInstance() {
        return new ProvidersDataRequestBuilder();
    }

    public ProvidersDataRequestBuilder byId(int id) {
        withProjections(Collections.emptySet());

        builder.setClauses(
                ClausesData.newBuilder()
                           .setId(
                                   Int32ComparisonData.newBuilder()
                                                      .setIsEqual(
                                                              Int32SingleComparisonData.newBuilder()
                                                                                       .setValue(id)
                                                                                       .build()
                                                      )
                                                      .build()
                           )
                           .build()
        );

        return this;
    }

    @Override
    public ProvidersDataRequestBuilder withProjections(@NotNull Set<String> projections) {
        boolean allProjectionsSelected = CollectionUtils.isEmpty(projections);

        builder.setProjections(
                ProjectionsData.newBuilder()
                               .setId(allProjectionsSelected || projections.contains("rows/id"))
                               .setName(allProjectionsSelected || projections.contains("rows/name"))
                               .setSiteUrl(allProjectionsSelected || projections.contains("rows/siteUrl"))
                               .setDataUrl(allProjectionsSelected || projections.contains("rows/dataUrl"))
                               .setMechanism(allProjectionsSelected || projections.contains("rows/mechanism"))
                               .setParams(allProjectionsSelected || projections.contains("rows/params"))
                               .setCronExpression(allProjectionsSelected || projections.contains("rows/cronExpression"))
                               .setLogo(allProjectionsSelected || projections.contains("rows/logoUrl"))
                               .setActive(allProjectionsSelected || projections.contains("rows/active"))
                               .build()
        );

        return this;
    }

    @Override
    public ProvidersDataRequestBuilder withInput(@Nullable ProviderInput input) {
        if (nonNull(input)) {
            Optional.ofNullable(input.getClauses()).ifPresent(this::configureClauses);
            Optional.ofNullable(input.getOrders()).ifPresent(this::configureOrders);
            Optional.ofNullable(input.getPagination()).ifPresent(this::configurePagination);
        }

        return this;
    }

    @Override
    public GetProvidersDataRequest build() {
        return builder.build();
    }

    private void configureClauses(ProviderInput.Clauses clauses) {
        builder.setClauses(
                ClausesData.newBuilder()
                           .setId(IntegerComparisonBuilder.build(clauses.getId()))
                           .setName(StringComparisonBuilder.build(clauses.getName()))
                           .setSiteUrl(StringComparisonBuilder.build(clauses.getSiteUrl()))
                           .setDataUrl(StringComparisonBuilder.build(clauses.getDataUrl()))
                           .setMechanism(ProviderMechanismComparisonBuilder.build(clauses.getMechanism()))
                           .setParams(StringComparisonBuilder.build(clauses.getParams()))
                           .setCronExpression(StringComparisonBuilder.build(clauses.getCronExpression()))
                           .setLogo(BytesComparisonBuilder.build(clauses.getLogo()))
                           .setActive(BooleanComparisonBuilder.build(clauses.getActive()))
                           .build()
        );
    }

    private void configureOrders(ProviderInput.Orders orders) {
        builder.setOrders(
                OrdersData.newBuilder()
                          .setId(OrderDetailBuilder.build(orders.getId()))
                          .setName(OrderDetailBuilder.build(orders.getName()))
                          .setSiteUrl(OrderDetailBuilder.build(orders.getSiteUrl()))
                          .setDataUrl(OrderDetailBuilder.build(orders.getDataUrl()))
                          .setMechanism(OrderDetailBuilder.build(orders.getMechanism()))
                          .setActive(OrderDetailBuilder.build(orders.getActive()))
                          .build()
        );
    }

    private void configurePagination(PaginationInput pagination) {
        builder.setPagination(PaginationBuilder.build(pagination));
    }

}

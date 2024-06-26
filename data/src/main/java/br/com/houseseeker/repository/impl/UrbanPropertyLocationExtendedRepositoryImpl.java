package br.com.houseseeker.repository.impl;

import br.com.houseseeker.entity.UrbanPropertyLocation;
import br.com.houseseeker.repository.UrbanPropertyLocationExtendedRepository;
import br.com.houseseeker.repository.builder.ExpressionBuilder;
import br.com.houseseeker.repository.builder.OrderBuilder;
import br.com.houseseeker.repository.builder.PredicateBuilder;
import br.com.houseseeker.service.proto.GetUrbanPropertyLocationsRequest;
import br.com.houseseeker.util.PaginationUtils;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import static br.com.houseseeker.entity.QDslUrbanPropertyLocation.urbanPropertyLocation;

@Repository
@RequiredArgsConstructor
public class UrbanPropertyLocationExtendedRepositoryImpl implements UrbanPropertyLocationExtendedRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<UrbanPropertyLocation> findBy(@NotNull GetUrbanPropertyLocationsRequest request) {
        return PaginationUtils.collectPaginationMetadata(
                configureBaseQuery(request).fetch(),
                request.getPagination(),
                () -> configureCountQuery(request).fetchFirst()
        );
    }

    private JPAQuery<UrbanPropertyLocation> configureBaseQuery(GetUrbanPropertyLocationsRequest request) {
        JPAQuery<UrbanPropertyLocation> query = jpaQueryFactory.select(
                                                                       Projections.bean(
                                                                               UrbanPropertyLocation.class,
                                                                               buildProjectionExpressions(request.getProjections())
                                                                       )
                                                               )
                                                               .from(urbanPropertyLocation)
                                                               .innerJoin(urbanPropertyLocation.urbanProperty)
                                                               .where(buildWherePredicates(request.getClauses()))
                                                               .orderBy(buildOrderSpecifiers(request.getOrders()));
        PaginationUtils.paginateQuery(query, request.getPagination());
        return query;
    }

    private JPAQuery<Long> configureCountQuery(GetUrbanPropertyLocationsRequest request) {
        return jpaQueryFactory.select(urbanPropertyLocation.count())
                              .from(urbanPropertyLocation)
                              .where(buildWherePredicates(request.getClauses()));
    }

    private Expression<?>[] buildProjectionExpressions(GetUrbanPropertyLocationsRequest.ProjectionsData projections) {
        return ExpressionBuilder.newInstance()
                                .append(urbanPropertyLocation.id, projections.getId())
                                .append(urbanPropertyLocation.urbanProperty, projections.getUrbanProperty())
                                .append(urbanPropertyLocation.state, projections.getState())
                                .append(urbanPropertyLocation.city, projections.getCity())
                                .append(urbanPropertyLocation.district, projections.getDistrict())
                                .append(urbanPropertyLocation.zipCode, projections.getZipCode())
                                .append(urbanPropertyLocation.streetName, projections.getStreetName())
                                .append(urbanPropertyLocation.streetNumber, projections.getStreetNumber())
                                .append(urbanPropertyLocation.complement, projections.getComplement())
                                .append(urbanPropertyLocation.latitude, projections.getLatitude())
                                .append(urbanPropertyLocation.longitude, projections.getLongitude())
                                .build();
    }

    private Predicate[] buildWherePredicates(GetUrbanPropertyLocationsRequest.ClausesData clauses) {
        return PredicateBuilder.newInstance()
                               .append(urbanPropertyLocation.id, clauses.getId())
                               .append(urbanPropertyLocation.urbanProperty.id, clauses.getUrbanPropertyId())
                               .append(urbanPropertyLocation.state, clauses.getState())
                               .append(urbanPropertyLocation.city, clauses.getCity())
                               .append(urbanPropertyLocation.district, clauses.getDistrict())
                               .append(urbanPropertyLocation.zipCode, clauses.getZipCode())
                               .append(urbanPropertyLocation.streetName, clauses.getStreetName())
                               .append(urbanPropertyLocation.streetNumber, clauses.getStreetNumber())
                               .append(urbanPropertyLocation.complement, clauses.getComplement())
                               .append(urbanPropertyLocation.latitude, clauses.getLatitude())
                               .append(urbanPropertyLocation.longitude, clauses.getLongitude())
                               .build();
    }

    private OrderSpecifier<?>[] buildOrderSpecifiers(GetUrbanPropertyLocationsRequest.OrdersData orders) {
        return OrderBuilder.newInstance()
                           .append(urbanPropertyLocation.id, orders.getId())
                           .append(urbanPropertyLocation.urbanProperty.id, orders.getUrbanPropertyId())
                           .append(urbanPropertyLocation.state, orders.getState())
                           .append(urbanPropertyLocation.city, orders.getCity())
                           .append(urbanPropertyLocation.district, orders.getDistrict())
                           .append(urbanPropertyLocation.zipCode, orders.getZipCode())
                           .append(urbanPropertyLocation.streetName, orders.getStreetName())
                           .append(urbanPropertyLocation.streetNumber, orders.getStreetNumber())
                           .append(urbanPropertyLocation.complement, orders.getComplement())
                           .append(urbanPropertyLocation.latitude, orders.getLatitude())
                           .append(urbanPropertyLocation.longitude, orders.getLongitude())
                           .build();
    }

}

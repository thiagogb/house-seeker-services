package br.com.houseseeker.repository.impl;

import br.com.houseseeker.entity.UrbanPropertyPriceVariation;
import br.com.houseseeker.repository.UrbanPropertyPriceVariationExtendedRepository;
import br.com.houseseeker.repository.builder.ExpressionBuilder;
import br.com.houseseeker.repository.builder.OrderBuilder;
import br.com.houseseeker.repository.builder.PredicateBuilder;
import br.com.houseseeker.service.proto.GetUrbanPropertyPriceVariationsRequest;
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

import static br.com.houseseeker.entity.QDslUrbanPropertyPriceVariation.urbanPropertyPriceVariation;

@Repository
@RequiredArgsConstructor
public class UrbanPropertyPriceVariationExtendedRepositoryImpl implements UrbanPropertyPriceVariationExtendedRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<UrbanPropertyPriceVariation> findBy(@NotNull GetUrbanPropertyPriceVariationsRequest request) {
        return PaginationUtils.collectPaginationMetadata(
                configureBaseQuery(request).fetch(),
                request.getPagination(),
                () -> configureCountQuery(request).fetchFirst()
        );
    }

    private JPAQuery<UrbanPropertyPriceVariation> configureBaseQuery(GetUrbanPropertyPriceVariationsRequest request) {
        JPAQuery<UrbanPropertyPriceVariation> query = jpaQueryFactory.select(
                                                                             Projections.bean(
                                                                                     UrbanPropertyPriceVariation.class,
                                                                                     buildProjectionExpressions(request.getProjections())
                                                                             )
                                                                     )
                                                                     .from(urbanPropertyPriceVariation)
                                                                     .innerJoin(urbanPropertyPriceVariation.urbanProperty)
                                                                     .where(buildWherePredicates(request.getClauses()))
                                                                     .orderBy(buildOrderSpecifiers(request.getOrders()));
        PaginationUtils.paginateQuery(query, request.getPagination());
        return query;
    }

    private JPAQuery<Long> configureCountQuery(GetUrbanPropertyPriceVariationsRequest request) {
        return jpaQueryFactory.select(urbanPropertyPriceVariation.count())
                              .from(urbanPropertyPriceVariation)
                              .where(buildWherePredicates(request.getClauses()));
    }

    private Expression<?>[] buildProjectionExpressions(GetUrbanPropertyPriceVariationsRequest.ProjectionsData projections) {
        return ExpressionBuilder.newInstance()
                                .append(urbanPropertyPriceVariation.id, projections.getId())
                                .append(urbanPropertyPriceVariation.urbanProperty, projections.getUrbanProperty())
                                .append(urbanPropertyPriceVariation.analysisDate, projections.getAnalysisDate())
                                .append(urbanPropertyPriceVariation.type, projections.getType())
                                .append(urbanPropertyPriceVariation.price, projections.getPrice())
                                .append(urbanPropertyPriceVariation.variation, projections.getVariation())
                                .build();
    }

    private Predicate[] buildWherePredicates(GetUrbanPropertyPriceVariationsRequest.ClausesData clauses) {
        return PredicateBuilder.newInstance()
                               .append(urbanPropertyPriceVariation.id, clauses.getId())
                               .append(urbanPropertyPriceVariation.urbanProperty.id, clauses.getUrbanPropertyId())
                               .append(urbanPropertyPriceVariation.analysisDate, clauses.getAnalysisDate())
                               .append(urbanPropertyPriceVariation.type, clauses.getType(), UrbanPropertyPriceVariation.Type::valueOf)
                               .append(urbanPropertyPriceVariation.price, clauses.getPrice())
                               .append(urbanPropertyPriceVariation.variation, clauses.getVariation())
                               .build();
    }

    private OrderSpecifier<?>[] buildOrderSpecifiers(GetUrbanPropertyPriceVariationsRequest.OrdersData orders) {
        return OrderBuilder.newInstance()
                           .append(urbanPropertyPriceVariation.id, orders.getId())
                           .append(urbanPropertyPriceVariation.urbanProperty.id, orders.getUrbanPropertyId())
                           .append(urbanPropertyPriceVariation.analysisDate, orders.getAnalysisDate())
                           .append(urbanPropertyPriceVariation.type, orders.getType())
                           .append(urbanPropertyPriceVariation.price, orders.getPrice())
                           .append(urbanPropertyPriceVariation.variation, orders.getVariation())
                           .build();
    }

}

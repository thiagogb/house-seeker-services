package br.com.houseseeker.repository.impl;

import br.com.houseseeker.entity.UrbanPropertyMeasure;
import br.com.houseseeker.repository.UrbanPropertyMeasureExtendedRepository;
import br.com.houseseeker.repository.builder.ExpressionBuilder;
import br.com.houseseeker.repository.builder.OrderBuilder;
import br.com.houseseeker.repository.builder.PredicateBuilder;
import br.com.houseseeker.service.proto.GetUrbanPropertyMeasuresRequest;
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

import static br.com.houseseeker.entity.QDslUrbanPropertyMeasure.urbanPropertyMeasure;

@Repository
@RequiredArgsConstructor
public class UrbanPropertyMeasureExtendedRepositoryImpl implements UrbanPropertyMeasureExtendedRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<UrbanPropertyMeasure> findBy(@NotNull GetUrbanPropertyMeasuresRequest request) {
        return PaginationUtils.collectPaginationMetadata(
                configureBaseQuery(request).fetch(),
                request.getPagination(),
                () -> configureCountQuery(request).fetchFirst()
        );
    }

    private JPAQuery<UrbanPropertyMeasure> configureBaseQuery(GetUrbanPropertyMeasuresRequest request) {
        JPAQuery<UrbanPropertyMeasure> query = jpaQueryFactory.select(
                                                                      Projections.bean(
                                                                              UrbanPropertyMeasure.class,
                                                                              buildProjectionExpressions(request.getProjections())
                                                                      )
                                                              )
                                                              .from(urbanPropertyMeasure)
                                                              .innerJoin(urbanPropertyMeasure.urbanProperty)
                                                              .where(buildWherePredicates(request.getClauses()))
                                                              .orderBy(buildOrderSpecifiers(request.getOrders()));
        PaginationUtils.paginateQuery(query, request.getPagination());
        return query;
    }

    private JPAQuery<Long> configureCountQuery(GetUrbanPropertyMeasuresRequest request) {
        return jpaQueryFactory.select(urbanPropertyMeasure.count())
                              .from(urbanPropertyMeasure)
                              .where(buildWherePredicates(request.getClauses()));
    }

    private Expression<?>[] buildProjectionExpressions(GetUrbanPropertyMeasuresRequest.ProjectionsData projections) {
        return ExpressionBuilder.newInstance()
                                .append(urbanPropertyMeasure.id, projections.getId())
                                .append(urbanPropertyMeasure.urbanProperty, projections.getUrbanProperty())
                                .append(urbanPropertyMeasure.totalArea, projections.getTotalArea())
                                .append(urbanPropertyMeasure.privateArea, projections.getPrivateArea())
                                .append(urbanPropertyMeasure.usableArea, projections.getUsableArea())
                                .append(urbanPropertyMeasure.terrainTotalArea, projections.getTerrainTotalArea())
                                .append(urbanPropertyMeasure.terrainFront, projections.getTerrainFront())
                                .append(urbanPropertyMeasure.terrainBack, projections.getTerrainBack())
                                .append(urbanPropertyMeasure.terrainLeft, projections.getTerrainLeft())
                                .append(urbanPropertyMeasure.terrainRight, projections.getTerrainRight())
                                .append(urbanPropertyMeasure.areaUnit, projections.getAreaUnit())
                                .build();
    }

    private Predicate[] buildWherePredicates(GetUrbanPropertyMeasuresRequest.ClausesData clauses) {
        return PredicateBuilder.newInstance()
                               .append(urbanPropertyMeasure.id, clauses.getId())
                               .append(urbanPropertyMeasure.urbanProperty.id, clauses.getUrbanPropertyId())
                               .append(urbanPropertyMeasure.totalArea, clauses.getTotalArea())
                               .append(urbanPropertyMeasure.privateArea, clauses.getPrivateArea())
                               .append(urbanPropertyMeasure.usableArea, clauses.getUsableArea())
                               .append(urbanPropertyMeasure.terrainTotalArea, clauses.getTerrainTotalArea())
                               .append(urbanPropertyMeasure.terrainFront, clauses.getTerrainFront())
                               .append(urbanPropertyMeasure.terrainBack, clauses.getTerrainBack())
                               .append(urbanPropertyMeasure.terrainLeft, clauses.getTerrainLeft())
                               .append(urbanPropertyMeasure.terrainRight, clauses.getTerrainRight())
                               .append(urbanPropertyMeasure.areaUnit, clauses.getAreaUnit())
                               .build();
    }

    private OrderSpecifier<?>[] buildOrderSpecifiers(GetUrbanPropertyMeasuresRequest.OrdersData orders) {
        return OrderBuilder.newInstance()
                           .append(urbanPropertyMeasure.id, orders.getId())
                           .append(urbanPropertyMeasure.urbanProperty.id, orders.getUrbanPropertyId())
                           .append(urbanPropertyMeasure.totalArea, orders.getTotalArea())
                           .append(urbanPropertyMeasure.privateArea, orders.getPrivateArea())
                           .append(urbanPropertyMeasure.usableArea, orders.getUsableArea())
                           .append(urbanPropertyMeasure.terrainTotalArea, orders.getTerrainTotalArea())
                           .append(urbanPropertyMeasure.terrainFront, orders.getTerrainFront())
                           .append(urbanPropertyMeasure.terrainBack, orders.getTerrainBack())
                           .append(urbanPropertyMeasure.terrainLeft, orders.getTerrainLeft())
                           .append(urbanPropertyMeasure.terrainRight, orders.getTerrainRight())
                           .append(urbanPropertyMeasure.areaUnit, orders.getAreaUnit())
                           .build();
    }

}

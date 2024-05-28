package br.com.houseseeker.repository.impl;

import br.com.houseseeker.entity.UrbanPropertyConvenience;
import br.com.houseseeker.repository.UrbanPropertyConvenienceExtendedRepository;
import br.com.houseseeker.repository.builder.ExpressionBuilder;
import br.com.houseseeker.repository.builder.OrderBuilder;
import br.com.houseseeker.repository.builder.PredicateBuilder;
import br.com.houseseeker.service.proto.GetUrbanPropertyConveniencesRequest;
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

import static br.com.houseseeker.entity.QDslUrbanPropertyConvenience.urbanPropertyConvenience;

@Repository
@RequiredArgsConstructor
public class UrbanPropertyConvenienceExtendedRepositoryImpl implements UrbanPropertyConvenienceExtendedRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<UrbanPropertyConvenience> findBy(@NotNull GetUrbanPropertyConveniencesRequest request) {
        return PaginationUtils.collectPaginationMetadata(
                configureBaseQuery(request).fetch(),
                request.getPagination(),
                () -> configureCountQuery(request).fetchFirst()
        );
    }

    private JPAQuery<UrbanPropertyConvenience> configureBaseQuery(GetUrbanPropertyConveniencesRequest request) {
        JPAQuery<UrbanPropertyConvenience> query = jpaQueryFactory.select(
                                                                          Projections.bean(
                                                                                  UrbanPropertyConvenience.class,
                                                                                  buildProjectionExpressions(request.getProjections())
                                                                          )
                                                                  )
                                                                  .from(urbanPropertyConvenience)
                                                                  .innerJoin(urbanPropertyConvenience.urbanProperty)
                                                                  .where(buildWherePredicates(request.getClauses()))
                                                                  .orderBy(buildOrderSpecifiers(request.getOrders()));
        PaginationUtils.paginateQuery(query, request.getPagination());
        return query;
    }

    private JPAQuery<Long> configureCountQuery(GetUrbanPropertyConveniencesRequest request) {
        return jpaQueryFactory.select(urbanPropertyConvenience.count())
                              .from(urbanPropertyConvenience)
                              .where(buildWherePredicates(request.getClauses()));
    }

    private Expression<?>[] buildProjectionExpressions(GetUrbanPropertyConveniencesRequest.ProjectionsData projections) {
        return ExpressionBuilder.newInstance()
                                .append(urbanPropertyConvenience.id, projections.getId())
                                .append(urbanPropertyConvenience.urbanProperty, projections.getUrbanProperty())
                                .append(urbanPropertyConvenience.description, projections.getDescription())
                                .build();
    }

    private Predicate[] buildWherePredicates(GetUrbanPropertyConveniencesRequest.ClausesData clauses) {
        return PredicateBuilder.newInstance()
                               .append(urbanPropertyConvenience.id, clauses.getId())
                               .append(urbanPropertyConvenience.urbanProperty.id, clauses.getUrbanPropertyId())
                               .append(urbanPropertyConvenience.description, clauses.getDescription())
                               .build();
    }

    private OrderSpecifier<?>[] buildOrderSpecifiers(GetUrbanPropertyConveniencesRequest.OrdersData orders) {
        return OrderBuilder.newInstance()
                           .append(urbanPropertyConvenience.id, orders.getId())
                           .append(urbanPropertyConvenience.urbanProperty.id, orders.getUrbanPropertyId())
                           .append(urbanPropertyConvenience.description, orders.getDescription())
                           .build();
    }

}

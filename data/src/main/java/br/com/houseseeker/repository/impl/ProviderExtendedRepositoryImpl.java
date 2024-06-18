package br.com.houseseeker.repository.impl;

import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.repository.ProviderExtendedRepository;
import br.com.houseseeker.repository.builder.ExpressionBuilder;
import br.com.houseseeker.repository.builder.OrderBuilder;
import br.com.houseseeker.repository.builder.PredicateBuilder;
import br.com.houseseeker.service.proto.GetProvidersDataRequest;
import br.com.houseseeker.service.proto.GetProvidersDataRequest.ClausesData;
import br.com.houseseeker.service.proto.GetProvidersDataRequest.OrdersData;
import br.com.houseseeker.service.proto.GetProvidersDataRequest.ProjectionsData;
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

import static br.com.houseseeker.entity.QDslProvider.provider;

@Repository
@RequiredArgsConstructor
public class ProviderExtendedRepositoryImpl implements ProviderExtendedRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Provider> findBy(@NotNull GetProvidersDataRequest request) {
        return PaginationUtils.collectPaginationMetadata(
                configureBaseQuery(request).fetch(),
                request.getPagination(),
                () -> configureCountQuery(request).fetchFirst()
        );
    }

    private JPAQuery<Provider> configureBaseQuery(GetProvidersDataRequest request) {
        JPAQuery<Provider> query = jpaQueryFactory.select(
                                                          Projections.bean(
                                                                  Provider.class,
                                                                  buildProjectionExpressions(request.getProjections())
                                                          )
                                                  )
                                                  .from(provider)
                                                  .where(buildWherePredicates(request))
                                                  .orderBy(buildOrderSpecifiers(request.getOrders()));
        PaginationUtils.paginateQuery(query, request.getPagination());
        return query;
    }

    private JPAQuery<Long> configureCountQuery(GetProvidersDataRequest request) {
        return jpaQueryFactory.select(provider.count())
                              .from(provider)
                              .where(buildWherePredicates(request));
    }

    private Expression<?>[] buildProjectionExpressions(ProjectionsData projections) {
        return ExpressionBuilder.newInstance()
                                .append(provider.id, projections.getId())
                                .append(provider.name, projections.getName())
                                .append(provider.siteUrl, projections.getSiteUrl())
                                .append(provider.dataUrl, projections.getDataUrl())
                                .append(provider.mechanism, projections.getMechanism())
                                .append(provider.params, projections.getParams())
                                .append(provider.cronExpression, projections.getCronExpression())
                                .append(provider.logo, projections.getLogo())
                                .append(provider.active, projections.getActive())
                                .build();
    }

    private Predicate[] buildWherePredicates(GetProvidersDataRequest request) {
        return PredicateBuilder.newInstance()
                               .append(
                                       request.getClausesList(),
                                       this::buildWherePredicates,
                                       GetProvidersDataRequest.ClausesData::getInnerOperator,
                                       GetProvidersDataRequest.ClausesData::getOuterOperator
                               )
                               .build();
    }

    private Predicate[] buildWherePredicates(ClausesData clauses) {
        return PredicateBuilder.newInstance()
                               .append(provider.id, clauses.getId())
                               .append(provider.name, clauses.getName())
                               .append(provider.siteUrl, clauses.getSiteUrl())
                               .append(provider.dataUrl, clauses.getDataUrl())
                               .append(provider.mechanism, clauses.getMechanism(), ProviderMechanism::valueOf)
                               .append(provider.cronExpression, clauses.getCronExpression())
                               .append(provider.params, clauses.getParams())
                               .append(provider.logo, clauses.getLogo())
                               .append(provider.active, clauses.getActive())
                               .build();
    }

    private OrderSpecifier<?>[] buildOrderSpecifiers(OrdersData orders) {
        return OrderBuilder.newInstance()
                           .append(provider.id, orders.getId())
                           .append(provider.name, orders.getName())
                           .append(provider.siteUrl, orders.getSiteUrl())
                           .append(provider.dataUrl, orders.getDataUrl())
                           .append(provider.mechanism, orders.getMechanism())
                           .append(provider.active, orders.getActive())
                           .build();
    }

}

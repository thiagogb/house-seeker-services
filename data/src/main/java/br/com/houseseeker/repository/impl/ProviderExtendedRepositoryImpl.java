package br.com.houseseeker.repository.impl;

import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.QDslProvider;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProviderExtendedRepositoryImpl implements ProviderExtendedRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Provider> findBy(GetProvidersDataRequest getProvidersDataRequest) {
        return PaginationUtils.collectPaginationMetadata(
                configureBaseQuery(getProvidersDataRequest).fetch(),
                getProvidersDataRequest.getPagination(),
                () -> configureCountQuery(getProvidersDataRequest).fetchFirst()
        );
    }

    private JPAQuery<Provider> configureBaseQuery(GetProvidersDataRequest getProvidersDataRequest) {
        JPAQuery<Provider> query = jpaQueryFactory.select(
                                                          Projections.bean(
                                                                  Provider.class,
                                                                  buildProjectionExpressions(getProvidersDataRequest.getProjections())
                                                          )
                                                  )
                                                  .from(QDslProvider.provider)
                                                  .where(buildWherePredicates(getProvidersDataRequest.getClauses()))
                                                  .orderBy(buildOrderSpecifiers(getProvidersDataRequest.getOrders()));
        PaginationUtils.paginateQuery(query, getProvidersDataRequest.getPagination());
        return query;
    }

    private JPAQuery<Long> configureCountQuery(GetProvidersDataRequest getProvidersDataRequest) {
        return jpaQueryFactory.select(QDslProvider.provider.count())
                              .from(QDslProvider.provider)
                              .where(buildWherePredicates(getProvidersDataRequest.getClauses()));
    }

    private Expression<?>[] buildProjectionExpressions(ProjectionsData projectionsData) {
        return ExpressionBuilder.newInstance()
                                .append(QDslProvider.provider.id, projectionsData.getId())
                                .append(QDslProvider.provider.name, projectionsData.getName())
                                .append(QDslProvider.provider.siteUrl, projectionsData.getSiteUrl())
                                .append(QDslProvider.provider.dataUrl, projectionsData.getDataUrl())
                                .append(QDslProvider.provider.mechanism, projectionsData.getMechanism())
                                .append(QDslProvider.provider.params, projectionsData.getParams())
                                .append(QDslProvider.provider.cronExpression, projectionsData.getCronExpression())
                                .append(QDslProvider.provider.logo, projectionsData.getLogo())
                                .append(QDslProvider.provider.active, projectionsData.getActive())
                                .build();
    }

    private Predicate[] buildWherePredicates(ClausesData clausesData) {
        return PredicateBuilder.newInstance()
                               .append(QDslProvider.provider.id, clausesData.getId())
                               .append(QDslProvider.provider.name, clausesData.getName())
                               .append(QDslProvider.provider.siteUrl, clausesData.getSiteUrl())
                               .append(QDslProvider.provider.dataUrl, clausesData.getDataUrl())
                               .append(QDslProvider.provider.mechanism, clausesData.getMechanism(), ProviderMechanism::valueOf)
                               .append(QDslProvider.provider.cronExpression, clausesData.getCronExpression())
                               .append(QDslProvider.provider.params, clausesData.getParams())
                               .append(QDslProvider.provider.logo, clausesData.getLogo())
                               .append(QDslProvider.provider.active, clausesData.getActive())
                               .build();
    }

    private OrderSpecifier<?>[] buildOrderSpecifiers(OrdersData ordersData) {
        return OrderBuilder.newInstance()
                           .append(QDslProvider.provider.id, ordersData.getId())
                           .append(QDslProvider.provider.name, ordersData.getName())
                           .append(QDslProvider.provider.siteUrl, ordersData.getSiteUrl())
                           .append(QDslProvider.provider.dataUrl, ordersData.getDataUrl())
                           .append(QDslProvider.provider.mechanism, ordersData.getMechanism())
                           .append(QDslProvider.provider.active, ordersData.getActive())
                           .build();
    }

}

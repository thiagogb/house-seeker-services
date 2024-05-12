package br.com.houseseeker.repository.impl;

import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.QProvider;
import br.com.houseseeker.repository.ProviderExtendedRepository;
import br.com.houseseeker.repository.builder.ExpressionBuilder;
import br.com.houseseeker.repository.builder.OrderBuilder;
import br.com.houseseeker.repository.builder.PredicateBuilder;
import br.com.houseseeker.service.proto.GetProvidersDataRequest;
import br.com.houseseeker.service.proto.GetProvidersDataRequest.ClausesData;
import br.com.houseseeker.service.proto.GetProvidersDataRequest.OrdersData;
import br.com.houseseeker.service.proto.GetProvidersDataRequest.ProjectionsData;
import br.com.houseseeker.util.PaginationUtils;
import br.com.houseseeker.util.QueryDslUtils;
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
                configureQuery(getProvidersDataRequest).fetch(),
                getProvidersDataRequest.getPagination(),
                () -> QueryDslUtils.count(jpaQueryFactory, QProvider.provider)
        );
    }

    private JPAQuery<Provider> configureQuery(GetProvidersDataRequest getProvidersDataRequest) {
        JPAQuery<Provider> query = jpaQueryFactory.select(
                                                          Projections.bean(
                                                                  Provider.class,
                                                                  buildProjectionExpressions(getProvidersDataRequest.getProjections())
                                                          )
                                                  )
                                                  .from(QProvider.provider)
                                                  .where(buildWherePredicates(getProvidersDataRequest.getClauses()))
                                                  .orderBy(buildOrderSpecifiers(getProvidersDataRequest.getOrders()));
        PaginationUtils.paginateQuery(query, getProvidersDataRequest.getPagination());
        return query;
    }

    private Expression<?>[] buildProjectionExpressions(ProjectionsData projectionsData) {
        return ExpressionBuilder.newInstance()
                                .append(QProvider.provider.id, projectionsData.getId())
                                .append(QProvider.provider.name, projectionsData.getName())
                                .append(QProvider.provider.siteUrl, projectionsData.getSiteUrl())
                                .append(QProvider.provider.dataUrl, projectionsData.getDataUrl())
                                .append(QProvider.provider.mechanism, projectionsData.getMechanism())
                                .append(QProvider.provider.params, projectionsData.getParams())
                                .append(QProvider.provider.cronExpression, projectionsData.getCronExpression())
                                .append(QProvider.provider.logo, projectionsData.getLogo())
                                .append(QProvider.provider.active, projectionsData.getActive())
                                .build();
    }

    private Predicate[] buildWherePredicates(ClausesData clausesData) {
        return PredicateBuilder.newInstance()
                               .append(QProvider.provider.id, clausesData.getId())
                               .append(QProvider.provider.name, clausesData.getName())
                               .append(QProvider.provider.siteUrl, clausesData.getSiteUrl())
                               .append(QProvider.provider.dataUrl, clausesData.getDataUrl())
                               .append(QProvider.provider.mechanism, clausesData.getMechanism(), ProviderMechanism::valueOf)
                               .append(QProvider.provider.cronExpression, clausesData.getCronExpression())
                               .append(QProvider.provider.logo, clausesData.getLogo())
                               .append(QProvider.provider.active, clausesData.getActive())
                               .build();
    }

    private OrderSpecifier<?>[] buildOrderSpecifiers(OrdersData ordersData) {
        return OrderBuilder.newInstance()
                           .append(QProvider.provider.id, ordersData.getId())
                           .append(QProvider.provider.name, ordersData.getName())
                           .append(QProvider.provider.siteUrl, ordersData.getSiteUrl())
                           .append(QProvider.provider.dataUrl, ordersData.getDataUrl())
                           .append(QProvider.provider.mechanism, ordersData.getMechanism())
                           .append(QProvider.provider.active, ordersData.getActive())
                           .build();
    }

}

package br.com.houseseeker.repository.impl;

import br.com.houseseeker.domain.property.UrbanPropertyContract;
import br.com.houseseeker.domain.property.UrbanPropertyStatus;
import br.com.houseseeker.domain.property.UrbanPropertyType;
import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.repository.UrbanPropertyExtendedRepository;
import br.com.houseseeker.repository.builder.ExpressionBuilder;
import br.com.houseseeker.repository.builder.OrderBuilder;
import br.com.houseseeker.repository.builder.PredicateBuilder;
import br.com.houseseeker.service.proto.GetSubTypesRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertiesRequest;
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

import static br.com.houseseeker.entity.QDslUrbanProperty.urbanProperty;

@Repository
@RequiredArgsConstructor
public class UrbanPropertyExtendedRepositoryImpl implements UrbanPropertyExtendedRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<UrbanProperty> findBy(@NotNull GetUrbanPropertiesRequest request) {
        return PaginationUtils.collectPaginationMetadata(
                configureBaseQuery(request).fetch(),
                request.getPagination(),
                () -> configureCountQuery(request).fetchFirst()
        );
    }

    @Override
    public Page<String> findDistinctSubTypesBy(@NotNull GetSubTypesRequest request) {
        return PaginationUtils.collectPaginationMetadata(
                configureBaseQuery(request).fetch(),
                request.getPagination(),
                () -> configureCountQuery(request).fetchFirst()
        );
    }

    private JPAQuery<UrbanProperty> configureBaseQuery(GetUrbanPropertiesRequest request) {
        JPAQuery<UrbanProperty> query = jpaQueryFactory.select(
                                                               Projections.bean(
                                                                       UrbanProperty.class,
                                                                       buildProjectionExpressions(request.getProjections())
                                                               )
                                                       )
                                                       .from(urbanProperty)
                                                       .innerJoin(urbanProperty.provider)
                                                       .where(buildWherePredicates(request.getClauses()))
                                                       .orderBy(buildOrderSpecifiers(request.getOrders()));
        PaginationUtils.paginateQuery(query, request.getPagination());
        return query;
    }

    private JPAQuery<String> configureBaseQuery(GetSubTypesRequest request) {
        JPAQuery<String> query = jpaQueryFactory.selectDistinct(urbanProperty.subType)
                                                .from(urbanProperty)
                                                .innerJoin(urbanProperty.provider)
                                                .where(buildWherePredicates(request))
                                                .orderBy(buildOrderSpecifiers(request.getOrders()));
        PaginationUtils.paginateQuery(query, request.getPagination());
        return query;
    }

    private JPAQuery<Long> configureCountQuery(GetUrbanPropertiesRequest request) {
        return jpaQueryFactory.select(urbanProperty.count())
                              .from(urbanProperty)
                              .where(buildWherePredicates(request.getClauses()));
    }

    private JPAQuery<Long> configureCountQuery(GetSubTypesRequest request) {
        return jpaQueryFactory.select(urbanProperty.countDistinct())
                              .from(urbanProperty)
                              .where(buildWherePredicates(request));
    }

    private Expression<?>[] buildProjectionExpressions(GetUrbanPropertiesRequest.ProjectionsData projections) {
        return ExpressionBuilder.newInstance()
                                .append(urbanProperty.id, projections.getId())
                                .append(urbanProperty.provider, projections.getProvider())
                                .append(urbanProperty.providerCode, projections.getProviderCode())
                                .append(urbanProperty.url, projections.getUrl())
                                .append(urbanProperty.contract, projections.getContract())
                                .append(urbanProperty.type, projections.getType())
                                .append(urbanProperty.subType, projections.getSubType())
                                .append(urbanProperty.dormitories, projections.getDormitories())
                                .append(urbanProperty.suites, projections.getSuites())
                                .append(urbanProperty.bathrooms, projections.getBathrooms())
                                .append(urbanProperty.garages, projections.getGarages())
                                .append(urbanProperty.sellPrice, projections.getSellPrice())
                                .append(urbanProperty.rentPrice, projections.getRentPrice())
                                .append(urbanProperty.condominiumPrice, projections.getCondominiumPrice())
                                .append(urbanProperty.condominiumName, projections.getCondominiumName())
                                .append(urbanProperty.exchangeable, projections.getExchangeable())
                                .append(urbanProperty.status, projections.getStatus())
                                .append(urbanProperty.financeable, projections.getFinanceable())
                                .append(urbanProperty.occupied, projections.getOccupied())
                                .append(urbanProperty.notes, projections.getNotes())
                                .append(urbanProperty.creationDate, projections.getCreationDate())
                                .append(urbanProperty.lastAnalysisDate, projections.getLastAnalysisDate())
                                .append(urbanProperty.exclusionDate, projections.getExclusionDate())
                                .append(urbanProperty.analyzable, projections.getAnalyzable())
                                .build();
    }

    private Predicate[] buildWherePredicates(GetUrbanPropertiesRequest.ClausesData clauses) {
        return PredicateBuilder.newInstance()
                               .append(urbanProperty.id, clauses.getId())
                               .append(urbanProperty.provider.id, clauses.getProviderId())
                               .append(urbanProperty.providerCode, clauses.getProviderCode())
                               .append(urbanProperty.url, clauses.getUrl())
                               .append(urbanProperty.contract, clauses.getContract(), UrbanPropertyContract::valueOf)
                               .append(urbanProperty.type, clauses.getType(), UrbanPropertyType::valueOf)
                               .append(urbanProperty.subType, clauses.getSubType())
                               .append(urbanProperty.dormitories, clauses.getDormitories())
                               .append(urbanProperty.suites, clauses.getSuites())
                               .append(urbanProperty.bathrooms, clauses.getBathrooms())
                               .append(urbanProperty.garages, clauses.getGarages())
                               .append(urbanProperty.sellPrice, clauses.getSellPrice())
                               .append(urbanProperty.rentPrice, clauses.getRentPrice())
                               .append(urbanProperty.condominiumPrice, clauses.getCondominiumPrice())
                               .append(urbanProperty.condominiumName, clauses.getCondominiumName())
                               .append(urbanProperty.exchangeable, clauses.getExchangeable())
                               .append(urbanProperty.status, clauses.getStatus(), UrbanPropertyStatus::valueOf)
                               .append(urbanProperty.financeable, clauses.getFinanceable())
                               .append(urbanProperty.occupied, clauses.getOccupied())
                               .append(urbanProperty.notes, clauses.getNotes())
                               .append(urbanProperty.creationDate, clauses.getCreationDate())
                               .append(urbanProperty.lastAnalysisDate, clauses.getLastAnalysisDate())
                               .append(urbanProperty.exclusionDate, clauses.getExclusionDate())
                               .append(urbanProperty.analyzable, clauses.getAnalyzable())
                               .build();
    }

    private Predicate[] buildWherePredicates(GetSubTypesRequest request) {
        return PredicateBuilder.newInstance()
                               .append(
                                       request.getClausesList(),
                                       this::buildWherePredicates,
                                       GetSubTypesRequest.ClausesData::getInnerOperator,
                                       GetSubTypesRequest.ClausesData::getOuterOperator
                               )
                               .build();
    }

    private Predicate[] buildWherePredicates(GetSubTypesRequest.ClausesData clauses) {
        return PredicateBuilder.newInstance()
                               .append(urbanProperty.subType, clauses.getSubType())
                               .build();
    }

    private OrderSpecifier<?>[] buildOrderSpecifiers(GetUrbanPropertiesRequest.OrdersData orders) {
        return OrderBuilder.newInstance()
                           .append(urbanProperty.id, orders.getId())
                           .append(urbanProperty.provider.id, orders.getProviderId())
                           .append(urbanProperty.providerCode, orders.getProviderCode())
                           .append(urbanProperty.url, orders.getUrl())
                           .append(urbanProperty.contract, orders.getContract())
                           .append(urbanProperty.type, orders.getType())
                           .append(urbanProperty.subType, orders.getSubType())
                           .append(urbanProperty.dormitories, orders.getDormitories())
                           .append(urbanProperty.suites, orders.getSuites())
                           .append(urbanProperty.bathrooms, orders.getBathrooms())
                           .append(urbanProperty.garages, orders.getGarages())
                           .append(urbanProperty.sellPrice, orders.getSellPrice())
                           .append(urbanProperty.rentPrice, orders.getRentPrice())
                           .append(urbanProperty.condominiumPrice, orders.getCondominiumPrice())
                           .append(urbanProperty.condominiumName, orders.getCondominiumName())
                           .append(urbanProperty.exchangeable, orders.getExchangeable())
                           .append(urbanProperty.status, orders.getStatus())
                           .append(urbanProperty.financeable, orders.getFinanceable())
                           .append(urbanProperty.occupied, orders.getOccupied())
                           .append(urbanProperty.notes, orders.getNotes())
                           .append(urbanProperty.creationDate, orders.getCreationDate())
                           .append(urbanProperty.lastAnalysisDate, orders.getLastAnalysisDate())
                           .append(urbanProperty.exclusionDate, orders.getExclusionDate())
                           .append(urbanProperty.analyzable, orders.getAnalyzable())
                           .build();
    }

    private OrderSpecifier<?>[] buildOrderSpecifiers(GetSubTypesRequest.OrdersData orders) {
        return OrderBuilder.newInstance()
                           .append(urbanProperty.subType, orders.getSubType())
                           .build();
    }

}

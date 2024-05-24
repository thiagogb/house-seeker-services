package br.com.houseseeker.repository.impl;

import br.com.houseseeker.domain.property.UrbanPropertyMediaType;
import br.com.houseseeker.entity.UrbanPropertyMedia;
import br.com.houseseeker.repository.UrbanPropertyMediaExtendedRepository;
import br.com.houseseeker.repository.builder.ExpressionBuilder;
import br.com.houseseeker.repository.builder.OrderBuilder;
import br.com.houseseeker.repository.builder.PredicateBuilder;
import br.com.houseseeker.service.proto.GetUrbanPropertyMediasRequest;
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

import static br.com.houseseeker.entity.QDslUrbanPropertyMedia.urbanPropertyMedia;

@Repository
@RequiredArgsConstructor
public class UrbanPropertyMediaExtendedRepositoryImpl implements UrbanPropertyMediaExtendedRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<UrbanPropertyMedia> findBy(@NotNull GetUrbanPropertyMediasRequest request) {
        return PaginationUtils.collectPaginationMetadata(
                configureBaseQuery(request).fetch(),
                request.getPagination(),
                () -> configureCountQuery(request).fetchFirst()
        );
    }

    private JPAQuery<UrbanPropertyMedia> configureBaseQuery(GetUrbanPropertyMediasRequest request) {
        JPAQuery<UrbanPropertyMedia> query = jpaQueryFactory.select(
                                                                    Projections.bean(
                                                                            UrbanPropertyMedia.class,
                                                                            buildProjectionExpressions(request.getProjections())
                                                                    )
                                                            )
                                                            .from(urbanPropertyMedia)
                                                            .innerJoin(urbanPropertyMedia.urbanProperty)
                                                            .where(buildWherePredicates(request.getClauses()))
                                                            .orderBy(buildOrderSpecifiers(request.getOrders()));
        PaginationUtils.paginateQuery(query, request.getPagination());
        return query;
    }

    private JPAQuery<Long> configureCountQuery(GetUrbanPropertyMediasRequest request) {
        return jpaQueryFactory.select(urbanPropertyMedia.count())
                              .from(urbanPropertyMedia)
                              .where(buildWherePredicates(request.getClauses()));
    }

    private Expression<?>[] buildProjectionExpressions(GetUrbanPropertyMediasRequest.ProjectionsData projections) {
        return ExpressionBuilder.newInstance()
                                .append(urbanPropertyMedia.id, projections.getId())
                                .append(urbanPropertyMedia.urbanProperty, projections.getUrbanProperty())
                                .append(urbanPropertyMedia.link, projections.getLink())
                                .append(urbanPropertyMedia.linkThumb, projections.getLinkThumb())
                                .append(urbanPropertyMedia.mediaType, projections.getMediaType())
                                .append(urbanPropertyMedia.extension, projections.getExtension())
                                .build();
    }

    private Predicate[] buildWherePredicates(GetUrbanPropertyMediasRequest.ClausesData clauses) {
        return PredicateBuilder.newInstance()
                               .append(urbanPropertyMedia.id, clauses.getId())
                               .append(urbanPropertyMedia.urbanProperty.id, clauses.getUrbanPropertyId())
                               .append(urbanPropertyMedia.link, clauses.getLink())
                               .append(urbanPropertyMedia.linkThumb, clauses.getLinkThumb())
                               .append(urbanPropertyMedia.mediaType, clauses.getMediaType(), UrbanPropertyMediaType::valueOf)
                               .append(urbanPropertyMedia.extension, clauses.getExtension())
                               .build();
    }

    private OrderSpecifier<?>[] buildOrderSpecifiers(GetUrbanPropertyMediasRequest.OrdersData orders) {
        return OrderBuilder.newInstance()
                           .append(urbanPropertyMedia.id, orders.getId())
                           .append(urbanPropertyMedia.urbanProperty.id, orders.getUrbanPropertyId())
                           .append(urbanPropertyMedia.link, orders.getLink())
                           .append(urbanPropertyMedia.linkThumb, orders.getLinkThumb())
                           .append(urbanPropertyMedia.mediaType, orders.getMediaType())
                           .append(urbanPropertyMedia.extension, orders.getExtension())
                           .build();
    }

}

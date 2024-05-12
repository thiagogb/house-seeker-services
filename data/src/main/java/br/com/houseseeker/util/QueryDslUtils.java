package br.com.houseseeker.util;

import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class QueryDslUtils {

    public <T> Long count(@NotNull JPAQueryFactory jpaQueryFactory, @NotNull EntityPathBase<T> entityPath) {
        return jpaQueryFactory.select(entityPath.count())
                              .from(entityPath)
                              .fetchFirst();
    }

}

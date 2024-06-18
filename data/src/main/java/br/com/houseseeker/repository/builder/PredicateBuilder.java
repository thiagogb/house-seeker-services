package br.com.houseseeker.repository.builder;

import br.com.houseseeker.domain.proto.BoolComparisonData;
import br.com.houseseeker.domain.proto.BytesComparisonData;
import br.com.houseseeker.domain.proto.ClauseOperator;
import br.com.houseseeker.domain.proto.DateTimeComparisonData;
import br.com.houseseeker.domain.proto.DoubleComparisonData;
import br.com.houseseeker.domain.proto.EnumComparisonData;
import br.com.houseseeker.domain.proto.Int32ComparisonData;
import br.com.houseseeker.domain.proto.StringComparisonData;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.ArrayPath;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PredicateBuilder {

    private Predicate[] predicates = new Predicate[]{};

    public static PredicateBuilder newInstance() {
        return new PredicateBuilder();
    }

    public PredicateBuilder append(@NotNull BooleanPath path, @NotNull BoolComparisonData comparisonData) {
        BooleanPredicateBuilder.build(path, comparisonData)
                               .ifPresent(e -> predicates = ArrayUtils.add(predicates, e));

        return this;
    }

    public PredicateBuilder append(@NotNull NumberPath<Integer> path, @NotNull Int32ComparisonData comparisonData) {
        IntegerPredicateBuilder.build(path, comparisonData)
                               .ifPresent(e -> predicates = ArrayUtils.add(predicates, e));

        return this;
    }

    public PredicateBuilder append(@NotNull NumberPath<BigDecimal> path, @NotNull DoubleComparisonData comparisonData) {
        DoublePredicateBuilder.build(path, comparisonData)
                              .ifPresent(e -> predicates = ArrayUtils.add(predicates, e));

        return this;
    }

    public PredicateBuilder append(@NotNull StringPath path, @NotNull StringComparisonData comparisonData) {
        StringPredicateBuilder.build(path, comparisonData)
                              .ifPresent(e -> predicates = ArrayUtils.add(predicates, e));

        return this;
    }

    public <T extends Enum<T>> PredicateBuilder append(
            @NotNull EnumPath<T> path,
            @NotNull EnumComparisonData comparisonData,
            @NotNull Function<String, T> valueResolver
    ) {
        EnumPredicateBuilder.build(path, comparisonData, valueResolver)
                            .ifPresent(e -> predicates = ArrayUtils.add(predicates, e));

        return this;
    }

    public PredicateBuilder append(@NotNull DateTimePath<LocalDateTime> path, @NotNull DateTimeComparisonData comparisonData) {
        DateTimePredicateBuilder.build(path, comparisonData)
                                .ifPresent(e -> predicates = ArrayUtils.add(predicates, e));

        return this;
    }

    public <A, B> PredicateBuilder append(@NotNull ArrayPath<A, B> path, @NotNull BytesComparisonData comparisonData) {
        ArrayPredicateBuilder.build(path, comparisonData)
                             .ifPresent(e -> predicates = ArrayUtils.add(predicates, e));

        return this;
    }

    public <T> PredicateBuilder append(
            @NotNull List<T> clauses,
            @NotNull Function<T, Predicate[]> predicateSupplier,
            @NotNull Function<T, ClauseOperator> innerClauseSupplier,
            @NotNull Function<T, ClauseOperator> outerClauseSupplier
    ) {
        clauses.forEach(clause -> prepareExpressionGroup(
                predicateSupplier.apply(clause),
                innerClauseSupplier.apply(clause),
                outerClauseSupplier.apply(clause)
        ));

        return this;
    }

    public Predicate[] build() {
        return predicates;
    }

    private void prepareExpressionGroup(Predicate[] paths, ClauseOperator innerOperator, ClauseOperator outerOperator) {
        if (ArrayUtils.isEmpty(paths))
            return;

        Predicate predicate = applyInnerSeparator(paths, innerOperator);
        if (ArrayUtils.isNotEmpty(predicates)) {
            applyOuterOperator(outerOperator, predicate);
        } else {
            predicates = ArrayUtils.add(predicates, predicate);
        }
    }

    private Predicate applyInnerSeparator(Predicate[] paths, ClauseOperator operator) {
        if (ArrayUtils.getLength(paths) == 1)
            return ArrayUtils.get(paths, 0);

        return Expressions.predicate(
                Ops.WRAPPED,
                switch (operator) {
                    case AND, UNRECOGNIZED -> Expressions.predicate(Ops.AND, paths);
                    case OR -> Expressions.predicate(Ops.OR, paths);
                }
        );
    }

    private void applyOuterOperator(ClauseOperator operator, Predicate rightPredicate) {
        int lastIndex = predicates.length - 1;
        if (ArrayUtils.get(predicates, lastIndex) instanceof BooleanExpression lastExpression) {
            predicates[lastIndex] = switch (operator) {
                case AND, UNRECOGNIZED -> lastExpression.and(rightPredicate);
                case OR -> lastExpression.or(rightPredicate);
            };
        }
    }

}

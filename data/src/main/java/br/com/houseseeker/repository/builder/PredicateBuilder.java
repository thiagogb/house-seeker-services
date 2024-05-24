package br.com.houseseeker.repository.builder;

import br.com.houseseeker.domain.proto.BoolComparisonData;
import br.com.houseseeker.domain.proto.BytesComparisonData;
import br.com.houseseeker.domain.proto.DateTimeComparisonData;
import br.com.houseseeker.domain.proto.DoubleComparisonData;
import br.com.houseseeker.domain.proto.EnumComparisonData;
import br.com.houseseeker.domain.proto.Int32ComparisonData;
import br.com.houseseeker.domain.proto.StringComparisonData;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.ArrayPath;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    public Predicate[] build() {
        return predicates;
    }

}

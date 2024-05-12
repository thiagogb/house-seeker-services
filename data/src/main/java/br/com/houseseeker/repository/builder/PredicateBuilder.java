package br.com.houseseeker.repository.builder;

import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import br.com.houseseeker.domain.proto.BoolComparisonData;
import br.com.houseseeker.domain.proto.BoolSingleComparisonData;
import br.com.houseseeker.domain.proto.BytesComparisonData;
import br.com.houseseeker.domain.proto.EnumComparisonData;
import br.com.houseseeker.domain.proto.EnumListComparisonData;
import br.com.houseseeker.domain.proto.EnumSingleComparisonData;
import br.com.houseseeker.domain.proto.Int32ComparisonData;
import br.com.houseseeker.domain.proto.Int32IntervalComparisonData;
import br.com.houseseeker.domain.proto.Int32ListComparisonData;
import br.com.houseseeker.domain.proto.Int32SingleComparisonData;
import br.com.houseseeker.domain.proto.StringComparisonData;
import br.com.houseseeker.domain.proto.StringListComparisonData;
import br.com.houseseeker.domain.proto.StringSingleComparisonData;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.ArrayPath;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;

import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PredicateBuilder {

    private Predicate[] predicates = new Predicate[]{};

    public static PredicateBuilder newInstance() {
        return new PredicateBuilder();
    }

    public PredicateBuilder append(@NotNull BooleanPath path, @NotNull BoolComparisonData comparisonData) {
        int oneOfCase = comparisonData.getComparisonCase().getNumber();

        if (oneOfCase == 0)
            return this;

        return switch (oneOfCase) {
            case 1 -> appendIsNull(path, comparisonData.getIsNull());
            case 2 -> appendIsNotNull(path, comparisonData.getIsNotNull());
            case 3 -> appendIsEqual(path, comparisonData.getIsEqual());
            case 4 -> appendIsNotEqual(path, comparisonData.getIsNotEqual());
            default -> throw new ExtendedRuntimeException("Unknown comparison case");
        };
    }

    public PredicateBuilder append(@NotNull NumberPath<Integer> path, @NotNull Int32ComparisonData comparisonData) {
        int oneOfCase = comparisonData.getComparisonCase().getNumber();

        if (oneOfCase == 0)
            return this;

        return switch (oneOfCase) {
            case 1 -> appendIsNull(path, comparisonData.getIsNull());
            case 2 -> appendIsNotNull(path, comparisonData.getIsNotNull());
            case 3 -> appendIsEqual(path, comparisonData.getIsEqual());
            case 4 -> appendIsNotEqual(path, comparisonData.getIsNotEqual());
            case 5 -> appendIsGreater(path, comparisonData.getIsGreater());
            case 6 -> appendIsGreaterOrEqual(path, comparisonData.getIsGreaterOrEqual());
            case 7 -> appendIsLesser(path, comparisonData.getIsLesser());
            case 8 -> appendIsLesserOrEqual(path, comparisonData.getIsLesserOrEqual());
            case 9 -> appendIsBetween(path, comparisonData.getIsBetween());
            case 10 -> appendIsNotBetween(path, comparisonData.getIsNotBetween());
            case 11 -> appendIsIn(path, comparisonData.getIsIn());
            case 12 -> appendIsNotIn(path, comparisonData.getIsNotIn());
            default -> throw new ExtendedRuntimeException("Unknown comparison case");
        };
    }

    public PredicateBuilder append(@NotNull StringPath path, @NotNull StringComparisonData comparisonData) {
        int oneOfCase = comparisonData.getComparisonCase().getNumber();

        if (oneOfCase == 0)
            return this;

        return switch (oneOfCase) {
            case 1 -> appendIsNull(path, comparisonData.getIsNull());
            case 2 -> appendIsNotNull(path, comparisonData.getIsNotNull());
            case 3 -> appendIsBlank(path, comparisonData.getIsBlank());
            case 4 -> appendIsNotBlank(path, comparisonData.getIsNotBlank());
            case 5 -> appendIsEqual(path, comparisonData.getIsEqual());
            case 6 -> appendIsNotEqual(path, comparisonData.getIsNotEqual());
            case 7 -> appendIsStartingWith(path, comparisonData.getIsStartingWith());
            case 8 -> appendIsNotStartingWith(path, comparisonData.getIsNotStartingWith());
            case 9 -> appendIsEndingWith(path, comparisonData.getIsEndingWith());
            case 10 -> appendIsNotEndingWith(path, comparisonData.getIsNotEndingWith());
            case 11 -> appendItContains(path, comparisonData.getItContains());
            case 12 -> appendItNotContains(path, comparisonData.getItNotContains());
            case 13 -> appendIsIn(path, comparisonData.getIsIn());
            case 14 -> appendIsNotIn(path, comparisonData.getIsNotIn());
            default -> throw new ExtendedRuntimeException("Unknown comparison case");
        };
    }

    public <T extends Enum<T>> PredicateBuilder append(
            @NotNull EnumPath<T> path,
            @NotNull EnumComparisonData comparisonData,
            Function<String, T> valueResolver
    ) {
        int oneOfCase = comparisonData.getComparisonCase().getNumber();

        if (oneOfCase == 0)
            return this;

        return switch (oneOfCase) {
            case 1 -> appendIsNull(path, comparisonData.getIsNull());
            case 2 -> appendIsNotNull(path, comparisonData.getIsNotNull());
            case 3 -> appendIsEqual(path, comparisonData.getIsEqual(), valueResolver);
            case 4 -> appendIsNotEqual(path, comparisonData.getIsNotEqual(), valueResolver);
            case 5 -> appendIsIn(path, comparisonData.getIsIn(), valueResolver);
            case 6 -> appendIsNotIn(path, comparisonData.getIsNotIn(), valueResolver);
            default -> throw new ExtendedRuntimeException("Unknown comparison case");
        };
    }

    public <A, B> PredicateBuilder append(@NotNull ArrayPath<A, B> path, @NotNull BytesComparisonData comparisonData) {
        int oneOfCase = comparisonData.getComparisonCase().getNumber();

        if (oneOfCase == 0)
            return this;

        return switch (oneOfCase) {
            case 1 -> appendIsNull(path, comparisonData.getIsNull());
            case 2 -> appendIsNotNull(path, comparisonData.getIsNotNull());
            default -> throw new ExtendedRuntimeException("Unknown comparison case");
        };
    }

    public Predicate[] build() {
        return predicates;
    }

    private PredicateBuilder appendIsNull(BooleanPath path, boolean isApplied) {
        if (isApplied)
            predicates = ArrayUtils.add(predicates, path.isNull());
        return this;
    }

    private PredicateBuilder appendIsNull(NumberPath<Integer> path, boolean isApplied) {
        if (isApplied)
            predicates = ArrayUtils.add(predicates, path.isNull());
        return this;
    }

    private PredicateBuilder appendIsNull(StringPath path, boolean isApplied) {
        if (isApplied)
            predicates = ArrayUtils.add(predicates, path.isNull());
        return this;
    }

    private <T extends Enum<T>> PredicateBuilder appendIsNull(EnumPath<T> path, boolean isApplied) {
        if (isApplied)
            predicates = ArrayUtils.add(predicates, path.isNull());
        return this;
    }

    private <A, B> PredicateBuilder appendIsNull(ArrayPath<A, B> path, boolean isApplied) {
        if (isApplied)
            predicates = ArrayUtils.add(predicates, path.isNull());
        return this;
    }

    private PredicateBuilder appendIsNotNull(BooleanPath path, boolean isApplied) {
        if (isApplied)
            predicates = ArrayUtils.add(predicates, path.isNotNull());
        return this;
    }

    private PredicateBuilder appendIsNotNull(NumberPath<Integer> path, boolean isApplied) {
        if (isApplied)
            predicates = ArrayUtils.add(predicates, path.isNotNull());
        return this;
    }

    private PredicateBuilder appendIsNotNull(StringPath path, boolean isApplied) {
        if (isApplied)
            predicates = ArrayUtils.add(predicates, path.isNotNull());
        return this;
    }

    private <T extends Enum<T>> PredicateBuilder appendIsNotNull(EnumPath<T> path, boolean isApplied) {
        if (isApplied)
            predicates = ArrayUtils.add(predicates, path.isNotNull());
        return this;
    }

    private <A, B> PredicateBuilder appendIsNotNull(ArrayPath<A, B> path, boolean isApplied) {
        if (isApplied)
            predicates = ArrayUtils.add(predicates, path.isNotNull());
        return this;
    }

    private PredicateBuilder appendIsEqual(BooleanPath path, BoolSingleComparisonData singleComparisonData) {
        predicates = ArrayUtils.add(predicates, path.eq(singleComparisonData.getValue()));
        return this;
    }

    private PredicateBuilder appendIsEqual(NumberPath<Integer> path, Int32SingleComparisonData singleComparisonData) {
        predicates = ArrayUtils.add(predicates, path.eq(singleComparisonData.getValue()));
        return this;
    }

    private PredicateBuilder appendIsEqual(StringPath path, StringSingleComparisonData singleComparisonData) {
        predicates = ArrayUtils.add(predicates, path.equalsIgnoreCase(singleComparisonData.getValue()));
        return this;
    }

    private <T extends Enum<T>> PredicateBuilder appendIsEqual(
            EnumPath<T> path,
            EnumSingleComparisonData singleComparisonData,
            Function<String, T> valueResolver
    ) {
        predicates = ArrayUtils.add(predicates, path.eq(valueResolver.apply(singleComparisonData.getValue())));
        return this;
    }

    private PredicateBuilder appendIsNotEqual(BooleanPath path, BoolSingleComparisonData singleComparisonData) {
        predicates = ArrayUtils.add(predicates, path.ne(singleComparisonData.getValue()));
        return this;
    }

    private PredicateBuilder appendIsNotEqual(NumberPath<Integer> path, Int32SingleComparisonData singleComparisonData) {
        predicates = ArrayUtils.add(predicates, path.ne(singleComparisonData.getValue()));
        return this;
    }

    private PredicateBuilder appendIsNotEqual(StringPath path, StringSingleComparisonData singleComparisonData) {
        predicates = ArrayUtils.add(predicates, path.notEqualsIgnoreCase(singleComparisonData.getValue()));
        return this;
    }

    private <T extends Enum<T>> PredicateBuilder appendIsNotEqual(
            EnumPath<T> path,
            EnumSingleComparisonData singleComparisonData,
            Function<String, T> valueResolver
    ) {
        predicates = ArrayUtils.add(predicates, path.ne(valueResolver.apply(singleComparisonData.getValue())));
        return this;
    }

    private PredicateBuilder appendIsGreater(NumberPath<Integer> path, Int32SingleComparisonData singleComparisonData) {
        predicates = ArrayUtils.add(predicates, path.gt(singleComparisonData.getValue()));
        return this;
    }

    private PredicateBuilder appendIsGreaterOrEqual(NumberPath<Integer> path, Int32SingleComparisonData singleComparisonData) {
        predicates = ArrayUtils.add(predicates, path.goe(singleComparisonData.getValue()));
        return this;
    }

    private PredicateBuilder appendIsLesser(NumberPath<Integer> path, Int32SingleComparisonData singleComparisonData) {
        predicates = ArrayUtils.add(predicates, path.lt(singleComparisonData.getValue()));
        return this;
    }

    private PredicateBuilder appendIsLesserOrEqual(NumberPath<Integer> path, Int32SingleComparisonData singleComparisonData) {
        predicates = ArrayUtils.add(predicates, path.loe(singleComparisonData.getValue()));
        return this;
    }

    private PredicateBuilder appendIsBetween(NumberPath<Integer> path, Int32IntervalComparisonData intervalComparisonData) {
        predicates = ArrayUtils.add(predicates, path.between(intervalComparisonData.getStart(), intervalComparisonData.getEnd()));
        return this;
    }

    private PredicateBuilder appendIsNotBetween(NumberPath<Integer> path, Int32IntervalComparisonData intervalComparisonData) {
        predicates = ArrayUtils.add(predicates, path.notBetween(intervalComparisonData.getStart(), intervalComparisonData.getEnd()));
        return this;
    }

    private PredicateBuilder appendIsIn(NumberPath<Integer> path, Int32ListComparisonData listComparisonData) {
        predicates = ArrayUtils.add(predicates, path.in(listComparisonData.getValuesList()));
        return this;
    }

    private PredicateBuilder appendIsIn(StringPath path, StringListComparisonData listComparisonData) {
        predicates = ArrayUtils.add(predicates, path.in(listComparisonData.getValuesList()));
        return this;
    }

    private <T extends Enum<T>> PredicateBuilder appendIsIn(
            EnumPath<T> path,
            EnumListComparisonData listComparisonData,
            Function<String, T> valueResolver
    ) {
        predicates = ArrayUtils.add(predicates, path.in(listComparisonData.getValuesList().stream().map(valueResolver).toList()));
        return this;
    }

    private PredicateBuilder appendIsNotIn(NumberPath<Integer> path, Int32ListComparisonData listComparisonData) {
        predicates = ArrayUtils.add(predicates, path.notIn(listComparisonData.getValuesList()));
        return this;
    }

    private PredicateBuilder appendIsNotIn(StringPath path, StringListComparisonData listComparisonData) {
        predicates = ArrayUtils.add(predicates, path.notIn(listComparisonData.getValuesList()));
        return this;
    }

    private <T extends Enum<T>> PredicateBuilder appendIsNotIn(
            EnumPath<T> path,
            EnumListComparisonData listComparisonData,
            Function<String, T> valueResolver
    ) {
        predicates = ArrayUtils.add(predicates, path.notIn(listComparisonData.getValuesList().stream().map(valueResolver).toList()));
        return this;
    }

    private PredicateBuilder appendIsBlank(StringPath path, boolean isApplied) {
        if (isApplied)
            predicates = ArrayUtils.add(predicates, path.isEmpty());
        return this;
    }

    private PredicateBuilder appendIsNotBlank(StringPath path, boolean isApplied) {
        if (isApplied)
            predicates = ArrayUtils.add(predicates, path.isNotEmpty());
        return this;
    }

    private PredicateBuilder appendIsStartingWith(StringPath path, StringSingleComparisonData singleComparisonData) {
        predicates = ArrayUtils.add(predicates, path.startsWithIgnoreCase(singleComparisonData.getValue()));
        return this;
    }

    private PredicateBuilder appendIsNotStartingWith(StringPath path, StringSingleComparisonData singleComparisonData) {
        predicates = ArrayUtils.add(predicates, path.startsWithIgnoreCase(singleComparisonData.getValue()).not());
        return this;
    }

    private PredicateBuilder appendIsEndingWith(StringPath path, StringSingleComparisonData singleComparisonData) {
        predicates = ArrayUtils.add(predicates, path.endsWithIgnoreCase(singleComparisonData.getValue()));
        return this;
    }

    private PredicateBuilder appendIsNotEndingWith(StringPath path, StringSingleComparisonData singleComparisonData) {
        predicates = ArrayUtils.add(predicates, path.endsWithIgnoreCase(singleComparisonData.getValue()).not());
        return this;
    }

    private PredicateBuilder appendItContains(StringPath path, StringSingleComparisonData singleComparisonData) {
        predicates = ArrayUtils.add(predicates, path.containsIgnoreCase(singleComparisonData.getValue()));
        return this;
    }

    private PredicateBuilder appendItNotContains(StringPath path, StringSingleComparisonData singleComparisonData) {
        predicates = ArrayUtils.add(predicates, path.containsIgnoreCase(singleComparisonData.getValue()).not());
        return this;
    }

}

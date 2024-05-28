package br.com.houseseeker.repository.builder;

import br.com.houseseeker.domain.exception.GrpcStatusException;
import br.com.houseseeker.domain.proto.Int32ComparisonData;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import io.grpc.Status;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class IntegerPredicateBuilder {

    public Optional<BooleanExpression> build(@NotNull NumberPath<Integer> path, @NotNull Int32ComparisonData comparisonData) {
        int oneOfCase = comparisonData.getComparisonCase().getNumber();

        if (oneOfCase == 0)
            return Optional.empty();

        return Optional.ofNullable(
                switch (oneOfCase) {
                    case 1 -> comparisonData.getIsNull() ? path.isNull() : null;
                    case 2 -> comparisonData.getIsNotNull() ? path.isNotNull() : null;
                    case 3 -> path.eq(comparisonData.getIsEqual().getValue());
                    case 4 -> path.ne(comparisonData.getIsNotEqual().getValue());
                    case 5 -> path.gt(comparisonData.getIsGreater().getValue());
                    case 6 -> path.goe(comparisonData.getIsGreaterOrEqual().getValue());
                    case 7 -> path.lt(comparisonData.getIsLesser().getValue());
                    case 8 -> path.loe(comparisonData.getIsLesserOrEqual().getValue());
                    case 9 -> path.between(comparisonData.getIsBetween().getStart(), comparisonData.getIsBetween().getEnd());
                    case 10 -> path.notBetween(comparisonData.getIsNotBetween().getStart(), comparisonData.getIsNotBetween().getEnd());
                    case 11 -> path.in(comparisonData.getIsIn().getValuesList());
                    case 12 -> path.notIn(comparisonData.getIsNotIn().getValuesList());
                    default -> throw new GrpcStatusException(Status.INVALID_ARGUMENT, "Unknown comparison case");
                }
        );
    }

}

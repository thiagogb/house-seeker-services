package br.com.houseseeker.repository.builder;

import br.com.houseseeker.domain.exception.GrpcStatusException;
import br.com.houseseeker.domain.proto.DoubleComparisonData;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import io.grpc.Status;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.Optional;

@UtilityClass
public class DoublePredicateBuilder {

    public Optional<BooleanExpression> build(@NotNull NumberPath<BigDecimal> path, @NotNull DoubleComparisonData comparisonData) {
        int oneOfCase = comparisonData.getComparisonCase().getNumber();

        if (oneOfCase == 0)
            return Optional.empty();

        return Optional.ofNullable(
                switch (oneOfCase) {
                    case 1 -> comparisonData.getIsNull() ? path.isNull() : null;
                    case 2 -> comparisonData.getIsNotNull() ? path.isNotNull() : null;
                    case 3 -> path.eq(BigDecimal.valueOf(comparisonData.getIsEqual().getValue()));
                    case 4 -> path.ne(BigDecimal.valueOf(comparisonData.getIsNotEqual().getValue()));
                    case 5 -> path.gt(BigDecimal.valueOf(comparisonData.getIsGreater().getValue()));
                    case 6 -> path.goe(BigDecimal.valueOf(comparisonData.getIsGreaterOrEqual().getValue()));
                    case 7 -> path.lt(BigDecimal.valueOf(comparisonData.getIsLesser().getValue()));
                    case 8 -> path.loe(BigDecimal.valueOf(comparisonData.getIsLesserOrEqual().getValue()));
                    case 9 -> path.between(
                            BigDecimal.valueOf(comparisonData.getIsBetween().getStart()),
                            BigDecimal.valueOf(comparisonData.getIsBetween().getEnd())
                    );
                    case 10 -> path.notBetween(
                            BigDecimal.valueOf(comparisonData.getIsNotBetween().getStart()),
                            BigDecimal.valueOf(comparisonData.getIsNotBetween().getEnd())
                    );
                    default -> throw new GrpcStatusException(Status.INVALID_ARGUMENT, "Unknown comparison case");
                }
        );
    }

}

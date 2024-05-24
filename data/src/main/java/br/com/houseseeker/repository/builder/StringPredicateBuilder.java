package br.com.houseseeker.repository.builder;

import br.com.houseseeker.domain.exception.GrpcStatusException;
import br.com.houseseeker.domain.proto.StringComparisonData;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import io.grpc.Status;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class StringPredicateBuilder {

    public Optional<BooleanExpression> build(@NotNull StringPath path, @NotNull StringComparisonData comparisonData) {
        int oneOfCase = comparisonData.getComparisonCase().getNumber();

        if (oneOfCase == 0)
            return Optional.empty();

        return Optional.ofNullable(
                switch (oneOfCase) {
                    case 1 -> comparisonData.getIsNull() ? path.isNull() : null;
                    case 2 -> comparisonData.getIsNotNull() ? path.isNotNull() : null;
                    case 3 -> comparisonData.getIsBlank() ? path.isEmpty() : null;
                    case 4 -> comparisonData.getIsNotBlank() ? path.isNotEmpty() : null;
                    case 5 -> path.equalsIgnoreCase(comparisonData.getIsEqual().getValue());
                    case 6 -> path.notEqualsIgnoreCase(comparisonData.getIsNotEqual().getValue());
                    case 7 -> path.startsWithIgnoreCase(comparisonData.getIsStartingWith().getValue());
                    case 8 -> path.startsWithIgnoreCase(comparisonData.getIsNotStartingWith().getValue()).not();
                    case 9 -> path.endsWithIgnoreCase(comparisonData.getIsEndingWith().getValue());
                    case 10 -> path.endsWithIgnoreCase(comparisonData.getIsNotEndingWith().getValue()).not();
                    case 11 -> path.containsIgnoreCase(comparisonData.getItContains().getValue());
                    case 12 -> path.containsIgnoreCase(comparisonData.getItNotContains().getValue()).not();
                    case 13 -> path.in(comparisonData.getIsIn().getValuesList());
                    case 14 -> path.notIn(comparisonData.getIsNotIn().getValuesList());
                    default -> throw new GrpcStatusException(Status.INVALID_ARGUMENT, "Unknown comparison case");
                }
        );
    }

}

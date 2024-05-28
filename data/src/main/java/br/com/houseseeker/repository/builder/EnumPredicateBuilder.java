package br.com.houseseeker.repository.builder;

import br.com.houseseeker.domain.exception.GrpcStatusException;
import br.com.houseseeker.domain.proto.EnumComparisonData;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;
import io.grpc.Status;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;

import java.util.Optional;
import java.util.function.Function;

@UtilityClass
public class EnumPredicateBuilder {

    public <T extends Enum<T>> Optional<BooleanExpression> build(
            @NotNull EnumPath<T> path,
            @NotNull EnumComparisonData comparisonData,
            @NotNull Function<String, T> valueResolver
    ) {
        int oneOfCase = comparisonData.getComparisonCase().getNumber();

        if (oneOfCase == 0)
            return Optional.empty();

        return Optional.ofNullable(
                switch (oneOfCase) {
                    case 1 -> comparisonData.getIsNull() ? path.isNull() : null;
                    case 2 -> comparisonData.getIsNotNull() ? path.isNotNull() : null;
                    case 3 -> path.eq(valueResolver.apply(comparisonData.getIsEqual().getValue()));
                    case 4 -> path.ne(valueResolver.apply(comparisonData.getIsNotEqual().getValue()));
                    case 5 -> path.in(comparisonData.getIsIn().getValuesList().stream().map(valueResolver).toList());
                    case 6 -> path.notIn(comparisonData.getIsNotIn().getValuesList().stream().map(valueResolver).toList());
                    default -> throw new GrpcStatusException(Status.INVALID_ARGUMENT, "Unknown comparison case");
                }
        );
    }

}

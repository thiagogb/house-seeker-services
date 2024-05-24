package br.com.houseseeker.repository.builder;

import br.com.houseseeker.domain.exception.GrpcStatusException;
import br.com.houseseeker.domain.proto.BoolComparisonData;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanPath;
import io.grpc.Status;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class BooleanPredicateBuilder {

    public Optional<BooleanExpression> build(@NotNull BooleanPath path, @NotNull BoolComparisonData comparisonData) {
        int oneOfCase = comparisonData.getComparisonCase().getNumber();

        if (oneOfCase == 0)
            return Optional.empty();

        return Optional.ofNullable(
                switch (oneOfCase) {
                    case 1 -> comparisonData.getIsNull() ? path.isNull() : null;
                    case 2 -> comparisonData.getIsNotNull() ? path.isNotNull() : null;
                    case 3 -> path.eq(comparisonData.getIsEqual().getValue());
                    case 4 -> path.ne(comparisonData.getIsNotEqual().getValue());
                    default -> throw new GrpcStatusException(Status.INVALID_ARGUMENT, "Unknown comparison case");
                }
        );
    }

}

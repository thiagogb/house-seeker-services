package br.com.houseseeker.repository.builder;

import br.com.houseseeker.domain.exception.GrpcStatusException;
import com.querydsl.core.types.Expression;
import io.grpc.Status;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExpressionBuilder {

    private Expression<?>[] projections = new Expression<?>[]{};

    public static ExpressionBuilder newInstance() {
        return new ExpressionBuilder();
    }

    public <T> ExpressionBuilder append(@NotNull Expression<T> expression, boolean selected) {
        if (selected)
            projections = ArrayUtils.add(projections, expression);

        return this;
    }

    public Expression<?>[] build() {
        if (ArrayUtils.isEmpty(projections))
            throw new GrpcStatusException(Status.INVALID_ARGUMENT, "At least one projection must be defined");

        return projections;
    }

}

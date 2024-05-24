package br.com.houseseeker.repository.builder;

import br.com.houseseeker.domain.exception.GrpcStatusException;
import br.com.houseseeker.domain.proto.DateTimeComparisonData;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import io.grpc.Status;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.Optional;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

@UtilityClass
public class DateTimePredicateBuilder {

    public Optional<BooleanExpression> build(@NotNull DateTimePath<LocalDateTime> path, @NotNull DateTimeComparisonData comparisonData) {
        int oneOfCase = comparisonData.getComparisonCase().getNumber();

        if (oneOfCase == 0)
            return Optional.empty();

        return Optional.ofNullable(
                switch (oneOfCase) {
                    case 1 -> comparisonData.getIsNull() ? path.isNull() : null;
                    case 2 -> comparisonData.getIsNotNull() ? path.isNotNull() : null;
                    case 3 -> path.eq(LocalDateTime.parse(comparisonData.getIsEqual().getValue(), ISO_LOCAL_DATE_TIME));
                    case 4 -> path.ne(LocalDateTime.parse(comparisonData.getIsNotEqual().getValue(), ISO_LOCAL_DATE_TIME));
                    case 5 -> path.gt(LocalDateTime.parse(comparisonData.getIsGreater().getValue(), ISO_LOCAL_DATE_TIME));
                    case 6 -> path.goe(LocalDateTime.parse(comparisonData.getIsGreaterOrEqual().getValue(), ISO_LOCAL_DATE_TIME));
                    case 7 -> path.lt(LocalDateTime.parse(comparisonData.getIsLesser().getValue(), ISO_LOCAL_DATE_TIME));
                    case 8 -> path.loe(LocalDateTime.parse(comparisonData.getIsLesserOrEqual().getValue(), ISO_LOCAL_DATE_TIME));
                    case 9 -> path.between(
                            LocalDateTime.parse(comparisonData.getIsBetween().getStart(), ISO_LOCAL_DATE_TIME),
                            LocalDateTime.parse(comparisonData.getIsBetween().getEnd(), ISO_LOCAL_DATE_TIME)
                    );
                    case 10 -> path.notBetween(
                            LocalDateTime.parse(comparisonData.getIsNotBetween().getStart(), ISO_LOCAL_DATE_TIME),
                            LocalDateTime.parse(comparisonData.getIsNotBetween().getEnd(), ISO_LOCAL_DATE_TIME)
                    );
                    default -> throw new GrpcStatusException(Status.INVALID_ARGUMENT, "Unknown comparison case");
                }
        );
    }

}

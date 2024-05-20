package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.input.BytesClauseInput;
import br.com.houseseeker.domain.proto.BytesComparisonData;
import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Objects.nonNull;

@UtilityClass
public class BytesComparisonBuilder {

    public BytesComparisonData build(@Nullable BytesClauseInput input) {
        BytesComparisonData.Builder builder = BytesComparisonData.newBuilder();
        if (nonNull(input)) {
            apply(input.getIsNull(), builder::setIsNull);
            apply(input.getIsNotNull(), builder::setIsNotNull);
        }
        return builder.build();
    }

    private void apply(Boolean input, Consumer<Boolean> consumer) {
        Optional.ofNullable(input).filter(v -> v).ifPresent(consumer);
    }

}

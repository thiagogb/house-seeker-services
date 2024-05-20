package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.input.BooleanClauseInput;
import br.com.houseseeker.domain.input.BooleanInput;
import br.com.houseseeker.domain.proto.BoolComparisonData;
import br.com.houseseeker.domain.proto.BoolSingleComparisonData;
import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Objects.nonNull;

@UtilityClass
public class BooleanComparisonBuilder {

    public BoolComparisonData build(@Nullable BooleanClauseInput input) {
        BoolComparisonData.Builder builder = BoolComparisonData.newBuilder();
        if (nonNull(input)) {
            apply(input.getIsNull(), builder::setIsNull);
            apply(input.getIsNotNull(), builder::setIsNotNull);
            apply(input.getIsEqual(), builder::setIsEqual);
            apply(input.getIsNotEqual(), builder::setIsNotEqual);
        }
        return builder.build();
    }

    private void apply(Boolean input, Consumer<Boolean> consumer) {
        Optional.ofNullable(input).filter(v -> v).ifPresent(consumer);
    }

    private void apply(BooleanInput input, Consumer<BoolSingleComparisonData> consumer) {
        Optional.ofNullable(input).map(BooleanComparisonBuilder::from).ifPresent(consumer);
    }

    private BoolSingleComparisonData from(BooleanInput input) {
        return BoolSingleComparisonData.newBuilder()
                                       .setValue(input.getValue())
                                       .build();
    }

}

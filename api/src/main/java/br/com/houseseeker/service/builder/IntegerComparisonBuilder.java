package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.input.IntegerClauseInput;
import br.com.houseseeker.domain.input.IntegerInput;
import br.com.houseseeker.domain.input.IntegerIntervalInput;
import br.com.houseseeker.domain.input.IntegerListInput;
import br.com.houseseeker.domain.proto.Int32ComparisonData;
import br.com.houseseeker.domain.proto.Int32IntervalComparisonData;
import br.com.houseseeker.domain.proto.Int32ListComparisonData;
import br.com.houseseeker.domain.proto.Int32SingleComparisonData;
import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Objects.nonNull;

@UtilityClass
public class IntegerComparisonBuilder {

    public Int32ComparisonData build(@Nullable IntegerClauseInput input) {
        Int32ComparisonData.Builder builder = Int32ComparisonData.newBuilder();
        if (nonNull(input)) {
            apply(input.getIsNull(), builder::setIsNull);
            apply(input.getIsNotNull(), builder::setIsNotNull);
            apply(input.getIsEqual(), builder::setIsEqual);
            apply(input.getIsNotEqual(), builder::setIsNotEqual);
            apply(input.getIsGreater(), builder::setIsGreater);
            apply(input.getIsGreaterOrEqual(), builder::setIsGreaterOrEqual);
            apply(input.getIsLesser(), builder::setIsLesser);
            apply(input.getIsLesserOrEqual(), builder::setIsLesserOrEqual);
            apply(input.getIsBetween(), builder::setIsBetween);
            apply(input.getIsNotBetween(), builder::setIsNotBetween);
            apply(input.getIsIn(), builder::setIsIn);
            apply(input.getIsNotIn(), builder::setIsNotIn);
        }
        return builder.build();
    }

    private void apply(Boolean input, Consumer<Boolean> consumer) {
        Optional.ofNullable(input).filter(v -> v).ifPresent(consumer);
    }

    private void apply(IntegerInput input, Consumer<Int32SingleComparisonData> consumer) {
        Optional.ofNullable(input).map(IntegerComparisonBuilder::from).ifPresent(consumer);
    }

    private void apply(IntegerIntervalInput input, Consumer<Int32IntervalComparisonData> consumer) {
        Optional.ofNullable(input).map(IntegerComparisonBuilder::from).ifPresent(consumer);
    }

    private void apply(IntegerListInput input, Consumer<Int32ListComparisonData> consumer) {
        Optional.ofNullable(input).map(IntegerComparisonBuilder::from).ifPresent(consumer);
    }

    private Int32SingleComparisonData from(IntegerInput input) {
        return Int32SingleComparisonData.newBuilder()
                                        .setValue(input.getValue())
                                        .build();
    }

    private Int32IntervalComparisonData from(IntegerIntervalInput input) {
        return Int32IntervalComparisonData.newBuilder()
                                          .setStart(input.getStart())
                                          .setEnd(input.getEnd())
                                          .build();
    }

    private Int32ListComparisonData from(IntegerListInput input) {
        return Int32ListComparisonData.newBuilder()
                                      .addAllValues(input.getValues())
                                      .build();
    }

}

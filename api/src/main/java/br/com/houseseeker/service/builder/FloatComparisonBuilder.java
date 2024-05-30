package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.input.FloatClauseInput;
import br.com.houseseeker.domain.input.FloatInput;
import br.com.houseseeker.domain.input.FloatIntervalInput;
import br.com.houseseeker.domain.proto.DoubleComparisonData;
import br.com.houseseeker.domain.proto.DoubleIntervalComparisonData;
import br.com.houseseeker.domain.proto.DoubleSingleComparisonData;
import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Objects.nonNull;

@UtilityClass
public class FloatComparisonBuilder {

    public DoubleComparisonData build(@Nullable FloatClauseInput input) {
        DoubleComparisonData.Builder builder = DoubleComparisonData.newBuilder();
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
        }
        return builder.build();
    }

    private void apply(Boolean input, Consumer<Boolean> consumer) {
        Optional.ofNullable(input).filter(v -> v).ifPresent(consumer);
    }

    private void apply(FloatInput input, Consumer<DoubleSingleComparisonData> consumer) {
        Optional.ofNullable(input).map(FloatComparisonBuilder::from).ifPresent(consumer);
    }

    private void apply(FloatIntervalInput input, Consumer<DoubleIntervalComparisonData> consumer) {
        Optional.ofNullable(input).map(FloatComparisonBuilder::from).ifPresent(consumer);
    }

    private DoubleSingleComparisonData from(FloatInput input) {
        return DoubleSingleComparisonData.newBuilder()
                                         .setValue(input.getValue().doubleValue())
                                         .build();
    }

    private DoubleIntervalComparisonData from(FloatIntervalInput input) {
        return DoubleIntervalComparisonData.newBuilder()
                                           .setStart(input.getStart().doubleValue())
                                           .setEnd(input.getEnd().doubleValue())
                                           .build();
    }

}

package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.input.DateTimeClauseInput;
import br.com.houseseeker.domain.input.DateTimeInput;
import br.com.houseseeker.domain.input.DateTimeIntervalInput;
import br.com.houseseeker.domain.proto.DateTimeComparisonData;
import br.com.houseseeker.domain.proto.DateTimeIntervalComparisonData;
import br.com.houseseeker.domain.proto.DateTimeSingleComparisonData;
import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static java.util.Objects.nonNull;

@UtilityClass
public class DateTimeComparisonBuilder {

    public DateTimeComparisonData build(@Nullable DateTimeClauseInput input) {
        DateTimeComparisonData.Builder builder = DateTimeComparisonData.newBuilder();
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

    private void apply(DateTimeInput input, Consumer<DateTimeSingleComparisonData> consumer) {
        Optional.ofNullable(input).map(DateTimeComparisonBuilder::from).ifPresent(consumer);
    }

    private void apply(DateTimeIntervalInput input, Consumer<DateTimeIntervalComparisonData> consumer) {
        Optional.ofNullable(input).map(DateTimeComparisonBuilder::from).ifPresent(consumer);
    }

    private DateTimeSingleComparisonData from(DateTimeInput input) {
        return DateTimeSingleComparisonData.newBuilder()
                                           .setValue(input.getValue().format(ISO_LOCAL_DATE_TIME))
                                           .build();
    }

    private DateTimeIntervalComparisonData from(DateTimeIntervalInput input) {
        return DateTimeIntervalComparisonData.newBuilder()
                                             .setStart(input.getStart().format(ISO_LOCAL_DATE_TIME))
                                             .setEnd(input.getEnd().format(ISO_LOCAL_DATE_TIME))
                                             .build();
    }

}

package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.input.StringClauseInput;
import br.com.houseseeker.domain.input.StringInput;
import br.com.houseseeker.domain.input.StringListInput;
import br.com.houseseeker.domain.proto.StringComparisonData;
import br.com.houseseeker.domain.proto.StringListComparisonData;
import br.com.houseseeker.domain.proto.StringSingleComparisonData;
import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Objects.nonNull;

@UtilityClass
public class StringComparisonBuilder {

    public StringComparisonData build(@Nullable StringClauseInput input) {
        StringComparisonData.Builder builder = StringComparisonData.newBuilder();
        if (nonNull(input)) {
            apply(input.getIsNull(), builder::setIsNull);
            apply(input.getIsNotNull(), builder::setIsNotNull);
            apply(input.getIsBlank(), builder::setIsBlank);
            apply(input.getIsNotBlank(), builder::setIsNotBlank);
            apply(input.getIsEqual(), builder::setIsEqual);
            apply(input.getIsNotEqual(), builder::setIsNotEqual);
            apply(input.getIsStartingWith(), builder::setIsStartingWith);
            apply(input.getIsNotStartingWith(), builder::setIsNotStartingWith);
            apply(input.getIsEndingWith(), builder::setIsEndingWith);
            apply(input.getIsNotEndingWith(), builder::setIsNotEndingWith);
            apply(input.getItContains(), builder::setItContains);
            apply(input.getItNotContains(), builder::setItNotContains);
            apply(input.getIsIn(), builder::setIsIn);
            apply(input.getIsNotIn(), builder::setIsNotIn);
        }
        return builder.build();
    }

    private void apply(Boolean input, Consumer<Boolean> consumer) {
        Optional.ofNullable(input).filter(v -> v).ifPresent(consumer);
    }

    private void apply(StringInput input, Consumer<StringSingleComparisonData> consumer) {
        Optional.ofNullable(input).map(StringComparisonBuilder::from).ifPresent(consumer);
    }

    private void apply(StringListInput input, Consumer<StringListComparisonData> consumer) {
        Optional.ofNullable(input).map(StringComparisonBuilder::from).ifPresent(consumer);
    }

    private StringSingleComparisonData from(StringInput input) {
        return StringSingleComparisonData.newBuilder()
                                         .setValue(input.getValue())
                                         .build();
    }

    private StringListComparisonData from(StringListInput input) {
        return StringListComparisonData.newBuilder()
                                       .addAllValues(input.getValues())
                                       .build();
    }

}

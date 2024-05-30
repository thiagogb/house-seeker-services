package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.input.AbstractEnumClauseInput;
import br.com.houseseeker.domain.input.AbstractInput;
import br.com.houseseeker.domain.input.AbstractListInput;
import br.com.houseseeker.domain.proto.EnumComparisonData;
import br.com.houseseeker.domain.proto.EnumListComparisonData;
import br.com.houseseeker.domain.proto.EnumSingleComparisonData;
import org.springframework.lang.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Objects.nonNull;

abstract class AbstractEnumComparisonBuilder
        <T extends Enum<T>, U extends AbstractInput<T>, V extends AbstractListInput<T>, X extends AbstractEnumClauseInput<T, U, V>> {

    protected final EnumComparisonData internalBuild(@Nullable X input) {
        EnumComparisonData.Builder builder = EnumComparisonData.newBuilder();
        if (nonNull(input)) {
            apply(input.getIsNull(), builder::setIsNull);
            apply(input.getIsNotNull(), builder::setIsNotNull);
            apply(input.getIsEqual(), builder::setIsEqual);
            apply(input.getIsNotEqual(), builder::setIsNotEqual);
            apply(input.getIsIn(), builder::setIsIn);
            apply(input.getIsNotIn(), builder::setIsNotIn);
        }
        return builder.build();
    }

    private void apply(Boolean input, Consumer<Boolean> consumer) {
        Optional.ofNullable(input).filter(v -> v).ifPresent(consumer);
    }

    private void apply(U input, Consumer<EnumSingleComparisonData> consumer) {
        Optional.ofNullable(input).map(this::from).ifPresent(consumer);
    }

    private void apply(V input, Consumer<EnumListComparisonData> consumer) {
        Optional.ofNullable(input).map(this::from).ifPresent(consumer);
    }

    private EnumSingleComparisonData from(U input) {
        return EnumSingleComparisonData.newBuilder()
                                       .setValue(input.getValue().name())
                                       .build();
    }

    private EnumListComparisonData from(V input) {
        return EnumListComparisonData.newBuilder()
                                     .addAllValues(input.getValues().stream().map(Enum::name).toList())
                                     .build();
    }

}

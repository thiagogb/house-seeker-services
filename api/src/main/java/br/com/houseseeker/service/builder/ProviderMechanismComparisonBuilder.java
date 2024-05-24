package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.input.ProviderMechanismClausesInput;
import br.com.houseseeker.domain.input.ProviderMechanismInput;
import br.com.houseseeker.domain.input.ProviderMechanismListInput;
import br.com.houseseeker.domain.proto.EnumComparisonData;
import br.com.houseseeker.domain.proto.EnumListComparisonData;
import br.com.houseseeker.domain.proto.EnumSingleComparisonData;
import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Objects.nonNull;

@UtilityClass
public class ProviderMechanismComparisonBuilder {

    public EnumComparisonData build(@Nullable ProviderMechanismClausesInput input) {
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

    private void apply(ProviderMechanismInput input, Consumer<EnumSingleComparisonData> consumer) {
        Optional.ofNullable(input).map(ProviderMechanismComparisonBuilder::from).ifPresent(consumer);
    }

    private void apply(ProviderMechanismListInput input, Consumer<EnumListComparisonData> consumer) {
        Optional.ofNullable(input).map(ProviderMechanismComparisonBuilder::from).ifPresent(consumer);
    }

    private EnumSingleComparisonData from(ProviderMechanismInput input) {
        return EnumSingleComparisonData.newBuilder()
                                       .setValue(input.getValue().name())
                                       .build();
    }

    private EnumListComparisonData from(ProviderMechanismListInput input) {
        return EnumListComparisonData.newBuilder()
                                     .addAllValues(input.getValues().stream().map(Enum::name).toList())
                                     .build();
    }

}

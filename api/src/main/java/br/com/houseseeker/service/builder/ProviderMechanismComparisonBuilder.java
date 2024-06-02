package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.input.ProviderMechanismClausesInput;
import br.com.houseseeker.domain.input.ProviderMechanismInput;
import br.com.houseseeker.domain.input.ProviderMechanismListInput;
import br.com.houseseeker.domain.proto.EnumComparisonData;
import br.com.houseseeker.domain.provider.ProviderMechanism;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProviderMechanismComparisonBuilder
        extends AbstractEnumComparisonBuilder<ProviderMechanism, ProviderMechanismInput, ProviderMechanismListInput, ProviderMechanismClausesInput> {

    public static EnumComparisonData build(@Nullable ProviderMechanismClausesInput input) {
        return new ProviderMechanismComparisonBuilder().internalBuild(input);
    }

}

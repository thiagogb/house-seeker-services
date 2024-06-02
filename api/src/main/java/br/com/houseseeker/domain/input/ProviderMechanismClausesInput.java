package br.com.houseseeker.domain.input;

import br.com.houseseeker.domain.provider.ProviderMechanism;
import lombok.Builder;

public class ProviderMechanismClausesInput extends AbstractEnumClauseInput<ProviderMechanism, ProviderMechanismInput, ProviderMechanismListInput> {

    @Builder
    public ProviderMechanismClausesInput(
            Boolean isNull,
            Boolean isNotNull,
            ProviderMechanismInput isEqual,
            ProviderMechanismInput isNotEqual,
            ProviderMechanismListInput isIn,
            ProviderMechanismListInput isNotIn
    ) {
        super(isNull, isNotNull, isEqual, isNotEqual, isIn, isNotIn);
    }

}

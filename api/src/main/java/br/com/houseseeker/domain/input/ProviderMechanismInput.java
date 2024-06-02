package br.com.houseseeker.domain.input;

import br.com.houseseeker.domain.provider.ProviderMechanism;
import lombok.Builder;

public class ProviderMechanismInput extends AbstractInput<ProviderMechanism> {

    @Builder
    public ProviderMechanismInput(ProviderMechanism value) {
        super(value);
    }

}

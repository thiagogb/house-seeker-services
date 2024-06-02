package br.com.houseseeker.domain.input;

import br.com.houseseeker.domain.provider.ProviderMechanism;
import lombok.Builder;

import java.util.List;

public class ProviderMechanismListInput extends AbstractListInput<ProviderMechanism> {

    @Builder
    public ProviderMechanismListInput(List<ProviderMechanism> values) {
        super(values);
    }

}

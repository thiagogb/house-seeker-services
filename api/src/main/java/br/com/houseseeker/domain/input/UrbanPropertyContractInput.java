package br.com.houseseeker.domain.input;

import br.com.houseseeker.domain.property.UrbanPropertyContract;
import lombok.Builder;

public class UrbanPropertyContractInput extends AbstractInput<UrbanPropertyContract> {

    @Builder
    public UrbanPropertyContractInput(UrbanPropertyContract value) {
        super(value);
    }

}

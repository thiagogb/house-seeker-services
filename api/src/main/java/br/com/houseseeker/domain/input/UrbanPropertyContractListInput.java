package br.com.houseseeker.domain.input;

import br.com.houseseeker.domain.property.UrbanPropertyContract;
import lombok.Builder;

import java.util.List;

public class UrbanPropertyContractListInput extends AbstractListInput<UrbanPropertyContract> {

    @Builder
    public UrbanPropertyContractListInput(List<UrbanPropertyContract> values) {
        super(values);
    }

}

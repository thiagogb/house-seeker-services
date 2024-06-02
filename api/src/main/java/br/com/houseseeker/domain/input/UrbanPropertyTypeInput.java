package br.com.houseseeker.domain.input;

import br.com.houseseeker.domain.property.UrbanPropertyType;
import lombok.Builder;

public class UrbanPropertyTypeInput extends AbstractInput<UrbanPropertyType> {

    @Builder
    public UrbanPropertyTypeInput(UrbanPropertyType value) {
        super(value);
    }

}

package br.com.houseseeker.domain.input;

import br.com.houseseeker.domain.property.UrbanPropertyType;
import lombok.Builder;

import java.util.List;

public class UrbanPropertyTypeListInput extends AbstractListInput<UrbanPropertyType> {

    @Builder
    public UrbanPropertyTypeListInput(List<UrbanPropertyType> values) {
        super(values);
    }

}

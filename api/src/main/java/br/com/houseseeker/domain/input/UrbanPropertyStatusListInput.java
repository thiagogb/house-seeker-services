package br.com.houseseeker.domain.input;

import br.com.houseseeker.domain.property.UrbanPropertyStatus;
import lombok.Builder;

import java.util.List;

public class UrbanPropertyStatusListInput extends AbstractListInput<UrbanPropertyStatus> {

    @Builder
    public UrbanPropertyStatusListInput(List<UrbanPropertyStatus> values) {
        super(values);
    }

}

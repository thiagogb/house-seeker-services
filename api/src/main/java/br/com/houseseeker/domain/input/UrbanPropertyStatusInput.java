package br.com.houseseeker.domain.input;

import br.com.houseseeker.domain.property.UrbanPropertyStatus;
import lombok.Builder;

public class UrbanPropertyStatusInput extends AbstractInput<UrbanPropertyStatus> {

    @Builder
    public UrbanPropertyStatusInput(UrbanPropertyStatus value) {
        super(value);
    }

}

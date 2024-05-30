package br.com.houseseeker.domain.input;

import lombok.Builder;

public class BooleanInput extends AbstractInput<Boolean> {

    @Builder
    public BooleanInput(Boolean value) {
        super(value);
    }

}

package br.com.houseseeker.domain.input;

import lombok.Builder;

public class IntegerInput extends AbstractInput<Integer> {

    @Builder
    public IntegerInput(Integer value) {
        super(value);
    }

}

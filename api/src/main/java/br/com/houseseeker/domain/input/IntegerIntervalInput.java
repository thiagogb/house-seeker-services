package br.com.houseseeker.domain.input;

import lombok.Builder;

public class IntegerIntervalInput extends AbstractIntervalInput<Integer> {

    @Builder
    public IntegerIntervalInput(Integer start, Integer end) {
        super(start, end);
    }

}

package br.com.houseseeker.domain.input;

import lombok.Builder;

import java.util.List;

public class IntegerListInput extends AbstractListInput<Integer> {

    @Builder
    public IntegerListInput(List<Integer> values) {
        super(values);
    }

}

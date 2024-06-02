package br.com.houseseeker.domain.input;

import lombok.Builder;

import java.util.List;

public class StringListInput extends AbstractListInput<String> {

    @Builder
    public StringListInput(List<String> values) {
        super(values);
    }

}

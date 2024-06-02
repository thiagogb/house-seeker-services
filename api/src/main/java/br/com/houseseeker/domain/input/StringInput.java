package br.com.houseseeker.domain.input;

import lombok.Builder;

public class StringInput extends AbstractInput<String> {

    @Builder
    public StringInput(String value) {
        super(value);
    }

}

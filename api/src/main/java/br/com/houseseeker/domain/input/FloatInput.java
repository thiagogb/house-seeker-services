package br.com.houseseeker.domain.input;

import lombok.Builder;

import java.math.BigDecimal;

public class FloatInput extends AbstractInput<BigDecimal> {

    @Builder
    public FloatInput(BigDecimal value) {
        super(value);
    }

}

package br.com.houseseeker.domain.input;

import lombok.Builder;

import java.math.BigDecimal;

public class FloatIntervalInput extends AbstractIntervalInput<BigDecimal> {

    @Builder
    public FloatIntervalInput(BigDecimal start, BigDecimal end) {
        super(start, end);
    }

}

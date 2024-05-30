package br.com.houseseeker.domain.input;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FloatClauseInput {

    private Boolean isNull;
    private Boolean isNotNull;
    private FloatInput isEqual;
    private FloatInput isNotEqual;
    private FloatInput isGreater;
    private FloatInput isGreaterOrEqual;
    private FloatInput isLesser;
    private FloatInput isLesserOrEqual;
    private FloatIntervalInput isBetween;
    private FloatIntervalInput isNotBetween;

}

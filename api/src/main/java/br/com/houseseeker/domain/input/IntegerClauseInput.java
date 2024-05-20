package br.com.houseseeker.domain.input;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IntegerClauseInput {

    private Boolean isNull;
    private Boolean isNotNull;
    private IntegerInput isEqual;
    private IntegerInput isNotEqual;
    private IntegerInput isGreater;
    private IntegerInput isGreaterOrEqual;
    private IntegerInput isLesser;
    private IntegerInput isLesserOrEqual;
    private IntegerIntervalInput isBetween;
    private IntegerIntervalInput isNotBetween;
    private IntegerListInput isIn;
    private IntegerListInput isNotIn;

}

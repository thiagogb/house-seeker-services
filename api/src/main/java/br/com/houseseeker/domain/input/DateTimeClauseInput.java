package br.com.houseseeker.domain.input;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DateTimeClauseInput {

    private Boolean isNull;
    private Boolean isNotNull;
    private DateTimeInput isEqual;
    private DateTimeInput isNotEqual;
    private DateTimeInput isGreater;
    private DateTimeInput isGreaterOrEqual;
    private DateTimeInput isLesser;
    private DateTimeInput isLesserOrEqual;
    private DateTimeIntervalInput isBetween;
    private DateTimeIntervalInput isNotBetween;

}

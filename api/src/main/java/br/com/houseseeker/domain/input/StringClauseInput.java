package br.com.houseseeker.domain.input;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StringClauseInput {

    private Boolean isNull;
    private Boolean isNotNull;
    private Boolean isBlank;
    private Boolean isNotBlank;
    private StringInput isEqual;
    private StringInput isNotEqual;
    private StringInput isStartingWith;
    private StringInput isNotStartingWith;
    private StringInput isEndingWith;
    private StringInput isNotEndingWith;
    private StringInput itContains;
    private StringInput itNotContains;
    private StringListInput isIn;
    private StringListInput isNotIn;

}

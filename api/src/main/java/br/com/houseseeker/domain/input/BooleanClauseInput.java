package br.com.houseseeker.domain.input;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BooleanClauseInput {

    private Boolean isNull;
    private Boolean isNotNull;
    private BooleanInput isEqual;
    private BooleanInput isNotEqual;

}

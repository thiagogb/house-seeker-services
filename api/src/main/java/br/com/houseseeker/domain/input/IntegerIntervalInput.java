package br.com.houseseeker.domain.input;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IntegerIntervalInput {

    private Integer start;
    private Integer end;

}

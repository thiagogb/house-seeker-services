package br.com.houseseeker.domain.input;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BytesClauseInput {

    private Boolean isNull;
    private Boolean isNotNull;

}

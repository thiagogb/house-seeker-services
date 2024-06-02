package br.com.houseseeker.domain.input;

import br.com.houseseeker.domain.property.UrbanPropertyStatus;
import lombok.Builder;

public class UrbanPropertyStatusClauseInput extends AbstractEnumClauseInput<UrbanPropertyStatus, UrbanPropertyStatusInput, UrbanPropertyStatusListInput> {

    @Builder
    public UrbanPropertyStatusClauseInput(
            Boolean isNull,
            Boolean isNotNull,
            UrbanPropertyStatusInput isEqual,
            UrbanPropertyStatusInput isNotEqual,
            UrbanPropertyStatusListInput isIn,
            UrbanPropertyStatusListInput isNotIn
    ) {
        super(isNull, isNotNull, isEqual, isNotEqual, isIn, isNotIn);
    }

}

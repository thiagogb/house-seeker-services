package br.com.houseseeker.domain.input;

import br.com.houseseeker.domain.property.UrbanPropertyType;
import lombok.Builder;

public class UrbanPropertyTypeClauseInput extends AbstractEnumClauseInput<UrbanPropertyType, UrbanPropertyTypeInput, UrbanPropertyTypeListInput> {

    @Builder
    public UrbanPropertyTypeClauseInput(
            Boolean isNull,
            Boolean isNotNull,
            UrbanPropertyTypeInput isEqual,
            UrbanPropertyTypeInput isNotEqual,
            UrbanPropertyTypeListInput isIn,
            UrbanPropertyTypeListInput isNotIn
    ) {
        super(isNull, isNotNull, isEqual, isNotEqual, isIn, isNotIn);
    }

}

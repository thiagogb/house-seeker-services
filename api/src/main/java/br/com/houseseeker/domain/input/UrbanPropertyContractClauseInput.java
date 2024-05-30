package br.com.houseseeker.domain.input;

import br.com.houseseeker.domain.property.UrbanPropertyContract;
import lombok.Builder;

public class UrbanPropertyContractClauseInput extends AbstractEnumClauseInput<UrbanPropertyContract, UrbanPropertyContractInput, UrbanPropertyContractListInput> {

    @Builder
    public UrbanPropertyContractClauseInput(
            Boolean isNull,
            Boolean isNotNull,
            UrbanPropertyContractInput isEqual,
            UrbanPropertyContractInput isNotEqual,
            UrbanPropertyContractListInput isIn,
            UrbanPropertyContractListInput isNotIn
    ) {
        super(isNull, isNotNull, isEqual, isNotEqual, isIn, isNotIn);
    }

}

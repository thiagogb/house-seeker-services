package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.input.UrbanPropertyContractClauseInput;
import br.com.houseseeker.domain.input.UrbanPropertyContractInput;
import br.com.houseseeker.domain.input.UrbanPropertyContractListInput;
import br.com.houseseeker.domain.property.UrbanPropertyContract;
import br.com.houseseeker.domain.proto.EnumComparisonData;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UrbanPropertyContractComparisonBuilder
        extends AbstractEnumComparisonBuilder<UrbanPropertyContract, UrbanPropertyContractInput, UrbanPropertyContractListInput, UrbanPropertyContractClauseInput> {

    public static EnumComparisonData build(@Nullable UrbanPropertyContractClauseInput input) {
        return new UrbanPropertyContractComparisonBuilder().internalBuild(input);
    }

}

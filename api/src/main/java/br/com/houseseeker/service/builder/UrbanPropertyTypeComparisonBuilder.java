package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.input.UrbanPropertyTypeClauseInput;
import br.com.houseseeker.domain.input.UrbanPropertyTypeInput;
import br.com.houseseeker.domain.input.UrbanPropertyTypeListInput;
import br.com.houseseeker.domain.property.UrbanPropertyType;
import br.com.houseseeker.domain.proto.EnumComparisonData;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UrbanPropertyTypeComparisonBuilder
        extends AbstractEnumComparisonBuilder<UrbanPropertyType, UrbanPropertyTypeInput, UrbanPropertyTypeListInput, UrbanPropertyTypeClauseInput> {

    public static EnumComparisonData build(@Nullable UrbanPropertyTypeClauseInput input) {
        return new UrbanPropertyTypeComparisonBuilder().internalBuild(input);
    }

}

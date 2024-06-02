package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.input.UrbanPropertyStatusClauseInput;
import br.com.houseseeker.domain.input.UrbanPropertyStatusInput;
import br.com.houseseeker.domain.input.UrbanPropertyStatusListInput;
import br.com.houseseeker.domain.property.UrbanPropertyStatus;
import br.com.houseseeker.domain.proto.EnumComparisonData;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UrbanPropertyStatusComparisonBuilder
        extends AbstractEnumComparisonBuilder<UrbanPropertyStatus, UrbanPropertyStatusInput, UrbanPropertyStatusListInput, UrbanPropertyStatusClauseInput> {

    public static EnumComparisonData build(@Nullable UrbanPropertyStatusClauseInput input) {
        return new UrbanPropertyStatusComparisonBuilder().internalBuild(input);
    }

}

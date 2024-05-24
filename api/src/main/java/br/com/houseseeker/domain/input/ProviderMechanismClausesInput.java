package br.com.houseseeker.domain.input;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProviderMechanismClausesInput {

    private Boolean isNull;
    private Boolean isNotNull;
    private ProviderMechanismInput isEqual;
    private ProviderMechanismInput isNotEqual;
    private ProviderMechanismListInput isIn;
    private ProviderMechanismListInput isNotIn;

}

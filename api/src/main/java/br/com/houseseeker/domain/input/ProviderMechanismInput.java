package br.com.houseseeker.domain.input;

import br.com.houseseeker.domain.provider.ProviderMechanism;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProviderMechanismInput {

    private ProviderMechanism value;

}

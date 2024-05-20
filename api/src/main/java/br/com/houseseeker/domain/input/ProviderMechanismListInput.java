package br.com.houseseeker.domain.input;

import br.com.houseseeker.domain.provider.ProviderMechanism;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProviderMechanismListInput {

    private List<ProviderMechanism> values;

}

package br.com.houseseeker.domain.input;

import br.com.houseseeker.domain.provider.ProviderMechanism;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.URL;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class ProviderEditionInput extends AbstractEditionInput {

    @Size(max = 255)
    private String name;
    @Size(max = 255)
    @URL
    private String siteUrl;
    @Size(max = 255)
    @URL
    private String dataUrl;
    private ProviderMechanism mechanism;
    private String params;
    @Size(max = 255)
    private String cronExpression;
    private String logo;
    private Boolean active;

}

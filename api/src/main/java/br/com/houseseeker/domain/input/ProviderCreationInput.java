package br.com.houseseeker.domain.input;

import br.com.houseseeker.domain.provider.ProviderMechanism;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
public class ProviderCreationInput {

    @NotBlank
    @Size(max = 255)
    private String name;
    @NotBlank
    @Size(max = 255)
    @URL
    private String siteUrl;
    @Size(max = 255)
    @URL
    private String dataUrl;
    @NotNull
    private ProviderMechanism mechanism;
    private String params;
    @NotBlank
    @Size(max = 255)
    private String cronExpression;
    private String logo;
    @NotNull
    private Boolean active;

}

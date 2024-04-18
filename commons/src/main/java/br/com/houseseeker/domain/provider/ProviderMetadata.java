package br.com.houseseeker.domain.provider;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class ProviderMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = -2420041296392961908L;

    @NotNull
    @Min(1)
    private final Integer id;
    @NotBlank
    private final String name;
    @NotBlank
    @URL
    private final String siteUrl;
    @URL
    private final String dataUrl;
    @NotNull
    private final ProviderMechanism mechanism;
    private final String params;

}

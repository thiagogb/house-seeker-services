package br.com.houseseeker.domain.provider;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.URL;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProviderMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = -2420041296392961908L;

    @NotNull
    @Min(1)
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    @URL
    private String siteUrl;
    @URL
    private String dataUrl;
    @NotNull
    private ProviderMechanism mechanism;
    private String params;

    public String getBaseUrl() {
        return Optional.ofNullable(dataUrl)
                       .filter(StringUtils::isNotBlank)
                       .orElse(siteUrl);
    }

}

package br.com.houseseeker.mock;

import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.domain.provider.ProviderMetadata;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProviderMetadataMocks {

    private static final String DEFAULT_URL = "http://localhost";

    public ProviderMetadata withMechanism(@NotNull ProviderMechanism mechanism) {
        return withUrlAndMechanism(DEFAULT_URL, mechanism);
    }

    public ProviderMetadata withUrlAndMechanism(@NotNull String url, @NotNull ProviderMechanism mechanism) {
        return ProviderMetadata.builder()
                               .id(1)
                               .name("Test Provider")
                               .siteUrl(url)
                               .dataUrl(url)
                               .mechanism(mechanism)
                               .build();
    }

}

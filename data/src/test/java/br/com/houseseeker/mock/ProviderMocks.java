package br.com.houseseeker.mock;

import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.entity.Provider;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;

@UtilityClass
public class ProviderMocks {

    public Provider testProviderWithIdAndMechanism(Integer id, ProviderMechanism mechanism) {
        return Provider.builder()
                       .id(id)
                       .name(String.format("Test Provider %d", id))
                       .siteUrl(String.format("http://test.provider.com/%d", id))
                       .dataUrl(String.format("http://test.provider.com/%d/api", id))
                       .mechanism(mechanism)
                       .params("{\"connection\":{}}")
                       .cronExpression("0 0 9 ? * MON,WED,FRI *")
                       .logo(String.format("logo%d", id).getBytes(StandardCharsets.UTF_8))
                       .active(true)
                       .build();
    }

}

package br.com.houseseeker.service.v4;

import br.com.houseseeker.domain.provider.ProviderMetadata;
import br.com.houseseeker.domain.provider.ProviderParameters;
import br.com.houseseeker.domain.provider.ProviderScraperResponse;
import br.com.houseseeker.service.AbstractDataScraperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataScraperV4Service extends AbstractDataScraperService {

    @Override
    protected ProviderScraperResponse execute(
            ProviderMetadata providerMetadata,
            ProviderParameters providerParameters,
            Retrofit retrofit
    ) {
        return null;
    }

}

package br.com.houseseeker.repository;

import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.service.proto.GetProvidersDataRequest;
import org.springframework.data.domain.Page;

public interface ProviderExtendedRepository {

    Page<Provider> findBy(GetProvidersDataRequest getProvidersDataRequest);

}

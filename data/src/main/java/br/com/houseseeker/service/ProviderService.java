package br.com.houseseeker.service;

import br.com.houseseeker.domain.proto.ProviderData;
import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.repository.ProviderRepository;
import br.com.houseseeker.service.proto.GetProvidersDataRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProviderService {

    private final ProviderRepository providerRepository;

    @Transactional
    public Optional<Provider> findById(int id) {
        return providerRepository.findById(id);
    }

    @Transactional
    public Page<Provider> findBy(@NotNull GetProvidersDataRequest getProvidersDataRequest) {
        return providerRepository.findBy(getProvidersDataRequest);
    }

    @Transactional
    public Provider insert(@NotNull ProviderData providerData) {
        return null;
    }

    @Transactional
    public Provider update(@NotNull ProviderData providerData) {
        return null;
    }

}

package br.com.houseseeker.service;

import br.com.houseseeker.domain.exception.GrpcStatusException;
import br.com.houseseeker.domain.proto.ProviderData;
import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.mapper.ProviderMapper;
import br.com.houseseeker.repository.ProviderRepository;
import br.com.houseseeker.service.proto.GetProvidersDataRequest;
import br.com.houseseeker.service.validator.EntityValidatorService;
import br.com.houseseeker.util.ProtoWrapperUtils;
import io.grpc.Status;
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
    private final ProviderMapper providerMapper;
    private final EntityValidatorService entityValidatorService;

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
        Provider provider = providerMapper.createEntity(providerData);
        entityValidatorService.validate(provider);
        return providerRepository.save(provider);
    }

    @Transactional
    public Provider update(@NotNull ProviderData providerData) {
        int id = ProtoWrapperUtils.getValue(providerData.getId());
        Provider provider = findById(id).orElseThrow(() -> new GrpcStatusException(Status.NOT_FOUND, "Provider %d not found", id));
        providerMapper.copyToEntity(providerData, provider);
        entityValidatorService.validate(provider);
        return providerRepository.save(provider);
    }

}

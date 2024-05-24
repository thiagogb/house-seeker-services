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
    private final UrbanPropertyService urbanPropertyService;
    private final UrbanPropertyLocationService urbanPropertyLocationService;
    private final UrbanPropertyMeasureService urbanPropertyMeasureService;
    private final UrbanPropertyConvenienceService urbanPropertyConvenienceService;
    private final UrbanPropertyMediaService urbanPropertyMediaService;
    private final UrbanPropertyPriceVariationService urbanPropertyPriceVariationService;

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
        Provider provider = providerMapper.toEntity(providerData);
        entityValidatorService.validate(provider);
        return providerRepository.save(provider);
    }

    @Transactional
    public Provider update(@NotNull ProviderData providerData) {
        int id = ProtoWrapperUtils.getInt(providerData.getId());
        Provider provider = findByIdOrThrowNotFound(id);
        providerMapper.toEntity(providerData, provider);
        entityValidatorService.validate(provider);
        return providerRepository.save(provider);
    }

    @Transactional
    public void wipe(int id) {
        Provider provider = findByIdOrThrowNotFound(id);
        urbanPropertyLocationService.deleteAllByProvider(provider);
        urbanPropertyMeasureService.deleteAllByProvider(provider);
        urbanPropertyConvenienceService.deleteAll(urbanPropertyConvenienceService.findAllByProvider(provider));
        urbanPropertyMediaService.deleteAll(urbanPropertyMediaService.findAllByProvider(provider));
        urbanPropertyPriceVariationService.deleteAllByProvider(provider);
        urbanPropertyService.deleteAllByProvider(provider);
    }

    private Provider findByIdOrThrowNotFound(int id) {
        return providerRepository.findById(id)
                                 .orElseThrow(() -> new GrpcStatusException(Status.NOT_FOUND, "Provider %d not found", id));
    }

}

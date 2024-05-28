package br.com.houseseeker.service;

import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanPropertyPriceVariation;
import br.com.houseseeker.repository.UrbanPropertyPriceVariationRepository;
import br.com.houseseeker.service.proto.GetUrbanPropertyPriceVariationsRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrbanPropertyPriceVariationService {

    private final UrbanPropertyPriceVariationRepository urbanPropertyPriceVariationRepository;

    @Transactional
    public List<UrbanPropertyPriceVariation> findAllByProvider(@NotNull Provider provider) {
        return urbanPropertyPriceVariationRepository.findAllByProvider(provider);
    }

    @Transactional
    public Page<UrbanPropertyPriceVariation> findBy(@NotNull GetUrbanPropertyPriceVariationsRequest request) {
        return urbanPropertyPriceVariationRepository.findBy(request);
    }

    @Transactional
    public List<UrbanPropertyPriceVariation> saveAll(@NotNull Iterable<UrbanPropertyPriceVariation> urbanPropertyPriceVariations) {
        return urbanPropertyPriceVariationRepository.saveAll(urbanPropertyPriceVariations);
    }

    @Transactional
    public int deleteAllByProvider(@NotNull Provider provider) {
        return urbanPropertyPriceVariationRepository.deleteAllByProvider(provider);
    }

}

package br.com.houseseeker.service;

import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanPropertyPriceVariation;
import br.com.houseseeker.repository.UrbanPropertyPriceVariationRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrbanPropertyPriceVariationService {

    private final UrbanPropertyPriceVariationRepository urbanPropertyPriceVariationRepository;

    public List<UrbanPropertyPriceVariation> findAllByProvider(@NotNull Provider provider) {
        return urbanPropertyPriceVariationRepository.findAllByProvider(provider);
    }

    @Transactional
    public List<UrbanPropertyPriceVariation> saveAll(@NotNull Iterable<UrbanPropertyPriceVariation> urbanPropertyPriceVariations) {
        return urbanPropertyPriceVariationRepository.saveAll(urbanPropertyPriceVariations);
    }

}

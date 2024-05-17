package br.com.houseseeker.service;

import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanPropertyMeasure;
import br.com.houseseeker.repository.UrbanPropertyMeasureRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrbanPropertyMeasureService {

    private final UrbanPropertyMeasureRepository urbanPropertyMeasureRepository;

    @Transactional
    public List<UrbanPropertyMeasure> findAllByProvider(@NotNull Provider provider) {
        return urbanPropertyMeasureRepository.findAllByProvider(provider);
    }

    @Transactional
    public List<UrbanPropertyMeasure> saveAll(@NotNull Iterable<UrbanPropertyMeasure> urbanPropertyMeasures) {
        return urbanPropertyMeasureRepository.saveAll(urbanPropertyMeasures);
    }

    @Transactional
    public int deleteAllByProvider(@NotNull Provider provider) {
        return urbanPropertyMeasureRepository.deleteAllByProvider(provider);
    }

}

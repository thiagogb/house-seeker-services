package br.com.houseseeker.service;

import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanPropertyConvenience;
import br.com.houseseeker.repository.UrbanPropertyConvenienceRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrbanPropertyConvenienceService {

    private final UrbanPropertyConvenienceRepository urbanPropertyConvenienceRepository;

    @Transactional
    public List<UrbanPropertyConvenience> findAllByProvider(@NotNull Provider provider) {
        return urbanPropertyConvenienceRepository.findAllByProvider(provider);
    }

    @Transactional
    public List<UrbanPropertyConvenience> saveAll(@NotNull Iterable<UrbanPropertyConvenience> urbanPropertyConveniences) {
        return urbanPropertyConvenienceRepository.saveAll(urbanPropertyConveniences);
    }

    @Transactional
    public void deleteAll(@NotNull Iterable<UrbanPropertyConvenience> urbanPropertyConveniences) {
        urbanPropertyConvenienceRepository.deleteAll(urbanPropertyConveniences);
    }

}

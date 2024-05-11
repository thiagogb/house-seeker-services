package br.com.houseseeker.service;

import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanPropertyLocation;
import br.com.houseseeker.repository.UrbanPropertyLocationRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrbanPropertyLocationService {

    private final UrbanPropertyLocationRepository urbanPropertyLocationRepository;

    @Transactional
    public List<UrbanPropertyLocation> findAllByProvider(@NotNull Provider provider) {
        return urbanPropertyLocationRepository.findAllByProvider(provider);
    }

    @Transactional
    public List<UrbanPropertyLocation> saveAll(@NotNull Iterable<UrbanPropertyLocation> urbanPropertyLocations) {
        return urbanPropertyLocationRepository.saveAll(urbanPropertyLocations);
    }

}

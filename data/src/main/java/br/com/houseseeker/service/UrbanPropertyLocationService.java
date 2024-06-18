package br.com.houseseeker.service;

import br.com.houseseeker.domain.projection.City;
import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanPropertyLocation;
import br.com.houseseeker.repository.UrbanPropertyLocationRepository;
import br.com.houseseeker.service.proto.GetCitiesRequest;
import br.com.houseseeker.service.proto.GetStatesRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertyLocationsRequest;
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
public class UrbanPropertyLocationService {

    private final UrbanPropertyLocationRepository urbanPropertyLocationRepository;

    @Transactional
    public List<UrbanPropertyLocation> findAllByProvider(@NotNull Provider provider) {
        return urbanPropertyLocationRepository.findAllByProvider(provider);
    }

    @Transactional
    public Page<UrbanPropertyLocation> findBy(@NotNull GetUrbanPropertyLocationsRequest request) {
        return urbanPropertyLocationRepository.findBy(request);
    }

    @Transactional
    public Page<String> findDistinctStatesBy(@NotNull GetStatesRequest request) {
        return urbanPropertyLocationRepository.findDistinctStatesBy(request);
    }

    @Transactional
    public Page<City> findDistinctCitiesBy(@NotNull GetCitiesRequest request) {
        return urbanPropertyLocationRepository.findDistinctCitiesBy(request);
    }

    @Transactional
    public List<UrbanPropertyLocation> saveAll(@NotNull Iterable<UrbanPropertyLocation> urbanPropertyLocations) {
        return urbanPropertyLocationRepository.saveAll(urbanPropertyLocations);
    }

    @Transactional
    public int deleteAllByProvider(@NotNull Provider provider) {
        return urbanPropertyLocationRepository.deleteAllByProvider(provider);
    }

}

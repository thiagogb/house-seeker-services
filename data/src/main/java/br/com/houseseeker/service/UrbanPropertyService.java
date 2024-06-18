package br.com.houseseeker.service;

import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.repository.UrbanPropertyRepository;
import br.com.houseseeker.service.proto.GetSubTypesRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertiesRequest;
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
public class UrbanPropertyService {

    private final UrbanPropertyRepository urbanPropertyRepository;

    @Transactional
    public List<UrbanProperty> findAllByProvider(@NotNull Provider provider) {
        return urbanPropertyRepository.findAllByProvider(provider);
    }

    @Transactional
    public Page<UrbanProperty> findBy(@NotNull GetUrbanPropertiesRequest request) {
        return urbanPropertyRepository.findBy(request);
    }

    @Transactional
    public Page<String> findDistinctSubTypesBy(@NotNull GetSubTypesRequest request) {
        return urbanPropertyRepository.findDistinctSubTypesBy(request);
    }

    @Transactional
    public List<UrbanProperty> saveAll(@NotNull Iterable<UrbanProperty> urbanProperties) {
        return urbanPropertyRepository.saveAll(urbanProperties);
    }

    @Transactional
    public int deleteAllByProvider(@NotNull Provider provider) {
        return urbanPropertyRepository.deleteAllByProvider(provider);
    }

}

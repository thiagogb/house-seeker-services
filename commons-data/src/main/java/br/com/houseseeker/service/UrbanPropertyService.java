package br.com.houseseeker.service;

import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.repository.UrbanPropertyRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrbanPropertyService {

    private final UrbanPropertyRepository urbanPropertyRepository;

    public List<UrbanProperty> findAllByProvider(@NotNull Provider provider) {
        return urbanPropertyRepository.findAllByProvider(provider);
    }

    @Transactional
    public List<UrbanProperty> saveAll(@NotNull Iterable<UrbanProperty> urbanProperties) {
        return urbanPropertyRepository.saveAll(urbanProperties);
    }

}

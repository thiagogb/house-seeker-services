package br.com.houseseeker.service;

import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.repository.ProviderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProviderService {

    private final ProviderRepository providerRepository;

    @Transactional
    public Optional<Provider> findById(int id) {
        return providerRepository.findById(id);
    }

}

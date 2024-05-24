package br.com.houseseeker.service;

import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanPropertyMedia;
import br.com.houseseeker.repository.UrbanPropertyMediaRepository;
import br.com.houseseeker.service.proto.GetUrbanPropertyMediasRequest;
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
public class UrbanPropertyMediaService {

    private final UrbanPropertyMediaRepository urbanPropertyMediaRepository;

    @Transactional
    public List<UrbanPropertyMedia> findAllByProvider(@NotNull Provider provider) {
        return urbanPropertyMediaRepository.findAllByProvider(provider);
    }

    @Transactional
    public Page<UrbanPropertyMedia> findBy(@NotNull GetUrbanPropertyMediasRequest request) {
        return urbanPropertyMediaRepository.findBy(request);
    }

    @Transactional
    public List<UrbanPropertyMedia> saveAll(@NotNull Iterable<UrbanPropertyMedia> urbanPropertyMedias) {
        return urbanPropertyMediaRepository.saveAll(urbanPropertyMedias);
    }

    @Transactional
    public void deleteAll(@NotNull Iterable<UrbanPropertyMedia> urbanPropertyMedias) {
        urbanPropertyMediaRepository.deleteAll(urbanPropertyMedias);
    }

}

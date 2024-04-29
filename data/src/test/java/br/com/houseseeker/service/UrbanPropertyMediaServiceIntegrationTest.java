package br.com.houseseeker.service;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.domain.property.UrbanPropertyMediaType;
import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanPropertyMedia;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class UrbanPropertyMediaServiceIntegrationTest extends AbstractJpaIntegrationTest {

    private static final int TEST_PROVIDER = 10000;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UrbanPropertyMediaService urbanPropertyMediaService;

    @Test
    @DisplayName("given a provider with existing property medias when calls findAllByProvider then expects 99 rows")
    void givenAProviderWithExistingPropertyMedias_whenCallsFindAllByProvider_thenReturn99Rows() {
        Provider provider = findProviderById(TEST_PROVIDER);

        assertThat(urbanPropertyMediaService.findAllByProvider(provider)).hasSize(99);
    }

    @Test
    @DisplayName("given a batch of property medias to save when calls saveAll then expects to save all and return")
    void givenABatchOfPropertyMediasToSave_whenCallsSaveAll_thenExpectsToSaveAllAndReturn() {
        List<UrbanPropertyMedia> urbanPropertyMedias = IntStream.rangeClosed(10000, 10004)
                                                                .mapToObj(id -> UrbanPropertyMedia.builder()
                                                                                                  .urbanProperty(findUrbanPropertyById(id))
                                                                                                  .link("http://test-link")
                                                                                                  .mediaType(UrbanPropertyMediaType.IMAGE)
                                                                                                  .extension("jpg")
                                                                                                  .build()
                                                                )
                                                                .toList();

        assertThat(urbanPropertyMediaService.saveAll(urbanPropertyMedias)).hasSize(5);
    }

    @Test
    @DisplayName("given a batch of property medias to delete when calls deleteAll then expects to delete all")
    void givenABatchOfPropertyMediasToDelete_whenCallsDeleteAll_thenExpectsToDeleteAll() {
        List<UrbanPropertyMedia> mediasToDelete = findAllMediasByUrbanPropertyIds(List.of(10000, 10001));

        urbanPropertyMediaService.deleteAll(mediasToDelete);

        assertThat(findAllMediasByUrbanPropertyIds(List.of(10000, 10001))).isEmpty();
    }


    protected final List<UrbanPropertyMedia> findAllMediasByUrbanPropertyIds(List<Integer> ids) {
        return entityManager.createQuery("select upm from UrbanPropertyMedia upm where upm.urbanProperty.id in :ids", UrbanPropertyMedia.class)
                            .setParameter("ids", ids)
                            .getResultList();
    }

}
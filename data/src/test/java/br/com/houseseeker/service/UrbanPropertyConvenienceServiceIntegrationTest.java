package br.com.houseseeker.service;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanPropertyConvenience;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class UrbanPropertyConvenienceServiceIntegrationTest extends AbstractJpaIntegrationTest {

    private static final int TEST_PROVIDER = 10000;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UrbanPropertyConvenienceService urbanPropertyConvenienceService;

    @Test
    @DisplayName("given a provider with existing property conveniences when calls findAllByProvider then expects 73 rows")
    void givenAProviderWithExistingPropertyConveniences_whenCallsFindAllByProvider_thenReturn73Rows() {
        Provider provider = findProviderById(TEST_PROVIDER);

        assertThat(urbanPropertyConvenienceService.findAllByProvider(provider)).hasSize(73);
    }

    @Test
    @DisplayName("given a batch of property conveniences to save when calls saveAll then expects to save all and return")
    void givenABatchOfPropertyConveniencesToSave_whenCallsSaveAll_thenExpectsToSaveAllAndReturn() {
        List<UrbanPropertyConvenience> urbanPropertyConveniences = IntStream.rangeClosed(10000, 10004)
                                                                            .mapToObj(id -> UrbanPropertyConvenience.builder()
                                                                                                                    .urbanProperty(findUrbanPropertyById(id))
                                                                                                                    .description("Example convenience")
                                                                                                                    .build()
                                                                            )
                                                                            .toList();

        assertThat(urbanPropertyConvenienceService.saveAll(urbanPropertyConveniences)).hasSize(5);
    }

    @Test
    @DisplayName("given a batch of property conveniences to delete when calls deleteAll then expects to delete all")
    void givenABatchOfPropertyMediasToDelete_whenCallsDeleteAll_thenExpectsToDeleteAll() {
        List<UrbanPropertyConvenience> conveniencesToDelete = findAllConveniencesByUrbanPropertyIds(List.of(10000, 10001));

        urbanPropertyConvenienceService.deleteAll(conveniencesToDelete);

        Assertions.assertThat(findAllConveniencesByUrbanPropertyIds(List.of(10000, 10001))).isEmpty();
    }


    protected final List<UrbanPropertyConvenience> findAllConveniencesByUrbanPropertyIds(List<Integer> ids) {
        return entityManager.createQuery("select upc from UrbanPropertyConvenience upc where upc.urbanProperty.id in :ids", UrbanPropertyConvenience.class)
                            .setParameter("ids", ids)
                            .getResultList();
    }

}
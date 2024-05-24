package br.com.houseseeker.service;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.domain.property.UrbanPropertyMediaType;
import br.com.houseseeker.domain.proto.Int32ComparisonData;
import br.com.houseseeker.domain.proto.Int32ListComparisonData;
import br.com.houseseeker.domain.proto.OrderDetailsData;
import br.com.houseseeker.domain.proto.OrderDirectionData;
import br.com.houseseeker.domain.proto.PaginationRequestData;
import br.com.houseseeker.domain.proto.StringComparisonData;
import br.com.houseseeker.domain.proto.StringSingleComparisonData;
import br.com.houseseeker.entity.UrbanPropertyMedia;
import br.com.houseseeker.service.proto.GetUrbanPropertyMediasRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertyMediasRequest.ClausesData;
import br.com.houseseeker.service.proto.GetUrbanPropertyMediasRequest.OrdersData;
import br.com.houseseeker.service.proto.GetUrbanPropertyMediasRequest.ProjectionsData;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class UrbanPropertyMediaServiceIntegrationTest extends AbstractJpaIntegrationTest {

    private static final int TEST_PROVIDER = 10000;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UrbanPropertyMediaService urbanPropertyMediaService;

    @Test
    @DisplayName("given a provider with existing property medias when calls findAllByProvider then expects 99 rows")
    void givenAProviderWithExistingPropertyMedias_whenCallsFindAllByProvider_thenReturn99Rows() {
        var provider = findProviderById(TEST_PROVIDER);

        assertThat(urbanPropertyMediaService.findAllByProvider(provider)).hasSize(99);
    }

    @Test
    @DisplayName("given a batch of property medias to save when calls saveAll then expects to save all and return")
    void givenABatchOfPropertyMediasToSave_whenCallsSaveAll_thenExpectsToSaveAllAndReturn() {
        var urbanPropertyMedias = IntStream.rangeClosed(10000, 10004)
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
    @DisplayName("given a proto request when calls findBy then expects")
    void givenAProtoRequest_whenCallsFindBy_thenExpects() {
        var request = GetUrbanPropertyMediasRequest.newBuilder()
                                                   .setProjections(
                                                           ProjectionsData.newBuilder()
                                                                          .setId(true)
                                                                          .setMediaType(true)
                                                                          .setExtension(true)
                                                                          .build()
                                                   )
                                                   .setClauses(
                                                           ClausesData.newBuilder()
                                                                      .setUrbanPropertyId(
                                                                              Int32ComparisonData.newBuilder()
                                                                                                 .setIsIn(
                                                                                                         Int32ListComparisonData.newBuilder()
                                                                                                                                .addAllValues(List.of(10001, 10002))
                                                                                                                                .build()
                                                                                                 )
                                                                                                 .build()
                                                                      )
                                                                      .setLink(
                                                                              StringComparisonData.newBuilder()
                                                                                                  .setIsStartingWith(
                                                                                                          StringSingleComparisonData.newBuilder()
                                                                                                                                    .setValue("https://")
                                                                                                                                    .build()
                                                                                                  )
                                                                                                  .build()
                                                                      )
                                                                      .setExtension(
                                                                              StringComparisonData.newBuilder()
                                                                                                  .setIsEqual(
                                                                                                          StringSingleComparisonData.newBuilder()
                                                                                                                                    .setValue("webp")
                                                                                                                                    .build()
                                                                                                  )
                                                                                                  .build()
                                                                      )
                                                                      .build()
                                                   )
                                                   .setOrders(
                                                           OrdersData.newBuilder()
                                                                     .setId(
                                                                             OrderDetailsData.newBuilder()
                                                                                             .setIndex(1)
                                                                                             .setDirection(OrderDirectionData.ASC)
                                                                                             .build()
                                                                     )
                                                                     .build()
                                                   )
                                                   .setPagination(
                                                           PaginationRequestData.newBuilder()
                                                                                .setPageSize(2)
                                                                                .setPageNumber(2)
                                                                                .build()
                                                   )
                                                   .build();

        assertThat(urbanPropertyMediaService.findBy(request))
                .extracting("id", "mediaType", "extension")
                .containsExactly(
                        tuple(10002, UrbanPropertyMediaType.IMAGE, "webp"),
                        tuple(10003, UrbanPropertyMediaType.IMAGE, "webp")
                );
    }

    @Test
    @DisplayName("given a batch of property medias to delete when calls deleteAll then expects to delete all")
    void givenABatchOfPropertyMediasToDelete_whenCallsDeleteAll_thenExpectsToDeleteAll() {
        var mediasToDelete = findAllMediasByUrbanPropertyIds(List.of(10000, 10001));

        urbanPropertyMediaService.deleteAll(mediasToDelete);

        Assertions.assertThat(findAllMediasByUrbanPropertyIds(List.of(10000, 10001))).isEmpty();
    }


    protected final List<UrbanPropertyMedia> findAllMediasByUrbanPropertyIds(List<Integer> ids) {
        return entityManager.createQuery("select upm from UrbanPropertyMedia upm where upm.urbanProperty.id in :ids", UrbanPropertyMedia.class)
                            .setParameter("ids", ids)
                            .getResultList();
    }

}
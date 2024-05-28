package br.com.houseseeker.service;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.domain.proto.DoubleComparisonData;
import br.com.houseseeker.domain.proto.DoubleIntervalComparisonData;
import br.com.houseseeker.domain.proto.EnumComparisonData;
import br.com.houseseeker.domain.proto.EnumSingleComparisonData;
import br.com.houseseeker.domain.proto.Int32ComparisonData;
import br.com.houseseeker.domain.proto.Int32SingleComparisonData;
import br.com.houseseeker.domain.proto.StringComparisonData;
import br.com.houseseeker.domain.proto.StringSingleComparisonData;
import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.service.proto.GetUrbanPropertiesRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertiesRequest.ClausesData;
import br.com.houseseeker.service.proto.GetUrbanPropertiesRequest.ProjectionsData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static br.com.houseseeker.domain.property.UrbanPropertyContract.SELL;
import static br.com.houseseeker.domain.property.UrbanPropertyType.RESIDENTIAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class UrbanPropertyServiceIntegrationTest extends AbstractJpaIntegrationTest {

    private static final int TEST_PROVIDER = 10000;

    @Autowired
    private UrbanPropertyService urbanPropertyService;

    @Test
    @DisplayName("given a provider with existing properties when calls findAllByProvider then expects five rows")
    void givenAProviderWithExistingProperties_whenCallsFindAllByProvider_thenExpectFiveRows() {
        var provider = findProviderById(TEST_PROVIDER);

        assertThat(urbanPropertyService.findAllByProvider(provider))
                .extracting("providerCode", "contract", "type", "subType", "sellPrice", "rentPrice")
                .containsExactly(
                        tuple("145687", SELL, RESIDENTIAL, "Casa de Condomínio", new BigDecimal("1180000.00"), new BigDecimal("1180.00")),
                        tuple("2302", SELL, RESIDENTIAL, "Apartamento", new BigDecimal("1166000.00"), new BigDecimal("1166.00")),
                        tuple("3272", SELL, RESIDENTIAL, "Casa", new BigDecimal("1590000.00"), new BigDecimal("1590.00")),
                        tuple("500489", SELL, RESIDENTIAL, "Casa de Condomínio", new BigDecimal("1800000.00"), new BigDecimal("1800.00")),
                        tuple("98297", SELL, RESIDENTIAL, "Cobertura", new BigDecimal("960000.00"), new BigDecimal("960.00"))
                );
    }

    @Test
    @DisplayName("given a proto request when calls findBy then expects")
    void givenAProtoRequest_whenCallsFindBy_thenExpects() {
        var request = GetUrbanPropertiesRequest.newBuilder()
                                               .setProjections(
                                                       ProjectionsData.newBuilder()
                                                                      .setId(true)
                                                                      .setProviderCode(true)
                                                                      .setUrl(true)
                                                                      .setSubType(true)
                                                                      .setDormitories(true)
                                                                      .setGarages(true)
                                                                      .setSellPrice(true)
                                                                      .build()
                                               )
                                               .setClauses(
                                                       ClausesData.newBuilder()
                                                                  .setProviderId(
                                                                          Int32ComparisonData.newBuilder()
                                                                                             .setIsEqual(
                                                                                                     Int32SingleComparisonData.newBuilder()
                                                                                                                              .setValue(10000)
                                                                                                                              .build()
                                                                                             )
                                                                                             .build()
                                                                  )
                                                                  .setUrl(
                                                                          StringComparisonData.newBuilder()
                                                                                              .setItContains(
                                                                                                      StringSingleComparisonData.newBuilder()
                                                                                                                                .setValue("oliveiraimoveissm")
                                                                                                                                .build()
                                                                                              )
                                                                                              .build()
                                                                  )
                                                                  .setContract(
                                                                          EnumComparisonData.newBuilder()
                                                                                            .setIsEqual(
                                                                                                    EnumSingleComparisonData.newBuilder()
                                                                                                                            .setValue(SELL.name())
                                                                                                                            .build()
                                                                                            )
                                                                                            .build()
                                                                  )
                                                                  .setType(
                                                                          EnumComparisonData.newBuilder()
                                                                                            .setIsEqual(
                                                                                                    EnumSingleComparisonData.newBuilder()
                                                                                                                            .setValue(RESIDENTIAL.name())
                                                                                                                            .build()
                                                                                            )
                                                                                            .build()
                                                                  )
                                                                  .setSellPrice(
                                                                          DoubleComparisonData.newBuilder()
                                                                                              .setIsBetween(
                                                                                                      DoubleIntervalComparisonData.newBuilder()
                                                                                                                                  .setStart(1500000)
                                                                                                                                  .setEnd(2000000)
                                                                                                                                  .build()
                                                                                              )
                                                                                              .build()
                                                                  )
                                                                  .build()
                                               )
                                               .build();

        assertThat(urbanPropertyService.findBy(request))
                .extracting("id", "providerCode", "url", "subType", "dormitories", "garages", "sellPrice")
                .containsExactly(
                        tuple(10004, "3272", "https://www.oliveiraimoveissm.com.br/imovel/3272", "Casa", 4, 4, new BigDecimal("1590000.00")),
                        tuple(10000, "500489", "https://www.oliveiraimoveissm.com.br/imovel/500489", "Casa de Condomínio", 4, 2, new BigDecimal("1800000.00"))
                );
    }

    @Test
    @DisplayName("given a batch of properties to save when calls saveAll then expects to save all then return")
    void givenABatchOfPropertiesToSave_whenCallsSaveAll_thenExpectsToSaveAllAndReturn() {
        var provider = findProviderById(TEST_PROVIDER);

        var batchOfProperties = IntStream.range(0, 100)
                                         .mapToObj(i -> UrbanProperty.builder()
                                                                     .provider(provider)
                                                                     .providerCode(String.format("PC%d", i + 1))
                                                                     .url(String.format("http://test.com/property/PC%d", i + 1))
                                                                     .contract(SELL)
                                                                     .creationDate(LocalDateTime.now())
                                                                     .analyzable(true)
                                                                     .build()
                                         )
                                         .toList();

        assertThat(urbanPropertyService.saveAll(batchOfProperties)).hasSize(100);
    }

    @Test
    @DisplayName("given a provider with properties when calls deleteAllByProvider then expects")
    void givenAProviderWithProperties_whenCallsDeleteAllByProvider_thenExpects() {
        var provider = findProviderById(TEST_PROVIDER);

        assertThat(urbanPropertyService.deleteAllByProvider(provider)).isEqualTo(5);
    }

}
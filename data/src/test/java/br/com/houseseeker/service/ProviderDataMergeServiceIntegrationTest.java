package br.com.houseseeker.service;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.domain.property.AbstractUrbanPropertyMetadata;
import br.com.houseseeker.domain.property.UrbanPropertyContract;
import br.com.houseseeker.domain.property.UrbanPropertyMediaType;
import br.com.houseseeker.domain.property.UrbanPropertyStatus;
import br.com.houseseeker.domain.property.UrbanPropertyType;
import br.com.houseseeker.domain.provider.UrbanPropertyMediaMetadata;
import br.com.houseseeker.domain.provider.UrbanPropertyMetadata;
import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.entity.UrbanPropertyConvenience;
import br.com.houseseeker.entity.UrbanPropertyLocation;
import br.com.houseseeker.entity.UrbanPropertyMeasure;
import br.com.houseseeker.entity.UrbanPropertyMedia;
import br.com.houseseeker.entity.UrbanPropertyPriceVariation;
import br.com.houseseeker.service.ProviderDataCollectorService.UrbanPropertyFullData;
import jakarta.persistence.EntityManager;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static br.com.houseseeker.entity.UrbanPropertyPriceVariation.Type.CONDOMINIUM;
import static br.com.houseseeker.entity.UrbanPropertyPriceVariation.Type.RENT;
import static br.com.houseseeker.entity.UrbanPropertyPriceVariation.Type.SELL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProviderDataMergeServiceIntegrationTest extends AbstractJpaIntegrationTest {

    private static final int TEST_PROVIDER = 10000;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ProviderDataMergeService providerDataMergeService;

    @Autowired
    private ProviderDataCollectorService providerDataCollectorService;

    @MockBean
    private Clock clock;

    @BeforeEach
    @Override
    public void setup() {
        super.setup();
        when(clock.instant()).thenReturn(Instant.parse("2024-01-01T12:30:45.000Z"));
        when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    }

    @Test
    @DisplayName("given a extracted data with all scenarios when calls merge then expects")
    void givenAExtractedDataWithAllScenarios_whenCallsMerge_thenExpects() {
        Provider provider = findProviderById(TEST_PROVIDER);
        Map<UrbanProperty, UrbanPropertyFullData> beforeCollectedData = collectProviderData(provider, true);
        AbstractUrbanPropertyMetadata property500489 = property500489();
        AbstractUrbanPropertyMetadata property9999 = property9999();
        List<AbstractUrbanPropertyMetadata> extractedData = List.of(property500489, property9999);

        providerDataMergeService.merge(provider, collectProviderData(provider, false), extractedData);

        Map<UrbanProperty, UrbanPropertyFullData> afterCollectedData = collectProviderData(provider, false);

        assertThat(afterCollectedData.keySet())
                .extracting("providerCode")
                .containsExactly("145687", "2302", "3272", "500489", "98297", "9999");

        testIfPropertyWasDeleted("145687", beforeCollectedData, afterCollectedData);
        testIfPropertyWasDeleted("2302", beforeCollectedData, afterCollectedData);
        testIfPropertyWasDeleted("3272", beforeCollectedData, afterCollectedData);
        testIfPropertyWasEdited(
                "500489",
                property500489,
                beforeCollectedData,
                afterCollectedData,
                tuple(LocalDateTime.now(clock), RENT, BigDecimal.valueOf(5000), BigDecimal.valueOf(177.78)),
                tuple(LocalDateTime.now(clock), CONDOMINIUM, BigDecimal.valueOf(1000), new BigDecimal("100.00"))
        );
        testIfPropertyWasDeleted("98297", beforeCollectedData, afterCollectedData);
        testIfPropertyWasCreated(
                "9999",
                property9999,
                afterCollectedData,
                tuple(LocalDateTime.now(clock), SELL, BigDecimal.valueOf(150000), BigDecimal.ZERO)
        );
    }

    private Map<UrbanProperty, UrbanPropertyFullData> collectProviderData(Provider provider, boolean detach) {
        Map<UrbanProperty, UrbanPropertyFullData> result = providerDataCollectorService.collect(provider);
        if (detach) {
            result.forEach((key, value) -> {
                entityManager.detach(key);
                entityManager.detach(value.getLocation());
                entityManager.detach(value.getMeasure());
                value.getConveniences().forEach(entityManager::detach);
                value.getMedias().forEach(entityManager::detach);
                value.getPriceVariations().forEach(entityManager::detach);
            });
        }
        return result;
    }

    private AbstractUrbanPropertyMetadata property500489() {
        return UrbanPropertyMetadata.builder()
                                    .providerCode("500489")
                                    .url("https://www.oliveiraimoveissm.com.br/imovel/500489/edited")
                                    .contract(UrbanPropertyContract.RENT)
                                    .type(UrbanPropertyType.COMMERCIAL)
                                    .subType("Casa de Condomínio Comercial")
                                    .dormitories(5)
                                    .suites(2)
                                    .bathrooms(2)
                                    .garages(4)
                                    .sellPrice(null)
                                    .rentPrice(BigDecimal.valueOf(5000))
                                    .condominiumPrice(BigDecimal.valueOf(1000))
                                    .condominiumName("Real Park")
                                    .isExchangeable(false)
                                    .status(UrbanPropertyStatus.USED)
                                    .isFinanceable(false)
                                    .isOccupied(false)
                                    .notes("A sua nova empresa em um condomínio com segurança e infraestrutura completa")
                                    .conveniences(List.of("CÂMERA DE VIGILÂNCIA"))
                                    .state("Rio Grande do Sul")
                                    .city("Porto Alegre")
                                    .district("Jardim Planalto")
                                    .zipCode("97090110")
                                    .streetName("Rua Principal")
                                    .streetNumber(1)
                                    .complement("Na esquina")
                                    .latitude(BigDecimal.valueOf(-59.720691100))
                                    .longitude(BigDecimal.valueOf(93.794736800))
                                    .totalArea(BigDecimal.valueOf(272.00))
                                    .privateArea(BigDecimal.valueOf(302.00))
                                    .usableArea(BigDecimal.valueOf(252.00))
                                    .terrainTotalArea(BigDecimal.valueOf(500))
                                    .terrainFront(BigDecimal.valueOf(25))
                                    .terrainBack(BigDecimal.valueOf(25))
                                    .terrainLeft(BigDecimal.valueOf(25))
                                    .terrainRight(BigDecimal.valueOf(25))
                                    .areaUnit("m³")
                                    .medias(List.of(
                                            UrbanPropertyMediaMetadata.builder()
                                                                      .link("https://s01.jetimgs.com/500489jpeg.webp")
                                                                      .mediaType(UrbanPropertyMediaType.IMAGE)
                                                                      .extension("webp")
                                                                      .build()
                                    ))
                                    .build();
    }

    private AbstractUrbanPropertyMetadata property9999() {
        return UrbanPropertyMetadata.builder()
                                    .providerCode("9999")
                                    .url("https://www.oliveiraimoveissm.com.br/imovel/9999")
                                    .contract(UrbanPropertyContract.SELL)
                                    .type(UrbanPropertyType.RESIDENTIAL)
                                    .subType("Casa")
                                    .dormitories(2)
                                    .suites(1)
                                    .bathrooms(1)
                                    .garages(1)
                                    .sellPrice(BigDecimal.valueOf(150000))
                                    .conveniences(List.of("PORCELANATO"))
                                    .state("RS")
                                    .city("Santa Maria")
                                    .district("Medianeira")
                                    .totalArea(BigDecimal.valueOf(85.00))
                                    .medias(List.of(
                                            UrbanPropertyMediaMetadata.builder()
                                                                      .link("https://s01.jetimgs.com/9999jpeg.webp")
                                                                      .mediaType(UrbanPropertyMediaType.IMAGE)
                                                                      .extension("webp")
                                                                      .build()
                                    ))
                                    .build();
    }

    private void testIfPropertyWasDeleted(
            String providerCode,
            Map<UrbanProperty, UrbanPropertyFullData> dataBeforeMerging,
            Map<UrbanProperty, UrbanPropertyFullData> dataAfterMerging
    ) {
        Map.Entry<UrbanProperty, UrbanPropertyFullData> oldData = findByProviderCode(dataBeforeMerging, providerCode);
        Map.Entry<UrbanProperty, UrbanPropertyFullData> newData = findByProviderCode(dataAfterMerging, providerCode);

        assertThat(oldData.getKey())
                .usingRecursiveComparison()
                .ignoringFields("lastAnalysisDate", "exclusionDate")
                .isEqualTo(newData.getKey());

        assertThat(newData.getKey())
                .extracting("lastAnalysisDate", "exclusionDate")
                .containsExactly(LocalDateTime.now(clock), LocalDateTime.now(clock));

        assertThat(oldData.getValue())
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(UrbanProperty.class)
                .isEqualTo(newData.getValue());
    }

    private void testIfPropertyWasEdited(
            String providerCode,
            AbstractUrbanPropertyMetadata extractedData,
            Map<UrbanProperty, UrbanPropertyFullData> dataBeforeMerging,
            Map<UrbanProperty, UrbanPropertyFullData> dataAfterMerging,
            Tuple... newPriceVariations
    ) {
        Map.Entry<UrbanProperty, UrbanPropertyFullData> oldData = findByProviderCode(dataBeforeMerging, providerCode);
        Map.Entry<UrbanProperty, UrbanPropertyFullData> newData = findByProviderCode(dataAfterMerging, providerCode);

        assertThat(newData.getKey())
                .extracting(
                        UrbanProperty::getProviderCode,
                        UrbanProperty::getUrl,
                        UrbanProperty::getContract,
                        UrbanProperty::getType,
                        UrbanProperty::getSubType,
                        UrbanProperty::getDormitories,
                        UrbanProperty::getSuites,
                        UrbanProperty::getBathrooms,
                        UrbanProperty::getGarages,
                        UrbanProperty::getSellPrice,
                        UrbanProperty::getRentPrice,
                        UrbanProperty::getCondominiumPrice,
                        UrbanProperty::getCondominiumName,
                        UrbanProperty::getExchangeable,
                        UrbanProperty::getStatus,
                        UrbanProperty::getFinanceable,
                        UrbanProperty::getOccupied,
                        UrbanProperty::getNotes,
                        UrbanProperty::getCreationDate,
                        UrbanProperty::getLastAnalysisDate,
                        UrbanProperty::getExclusionDate,
                        UrbanProperty::getAnalyzable

                )
                .containsExactly(
                        extractedData.getProviderCode(),
                        extractedData.getUrl(),
                        extractedData.getContract(),
                        extractedData.getType(),
                        extractedData.getSubType(),
                        extractedData.getDormitories(),
                        extractedData.getSuites(),
                        extractedData.getBathrooms(),
                        extractedData.getGarages(),
                        extractedData.getSellPrice(),
                        extractedData.getRentPrice(),
                        extractedData.getCondominiumPrice(),
                        extractedData.getCondominiumName(),
                        extractedData.isExchangeable(),
                        extractedData.getStatus(),
                        extractedData.isFinanceable(),
                        extractedData.isOccupied(),
                        extractedData.getNotes(),
                        oldData.getKey().getCreationDate(),
                        LocalDateTime.now(clock),
                        null,
                        true
                );

        testLocationAfterSave(extractedData, newData.getValue().getLocation());
        testMeasureAfterSave(extractedData, newData.getValue().getMeasure());
        testConveniencesAfterSave(extractedData, newData.getValue().getConveniences());
        testMediasAfterSave(extractedData, newData.getValue().getMedias());
        testPriceVariationsAfterSave(oldData.getValue().getPriceVariations(), newData.getValue().getPriceVariations(), newPriceVariations);
    }

    private void testIfPropertyWasCreated(
            String providerCode,
            AbstractUrbanPropertyMetadata extractedData,
            Map<UrbanProperty, UrbanPropertyFullData> dataAfterMerging,
            Tuple... newPriceVariations
    ) {
        Map.Entry<UrbanProperty, UrbanPropertyFullData> newData = findByProviderCode(dataAfterMerging, providerCode);

        assertThat(newData.getKey())
                .extracting(
                        UrbanProperty::getProviderCode,
                        UrbanProperty::getUrl,
                        UrbanProperty::getContract,
                        UrbanProperty::getType,
                        UrbanProperty::getSubType,
                        UrbanProperty::getDormitories,
                        UrbanProperty::getSuites,
                        UrbanProperty::getBathrooms,
                        UrbanProperty::getGarages,
                        UrbanProperty::getSellPrice,
                        UrbanProperty::getRentPrice,
                        UrbanProperty::getCondominiumPrice,
                        UrbanProperty::getCondominiumName,
                        UrbanProperty::getExchangeable,
                        UrbanProperty::getStatus,
                        UrbanProperty::getFinanceable,
                        UrbanProperty::getOccupied,
                        UrbanProperty::getNotes,
                        UrbanProperty::getCreationDate,
                        UrbanProperty::getLastAnalysisDate,
                        UrbanProperty::getExclusionDate,
                        UrbanProperty::getAnalyzable

                )
                .containsExactly(
                        extractedData.getProviderCode(),
                        extractedData.getUrl(),
                        extractedData.getContract(),
                        extractedData.getType(),
                        extractedData.getSubType(),
                        extractedData.getDormitories(),
                        extractedData.getSuites(),
                        extractedData.getBathrooms(),
                        extractedData.getGarages(),
                        extractedData.getSellPrice(),
                        extractedData.getRentPrice(),
                        extractedData.getCondominiumPrice(),
                        extractedData.getCondominiumName(),
                        extractedData.isExchangeable(),
                        extractedData.getStatus(),
                        extractedData.isFinanceable(),
                        extractedData.isOccupied(),
                        extractedData.getNotes(),
                        LocalDateTime.now(clock),
                        LocalDateTime.now(clock),
                        null,
                        true
                );

        testLocationAfterSave(extractedData, newData.getValue().getLocation());
        testMeasureAfterSave(extractedData, newData.getValue().getMeasure());
        testConveniencesAfterSave(extractedData, newData.getValue().getConveniences());
        testMediasAfterSave(extractedData, newData.getValue().getMedias());
        testPriceVariationsAfterSave(Collections.emptyList(), newData.getValue().getPriceVariations(), newPriceVariations);
    }

    private void testLocationAfterSave(
            AbstractUrbanPropertyMetadata extractedData,
            UrbanPropertyLocation dataAfterMerging
    ) {
        assertThat(dataAfterMerging)
                .extracting(
                        UrbanPropertyLocation::getState,
                        UrbanPropertyLocation::getCity,
                        UrbanPropertyLocation::getDistrict,
                        UrbanPropertyLocation::getZipCode,
                        UrbanPropertyLocation::getStreetName,
                        UrbanPropertyLocation::getStreetNumber,
                        UrbanPropertyLocation::getComplement,
                        UrbanPropertyLocation::getLatitude,
                        UrbanPropertyLocation::getLongitude
                )
                .containsExactly(
                        extractedData.getState(),
                        extractedData.getCity(),
                        extractedData.getDistrict(),
                        extractedData.getZipCode(),
                        extractedData.getStreetName(),
                        extractedData.getStreetNumber(),
                        extractedData.getComplement(),
                        extractedData.getLatitude(),
                        extractedData.getLongitude()
                );
    }

    private void testMeasureAfterSave(
            AbstractUrbanPropertyMetadata extractedData,
            UrbanPropertyMeasure dataAfterMerging
    ) {
        assertThat(dataAfterMerging)
                .extracting(
                        UrbanPropertyMeasure::getTotalArea,
                        UrbanPropertyMeasure::getPrivateArea,
                        UrbanPropertyMeasure::getUsableArea,
                        UrbanPropertyMeasure::getTerrainTotalArea,
                        UrbanPropertyMeasure::getTerrainFront,
                        UrbanPropertyMeasure::getTerrainBack,
                        UrbanPropertyMeasure::getTerrainLeft,
                        UrbanPropertyMeasure::getTerrainRight
                )
                .containsExactly(
                        extractedData.getTotalArea(),
                        extractedData.getPrivateArea(),
                        extractedData.getUsableArea(),
                        extractedData.getTerrainTotalArea(),
                        extractedData.getTerrainFront(),
                        extractedData.getTerrainBack(),
                        extractedData.getTerrainLeft(),
                        extractedData.getTerrainRight()
                );
    }

    private void testConveniencesAfterSave(
            AbstractUrbanPropertyMetadata extractedData,
            List<UrbanPropertyConvenience> dataAfterMerging
    ) {
        assertThat(dataAfterMerging)
                .extracting(UrbanPropertyConvenience::getDescription)
                .containsExactly(extractedData.getConveniences().toArray(String[]::new));
    }

    private void testMediasAfterSave(
            AbstractUrbanPropertyMetadata extractedData,
            List<UrbanPropertyMedia> dataAfterMerging
    ) {
        assertThat(dataAfterMerging)
                .extracting(
                        UrbanPropertyMedia::getLink,
                        UrbanPropertyMedia::getLinkThumb,
                        UrbanPropertyMedia::getMediaType,
                        UrbanPropertyMedia::getExtension
                )
                .containsExactly(
                        extractedData.getMedias()
                                     .stream()
                                     .map(m -> tuple(m.getLink(), m.getLinkThumb(), m.getMediaType(), m.getExtension()))
                                     .toList()
                                     .toArray(Tuple[]::new)
                );
    }

    private void testPriceVariationsAfterSave(
            List<UrbanPropertyPriceVariation> dataBeforeMerging,
            List<UrbanPropertyPriceVariation> dataAfterMerging,
            Tuple... newPriceVariations
    ) {
        List<Tuple> tuples = Stream.concat(
                dataBeforeMerging.stream().map(pv -> tuple(pv.getAnalysisDate(), pv.getType(), pv.getPrice(), pv.getVariation())),
                Arrays.stream(newPriceVariations)
        ).toList();

        assertThat(dataAfterMerging)
                .extracting(
                        UrbanPropertyPriceVariation::getAnalysisDate,
                        UrbanPropertyPriceVariation::getType,
                        UrbanPropertyPriceVariation::getPrice,
                        UrbanPropertyPriceVariation::getVariation
                )
                .containsExactlyInAnyOrder(tuples.toArray(Tuple[]::new));
    }

    private Map.Entry<UrbanProperty, UrbanPropertyFullData> findByProviderCode(
            Map<UrbanProperty, UrbanPropertyFullData> dataBeforeMerging,
            String providerCode
    ) {
        return dataBeforeMerging.entrySet()
                                .stream()
                                .filter(e -> e.getKey().getProviderCode().equals(providerCode))
                                .findFirst()
                                .orElseThrow(NoSuchElementException::new);
    }

}
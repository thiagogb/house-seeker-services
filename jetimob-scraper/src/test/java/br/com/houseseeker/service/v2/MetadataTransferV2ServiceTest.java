package br.com.houseseeker.service.v2;

import br.com.houseseeker.configuration.ObjectMapperConfiguration;
import br.com.houseseeker.domain.jetimob.PropertyCharacteristic;
import br.com.houseseeker.domain.jetimob.PropertyDetail;
import br.com.houseseeker.domain.jetimob.v2.PropertyInfoMetadata;
import br.com.houseseeker.domain.property.AbstractUrbanPropertyMediaMetadata;
import br.com.houseseeker.domain.property.UrbanPropertyContract;
import br.com.houseseeker.domain.property.UrbanPropertyMediaType;
import br.com.houseseeker.domain.property.UrbanPropertyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {
        ObjectMapperConfiguration.class,
        MetadataTransferV2Service.class
})
@ExtendWith(MockitoExtension.class)
class MetadataTransferV2ServiceTest {

    @Autowired
    private MetadataTransferV2Service metadataTransferV2Service;

    @Mock
    private PropertyInfoMetadata mockedPropertyInfoMetadata;

    @Mock
    private PropertyInfoMetadata.Pricing mockedPropertyInfoMetadataPricing;

    @Mock
    private PropertyInfoMetadata.Location mockedPropertyInfoMetadataLocation;

    @BeforeEach
    void setup() {
        lenient().when(mockedPropertyInfoMetadata.getPricing()).thenReturn(mockedPropertyInfoMetadataPricing);
        lenient().when(mockedPropertyInfoMetadata.getLocation()).thenReturn(mockedPropertyInfoMetadataLocation);
    }

    @Test
    @DisplayName("given a property metadata when calls getProviderCode then expects")
    void givenAPropertyMetadata_whenCalllsGetProviderCode_thenExpects() {
        when(mockedPropertyInfoMetadata.getProviderCode()).thenReturn("12345");

        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("providerCode")
                .isEqualTo("12345");

        verify(mockedPropertyInfoMetadata, atLeastOnce()).getProviderCode();
        verifyNoMoreInteractions(mockedPropertyInfoMetadata);
    }

    @Test
    @DisplayName("given a property metadata when calls getUrl then expects")
    void givenAPropertyMetadata_whenCallsGetUrl_thenExpects() {
        when(mockedPropertyInfoMetadata.getUrl()).thenReturn("http://localhost");

        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("url")
                .isEqualTo("http://localhost");

        verify(mockedPropertyInfoMetadata, atLeastOnce()).getUrl();
        verifyNoMoreInteractions(mockedPropertyInfoMetadata);
    }

    @Test
    @DisplayName("given a property metadata when calls getContract then expects")
    void givenAPropertyMetadata_whenCallsGetContract_thenExpects() {
        when(mockedPropertyInfoMetadata.getContract()).thenReturn(UrbanPropertyContract.RENT);

        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("contract")
                .isEqualTo(UrbanPropertyContract.RENT);

        verify(mockedPropertyInfoMetadata, atLeastOnce()).getContract();
        verifyNoMoreInteractions(mockedPropertyInfoMetadata);
    }

    @Test
    @DisplayName("given a property metadata when calls getType then expects")
    void givenAPropertyMetadata_whenCallsGetType_thenExpects() {
        when(mockedPropertyInfoMetadata.getSubType()).thenReturn("Casa comercial");

        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("type")
                .isEqualTo(UrbanPropertyType.COMMERCIAL);

        verify(mockedPropertyInfoMetadata, atLeastOnce()).getSubType();
        verifyNoMoreInteractions(mockedPropertyInfoMetadata);
    }

    @Test
    @DisplayName("given a property metadata when calls getSubType then expects")
    void givenAPropertyMetadata_whenCallsGetSubType_thenExpects() {
        when(mockedPropertyInfoMetadata.getSubType()).thenReturn("Apartamento");

        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("subType")
                .isEqualTo("Apartamento");

        verify(mockedPropertyInfoMetadata, atLeastOnce()).getSubType();
        verifyNoMoreInteractions(mockedPropertyInfoMetadata);
    }

    @Test
    @DisplayName("given a property metadata when calls getDormitories then expects")
    void givenAPropertyMetadata_whenCallsGetDormitories_thenExpects() {
        var propertyInfoMetadata = spy(
                PropertyInfoMetadata.builder()
                                    .characteristics(List.of(
                                            PropertyCharacteristic.builder()
                                                                  .type(PropertyCharacteristic.Type.DORMITORIES)
                                                                  .value("2")
                                                                  .build()
                                    ))
                                    .build()
        );

        assertThat(metadataTransferV2Service.transfer(propertyInfoMetadata))
                .extracting("dormitories")
                .isEqualTo(2);

        verify(propertyInfoMetadata, atLeastOnce()).extractCharacteristicsByTypes(
                any(),
                eq(PropertyCharacteristic.Type.DORMITORIES),
                eq(PropertyCharacteristic.Type.ROOMS)
        );
        verifyNoMoreInteractions(propertyInfoMetadata);
    }

    @Test
    @DisplayName("given a property metadata when calls getSuites then expects")
    void givenAPropertyMetadata_whenCallsGetSuites_thenExpects() {
        var propertyInfoMetadata = spy(
                PropertyInfoMetadata.builder()
                                    .characteristics(List.of(
                                            PropertyCharacteristic.builder()
                                                                  .type(PropertyCharacteristic.Type.SUITES)
                                                                  .value("2")
                                                                  .build()
                                    ))
                                    .build()
        );

        assertThat(metadataTransferV2Service.transfer(propertyInfoMetadata))
                .extracting("suites")
                .isEqualTo(2);

        verify(propertyInfoMetadata, atLeastOnce()).extractCharacteristicsByTypes(any(), eq(PropertyCharacteristic.Type.SUITES));
        verifyNoMoreInteractions(propertyInfoMetadata);
    }

    @Test
    @DisplayName("given a property metadata when calls getBathrooms then expects")
    void givenAPropertyMetadata_whenCallsGetBathrooms_thenExpects() {
        var propertyInfoMetadata = spy(
                PropertyInfoMetadata.builder()
                                    .characteristics(List.of(
                                            PropertyCharacteristic.builder()
                                                                  .type(PropertyCharacteristic.Type.BATHROOMS)
                                                                  .value("2")
                                                                  .build()
                                    ))
                                    .build()
        );

        assertThat(metadataTransferV2Service.transfer(propertyInfoMetadata))
                .extracting("bathrooms")
                .isEqualTo(2);

        verify(propertyInfoMetadata, atLeastOnce()).extractCharacteristicsByTypes(any(), eq(PropertyCharacteristic.Type.BATHROOMS));
        verifyNoMoreInteractions(propertyInfoMetadata);
    }

    @Test
    @DisplayName("given a property metadata when calls getGarages then expects")
    void givenAPropertyMetadata_whenCallsGetGarages_thenExpects() {
        var propertyInfoMetadata = spy(
                PropertyInfoMetadata.builder()
                                    .characteristics(List.of(
                                            PropertyCharacteristic.builder()
                                                                  .type(PropertyCharacteristic.Type.GARAGES)
                                                                  .value("2")
                                                                  .build()
                                    ))
                                    .build()
        );

        assertThat(metadataTransferV2Service.transfer(propertyInfoMetadata))
                .extracting("garages")
                .isEqualTo(2);

        verify(propertyInfoMetadata, atLeastOnce()).extractCharacteristicsByTypes(any(), eq(PropertyCharacteristic.Type.GARAGES));
        verifyNoMoreInteractions(propertyInfoMetadata);
    }

    @Test
    @DisplayName("given a property metadata when calls getSellPrice then expects")
    void givenAPropertyMetadata_whenCallsGetSellPrice_thenExpects() {
        when(mockedPropertyInfoMetadataPricing.getSellPriceAsBigDecimal()).thenReturn(BigDecimal.valueOf(100000));

        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("sellPrice")
                .isEqualTo(BigDecimal.valueOf(100000));

        verify(mockedPropertyInfoMetadataPricing, atLeastOnce()).getSellPriceAsBigDecimal();
        verifyNoMoreInteractions(mockedPropertyInfoMetadataPricing);
    }

    @Test
    @DisplayName("given a property metadata when calls getRentPrice then expects")
    void givenAPropertyMetadata_whenCallsGetRentPrice_thenExpects() {
        when(mockedPropertyInfoMetadataPricing.getRentPriceAsBigDecimal()).thenReturn(BigDecimal.valueOf(1000));

        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("rentPrice")
                .isEqualTo(BigDecimal.valueOf(1000));

        verify(mockedPropertyInfoMetadataPricing, atLeastOnce()).getRentPriceAsBigDecimal();
        verifyNoMoreInteractions(mockedPropertyInfoMetadataPricing);
    }

    @Test
    @DisplayName("given a property metadata when calls getCondominiumPrice then expects")
    void givenAPropertyMetadata_whenCallsGetCondominiumPrice_thenExpects() {
        when(mockedPropertyInfoMetadataPricing.getCondominiumPriceAsBigDecimal()).thenReturn(BigDecimal.valueOf(350));

        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("condominiumPrice")
                .isEqualTo(BigDecimal.valueOf(350));

        verify(mockedPropertyInfoMetadataPricing, atLeastOnce()).getCondominiumPriceAsBigDecimal();
        verifyNoMoreInteractions(mockedPropertyInfoMetadataPricing);
    }

    @Test
    @DisplayName("given a property metadata when calls getCondominiumName then expects")
    void givenAPropertyMetadata_whenCallsGetCondominiumName_thenExpects() {
        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("condominiumName")
                .isNull();

        verifyNoInteractions(mockedPropertyInfoMetadata);
        verifyNoInteractions(mockedPropertyInfoMetadataPricing);
    }

    @Test
    @DisplayName("given a property metadata when calls isExchangeable then expects")
    void givenAPropertyMetadata_whenCallsIsExchangeable_thenExpects() {
        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("exchangeable")
                .isNull();

        verifyNoInteractions(mockedPropertyInfoMetadata);
        verifyNoInteractions(mockedPropertyInfoMetadataPricing);
    }

    @Test
    @DisplayName("given a property metadata when calls getStatus then expects")
    void givenAPropertyMetadata_whenCallsGetStatus_thenExpects() {
        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("status")
                .isNull();

        verifyNoInteractions(mockedPropertyInfoMetadata);
        verifyNoInteractions(mockedPropertyInfoMetadataPricing);
    }

    @Test
    @DisplayName("given a property metadata when calls isFinanceable then expects")
    void givenAPropertyMetadata_whenCallsIsFinanceable_thenExpects() {
        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("financeable")
                .isNull();

        verifyNoInteractions(mockedPropertyInfoMetadata);
        verifyNoInteractions(mockedPropertyInfoMetadataPricing);
    }

    @Test
    @DisplayName("given a property metadata when calls isOccupied then expects")
    void givenAPropertyMetadata_whenCallsIsOccupied_thenExpects() {
        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("occupied")
                .isNull();

        verifyNoInteractions(mockedPropertyInfoMetadata);
        verifyNoInteractions(mockedPropertyInfoMetadataPricing);
    }

    @Test
    @DisplayName("given a property metadata when calls getNotes then expects")
    void givenAPropertyMetadata_whenCallsGetNotes_thenExpects() {
        when(mockedPropertyInfoMetadata.getDescription()).thenReturn("value");

        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("notes")
                .isEqualTo("value");

        verify(mockedPropertyInfoMetadata, atLeastOnce()).getDescription();
        verifyNoMoreInteractions(mockedPropertyInfoMetadata);
    }

    @Test
    @DisplayName("given a property metadata when calls getConveniences then expects")
    void givenAPropertyMetadata_whenCallsGetConveniences_thenExpects() {
        when(mockedPropertyInfoMetadata.getComforts()).thenReturn(List.of("Piscina", "Câmeras"));

        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("conveniences")
                .isEqualTo(List.of("Piscina", "Câmeras"));

        verify(mockedPropertyInfoMetadata, atLeastOnce()).getComforts();
        verifyNoMoreInteractions(mockedPropertyInfoMetadata);
    }

    @Test
    @DisplayName("given a property metadata when calls getState then expects")
    void givenAPropertyMetadata_whenCallsGetState_thenExpects() {
        when(mockedPropertyInfoMetadataLocation.getState()).thenReturn("Rua A");

        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("state")
                .isEqualTo("Rua A");

        verify(mockedPropertyInfoMetadataLocation, atLeastOnce()).getState();
        verifyNoMoreInteractions(mockedPropertyInfoMetadataLocation);
    }

    @Test
    @DisplayName("given a property metadata when calls getCity then expects")
    void givenAPropertyMetadata_whenCallsGetCity_thenExpects() {
        when(mockedPropertyInfoMetadataLocation.getCity()).thenReturn("São Paulo");

        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("city")
                .isEqualTo("São Paulo");

        verify(mockedPropertyInfoMetadataLocation, atLeastOnce()).getCity();
        verifyNoMoreInteractions(mockedPropertyInfoMetadataLocation);
    }

    @Test
    @DisplayName("given a property metadata when calls getDistrict then expects")
    void givenAPropertyMetadata_whenCallsGetDistrict_thenExpects() {
        when(mockedPropertyInfoMetadataLocation.getDistrict()).thenReturn("Medianeira");

        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("district")
                .isEqualTo("Medianeira");

        verify(mockedPropertyInfoMetadataLocation, atLeastOnce()).getDistrict();
        verifyNoMoreInteractions(mockedPropertyInfoMetadataLocation);
    }

    @Test
    @DisplayName("given a property metadata when calls getZipCode then expects")
    void givenAPropertyMetadata_whenCallsGetZipCode_thenExpects() {
        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("zipCode")
                .isNull();

        verifyNoInteractions(mockedPropertyInfoMetadataLocation);
    }

    @Test
    @DisplayName("given a property metadata when calls getStreetName then expects")
    void givenAPropertyMetadata_whenCallsGetStreetName_thenExpects() {
        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("streetName")
                .isNull();

        verifyNoInteractions(mockedPropertyInfoMetadataLocation);
    }

    @Test
    @DisplayName("given a property metadata when calls getStreetNumber then expects")
    void givenAPropertyMetadata_whenCallsGetStreetNumber_thenExpects() {
        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("streetNumber")
                .isNull();

        verifyNoInteractions(mockedPropertyInfoMetadataLocation);
    }

    @Test
    @DisplayName("given a property metadata when calls getComplement then expects")
    void givenAPropertyMetadata_whenCallsGetComplement_thenExpects() {
        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("complement")
                .isNull();

        verifyNoInteractions(mockedPropertyInfoMetadataLocation);
    }

    @Test
    @DisplayName("given a property metadata when calls getLatitude then expects")
    void givenAPropertyMetadata_whenCallsGetLatitude_thenExpects() {
        when(mockedPropertyInfoMetadataLocation.getLatitudeAsBigDecimal()).thenReturn(BigDecimal.valueOf(12.12345));

        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("latitude")
                .isEqualTo(BigDecimal.valueOf(12.12345));

        verify(mockedPropertyInfoMetadataLocation, atLeastOnce()).getLatitudeAsBigDecimal();
        verifyNoMoreInteractions(mockedPropertyInfoMetadataLocation);
    }

    @Test
    @DisplayName("given a property metadata when calls getLongitude then expects")
    void givenAPropertyMetadata_whenCallsGetLongitude_thenExpects() {
        when(mockedPropertyInfoMetadataLocation.getLongitudeAsBigDecimal()).thenReturn(BigDecimal.valueOf(12.12345));

        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("longitude")
                .isEqualTo(BigDecimal.valueOf(12.12345));

        verify(mockedPropertyInfoMetadataLocation, atLeastOnce()).getLongitudeAsBigDecimal();
        verifyNoMoreInteractions(mockedPropertyInfoMetadataLocation);
    }

    @Test
    @DisplayName("given a property metadata when calls getTotalArea then expects")
    void givenAPropertyMetadata_whenCallsGetTotalArea_thenExpects() {
        var propertyInfoMetadata = spy(
                PropertyInfoMetadata.builder()
                                    .characteristics(List.of(
                                            PropertyCharacteristic.builder()
                                                                  .type(PropertyCharacteristic.Type.TOTAL_AREA)
                                                                  .value("100 m²")
                                                                  .build()
                                    ))
                                    .build()
        );

        assertThat(metadataTransferV2Service.transfer(propertyInfoMetadata))
                .extracting("totalArea")
                .isEqualTo(BigDecimal.valueOf(100.0));

        verify(propertyInfoMetadata, atLeastOnce()).extractCharacteristicsByTypes(any(), eq(PropertyCharacteristic.Type.TOTAL_AREA));
        verifyNoMoreInteractions(propertyInfoMetadata);
    }

    @Test
    @DisplayName("given a property metadata when calls getPrivateArea then expects")
    void givenAPropertyMetadata_whenCallsGetPrivateArea_thenExpects() {
        var propertyInfoMetadata = spy(
                PropertyInfoMetadata.builder()
                                    .characteristics(List.of(
                                            PropertyCharacteristic.builder()
                                                                  .type(PropertyCharacteristic.Type.PRIVATE_AREA)
                                                                  .value("100 m²")
                                                                  .build()
                                    ))
                                    .build()
        );

        assertThat(metadataTransferV2Service.transfer(propertyInfoMetadata))
                .extracting("privateArea")
                .isEqualTo(BigDecimal.valueOf(100.0));

        verify(propertyInfoMetadata, atLeastOnce()).extractCharacteristicsByTypes(any(), eq(PropertyCharacteristic.Type.PRIVATE_AREA));
        verifyNoMoreInteractions(propertyInfoMetadata);
    }

    @Test
    @DisplayName("given a property metadata when calls getUsableArea then expects")
    void givenAPropertyMetadata_whenCallsGetUsableArea_thenExpects() {
        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("usableArea")
                .isNull();

        verifyNoInteractions(mockedPropertyInfoMetadata);
    }

    @Test
    @DisplayName("given a property metadata when calls getTerrainTotalArea then expects")
    void givenAPropertyMetadata_whenCallsGetTerrainTotalArea_thenExpects() {
        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("terrainTotalArea")
                .isNull();

        verifyNoInteractions(mockedPropertyInfoMetadata);
    }

    @Test
    @DisplayName("given a property metadata when calls getTerrainFront then expects")
    void givenAPropertyMetadata_whenCallsGetTerrainFront_thenExpects() {
        when(mockedPropertyInfoMetadata.getDetails()).thenReturn(List.of(
                PropertyDetail.builder()
                              .type(PropertyDetail.Type.MEASURES)
                              .items(List.of("frente 100 ²"))
                              .build()
        ));

        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("terrainFront")
                .isEqualTo(BigDecimal.valueOf(100.0));

        verify(mockedPropertyInfoMetadata, atLeastOnce()).getDetails();
        verifyNoMoreInteractions(mockedPropertyInfoMetadata);
    }

    @Test
    @DisplayName("given a property metadata when calls getTerrainBack then expects")
    void givenAPropertyMetadata_whenCallsGetTerrainBack_thenExpects() {
        when(mockedPropertyInfoMetadata.getDetails()).thenReturn(List.of(
                PropertyDetail.builder()
                              .type(PropertyDetail.Type.MEASURES)
                              .items(List.of("fundos 100 ²"))
                              .build()
        ));

        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("terrainBack")
                .isEqualTo(BigDecimal.valueOf(100.0));

        verify(mockedPropertyInfoMetadata, atLeastOnce()).getDetails();
        verifyNoMoreInteractions(mockedPropertyInfoMetadata);
    }

    @Test
    @DisplayName("given a property metadata when calls getTerrainLeft then expects")
    void givenAPropertyMetadata_whenCallsGetTerrainLeft_thenExpects() {
        when(mockedPropertyInfoMetadata.getDetails()).thenReturn(List.of(
                PropertyDetail.builder()
                              .type(PropertyDetail.Type.MEASURES)
                              .items(List.of("esquerda 100 ²"))
                              .build()
        ));

        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("terrainLeft")
                .isEqualTo(BigDecimal.valueOf(100.0));

        verify(mockedPropertyInfoMetadata, atLeastOnce()).getDetails();
        verifyNoMoreInteractions(mockedPropertyInfoMetadata);
    }

    @Test
    @DisplayName("given a property metadata when calls getTerrainRight then expects")
    void givenAPropertyMetadata_whenCallsGetTerrainRight_thenExpects() {
        when(mockedPropertyInfoMetadata.getDetails()).thenReturn(List.of(
                PropertyDetail.builder()
                              .type(PropertyDetail.Type.MEASURES)
                              .items(List.of("direita 100 ²"))
                              .build()
        ));

        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("terrainRight")
                .isEqualTo(BigDecimal.valueOf(100.0));

        verify(mockedPropertyInfoMetadata, atLeastOnce()).getDetails();
        verifyNoMoreInteractions(mockedPropertyInfoMetadata);
    }

    @Test
    @DisplayName("given a property metadata when calls getAreaUnit then expects")
    void givenAPropertyMetadata_whenCallsGetAreaUnit_thenExpects() {
        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata))
                .extracting("areaUnit")
                .isNull();

        verifyNoInteractions(mockedPropertyInfoMetadata);
    }

    @Test
    @DisplayName("given a property metadata when calls getMedias then expects")
    void givenAPropertyMetadata_whenCallsGetMedias_thenExpects() {
        when(mockedPropertyInfoMetadata.getMedias()).thenReturn(List.of(
                PropertyInfoMetadata.Media.builder()
                                          .link("http://localhost/img.png")
                                          .extension("png")
                                          .build()
        ));

        assertThat(metadataTransferV2Service.transfer(mockedPropertyInfoMetadata).getMedias())
                .extracting(
                        AbstractUrbanPropertyMediaMetadata::getLink,
                        AbstractUrbanPropertyMediaMetadata::getLinkThumb,
                        AbstractUrbanPropertyMediaMetadata::getMediaType,
                        AbstractUrbanPropertyMediaMetadata::getExtension
                )
                .containsExactly(tuple("http://localhost/img.png", null, UrbanPropertyMediaType.IMAGE, "png"));

        verify(mockedPropertyInfoMetadata, atLeastOnce()).getMedias();
        verifyNoMoreInteractions(mockedPropertyInfoMetadata);
    }

}
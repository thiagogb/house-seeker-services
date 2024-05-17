package br.com.houseseeker.service.v2;

import br.com.houseseeker.domain.jetimob.PropertyCharacteristic;
import br.com.houseseeker.domain.jetimob.PropertyDetail;
import br.com.houseseeker.domain.jetimob.PropertyTerrainSide;
import br.com.houseseeker.domain.jetimob.v2.PropertyInfoMetadata;
import br.com.houseseeker.domain.property.AbstractUrbanPropertyMediaMetadata;
import br.com.houseseeker.domain.property.AbstractUrbanPropertyMetadata;
import br.com.houseseeker.domain.property.UrbanPropertyContract;
import br.com.houseseeker.domain.property.UrbanPropertyMediaType;
import br.com.houseseeker.domain.property.UrbanPropertyStatus;
import br.com.houseseeker.domain.property.UrbanPropertyType;
import br.com.houseseeker.service.AbstractMedataTransferService;
import br.com.houseseeker.util.MediaUtils;
import br.com.houseseeker.util.ObjectMapperUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static br.com.houseseeker.util.ConverterUtils.tryToBigDecimalEnUs;
import static br.com.houseseeker.util.ConverterUtils.tryToInteger;
import static br.com.houseseeker.util.StringUtils.keepOnlyNumericSymbols;

@Service
@RequiredArgsConstructor
public class MetadataTransferV2Service extends AbstractMedataTransferService<PropertyInfoMetadata> {

    private final ObjectMapper objectMapper;

    @Override
    public AbstractUrbanPropertyMetadata transfer(@NotNull PropertyInfoMetadata metadata) {
        return new AbstractUrbanPropertyMetadata() {
            @Override
            public String getProviderCode() {
                return metadata.getProviderCode();
            }

            @Override
            public String getUrl() {
                return metadata.getUrl();
            }

            @Override
            public UrbanPropertyContract getContract() {
                return metadata.getContract();
            }

            @Override
            public UrbanPropertyType getType() {
                return UrbanPropertyType.detect(metadata.getSubType()).orElse(null);
            }

            @Override
            public String getSubType() {
                return metadata.getSubType();
            }

            @Override
            public Integer getDormitories() {
                return metadata.extractCharacteristicsByTypes(
                        c -> getCharacteristicValueAsInteger(c),
                        PropertyCharacteristic.Type.DORMITORIES,
                        PropertyCharacteristic.Type.ROOMS
                );
            }

            @Override
            public Integer getSuites() {
                return metadata.extractCharacteristicsByTypes(
                        c -> getCharacteristicValueAsInteger(c),
                        PropertyCharacteristic.Type.SUITES
                );
            }

            @Override
            public Integer getBathrooms() {
                return metadata.extractCharacteristicsByTypes(
                        c -> getCharacteristicValueAsInteger(c),
                        PropertyCharacteristic.Type.BATHROOMS
                );
            }

            @Override
            public Integer getGarages() {
                return metadata.extractCharacteristicsByTypes(
                        c -> getCharacteristicValueAsInteger(c),
                        PropertyCharacteristic.Type.GARAGES
                );
            }

            @Override
            public BigDecimal getSellPrice() {
                return metadata.getPricing().getSellPriceAsBigDecimal();
            }

            @Override
            public BigDecimal getRentPrice() {
                return metadata.getPricing().getRentPriceAsBigDecimal();
            }

            @Override
            public BigDecimal getCondominiumPrice() {
                return metadata.getPricing().getCondominiumPriceAsBigDecimal();
            }

            @Override
            public String getCondominiumName() {
                return null;
            }

            @Override
            public Boolean isExchangeable() {
                return null;
            }

            @Override
            public UrbanPropertyStatus getStatus() {
                return null;
            }

            @Override
            public Boolean isFinanceable() {
                return null;
            }

            @Override
            public Boolean isOccupied() {
                return null;
            }

            @Override
            public String getNotes() {
                return metadata.getDescription();
            }

            @Override
            public List<String> getConveniences() {
                return metadata.getComforts();
            }

            @Override
            public String getState() {
                return metadata.getLocation().getState();
            }

            @Override
            public String getCity() {
                return metadata.getLocation().getCity();
            }

            @Override
            public String getDistrict() {
                return metadata.getLocation().getDistrict();
            }

            @Override
            public String getZipCode() {
                return null;
            }

            @Override
            public String getStreetName() {
                return null;
            }

            @Override
            public Integer getStreetNumber() {
                return null;
            }

            @Override
            public String getComplement() {
                return null;
            }

            @Override
            public BigDecimal getLatitude() {
                return metadata.getLocation().getLatitudeAsBigDecimal();
            }

            @Override
            public BigDecimal getLongitude() {
                return metadata.getLocation().getLongitudeAsBigDecimal();
            }

            @Override
            public BigDecimal getTotalArea() {
                return metadata.extractCharacteristicsByTypes(
                        c -> getCharacteristicValueAsBigDecimal(c),
                        PropertyCharacteristic.Type.TOTAL_AREA
                );
            }

            @Override
            public BigDecimal getPrivateArea() {
                return metadata.extractCharacteristicsByTypes(
                        c -> getCharacteristicValueAsBigDecimal(c),
                        PropertyCharacteristic.Type.PRIVATE_AREA
                );
            }

            @Override
            public BigDecimal getUsableArea() {
                return null;
            }

            @Override
            public BigDecimal getTerrainTotalArea() {
                return null;
            }

            @Override
            public BigDecimal getTerrainFront() {
                return PropertyDetail.findType(metadata.getDetails(), PropertyDetail.Type.MEASURES)
                                     .flatMap(PropertyTerrainSide.FRONT::extract)
                                     .orElse(null);
            }

            @Override
            public BigDecimal getTerrainBack() {
                return PropertyDetail.findType(metadata.getDetails(), PropertyDetail.Type.MEASURES)
                                     .flatMap(PropertyTerrainSide.BACK::extract)
                                     .orElse(null);
            }

            @Override
            public BigDecimal getTerrainLeft() {
                return PropertyDetail.findType(metadata.getDetails(), PropertyDetail.Type.MEASURES)
                                     .flatMap(PropertyTerrainSide.LEFT::extract)
                                     .orElse(null);
            }

            @Override
            public BigDecimal getTerrainRight() {
                return PropertyDetail.findType(metadata.getDetails(), PropertyDetail.Type.MEASURES)
                                     .flatMap(PropertyTerrainSide.RIGHT::extract)
                                     .orElse(null);
            }

            @Override
            public String getAreaUnit() {
                return null;
            }

            @Override
            public List<AbstractUrbanPropertyMediaMetadata> getMedias() {
                return transferMediaMetadata(metadata.getMedias());
            }

            @Override
            public String toString() {
                return ObjectMapperUtils.serialize(objectMapper, this);
            }
        };
    }

    private Optional<Integer> getCharacteristicValueAsInteger(PropertyCharacteristic characteristic) {
        return tryToInteger(keepOnlyNumericSymbols(characteristic.getValue()));
    }

    private Optional<BigDecimal> getCharacteristicValueAsBigDecimal(PropertyCharacteristic characteristic) {
        return tryToBigDecimalEnUs(keepOnlyNumericSymbols(characteristic.getValue()));
    }

    private List<AbstractUrbanPropertyMediaMetadata> transferMediaMetadata(List<PropertyInfoMetadata.Media> mediaList) {
        return mediaList.stream()
                        .map(m -> new AbstractUrbanPropertyMediaMetadata() {
                            @Override
                            public String getLink() {
                                return m.getLink();
                            }

                            @Override
                            public String getLinkThumb() {
                                return null;
                            }

                            @Override
                            public UrbanPropertyMediaType getMediaType() {
                                return UrbanPropertyMediaType.IMAGE;
                            }

                            @Override
                            public String getExtension() {
                                return MediaUtils.getMediaExtension(m.getLink()).orElse(null);
                            }
                        })
                        .collect(Collectors.toList());
    }

}

package br.com.houseseeker.service.v1;

import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import br.com.houseseeker.domain.jetimob.v1.PropertyCharacteristicType;
import br.com.houseseeker.domain.jetimob.v1.PropertyInfoMetadata;
import br.com.houseseeker.domain.jetimob.v1.PropertyPricingType;
import br.com.houseseeker.domain.jetimob.v1.SearchPageMetadata;
import br.com.houseseeker.domain.property.AbstractUrbanPropertyMediaMetadata;
import br.com.houseseeker.domain.property.AbstractUrbanPropertyMetadata;
import br.com.houseseeker.domain.property.UrbanPropertyContract;
import br.com.houseseeker.domain.property.UrbanPropertyMediaType;
import br.com.houseseeker.domain.property.UrbanPropertyStatus;
import br.com.houseseeker.domain.property.UrbanPropertyType;
import br.com.houseseeker.service.AbstractMedataTransferService;
import br.com.houseseeker.util.ObjectMapperUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static br.com.houseseeker.util.ConverterUtils.tryToBigDecimalPtBR;
import static br.com.houseseeker.util.ConverterUtils.tryToInteger;
import static br.com.houseseeker.util.StringUtils.getNonBlank;
import static br.com.houseseeker.util.StringUtils.keepOnlyNumericSymbols;

@Service
@RequiredArgsConstructor
public class MetadataTransferV1Service extends AbstractMedataTransferService<Pair<SearchPageMetadata.Item, PropertyInfoMetadata>> {

    private final ObjectMapper objectMapper;

    @Override
    public AbstractUrbanPropertyMetadata transfer(@NotNull Pair<SearchPageMetadata.Item, PropertyInfoMetadata> metadata) {
        UrbanPropertyType type = UrbanPropertyType.detect(metadata.getLeft().getSubType())
                                                  .orElseThrow(() -> new ExtendedRuntimeException("Failed to detect property type"));

        Map<PropertyCharacteristicType, PropertyInfoMetadata.Characteristics> characteristicsMap = buildCharacteristicsMap(metadata.getRight());
        Map<PropertyPricingType, PropertyInfoMetadata.Pricing> pricingMap = buildPricingMap(metadata.getRight());

        return new AbstractUrbanPropertyMetadata() {
            @Override
            public String getProviderCode() {
                return metadata.getLeft().getProviderCode();
            }

            @Override
            public String getUrl() {
                return metadata.getLeft().getPageLink();
            }

            @Override
            public UrbanPropertyContract getContract() {
                return metadata.getRight().getContract();
            }

            @Override
            public UrbanPropertyType getType() {
                return type;
            }

            @Override
            public String getSubType() {
                return metadata.getLeft().getSubType();
            }

            @Override
            public Integer getDormitories() {
                return Optional.ofNullable(
                                       type.equals(UrbanPropertyType.RESIDENTIAL)
                                               ? characteristicsMap.get(PropertyCharacteristicType.DORMITORIES)
                                               : characteristicsMap.get(PropertyCharacteristicType.ROOMS)
                               )
                               .flatMap(c -> tryToInteger(keepOnlyNumericSymbols(c.getValue())))
                               .orElse(null);
            }

            @Override
            public Integer getSuites() {
                return Optional.ofNullable(
                                       type.equals(UrbanPropertyType.RESIDENTIAL)
                                               ? characteristicsMap.get(PropertyCharacteristicType.DORMITORIES)
                                               : characteristicsMap.get(PropertyCharacteristicType.ROOMS)
                               )
                               .flatMap(c -> tryToInteger(keepOnlyNumericSymbols(c.getAdditional())))
                               .orElse(null);
            }

            @Override
            public Integer getBathrooms() {
                return Optional.ofNullable(characteristicsMap.get(PropertyCharacteristicType.BATHROOM))
                               .flatMap(c -> tryToInteger(keepOnlyNumericSymbols(c.getAdditional())))
                               .orElse(null);
            }

            @Override
            public Integer getGarages() {
                return Optional.ofNullable(characteristicsMap.get(PropertyCharacteristicType.GARAGE))
                               .flatMap(c -> tryToInteger(keepOnlyNumericSymbols(c.getAdditional())))
                               .orElse(null);
            }

            @Override
            public BigDecimal getSellPrice() {
                return Optional.ofNullable(pricingMap.get(PropertyPricingType.SELL_PRICE))
                               .flatMap(p -> tryToBigDecimalPtBR(keepOnlyNumericSymbols(p.getValue())))
                               .orElse(null);
            }

            @Override
            public BigDecimal getRentPrice() {
                return Optional.ofNullable(pricingMap.get(PropertyPricingType.RENT_PRICE))
                               .flatMap(p -> tryToBigDecimalPtBR(keepOnlyNumericSymbols(p.getValue())))
                               .orElse(null);
            }

            @Override
            public BigDecimal getCondominiumPrice() {
                return Optional.ofNullable(pricingMap.get(PropertyPricingType.CONDOMINIUM_PRICE))
                               .flatMap(p -> tryToBigDecimalPtBR(keepOnlyNumericSymbols(p.getValue())))
                               .orElse(null);
            }

            @Override
            public String getCondominiumName() {
                return Optional.ofNullable(metadata.getRight().getLocation())
                               .map(PropertyInfoMetadata.Location::getCondominiumName)
                               .orElse(null);
            }

            @Override
            @Nullable
            public Boolean isExchangeable() {
                return null;
            }

            @Override
            public UrbanPropertyStatus getStatus() {
                return null;
            }

            @Override
            public Boolean isFinanceable() {
                return Optional.ofNullable(pricingMap.get(PropertyPricingType.SELL_PRICE))
                               .flatMap(p -> getNonBlank(p.getName()))
                               .map(n -> n.trim().toLowerCase().contains("financiável"))
                               .orElse(null);
            }

            @Override
            @Nullable
            public Boolean isOccupied() {
                return null;
            }

            @Override
            public String getNotes() {
                return Optional.ofNullable(metadata.getRight().getConvenience())
                               .map(PropertyInfoMetadata.Convenience::getDescription)
                               .orElse(null);
            }

            @Override
            public List<String> getConveniences() {
                return Optional.ofNullable(metadata.getRight().getConvenience())
                               .map(PropertyInfoMetadata.Convenience::getItems)
                               .orElse(Collections.emptyList());
            }

            @Override
            public String getState() {
                return Optional.ofNullable(metadata.getRight().getLocation())
                               .map(PropertyInfoMetadata.Location::getState)
                               .orElse(null);
            }

            @Override
            public String getCity() {
                return Optional.ofNullable(metadata.getRight().getLocation())
                               .map(PropertyInfoMetadata.Location::getCity)
                               .orElse(null);
            }

            @Override
            public String getDistrict() {
                return Optional.ofNullable(metadata.getRight().getLocation())
                               .map(PropertyInfoMetadata.Location::getDistrict)
                               .orElse(null);
            }

            @Override
            public String getZipCode() {
                return null;
            }

            @Override
            public String getStreetName() {
                return Optional.ofNullable(metadata.getRight().getLocation())
                               .map(PropertyInfoMetadata.Location::getStreetName)
                               .orElse(null);
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
                return null;
            }

            @Override
            public BigDecimal getLongitude() {
                return null;
            }

            @Override
            public BigDecimal getTotalArea() {
                return Optional.ofNullable(characteristicsMap.get(PropertyCharacteristicType.BUILD_AREA))
                               .flatMap(c -> tryToBigDecimalPtBR(keepOnlyNumericSymbols(c.getValue())))
                               .orElse(null);
            }

            @Override
            public BigDecimal getPrivateArea() {
                return Optional.ofNullable(characteristicsMap.get(PropertyCharacteristicType.PRIVATE_AREA))
                               .flatMap(c -> tryToBigDecimalPtBR(keepOnlyNumericSymbols(c.getValue())))
                               .orElse(null);
            }

            @Override
            public BigDecimal getUsableArea() {
                return Optional.ofNullable(characteristicsMap.get(PropertyCharacteristicType.USABLE_AREA))
                               .flatMap(c -> tryToBigDecimalPtBR(keepOnlyNumericSymbols(c.getValue())))
                               .orElse(null);
            }

            @Override
            public BigDecimal getTerrainTotalArea() {
                return Optional.ofNullable(characteristicsMap.get(PropertyCharacteristicType.TERRAIN_AREA))
                               .flatMap(c -> tryToBigDecimalPtBR(keepOnlyNumericSymbols(c.getValue())))
                               .orElse(null);
            }

            @Override
            public BigDecimal getTerrainFront() {
                return null;
            }

            @Override
            public BigDecimal getTerrainBack() {
                return null;
            }

            @Override
            public BigDecimal getTerrainLeft() {
                return null;
            }

            @Override
            public BigDecimal getTerrainRight() {
                return null;
            }

            @Override
            public String getAreaUnit() {
                return null;
            }

            @Override
            public List<AbstractUrbanPropertyMediaMetadata> getMedias() {
                return Optional.ofNullable(metadata.getRight().getMedias())
                               .map(m -> transferMediaMetadata(m))
                               .orElse(Collections.emptyList());
            }

            @Override
            public String toString() {
                return ObjectMapperUtils.serialize(objectMapper, this);
            }

        };
    }

    private Map<PropertyCharacteristicType, PropertyInfoMetadata.Characteristics> buildCharacteristicsMap(PropertyInfoMetadata metadata) {
        return metadata.getCharacteristics()
                       .stream()
                       .collect(Collectors.toMap(
                               PropertyInfoMetadata.Characteristics::getType,
                               Function.identity()
                       ));
    }

    private Map<PropertyPricingType, PropertyInfoMetadata.Pricing> buildPricingMap(PropertyInfoMetadata metadata) {
        return metadata.getPricing()
                       .stream()
                       .collect(Collectors.toMap(
                               PropertyInfoMetadata.Pricing::getType,
                               Function.identity()
                       ));
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
                                return m.getLinkThumb();
                            }

                            @Override
                            public UrbanPropertyMediaType getMediaType() {
                                if (m.getType().trim().equalsIgnoreCase("images"))
                                    return UrbanPropertyMediaType.IMAGE;

                                return null;
                            }

                            @Override
                            public String getExtension() {
                                return m.getExtension();
                            }
                        })
                        .collect(Collectors.toList());
    }

}

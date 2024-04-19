package br.com.houseseeker.service.v4;

import br.com.houseseeker.domain.jetimob.v4.PropertyInfoResponse;
import br.com.houseseeker.domain.urbanProperty.AbstractUrbanPropertyMediaData;
import br.com.houseseeker.domain.urbanProperty.AbstractUrbanPropertyMetadata;
import br.com.houseseeker.domain.urbanProperty.UrbanPropertyContract;
import br.com.houseseeker.domain.urbanProperty.UrbanPropertyMediaType;
import br.com.houseseeker.domain.urbanProperty.UrbanPropertyStatus;
import br.com.houseseeker.domain.urbanProperty.UrbanPropertyType;
import br.com.houseseeker.service.AbstractMedataTransfer;
import br.com.houseseeker.util.BigDecimalUtils;
import br.com.houseseeker.util.MediaUtils;
import br.com.houseseeker.util.ObjectMapperUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static br.com.houseseeker.util.BigDecimalUtils.divideBy100AndRoundByTwo;
import static br.com.houseseeker.util.ConverterUtils.tryToBigDecimalEnUs;
import static br.com.houseseeker.util.ConverterUtils.tryToInteger;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class MetadataTransferV4Service extends AbstractMedataTransfer<PropertyInfoResponse> {

    private static final int CONTRACT_SELL_VALUE = 1;
    private static final int CONTRACT_RENT_VALUE = 1;
    private static final String COMMERCIAL_TYPE_SUFFIX = "comercial";

    private final ObjectMapper objectMapper;

    @Override
    public AbstractUrbanPropertyMetadata transfer(@NotNull PropertyInfoResponse metadata) {
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
                if (isEmpty(metadata.getContracts()))
                    return null;

                if (metadata.getContracts().stream().anyMatch(c -> c.getId().equals(CONTRACT_SELL_VALUE)))
                    return UrbanPropertyContract.SELL;

                return UrbanPropertyContract.RENT;
            }

            @Override
            public UrbanPropertyType getType() {
                if (isBlank(metadata.getType()))
                    return null;

                if (metadata.getType().trim().toLowerCase().endsWith(COMMERCIAL_TYPE_SUFFIX))
                    return UrbanPropertyType.COMMERCIAL;

                return UrbanPropertyType.RESIDENTIAL;
            }

            @Override
            public String getSubType() {
                return metadata.getType();
            }

            @Override
            public Integer getDormitories() {
                return metadata.getDormitories();
            }

            @Override
            public Integer getSuites() {
                return metadata.getSuites();
            }

            @Override
            public Integer getBathrooms() {
                return metadata.getBathrooms();
            }

            @Override
            public Integer getGarages() {
                return metadata.getGarages();
            }

            @Override
            public BigDecimal getSellPrice() {
                if (isEmpty(metadata.getContracts()))
                    return null;

                return getContractPrice(metadata.getContracts(), CONTRACT_SELL_VALUE);
            }

            @Override
            public BigDecimal getRentPrice() {
                if (isEmpty(metadata.getContracts()))
                    return null;

                return getContractPrice(metadata.getContracts(), CONTRACT_RENT_VALUE);
            }

            @Override
            public BigDecimal getCondominiumPrice() {
                return Optional.ofNullable(metadata.getCondominiumPrice())
                               .flatMap(cp -> Optional.ofNullable(cp.getValue())
                                                      .map(BigDecimalUtils::divideBy100AndRoundByTwo)
                               )
                               .orElse(null);
            }

            @Override
            public String getCondominiumName() {
                return "";
            }

            @Override
            public Boolean isExchangeable() {
                return metadata.getExchangeable();
            }

            @Override
            public UrbanPropertyStatus getStatus() {
                return null;
            }

            @Override
            public Boolean isFinanceable() {
                return metadata.getFinanceable();
            }

            @Override
            public Boolean isOccupied() {
                return null;
            }

            @Override
            public String getNotes() {
                return metadata.getNotes();
            }

            @Override
            public List<String> getConveniences() {
                return metadata.getConveniences();
            }

            @Override
            public String getState() {
                return Optional.ofNullable(metadata.getAddress())
                               .map(PropertyInfoResponse.Address::getState)
                               .orElse(null);
            }

            @Override
            public String getCity() {
                return Optional.ofNullable(metadata.getAddress())
                               .map(PropertyInfoResponse.Address::getCity)
                               .orElse(null);
            }

            @Override
            public String getDistrict() {
                return Optional.ofNullable(metadata.getAddress())
                               .map(PropertyInfoResponse.Address::getDistrict)
                               .orElse(null);
            }

            @Override
            public String getZipCode() {
                return Optional.ofNullable(metadata.getAddress())
                               .map(PropertyInfoResponse.Address::getZipCode)
                               .orElse(null);
            }

            @Override
            public String getStreetName() {
                return Optional.ofNullable(metadata.getAddress())
                               .map(PropertyInfoResponse.Address::getStreetName)
                               .orElse(null);
            }

            @Override
            public Integer getStreetNumber() {
                return Optional.ofNullable(metadata.getAddress())
                               .flatMap(a -> tryToInteger(a.getStreetNumber()))
                               .orElse(null);
            }

            @Override
            public String getComplement() {
                return Optional.ofNullable(metadata.getAddress())
                               .map(PropertyInfoResponse.Address::getComplement)
                               .orElse(null);
            }

            @Override
            public BigDecimal getLatitude() {
                return Optional.ofNullable(metadata.getAddress())
                               .flatMap(a -> Optional.ofNullable(a.getCoordinate())
                                                     .flatMap(c -> tryToBigDecimalEnUs(c.getLatitude()))
                               )
                               .orElse(null);
            }

            @Override
            public BigDecimal getLongitude() {
                return Optional.ofNullable(metadata.getAddress())
                               .flatMap(a -> Optional.ofNullable(a.getCoordinate())
                                                     .flatMap(c -> tryToBigDecimalEnUs(c.getLongitude()))
                               )
                               .orElse(null);
            }

            @Override
            public BigDecimal getTotalArea() {
                return Optional.ofNullable(metadata.getTotalArea())
                               .map(PropertyInfoResponse.Measure::getValue)
                               .orElse(null);
            }

            @Override
            public BigDecimal getPrivateArea() {
                return Optional.ofNullable(metadata.getPrivateArea())
                               .map(PropertyInfoResponse.Measure::getValue)
                               .orElse(null);
            }

            @Override
            public BigDecimal getUsableArea() {
                return Optional.ofNullable(metadata.getUsableArea())
                               .map(PropertyInfoResponse.Measure::getValue)
                               .orElse(null);
            }

            @Override
            public BigDecimal getTerrainTotalArea() {
                return Optional.ofNullable(metadata.getTerrainTotalArea())
                               .map(PropertyInfoResponse.Measure::getValue)
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
                return Stream.of(metadata.getTotalArea(), metadata.getPrivateArea(), metadata.getUsableArea(), metadata.getTerrainTotalArea())
                             .filter(m -> nonNull(m) && isNotBlank(m.getMeasureUnit()))
                             .map(PropertyInfoResponse.Measure::getMeasureUnit)
                             .findFirst()
                             .orElse(null);
            }

            @Override
            public List<AbstractUrbanPropertyMediaData> getMedias() {
                return Optional.ofNullable(metadata.getMedias())
                               .map(m -> transferMediaMetadata(m))
                               .orElse(Collections.emptyList());
            }

            @Override
            public String toString() {
                return ObjectMapperUtils.serialize(objectMapper, this);
            }
        };
    }

    private BigDecimal getContractPrice(List<PropertyInfoResponse.Contract> contracts, int contractType) {
        return contracts.stream()
                        .filter(c -> c.getId().equals(contractType) && nonNull(c.getPrice()) && nonNull(c.getPrice().getValue()))
                        .findFirst()
                        .map(c -> divideBy100AndRoundByTwo(c.getPrice().getValue()))
                        .orElse(null);
    }

    private List<AbstractUrbanPropertyMediaData> transferMediaMetadata(List<PropertyInfoResponse.Media> mediaList) {
        return mediaList.stream()
                        .map(m -> new AbstractUrbanPropertyMediaData() {
                            @Override
                            public String getLink() {
                                return m.getLink();
                            }

                            @Override
                            public String getLinkThumb() {
                                if (isBlank(m.getLinkThumb()))
                                    return null;

                                List<String> thumbs = Arrays.asList(m.getLinkThumb().split(","));
                                if (thumbs.isEmpty())
                                    return null;

                                List<String> firstThumbParts = Arrays.asList(thumbs.getFirst().split(SPACE));
                                if (firstThumbParts.isEmpty())
                                    return null;

                                return firstThumbParts.getFirst().trim();
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

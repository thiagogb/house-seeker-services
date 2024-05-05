package br.com.houseseeker.service.v4;

import br.com.houseseeker.domain.jetimob.v4.PropertyInfoResponse;
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
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MetadataTransferV4Service extends AbstractMedataTransferService<PropertyInfoResponse> {

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
                return metadata.getMainContract();
            }

            @Override
            public UrbanPropertyType getType() {
                return metadata.getType();
            }

            @Override
            public String getSubType() {
                return metadata.getSubType();
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
                return metadata.getSellPrice();
            }

            @Override
            public BigDecimal getRentPrice() {
                return metadata.getRentPrice();
            }

            @Override
            public BigDecimal getCondominiumPrice() {
                return metadata.getCondominiumPrice();
            }

            @Override
            public String getCondominiumName() {
                return null;
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
            @Nullable
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
                return metadata.getState();
            }

            @Override
            public String getCity() {
                return metadata.getCity();
            }

            @Override
            public String getDistrict() {
                return metadata.getDistrict();
            }

            @Override
            public String getZipCode() {
                return metadata.getZipCode();
            }

            @Override
            public String getStreetName() {
                return metadata.getStreetName();
            }

            @Override
            public Integer getStreetNumber() {
                return metadata.getStreetNumber();
            }

            @Override
            public String getComplement() {
                return metadata.getComplement();
            }

            @Override
            public BigDecimal getLatitude() {
                return metadata.getLatitude();
            }

            @Override
            public BigDecimal getLongitude() {
                return metadata.getLongitude();
            }

            @Override
            public BigDecimal getTotalArea() {
                return metadata.getTotalArea();
            }

            @Override
            public BigDecimal getPrivateArea() {
                return metadata.getPrivateArea();
            }

            @Override
            public BigDecimal getUsableArea() {
                return metadata.getUsableArea();
            }

            @Override
            public BigDecimal getTerrainTotalArea() {
                return metadata.getTerrainTotalArea();
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
                return metadata.getAreaUnit();
            }

            @Override
            public List<AbstractUrbanPropertyMediaMetadata> getMedias() {
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

    private List<AbstractUrbanPropertyMediaMetadata> transferMediaMetadata(List<PropertyInfoResponse.Media> mediaList) {
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

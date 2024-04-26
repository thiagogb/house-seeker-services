package br.com.houseseeker.domain.provider;

import br.com.houseseeker.domain.property.AbstractUrbanPropertyMediaMetadata;
import br.com.houseseeker.domain.property.AbstractUrbanPropertyMetadata;
import br.com.houseseeker.domain.property.UrbanPropertyContract;
import br.com.houseseeker.domain.property.UrbanPropertyStatus;
import br.com.houseseeker.domain.property.UrbanPropertyType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UrbanPropertyMetadata extends AbstractUrbanPropertyMetadata {

    private String providerCode;
    private String url;
    private UrbanPropertyContract contract;
    private UrbanPropertyType type;
    private String subType;
    private Integer dormitories;
    private Integer suites;
    private Integer bathrooms;
    private Integer garages;
    private BigDecimal sellPrice;
    private BigDecimal rentPrice;
    private BigDecimal condominiumPrice;
    private String condominiumName;
    private Boolean isExchangeable;
    private UrbanPropertyStatus status;
    private Boolean isFinanceable;
    private Boolean isOccupied;
    private String notes;
    private List<String> conveniences;
    private String state;
    private String city;
    private String district;
    private String zipCode;
    private String streetName;
    private Integer streetNumber;
    private String complement;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal totalArea;
    private BigDecimal privateArea;
    private BigDecimal usableArea;
    private BigDecimal terrainTotalArea;
    private BigDecimal terrainFront;
    private BigDecimal terrainBack;
    private BigDecimal terrainLeft;
    private BigDecimal terrainRight;
    private String areaUnit;
    private List<UrbanPropertyMediaMetadata> medias;

    @Override
    public Boolean isExchangeable() {
        return isExchangeable;
    }

    @Override
    public Boolean isFinanceable() {
        return isFinanceable;
    }

    @Override
    public Boolean isOccupied() {
        return isOccupied;
    }

    @Override
    public List<AbstractUrbanPropertyMediaMetadata> getMedias() {
        return new ArrayList<>(medias);
    }


}

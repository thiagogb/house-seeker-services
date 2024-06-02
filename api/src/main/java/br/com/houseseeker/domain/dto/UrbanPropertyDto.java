package br.com.houseseeker.domain.dto;

import br.com.houseseeker.domain.property.UrbanPropertyContract;
import br.com.houseseeker.domain.property.UrbanPropertyStatus;
import br.com.houseseeker.domain.property.UrbanPropertyType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class UrbanPropertyDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 5712945666094162038L;

    Integer id;
    ProviderDto provider;
    String providerCode;
    String url;
    UrbanPropertyContract contract;
    UrbanPropertyType type;
    String subType;
    Integer dormitories;
    Integer suites;
    Integer bathrooms;
    Integer garages;
    BigDecimal sellPrice;
    BigDecimal rentPrice;
    BigDecimal condominiumPrice;
    String condominiumName;
    Boolean exchangeable;
    UrbanPropertyStatus status;
    Boolean financeable;
    Boolean occupied;
    String notes;
    LocalDateTime creationDate;
    LocalDateTime lastAnalysisDate;
    LocalDateTime exclusionDate;
    Boolean analyzable;
    UrbanPropertyLocationDto location;
    UrbanPropertyMeasureDto measure;
    List<UrbanPropertyConvenienceDto> conveniences;
    List<UrbanPropertyMediaDto> medias;
    List<UrbanPropertyPriceVariationDto> priceVariations;

    public static List<Integer> extractIds(@NotNull List<UrbanPropertyDto> dtoList) {
        return dtoList.stream().map(UrbanPropertyDto::getId).toList();
    }

}
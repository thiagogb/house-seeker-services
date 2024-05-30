package br.com.houseseeker.domain.dto;

import br.com.houseseeker.domain.property.UrbanPropertyPriceVariationType;
import lombok.Builder;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class UrbanPropertyPriceVariationDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 4981316640337316078L;

    Integer id;
    LocalDateTime analysisDate;
    UrbanPropertyPriceVariationType type;
    BigDecimal price;
    BigDecimal variation;

}
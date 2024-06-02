package br.com.houseseeker.domain.dto;

import lombok.Builder;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Value
@Builder
public class UrbanPropertyMeasureDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 7726691239845795186L;

    Integer id;
    BigDecimal totalArea;
    BigDecimal privateArea;
    BigDecimal usableArea;
    BigDecimal terrainTotalArea;
    BigDecimal terrainFront;
    BigDecimal terrainBack;
    BigDecimal terrainLeft;
    BigDecimal terrainRight;
    String areaUnit;

}
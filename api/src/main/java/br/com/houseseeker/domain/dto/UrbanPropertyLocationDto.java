package br.com.houseseeker.domain.dto;

import lombok.Builder;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Value
@Builder
public class UrbanPropertyLocationDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -2429206378636404558L;

    Integer id;
    String state;
    String city;
    String district;
    String zipCode;
    String streetName;
    Integer streetNumber;
    String complement;
    BigDecimal latitude;
    BigDecimal longitude;

}
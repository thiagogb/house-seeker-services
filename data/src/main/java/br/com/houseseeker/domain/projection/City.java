package br.com.houseseeker.domain.projection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class City {

    private String state;
    private String city;

}

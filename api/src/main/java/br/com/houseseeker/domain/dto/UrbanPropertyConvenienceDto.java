package br.com.houseseeker.domain.dto;

import lombok.Builder;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;

@Value
@Builder
public class UrbanPropertyConvenienceDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -7635168126741220349L;

    Integer id;
    String description;

}
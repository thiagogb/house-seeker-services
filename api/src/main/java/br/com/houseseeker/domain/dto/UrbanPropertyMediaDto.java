package br.com.houseseeker.domain.dto;

import br.com.houseseeker.domain.property.UrbanPropertyMediaType;
import lombok.Builder;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;

@Value
@Builder
public class UrbanPropertyMediaDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -3857402619911681626L;

    Integer id;
    String link;
    String linkThumb;
    UrbanPropertyMediaType mediaType;
    String extension;

}
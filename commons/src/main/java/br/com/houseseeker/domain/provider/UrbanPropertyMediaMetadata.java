package br.com.houseseeker.domain.provider;

import br.com.houseseeker.domain.property.AbstractUrbanPropertyMediaMetadata;
import br.com.houseseeker.domain.property.UrbanPropertyMediaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
public class UrbanPropertyMediaMetadata extends AbstractUrbanPropertyMediaMetadata {

    private String link;
    private String linkThumb;
    private UrbanPropertyMediaType mediaType;
    private String extension;

}

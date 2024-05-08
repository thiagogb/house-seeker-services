package br.com.houseseeker.domain.jetimob.v2;

import br.com.houseseeker.domain.jetimob.PropertyCharacteristic;
import br.com.houseseeker.domain.jetimob.PropertyDetail;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PropertyInfoMetadata {

    private final String description;
    private final Location location;
    private final Pricing pricing;
    private final List<PropertyCharacteristic> characteristics;
    private final List<Media> medias;
    private final List<PropertyDetail> details;
    private final List<String> comforts;

    @Data
    @Builder
    public static final class Location {

        private final String state;
        private final String city;
        private final String district;
        private final String latitude;
        private final String longitude;

    }

    @Data
    @Builder
    public static final class Pricing {

        private final String sellPrice;
        private final String rentPrice;
        private final String condominiumPrice;

    }

    @Data
    @Builder
    public static final class Media {

        private final String link;
        private final String extension;

    }

}

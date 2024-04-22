package br.com.houseseeker.domain.jetimob.v1;

import br.com.houseseeker.domain.property.UrbanPropertyContract;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

@Data
@Builder
@Accessors(chain = true)
public class PropertyInfoMetadata {

    private UrbanPropertyContract contract;
    private final Location location;
    @Builder.Default
    private final List<Characteristics> characteristics = new LinkedList<>();
    @Builder.Default
    private final List<Pricing> pricing = new LinkedList<>();
    @Builder.Default
    private final List<Media> medias = new LinkedList<>();
    private final Convenience convenience;

    @Data
    @Builder
    public static final class Location {

        private final String state;
        private final String city;
        private final String streetName;
        private final String district;
        private final String condominiumName;

    }

    @Data
    @Builder
    public static final class Characteristics {

        private final String name;
        private final String value;
        private final String additional;
        private final PropertyCharacteristicType type;

    }

    @Data
    @Builder
    public static final class Pricing {

        private final String name;
        private final String value;
        private final PropertyPricingType type;

    }

    @Data
    @Builder
    public static final class Media {

        private final String link;
        private final String linkThumb;
        private final String type;
        private final String extension;

    }

    @Data
    @Builder
    public static final class Convenience {

        private final String description;
        @Builder.Default
        private final List<String> items = new LinkedList<>();

    }

}

package br.com.houseseeker.domain.jetimob.v4;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class PropertyInfoResponse {

    private String url;

    @JsonProperty("code")
    private String providerCode;

    @JsonProperty("contracts")
    private List<Contract> contracts;

    @JsonProperty("type")
    private String type;

    @JsonProperty("bedrooms")
    private Integer dormitories;

    @JsonProperty("suites")
    private Integer suites;

    @JsonProperty("bathrooms")
    private Integer bathrooms;

    @JsonProperty("garage")
    private Integer garages;

    @JsonProperty("condominiumValue")
    private Price condominiumPrice;

    @JsonProperty("interchange")
    private Boolean exchangeable;

    @JsonProperty("financeable")
    private Boolean financeable;

    @JsonProperty("description")
    private String notes;

    @JsonProperty("facilities")
    private List<String> conveniences;

    @JsonProperty("address")
    private Address address;

    @JsonProperty("totalArea")
    private Measure totalArea;

    @JsonProperty("privateArea")
    private Measure privateArea;

    @JsonProperty("usefulArea")
    private Measure usableArea;

    @JsonProperty("terrainArea")
    private Measure terrainTotalArea;

    @JsonProperty("images")
    private List<Media> medias;

    @Data
    public static final class Contract {

        @JsonProperty("id")
        private Integer id;

        @JsonProperty("price")
        private Price price;

    }

    @Data
    public static final class Price {

        @JsonProperty("value")
        private BigDecimal value;

    }

    @Data
    public static final class Address {

        @JsonProperty("state")
        private String state;

        @JsonProperty("city")
        private String city;

        @JsonProperty("neighborhood")
        private String district;

        @JsonProperty("zipCode")
        private String zipCode;

        @JsonProperty("street")
        private String streetName;

        @JsonProperty("number")
        private String streetNumber;

        @JsonProperty("complement")
        private String complement;

        @JsonProperty("coordinate")
        private Coordinate coordinate;

        @Data
        public static final class Coordinate {

            @JsonProperty("latitude")
            private String latitude;

            @JsonProperty("longitude")
            private String longitude;

        }

    }

    @Data
    public static final class Measure {

        @JsonProperty("measurementUnit")
        private String measureUnit;

        @JsonProperty("value")
        private BigDecimal value;

    }

    @Data
    public static final class Media {

        @JsonProperty("src")
        private String link;

        @JsonProperty("srcset")
        private String linkThumb;

    }

}

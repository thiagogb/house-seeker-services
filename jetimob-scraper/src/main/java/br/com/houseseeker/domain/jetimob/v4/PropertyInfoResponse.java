package br.com.houseseeker.domain.jetimob.v4;

import br.com.houseseeker.domain.property.UrbanPropertyContract;
import br.com.houseseeker.domain.property.UrbanPropertyType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static br.com.houseseeker.util.BigDecimalUtils.ONE_HUNDRED;
import static br.com.houseseeker.util.BigDecimalUtils.divideAndRoundByTwo;
import static br.com.houseseeker.util.ConverterUtils.tryToBigDecimalEnUs;
import static br.com.houseseeker.util.ConverterUtils.tryToInteger;
import static br.com.houseseeker.util.StringUtils.getNonBlank;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.util.CollectionUtils.isEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class PropertyInfoResponse {

    private static final int CONTRACT_SELL_VALUE = 1;
    private static final int CONTRACT_RENT_VALUE = 2;
    private static final String COMMERCIAL_TYPE_SUFFIX = "comercial";

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
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static final class Contract {

        @JsonProperty("id")
        private Integer id;

        @JsonProperty("price")
        private Price price;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static final class Price {

        @JsonProperty("value")
        private BigDecimal value;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
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
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static final class Coordinate {

            @JsonProperty("latitude")
            private String latitude;

            @JsonProperty("longitude")
            private String longitude;

        }

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static final class Measure {

        @JsonProperty("measurementUnit")
        private String measureUnit;

        @JsonProperty("value")
        private BigDecimal value;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class Media {

        @JsonProperty("src")
        private String link;

        @JsonProperty("srcset")
        private String linkThumb;

        public String getLinkThumb() {
            if (isBlank(linkThumb))
                return null;

            List<String> thumbs = Arrays.asList(linkThumb.split(","));
            if (thumbs.isEmpty())
                return null;

            List<String> firstThumbParts = Arrays.asList(thumbs.getFirst().split(SPACE));
            if (firstThumbParts.isEmpty())
                return null;

            return firstThumbParts.getFirst().trim();
        }

    }

    public UrbanPropertyContract getMainContract() {
        if (isEmpty(contracts))
            return null;

        if (contracts.stream().anyMatch(c -> c.getId().equals(CONTRACT_SELL_VALUE)))
            return UrbanPropertyContract.SELL;

        if (contracts.stream().anyMatch(c -> c.getId().equals(CONTRACT_RENT_VALUE)))
            return UrbanPropertyContract.RENT;

        return null;
    }

    public UrbanPropertyType getType() {
        if (isBlank(type))
            return null;

        if (type.trim().toLowerCase().endsWith(COMMERCIAL_TYPE_SUFFIX))
            return UrbanPropertyType.COMMERCIAL;

        return UrbanPropertyType.RESIDENTIAL;
    }

    public String getSubType() {
        return type;
    }

    public BigDecimal getSellPrice() {
        return getContractPrice(contracts, CONTRACT_SELL_VALUE);
    }

    public BigDecimal getRentPrice() {
        return getContractPrice(contracts, CONTRACT_RENT_VALUE);
    }

    public BigDecimal getCondominiumPrice() {
        return extractPricingValue(condominiumPrice);
    }

    public String getState() {
        return flatExtractAddressInfo(a -> getNonBlank(a.getState()));
    }

    public String getCity() {
        return flatExtractAddressInfo(a -> getNonBlank(a.getCity()));
    }

    public String getDistrict() {
        return flatExtractAddressInfo(a -> getNonBlank(a.getDistrict()));
    }

    public String getZipCode() {
        return flatExtractAddressInfo(a -> getNonBlank(a.getZipCode()));
    }

    public String getStreetName() {
        return flatExtractAddressInfo(a -> getNonBlank(a.getStreetName()));
    }

    public Integer getStreetNumber() {
        return flatExtractAddressInfo(a -> tryToInteger(a.getStreetNumber()));
    }

    public String getComplement() {
        return flatExtractAddressInfo(a -> getNonBlank(a.getComplement()));
    }

    public BigDecimal getLatitude() {
        return flatExtractAddressInfo(a -> Optional.ofNullable(a.getCoordinate()).flatMap(c -> tryToBigDecimalEnUs(c.getLatitude())));
    }

    public BigDecimal getLongitude() {
        return flatExtractAddressInfo(a -> Optional.ofNullable(a.getCoordinate()).flatMap(c -> tryToBigDecimalEnUs(c.getLongitude())));
    }

    public BigDecimal getTotalArea() {
        return extractMeasureValue(totalArea);
    }

    public BigDecimal getPrivateArea() {
        return extractMeasureValue(privateArea);
    }

    public BigDecimal getUsableArea() {
        return extractMeasureValue(usableArea);
    }

    public BigDecimal getTerrainTotalArea() {
        return extractMeasureValue(terrainTotalArea);
    }

    public String getAreaUnit() {
        return Stream.of(totalArea, privateArea, usableArea, terrainTotalArea)
                     .filter(m -> nonNull(m) && isNotBlank(m.getMeasureUnit()))
                     .map(PropertyInfoResponse.Measure::getMeasureUnit)
                     .findFirst()
                     .orElse(null);
    }

    private BigDecimal getContractPrice(List<PropertyInfoResponse.Contract> contracts, int contractType) {
        return Optional.ofNullable(contracts).orElse(Collections.emptyList())
                       .stream()
                       .filter(c -> c.getId().equals(contractType) && nonNull(c.getPrice()) && nonNull(c.getPrice().getValue()))
                       .findFirst()
                       .map(c -> extractPricingValue(c.getPrice()))
                       .orElse(null);
    }

    private BigDecimal extractPricingValue(Price price) {
        return Optional.ofNullable(price)
                       .flatMap(cp -> Optional.ofNullable(cp.getValue())
                                              .map(v -> divideAndRoundByTwo(v, ONE_HUNDRED))
                       )
                       .orElse(null);
    }

    private <T> T flatExtractAddressInfo(Function<Address, Optional<T>> extractor) {
        return Optional.ofNullable(address).flatMap(extractor).orElse(null);
    }

    private BigDecimal extractMeasureValue(Measure measure) {
        return Optional.ofNullable(measure)
                       .map(PropertyInfoResponse.Measure::getValue)
                       .orElse(null);
    }

}

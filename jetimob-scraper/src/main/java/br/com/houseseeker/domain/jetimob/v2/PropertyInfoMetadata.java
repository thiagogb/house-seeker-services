package br.com.houseseeker.domain.jetimob.v2;

import br.com.houseseeker.domain.jetimob.PropertyCharacteristic;
import br.com.houseseeker.domain.jetimob.PropertyDetail;
import br.com.houseseeker.domain.property.UrbanPropertyContract;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static br.com.houseseeker.util.ConverterUtils.tryToBigDecimalEnUs;
import static br.com.houseseeker.util.ConverterUtils.tryToBigDecimalPtBR;
import static br.com.houseseeker.util.ConverterUtils.tryToInteger;
import static br.com.houseseeker.util.StringUtils.keepOnlyNumericSymbols;

@Data
@Builder(toBuilder = true)
@Accessors(chain = true)
public class PropertyInfoMetadata {

    private String providerCode;
    private String url;
    private UrbanPropertyContract contract;
    private String subType;
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

        public BigDecimal getLatitudeAsBigDecimal() {
            return getCoordValueAsBigDecimal(latitude);
        }

        public BigDecimal getLongitudeAsBigDecimal() {
            return getCoordValueAsBigDecimal(longitude);
        }

        private BigDecimal getCoordValueAsBigDecimal(String value) {
            return tryToBigDecimalEnUs(keepOnlyNumericSymbols(value)).orElse(null);
        }

    }

    @Data
    @Builder(toBuilder = true)
    public static final class Pricing {

        private final String sellPrice;
        private final String rentPrice;
        private final String condominiumPrice;

        public BigDecimal getSellPriceAsBigDecimal() {
            return getPricingValueAsBigDecimal(sellPrice);
        }

        public BigDecimal getRentPriceAsBigDecimal() {
            return getPricingValueAsBigDecimal(rentPrice);
        }

        public BigDecimal getCondominiumPriceAsBigDecimal() {
            return getPricingValueAsBigDecimal(condominiumPrice);
        }

        private BigDecimal getPricingValueAsBigDecimal(String value) {
            return tryToBigDecimalPtBR(keepOnlyNumericSymbols(value)).orElse(null);
        }

    }

    @Data
    @Builder
    public static final class Media {

        private final String link;
        private final String extension;

    }

    public <T> T extractCharacteristicsByTypes(
            @NotNull Function<PropertyCharacteristic, Optional<T>> valueSupplier,
            @NotNull PropertyCharacteristic.Type... types
    ) {
        return Arrays.stream(types)
                     .map(type -> PropertyCharacteristic.findType(characteristics, type))
                     .filter(Optional::isPresent)
                     .map(Optional::get)
                     .findFirst()
                     .flatMap(valueSupplier)
                     .orElse(null);
    }

    public Optional<Integer> getCharacteristicValueAsInteger(PropertyCharacteristic characteristic) {
        return tryToInteger(keepOnlyNumericSymbols(characteristic.getValue()));
    }

    public Optional<BigDecimal> getCharacteristicValueAsBigDecimal(PropertyCharacteristic characteristic) {
        return tryToBigDecimalEnUs(keepOnlyNumericSymbols(characteristic.getValue()));
    }

    public Optional<PropertyDetail> getDetailByType(@NotNull PropertyDetail.Type type) {
        return PropertyDetail.findType(details, type);
    }

}

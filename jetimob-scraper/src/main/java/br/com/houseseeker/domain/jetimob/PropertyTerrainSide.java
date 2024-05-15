package br.com.houseseeker.domain.jetimob;

import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Optional;

import static br.com.houseseeker.util.ConverterUtils.tryToBigDecimalEnUs;
import static br.com.houseseeker.util.StringUtils.keepOnlyNumericSymbols;

@RequiredArgsConstructor
public enum PropertyTerrainSide {

    FRONT {
        @Override
        public Optional<BigDecimal> extract(@NotNull PropertyDetail propertyDetail) {
            return ifPrefixMatchesThenExtract(propertyDetail, TERRAIN_FRONT_PREFIX);
        }
    },
    BACK {
        @Override
        public Optional<BigDecimal> extract(@NotNull PropertyDetail propertyDetail) {
            return ifPrefixMatchesThenExtract(propertyDetail, TERRAIN_BACK_PREFIX);
        }
    },
    LEFT {
        @Override
        public Optional<BigDecimal> extract(@NotNull PropertyDetail propertyDetail) {
            return ifPrefixMatchesThenExtract(propertyDetail, TERRAIN_LEFT_PREFIX);
        }
    },
    RIGHT {
        @Override
        public Optional<BigDecimal> extract(@NotNull PropertyDetail propertyDetail) {
            return ifPrefixMatchesThenExtract(propertyDetail, TERRAIN_RIGHT_PREFIX);
        }
    };

    private static final String TERRAIN_FRONT_PREFIX = "frente";
    private static final String TERRAIN_BACK_PREFIX = "fundos";
    private static final String TERRAIN_RIGHT_PREFIX = "direita";
    private static final String TERRAIN_LEFT_PREFIX = "esquerda";

    public abstract Optional<BigDecimal> extract(@NotNull PropertyDetail propertyDetail);

    protected Optional<BigDecimal> ifPrefixMatchesThenExtract(PropertyDetail propertyDetail, String prefix) {
        if (!propertyDetail.getType().equals(PropertyDetail.Type.MEASURES))
            throw new ExtendedRuntimeException(
                    "Invalid property detail type: got %s expected %s",
                    propertyDetail.getType(),
                    PropertyDetail.Type.MEASURES
            );

        return propertyDetail.getItems()
                             .stream()
                             .filter(i -> i.trim().toLowerCase().startsWith(prefix))
                             .findFirst()
                             .flatMap(this::getMeasureAsBigDecimal);
    }

    private Optional<BigDecimal> getMeasureAsBigDecimal(String value) {
        return tryToBigDecimalEnUs(keepOnlyNumericSymbols(value));
    }

}

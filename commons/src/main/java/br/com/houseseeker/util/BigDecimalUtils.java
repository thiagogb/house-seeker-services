package br.com.houseseeker.util;

import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;

@UtilityClass
public class BigDecimalUtils {

    private static final BigDecimal ONE_HUNDRED_DIVISOR = BigDecimal.valueOf(100);

    public BigDecimal divideBy100AndRoundByTwo(@NotNull BigDecimal value) {
        return value.divide(ONE_HUNDRED_DIVISOR, 2, RoundingMode.HALF_UP);
    }

}

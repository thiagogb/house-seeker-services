package br.com.houseseeker.util;

import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;

@UtilityClass
public class BigDecimalUtils {

    public static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    public BigDecimal divideBy100AndRoundByTwo(@NotNull BigDecimal value) {
        return divideByAndRoundByTwo(value, ONE_HUNDRED);
    }

    public BigDecimal divideByAndRoundByTwo(@NotNull BigDecimal value, @NotNull BigDecimal divisor) {
        return value.divide(divisor, 2, HALF_UP);
    }

    public boolean isZero(@NotNull BigDecimal value) {
        return isEqual(ZERO, value);
    }

    public boolean isEqual(@NotNull BigDecimal value1, @NotNull BigDecimal value2) {
        return value1.compareTo(value2) == 0;
    }

}

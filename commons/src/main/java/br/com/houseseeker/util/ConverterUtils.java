package br.com.houseseeker.util;

import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

@UtilityClass
public class ConverterUtils {

    private static final Locale PT_BR_LOCALE = Locale.of("pt", "BR");
    private static final Locale EN_US_LOCALE = Locale.of("en", "US");

    public Optional<Integer> tryToInteger(@Nullable String value) {
        if (isBlank(value))
            return Optional.empty();

        value = value.trim();

        try {
            return Optional.of(Integer.valueOf(value));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public Optional<BigDecimal> tryToBigDecimalPtBR(@Nullable String value) {
        return tryToBigDecimal(value, DecimalFormat.getInstance(PT_BR_LOCALE));
    }

    public Optional<BigDecimal> tryToBigDecimalEnUs(@Nullable String value) {
        return tryToBigDecimal(value, DecimalFormat.getInstance(EN_US_LOCALE));
    }

    private Optional<BigDecimal> tryToBigDecimal(
            @Nullable String value,
            NumberFormat numberFormat
    ) {
        if (isBlank(value))
            return Optional.empty();

        value = value.trim();

        try {
            BigDecimal result = BigDecimal.valueOf(numberFormat.parse(value).doubleValue());
            return Optional.of(result);
        } catch (NumberFormatException | ParseException e) {
            return Optional.empty();
        }
    }

}

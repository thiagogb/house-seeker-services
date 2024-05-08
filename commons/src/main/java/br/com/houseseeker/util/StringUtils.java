package br.com.houseseeker.util;

import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@UtilityClass
public class StringUtils {

    public Optional<String> getNonBlank(@Nullable String value) {
        return isNotBlank(value) ? Optional.of(value) : Optional.empty();
    }

    public String keepOnlyNumericSymbols(@Nullable String value) {
        if (isBlank(value))
            return value;

        return value.trim().replaceAll("[^0-9,.+-]", EMPTY);
    }

    public String removeParentheses(@Nullable String value) {
        if (isBlank(value))
            return value;

        return value.replaceAll("[()]", EMPTY);
    }

}

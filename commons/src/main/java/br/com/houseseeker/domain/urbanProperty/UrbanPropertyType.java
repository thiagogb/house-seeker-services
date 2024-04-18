package br.com.houseseeker.domain.urbanProperty;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public enum UrbanPropertyType {

    RESIDENTIAL {
        @Override
        protected boolean matches(String value) {
            return !value.trim().toLowerCase().contains(COMMERCIAL_LABEL);
        }
    },
    COMMERCIAL {
        @Override
        protected boolean matches(String value) {
            return value.trim().toLowerCase().contains(COMMERCIAL_LABEL);
        }
    };

    private static final String COMMERCIAL_LABEL = "comercial";

    protected abstract boolean matches(String value);

    public static Optional<UrbanPropertyType> detect(@NotNull String value) {
        for (var type : values())
            if (type.matches(value))
                return Optional.of(type);

        log.info("Undefined type for: {}", value);

        return Optional.empty();
    }

}

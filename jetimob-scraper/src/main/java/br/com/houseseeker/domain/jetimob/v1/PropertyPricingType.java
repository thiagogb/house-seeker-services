package br.com.houseseeker.domain.jetimob.v1;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public enum PropertyPricingType {

    SELL_PRICE {
        @Override
        protected boolean matches(String name) {
            return name.trim().toLowerCase().startsWith("venda");
        }
    },
    RENT_PRICE {
        @Override
        protected boolean matches(String name) {
            return name.trim().toLowerCase().startsWith("locação");
        }
    },
    CONDOMINIUM_PRICE {
        @Override
        protected boolean matches(String name) {
            return name.trim().toLowerCase().startsWith("condomínio");
        }
    },
    IPTU {
        @Override
        protected boolean matches(String name) {
            return name.trim().toLowerCase().startsWith("iptu");
        }
    },
    SEASON {
        @Override
        protected boolean matches(String name) {
            return name.trim().toLowerCase().contains("temporada");
        }
    };

    protected abstract boolean matches(String name);

    public static Optional<PropertyPricingType> getByName(@NotNull String name) {
        for (var type : values())
            if (type.matches(name))
                return Optional.of(type);

        log.info("Undefined pricing type: {}", name);

        return Optional.empty();
    }

}

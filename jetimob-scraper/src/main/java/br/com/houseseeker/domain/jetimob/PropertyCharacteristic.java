package br.com.houseseeker.domain.jetimob;

import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNullElse;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Data
@Builder
public class PropertyCharacteristic {

    private final String value;
    private final Type type;

    public enum Type {

        DORMITORIES {
            @Override
            protected boolean matches(String value) {
                return value.endsWith("dormitório") || value.endsWith("dormitórios");
            }
        },
        ROOMS {
            @Override
            protected boolean matches(String value) {
                return value.endsWith("sala") || value.endsWith("salas");
            }
        },
        SUITES {
            @Override
            protected boolean matches(String value) {
                return value.endsWith("suíte") || value.endsWith("suítes");
            }
        },
        TOTAL_AREA {
            @Override
            protected boolean matches(String value) {
                return value.endsWith("m² total");
            }
        },
        PRIVATE_AREA {
            @Override
            protected boolean matches(String value) {
                return value.endsWith("m² privativa");
            }
        },
        BATHROOMS {
            @Override
            protected boolean matches(String value) {
                return value.endsWith("banheiro") || value.endsWith("banheiros");
            }
        },
        GARAGES {
            @Override
            protected boolean matches(String value) {
                return value.endsWith("vaga") || value.endsWith("vagas");
            }
        };

        protected abstract boolean matches(String value);

        public static Type typeOf(@NotNull String value) {
            value = requireNonNullElse(value, EMPTY).trim().toLowerCase();

            for (Type type : values())
                if (type.matches(value))
                    return type;

            throw new ExtendedRuntimeException("Undefined type for characteristic with value: %s", String.join(",", value));
        }

    }

    public static Optional<PropertyCharacteristic> findType(
            @NotNull List<PropertyCharacteristic> characteristics,
            @NotNull PropertyCharacteristic.Type type
    ) {
        return characteristics.stream()
                              .filter(c -> c.getType().equals(type))
                              .findFirst();
    }

}

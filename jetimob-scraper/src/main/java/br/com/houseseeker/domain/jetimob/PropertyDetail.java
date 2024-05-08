package br.com.houseseeker.domain.jetimob;

import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

import static java.util.Objects.requireNonNullElse;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Data
@Builder
public class PropertyDetail {

    private final Type type;
    private final List<String> items;

    public enum Type {

        FLOOR_TYPE {
            @Override
            protected boolean matches(String value) {
                return value.equals("tipo de piso");
            }
        },
        SOLAR_POSITION {
            @Override
            protected boolean matches(String value) {
                return value.equals("posição solar");
            }
        },
        POSITION {
            @Override
            protected boolean matches(String value) {
                return value.equals("posição");
            }
        },
        MEASURES {
            @Override
            protected boolean matches(String value) {
                return value.equals("medidas");
            }
        },
        GENERAL_INFO {
            @Override
            protected boolean matches(String value) {
                return value.equals("informações gerais");
            }
        };

        protected abstract boolean matches(String value);

        public static Type typeOf(@NotNull String value) {
            value = requireNonNullElse(value, EMPTY).trim().toLowerCase();

            for (Type type : values())
                if (type.matches(value))
                    return type;

            throw new ExtendedRuntimeException("Undefined type for detail with value: %s", String.join(",", value));
        }

    }

}

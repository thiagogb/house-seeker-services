package br.com.houseseeker.entity.converter;

import br.com.houseseeker.util.StringUtils;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Optional;

@Converter
public class BooleanToVarCharConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean value) {
        return Optional.ofNullable(value)
                       .map(v -> v ? "Y" : "N")
                       .orElse(null);
    }

    @Override
    public Boolean convertToEntityAttribute(String value) {
        return Optional.ofNullable(value)
                       .flatMap(StringUtils::getNonBlank)
                       .map(v -> v.equalsIgnoreCase("Y"))
                       .orElse(null);
    }

}

package br.com.houseseeker.util;

import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ObjectMapperUtils {

    public <T> T deserializeAs(@NotNull ObjectMapper objectMapper, @NotBlank String content, @NotNull Class<T> tClass) {
        try {
            return objectMapper.readValue(content, tClass);
        } catch (JsonProcessingException e) {
            throw new ExtendedRuntimeException("Content read failed", e);
        }
    }

    public <T> String serialize(@NotNull ObjectMapper objectMapper, T object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ExtendedRuntimeException("Content write failed", e);
        }
    }

}

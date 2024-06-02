package br.com.houseseeker.mapper;

import com.google.protobuf.DoubleValue;
import org.mapstruct.Mapper;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public abstract class ProtoDoubleMapper {

    public DoubleValue toDoubleValue(@Nullable BigDecimal value) {
        return Optional.ofNullable(value).map(v -> DoubleValue.of(v.doubleValue())).orElse(DoubleValue.getDefaultInstance());
    }

    public BigDecimal toBigDecimal(@Nullable DoubleValue value) {
        return Optional.ofNullable(value).map(v -> BigDecimal.valueOf(v.getValue())).orElse(null);
    }

}

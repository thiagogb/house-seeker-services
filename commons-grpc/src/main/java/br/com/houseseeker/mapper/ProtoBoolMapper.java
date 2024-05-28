package br.com.houseseeker.mapper;

import com.google.protobuf.BoolValue;
import org.mapstruct.Mapper;
import org.springframework.lang.Nullable;

import java.util.Optional;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public abstract class ProtoBoolMapper {

    public Boolean toBoolean(@Nullable BoolValue value) {
        return Optional.ofNullable(value).map(BoolValue::getValue).orElse(null);
    }

    public BoolValue toBoolValue(@Nullable Boolean value) {
        return Optional.ofNullable(value).map(BoolValue::of).orElse(BoolValue.getDefaultInstance());
    }

}

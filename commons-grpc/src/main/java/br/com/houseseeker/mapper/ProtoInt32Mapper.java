package br.com.houseseeker.mapper;

import com.google.protobuf.Int32Value;
import org.mapstruct.Mapper;
import org.springframework.lang.Nullable;

import java.util.Optional;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public abstract class ProtoInt32Mapper {

    public Integer toInteger(@Nullable Int32Value value) {
        return Optional.ofNullable(value).map(Int32Value::getValue).orElse(null);
    }

    public Int32Value toInt32Value(@Nullable Integer value) {
        return Optional.ofNullable(value).map(Int32Value::of).orElse(Int32Value.getDefaultInstance());
    }

}

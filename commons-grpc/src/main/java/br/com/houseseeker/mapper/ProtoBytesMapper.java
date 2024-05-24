package br.com.houseseeker.mapper;

import com.google.protobuf.ByteString;
import com.google.protobuf.BytesValue;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.springframework.lang.Nullable;

import java.util.Base64;
import java.util.Optional;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public abstract class ProtoBytesMapper {

    public BytesValue toBytesValue(@Nullable byte[] value) {
        return Optional.ofNullable(value)
                       .map(this::bytesToByteString)
                       .map(BytesValue::of)
                       .orElse(BytesValue.getDefaultInstance());
    }

    protected BytesValue toBytesValue(@Nullable String value) {
        return Optional.ofNullable(value)
                       .filter(StringUtils::isNotBlank)
                       .map(v -> BytesValue.of(ByteString.copyFrom(Base64.getDecoder().decode(v))))
                       .orElse(BytesValue.getDefaultInstance());
    }

    public byte[] toBytes(@Nullable BytesValue value) {
        return Optional.ofNullable(value)
                       .map(v -> v.getValue().toByteArray())
                       .orElse(null);
    }

    private ByteString bytesToByteString(byte[] value) {
        return Optional.ofNullable(value).map(ByteString::copyFrom).orElse(ByteString.EMPTY);
    }

}

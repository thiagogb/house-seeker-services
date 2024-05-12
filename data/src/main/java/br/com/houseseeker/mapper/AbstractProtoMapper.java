package br.com.houseseeker.mapper;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import com.google.protobuf.BytesValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import org.mapstruct.Named;
import org.springframework.lang.Nullable;

import java.util.Optional;

abstract class AbstractProtoMapper {

    @Named("intToInt32Value")
    protected Int32Value intToInt32Value(@Nullable Integer value) {
        return Optional.ofNullable(value).map(Int32Value::of).orElse(Int32Value.getDefaultInstance());
    }

    @Named("stringToStringValue")
    protected StringValue stringToStringValue(@Nullable String value) {
        return Optional.ofNullable(value).map(StringValue::of).orElse(StringValue.getDefaultInstance());
    }

    @Named("bytesToBytesValue")
    protected BytesValue bytesToBytesValue(@Nullable byte[] value) {
        return Optional.ofNullable(value)
                       .map(this::bytesToByteString)
                       .map(BytesValue::of)
                       .orElse(BytesValue.getDefaultInstance());
    }

    @Named("boolToBoolValue")
    protected BoolValue boolToBoolValue(@Nullable Boolean value) {
        return Optional.ofNullable(value).map(BoolValue::of).orElse(BoolValue.getDefaultInstance());
    }

    private ByteString bytesToByteString(byte[] value) {
        return Optional.ofNullable(value).map(ByteString::copyFrom).orElse(ByteString.EMPTY);
    }

}
package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.property.UrbanPropertyContract;
import br.com.houseseeker.domain.property.UrbanPropertyMediaType;
import br.com.houseseeker.domain.property.UrbanPropertyPriceVariationType;
import br.com.houseseeker.domain.property.UrbanPropertyStatus;
import br.com.houseseeker.domain.property.UrbanPropertyType;
import br.com.houseseeker.domain.provider.ProviderMechanism;
import com.google.protobuf.StringValue;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public abstract class ProtoStringMapper {

    public String toString(@Nullable StringValue value) {
        return Optional.ofNullable(value).map(StringValue::getValue).orElse(null);
    }

    public ProviderMechanism toProviderMechanism(@Nullable StringValue value) {
        return toEnum(ProviderMechanism.class, value);
    }

    public UrbanPropertyContract toUrbanPropertyContract(@Nullable StringValue value) {
        return toEnum(UrbanPropertyContract.class, value);
    }

    public UrbanPropertyType toUrbanPropertyType(@Nullable StringValue value) {
        return toEnum(UrbanPropertyType.class, value);
    }

    public UrbanPropertyStatus toUrbanPropertyStatus(@Nullable StringValue value) {
        return toEnum(UrbanPropertyStatus.class, value);
    }

    public UrbanPropertyMediaType toUrbanPropertyMediaType(@Nullable StringValue value) {
        return toEnum(UrbanPropertyMediaType.class, value);
    }

    public UrbanPropertyPriceVariationType toPriceVariationType(@Nullable StringValue value) {
        return toEnum(UrbanPropertyPriceVariationType.class, value);
    }

    public abstract List<StringValue> toStringValue(@Nullable List<String> values);

    public StringValue toStringValue(@Nullable String value) {
        return Optional.ofNullable(value).map(StringValue::of).orElse(StringValue.getDefaultInstance());
    }

    public StringValue toStringValue(@Nullable ProviderMechanism value) {
        return Optional.ofNullable(value)
                       .map(v -> StringValue.of(v.name()))
                       .orElse(StringValue.getDefaultInstance());
    }

    public StringValue toStringValue(@Nullable UrbanPropertyContract value) {
        return Optional.ofNullable(value).map(v -> StringValue.of(v.name())).orElse(StringValue.getDefaultInstance());
    }

    public StringValue toStringValue(@Nullable UrbanPropertyType value) {
        return Optional.ofNullable(value).map(v -> StringValue.of(v.name())).orElse(StringValue.getDefaultInstance());
    }

    private <T extends Enum<T>> T toEnum(Class<T> tClass, @Nullable StringValue value) {
        return Optional.ofNullable(value)
                       .map(StringValue::getValue)
                       .filter(StringUtils::isNotBlank)
                       .map(v -> Enum.valueOf(tClass, v))
                       .orElse(null);
    }

}

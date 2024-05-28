package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.property.UrbanPropertyContract;
import br.com.houseseeker.domain.property.UrbanPropertyType;
import br.com.houseseeker.domain.provider.ProviderMechanism;
import com.google.protobuf.StringValue;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.springframework.lang.Nullable;

import java.util.Optional;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public abstract class ProtoStringMapper {

    public String toString(@Nullable StringValue value) {
        return Optional.ofNullable(value).map(StringValue::getValue).orElse(null);
    }

    public ProviderMechanism toProviderMechanism(@Nullable StringValue value) {
        return Optional.ofNullable(value)
                       .map(StringValue::getValue)
                       .filter(StringUtils::isNotBlank)
                       .map(ProviderMechanism::valueOf)
                       .orElse(null);
    }

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

}

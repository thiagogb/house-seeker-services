package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.proto.ProviderData;
import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.entity.Provider;
import com.google.protobuf.StringValue;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = IGNORE)
public abstract class ProviderMapper extends AbstractProtoMapper {

    public abstract List<ProviderData> toProto(@NotNull List<Provider> providers);

    @Mapping(source = "id", target = "id", qualifiedByName = "intToInt32Value")
    @Mapping(source = "name", target = "name", qualifiedByName = "stringToStringValue")
    @Mapping(source = "siteUrl", target = "siteUrl", qualifiedByName = "stringToStringValue")
    @Mapping(source = "dataUrl", target = "dataUrl", qualifiedByName = "stringToStringValue")
    @Mapping(source = "mechanism", target = "mechanism", qualifiedByName = "mechanismToStringValue")
    @Mapping(source = "params", target = "params", qualifiedByName = "stringToStringValue")
    @Mapping(source = "cronExpression", target = "cronExpression", qualifiedByName = "stringToStringValue")
    @Mapping(source = "logo", target = "logo", qualifiedByName = "bytesToBytesValue")
    @Mapping(source = "active", target = "active", qualifiedByName = "boolToBoolValue")
    public abstract ProviderData toProto(@NotNull Provider provider);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "name", target = "name", qualifiedByName = "stringValueToString")
    @Mapping(source = "siteUrl", target = "siteUrl", qualifiedByName = "stringValueToString")
    @Mapping(source = "dataUrl", target = "dataUrl", qualifiedByName = "stringValueToString")
    @Mapping(source = "mechanism", target = "mechanism", qualifiedByName = "stringValueToMechanism")
    @Mapping(source = "params", target = "params", qualifiedByName = "stringValueToString")
    @Mapping(source = "cronExpression", target = "cronExpression", qualifiedByName = "stringValueToString")
    @Mapping(source = "logo", target = "logo", qualifiedByName = "bytesValueToBytes")
    @Mapping(source = "active", target = "active", qualifiedByName = "boolValueToBool")
    public abstract Provider createEntity(@NotNull ProviderData providerData);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "name", target = "name", qualifiedByName = "stringValueToString")
    @Mapping(source = "siteUrl", target = "siteUrl", qualifiedByName = "stringValueToString")
    @Mapping(source = "dataUrl", target = "dataUrl", qualifiedByName = "stringValueToString")
    @Mapping(source = "mechanism", target = "mechanism", qualifiedByName = "stringValueToMechanism")
    @Mapping(source = "params", target = "params", qualifiedByName = "stringValueToString")
    @Mapping(source = "cronExpression", target = "cronExpression", qualifiedByName = "stringValueToString")
    @Mapping(source = "logo", target = "logo", qualifiedByName = "bytesValueToBytes")
    @Mapping(source = "active", target = "active", qualifiedByName = "boolValueToBool")
    public abstract void copyToEntity(@NotNull ProviderData providerData, @MappingTarget Provider target);

    @Named("mechanismToStringValue")
    protected StringValue mechanismToStringValue(@Nullable ProviderMechanism value) {
        return Optional.ofNullable(value).map(v -> StringValue.of(v.name())).orElse(StringValue.getDefaultInstance());
    }

    @Named("stringValueToMechanism")
    protected ProviderMechanism stringValueToMechanism(@Nullable StringValue value) {
        return Optional.ofNullable(value).map(v -> ProviderMechanism.valueOf(v.getValue())).orElse(null);
    }

}

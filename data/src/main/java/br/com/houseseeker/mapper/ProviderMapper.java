package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.proto.ProviderData;
import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.entity.Provider;
import com.google.protobuf.StringValue;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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

    @Named("mechanismToStringValue")
    protected StringValue mechanismToStringValue(@Nullable ProviderMechanism value) {
        return Optional.ofNullable(value).map(v -> StringValue.of(v.name())).orElse(StringValue.getDefaultInstance());
    }

}

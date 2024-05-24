package br.com.houseseeker.mapper;

import br.com.houseseeker.controller.ProviderController;
import br.com.houseseeker.domain.dto.ProviderDto;
import br.com.houseseeker.domain.input.ProviderCreationInput;
import br.com.houseseeker.domain.input.ProviderEditionInput;
import br.com.houseseeker.domain.proto.ProviderData;
import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.util.ProtoWrapperUtils;
import com.google.protobuf.ByteString;
import com.google.protobuf.BytesValue;
import com.google.protobuf.StringValue;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.lang.Nullable;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ProviderMapper extends AbstractProtoMapper {

    public abstract List<ProviderDto> toDto(List<ProviderData> dataList);

    @Mapping(source = "id", target = "id", qualifiedByName = "int32ValueToInteger")
    @Mapping(source = "name", target = "name", qualifiedByName = "stringValueToString")
    @Mapping(source = "siteUrl", target = "siteUrl", qualifiedByName = "stringValueToString")
    @Mapping(source = "dataUrl", target = "dataUrl", qualifiedByName = "stringValueToString")
    @Mapping(source = "mechanism", target = "mechanism", qualifiedByName = "stringValueToMechanism")
    @Mapping(source = "params", target = "params", qualifiedByName = "stringValueToString")
    @Mapping(source = "cronExpression", target = "cronExpression", qualifiedByName = "stringValueToString")
    @Mapping(source = "data", target = "logoUrl", qualifiedByName = "mapLogoUrl")
    @Mapping(source = "active", target = "active", qualifiedByName = "boolValueToBool")
    public abstract ProviderDto toDto(@NotNull ProviderData data);

    @Mapping(source = "name", target = "name", qualifiedByName = "stringToStringValue")
    @Mapping(source = "siteUrl", target = "siteUrl", qualifiedByName = "stringToStringValue")
    @Mapping(source = "dataUrl", target = "dataUrl", qualifiedByName = "stringToStringValue")
    @Mapping(source = "mechanism", target = "mechanism", qualifiedByName = "mechanismToStringValue")
    @Mapping(source = "params", target = "params", qualifiedByName = "stringToStringValue")
    @Mapping(source = "cronExpression", target = "cronExpression", qualifiedByName = "stringToStringValue")
    @Mapping(source = "logo", target = "logo", qualifiedByName = "stringToBytesValue")
    @Mapping(source = "active", target = "active", qualifiedByName = "boolToBoolValue")
    public abstract ProviderData toData(@NotNull ProviderCreationInput input);

    @Mapping(
            target = "name",
            expression = "java(input.hasChanged(\"name\") ? stringToStringValue(input.getName()) : dataBuilder.getName())"
    )
    @Mapping(
            target = "siteUrl",
            expression = "java(input.hasChanged(\"siteUrl\") ? stringToStringValue(input.getSiteUrl()) : dataBuilder.getSiteUrl())"
    )
    @Mapping(
            target = "dataUrl",
            expression = "java(input.hasChanged(\"dataUrl\") ? stringToStringValue(input.getDataUrl()) : dataBuilder.getDataUrl())"
    )
    @Mapping(
            target = "mechanism",
            expression = "java(input.hasChanged(\"mechanism\") ? mechanismToStringValue(input.getMechanism()) : dataBuilder.getMechanism())"
    )
    @Mapping(
            target = "params",
            expression = "java(input.hasChanged(\"params\") ? stringToStringValue(input.getParams()) : dataBuilder.getParams())"
    )
    @Mapping(
            target = "cronExpression",
            expression = "java(input.hasChanged(\"cronExpression\") ? stringToStringValue(input.getCronExpression()) : dataBuilder.getCronExpression())"
    )
    @Mapping(
            target = "logo",
            expression = "java(input.hasChanged(\"logo\") ? stringToBytesValue(input.getLogo()) : dataBuilder.getLogo())"
    )
    @Mapping(
            target = "active",
            expression = "java(input.hasChanged(\"active\") ? boolToBoolValue(input.getActive()) : dataBuilder.getActive())"
    )
    public abstract void copyToData(@NotNull ProviderEditionInput input, @MappingTarget ProviderData.Builder dataBuilder);

    @Named("mapLogoUrl")
    protected String mapLogoUrl(@Nullable ProviderData data) {
        if (ProtoWrapperUtils.getInt(data.getId()) == 0 || isNull(ProtoWrapperUtils.getBytes(data.getLogo())))
            return null;

        return WebMvcLinkBuilder.linkTo(ProviderController.class)
                                .slash(data.getId().getValue())
                                .slash("logo")
                                .toUri()
                                .toString();
    }

    @Named("stringValueToMechanism")
    protected ProviderMechanism stringValueToMechanism(@Nullable StringValue value) {
        return Optional.ofNullable(value)
                       .map(StringValue::getValue)
                       .filter(StringUtils::isNotBlank)
                       .map(ProviderMechanism::valueOf)
                       .orElse(null);
    }

    @Named("mechanismToStringValue")
    protected StringValue mechanismToStringValue(@Nullable ProviderMechanism value) {
        return Optional.ofNullable(value)
                       .map(v -> StringValue.of(v.name()))
                       .orElse(StringValue.getDefaultInstance());
    }

    @Named("stringToBytesValue")
    protected BytesValue stringToBytesValue(@Nullable String value) {
        return Optional.ofNullable(value)
                       .filter(StringUtils::isNotBlank)
                       .map(v -> BytesValue.of(ByteString.copyFrom(Base64.getDecoder().decode(v))))
                       .orElse(BytesValue.getDefaultInstance());
    }

}

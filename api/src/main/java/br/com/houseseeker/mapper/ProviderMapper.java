package br.com.houseseeker.mapper;

import br.com.houseseeker.controller.ProviderController;
import br.com.houseseeker.domain.dto.ProviderDto;
import br.com.houseseeker.domain.input.ProviderCreationInput;
import br.com.houseseeker.domain.input.ProviderEditionInput;
import br.com.houseseeker.domain.proto.ProviderData;
import br.com.houseseeker.util.ProtoWrapperUtils;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.lang.Nullable;

import java.util.List;

import static java.util.Objects.isNull;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(
        componentModel = SPRING,
        unmappedTargetPolicy = IGNORE,
        uses = {
                ProtoInt32Mapper.class,
                ProtoStringMapper.class,
                ProtoBytesMapper.class,
                ProtoBoolMapper.class
        }
)
public abstract class ProviderMapper {

    public abstract List<ProviderDto> toDto(List<ProviderData> dataList);

    @Mapping(target = "logoUrl", expression = "java(mapLogoUrl(proto))")
    public abstract ProviderDto toDto(@NotNull ProviderData proto);

    public abstract ProviderData toProto(@NotNull ProviderCreationInput input);

    @Mapping(
            target = "name",
            expression = "java(input.hasChanged(\"name\") ? protoStringMapper.toStringValue(input.getName()) : builder.getName())"
    )
    @Mapping(
            target = "siteUrl",
            expression = "java(input.hasChanged(\"siteUrl\") ? protoStringMapper.toStringValue(input.getSiteUrl()) : builder.getSiteUrl())"
    )
    @Mapping(
            target = "dataUrl",
            expression = "java(input.hasChanged(\"dataUrl\") ? protoStringMapper.toStringValue(input.getDataUrl()) : builder.getDataUrl())"
    )
    @Mapping(
            target = "mechanism",
            expression = "java(input.hasChanged(\"mechanism\") ? protoStringMapper.toStringValue(input.getMechanism()) : builder.getMechanism())"
    )
    @Mapping(
            target = "params",
            expression = "java(input.hasChanged(\"params\") ? protoStringMapper.toStringValue(input.getParams()) : builder.getParams())"
    )
    @Mapping(
            target = "cronExpression",
            expression = "java(input.hasChanged(\"cronExpression\") ? protoStringMapper.toStringValue(input.getCronExpression()) : builder.getCronExpression())"
    )
    @Mapping(
            target = "logo",
            expression = "java(input.hasChanged(\"logo\") ? protoBytesMapper.toBytesValue(input.getLogo()) : builder.getLogo())"
    )
    @Mapping(
            target = "active",
            expression = "java(input.hasChanged(\"active\") ? protoBoolMapper.toBoolValue(input.getActive()) : builder.getActive())"
    )
    public abstract void copyToProto(@NotNull ProviderEditionInput input, @MappingTarget ProviderData.Builder builder);

    @Named("mapLogoUrl")
    protected String mapLogoUrl(@Nullable ProviderData data) {
        if (isNull(data) || ProtoWrapperUtils.getInt(data.getId()) == 0 || isNull(ProtoWrapperUtils.getBytes(data.getLogo())))
            return null;

        return WebMvcLinkBuilder.linkTo(ProviderController.class)
                                .slash(data.getId().getValue())
                                .slash("logo")
                                .toUri()
                                .toString();
    }

}

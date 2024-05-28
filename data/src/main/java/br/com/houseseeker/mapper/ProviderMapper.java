package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.proto.ProviderData;
import br.com.houseseeker.entity.Provider;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.lang.Nullable;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(
        componentModel = SPRING,
        unmappedTargetPolicy = IGNORE,
        uses = {
                ProtoInt32Mapper.class,
                ProtoStringMapper.class,
                ProtoBoolMapper.class,
                ProtoBytesMapper.class
        }
)
public abstract class ProviderMapper {

    public abstract List<ProviderData> toProto(@Nullable List<Provider> entities);

    public abstract ProviderData toProto(@Nullable Provider entity);

    @Mapping(target = "id", ignore = true)
    public abstract Provider toEntity(@Nullable ProviderData proto);

    @Mapping(target = "id", ignore = true)
    public abstract void toEntity(@Nullable ProviderData proto, @MappingTarget Provider entity);

}

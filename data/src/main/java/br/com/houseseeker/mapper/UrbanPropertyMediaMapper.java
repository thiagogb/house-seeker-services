package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.property.AbstractUrbanPropertyMediaMetadata;
import br.com.houseseeker.domain.proto.UrbanPropertyMediaData;
import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.entity.UrbanPropertyMedia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
                UrbanPropertyMapper.class
        }
)
public abstract class UrbanPropertyMediaMapper {

    public abstract List<UrbanPropertyMediaData> toProto(@Nullable List<UrbanPropertyMedia> entities);

    @Mapping(
            source = "urbanProperty",
            target = "urbanProperty",
            conditionExpression = "java(!(entity.getUrbanProperty() instanceof org.hibernate.proxy.HibernateProxy) && entity.getUrbanProperty() != null)",
            defaultExpression = "java(br.com.houseseeker.domain.proto.UrbanPropertyData.getDefaultInstance())"
    )
    public abstract UrbanPropertyMediaData toProto(@Nullable UrbanPropertyMedia entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "urbanProperty", target = "urbanProperty")
    public abstract UrbanPropertyMedia toEntity(@Nullable UrbanProperty urbanProperty, @Nullable AbstractUrbanPropertyMediaMetadata source);

}

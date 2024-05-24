package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.proto.UrbanPropertyConvenienceData;
import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.entity.UrbanPropertyConvenience;
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
                ProtoDoubleMapper.class,
                UrbanPropertyMapper.class
        }
)
public abstract class UrbanPropertyConvenienceMapper {

    public abstract List<UrbanPropertyConvenienceData> toProto(@Nullable List<UrbanPropertyConvenience> entities);

    @Mapping(
            source = "urbanProperty",
            target = "urbanProperty",
            conditionExpression = "java(!(entity.getUrbanProperty() instanceof org.hibernate.proxy.HibernateProxy) && entity.getUrbanProperty() != null)",
            defaultExpression = "java(br.com.houseseeker.domain.proto.UrbanPropertyData.getDefaultInstance())"
    )
    public abstract UrbanPropertyConvenienceData toProto(@Nullable UrbanPropertyConvenience entity);


    @Mapping(target = "id", ignore = true)
    @Mapping(source = "urbanProperty", target = "urbanProperty")
    public abstract UrbanPropertyConvenience toEntity(@Nullable UrbanProperty urbanProperty, @Nullable String description);

}

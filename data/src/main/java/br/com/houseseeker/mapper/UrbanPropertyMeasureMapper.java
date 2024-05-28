package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.property.AbstractUrbanPropertyMetadata;
import br.com.houseseeker.domain.proto.UrbanPropertyMeasureData;
import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.entity.UrbanPropertyMeasure;
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
                ProtoDoubleMapper.class,
                UrbanPropertyMapper.class
        }
)
public abstract class UrbanPropertyMeasureMapper {

    public abstract List<UrbanPropertyMeasureData> toProto(@Nullable List<UrbanPropertyMeasure> entities);

    @Mapping(
            source = "urbanProperty",
            target = "urbanProperty",
            conditionExpression = "java(!(entity.getUrbanProperty() instanceof org.hibernate.proxy.HibernateProxy) && entity.getUrbanProperty() != null)",
            defaultExpression = "java(br.com.houseseeker.domain.proto.UrbanPropertyData.getDefaultInstance())"
    )
    public abstract UrbanPropertyMeasureData toProto(@Nullable UrbanPropertyMeasure entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "urbanProperty", target = "urbanProperty")
    public abstract UrbanPropertyMeasure toEntity(@Nullable UrbanProperty urbanProperty, @Nullable AbstractUrbanPropertyMetadata source);

    @Mapping(target = "id", ignore = true)
    public abstract void toEntity(
            @Nullable UrbanProperty urbanProperty,
            @Nullable AbstractUrbanPropertyMetadata source,
            @MappingTarget UrbanPropertyMeasure target
    );

}

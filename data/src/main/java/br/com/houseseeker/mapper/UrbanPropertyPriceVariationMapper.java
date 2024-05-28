package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.proto.UrbanPropertyPriceVariationData;
import br.com.houseseeker.entity.UrbanPropertyPriceVariation;
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
public abstract class UrbanPropertyPriceVariationMapper {

    public abstract List<UrbanPropertyPriceVariationData> toProto(@Nullable List<UrbanPropertyPriceVariation> entities);

    @Mapping(
            source = "urbanProperty",
            target = "urbanProperty",
            conditionExpression = "java(!(entity.getUrbanProperty() instanceof org.hibernate.proxy.HibernateProxy) && entity.getUrbanProperty() != null)",
            defaultExpression = "java(br.com.houseseeker.domain.proto.UrbanPropertyData.getDefaultInstance())"
    )
    public abstract UrbanPropertyPriceVariationData toProto(@Nullable UrbanPropertyPriceVariation entity);

}

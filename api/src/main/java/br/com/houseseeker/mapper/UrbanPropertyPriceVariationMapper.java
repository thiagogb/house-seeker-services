package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.dto.UrbanPropertyPriceVariationDto;
import br.com.houseseeker.domain.proto.UrbanPropertyPriceVariationData;
import org.mapstruct.Mapper;
import org.springframework.lang.Nullable;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
        componentModel = SPRING,
        uses = {
                ProtoInt32Mapper.class,
                ProtoStringMapper.class,
                ProtoDoubleMapper.class
        }
)
public abstract class UrbanPropertyPriceVariationMapper {

    public abstract List<UrbanPropertyPriceVariationDto> toDto(@Nullable List<UrbanPropertyPriceVariationData> data);

    public abstract UrbanPropertyPriceVariationDto toDto(@Nullable UrbanPropertyPriceVariationData data);

}

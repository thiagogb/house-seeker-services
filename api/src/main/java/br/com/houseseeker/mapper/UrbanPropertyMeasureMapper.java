package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.dto.UrbanPropertyMeasureDto;
import br.com.houseseeker.domain.proto.UrbanPropertyMeasureData;
import org.mapstruct.Mapper;
import org.springframework.lang.Nullable;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
        componentModel = SPRING,
        uses = {
                ProtoInt32Mapper.class,
                ProtoDoubleMapper.class,
                ProtoStringMapper.class
        }
)
public abstract class UrbanPropertyMeasureMapper {

    public abstract UrbanPropertyMeasureDto toDto(@Nullable UrbanPropertyMeasureData data);

}

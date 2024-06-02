package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.dto.UrbanPropertyConvenienceDto;
import br.com.houseseeker.domain.proto.UrbanPropertyConvenienceData;
import org.mapstruct.Mapper;
import org.springframework.lang.Nullable;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
        componentModel = SPRING,
        uses = {
                ProtoInt32Mapper.class,
                ProtoStringMapper.class
        }
)
public abstract class UrbanPropertyConvenienceMapper {

    public abstract List<UrbanPropertyConvenienceDto> toDto(@Nullable List<UrbanPropertyConvenienceData> data);

    public abstract UrbanPropertyConvenienceDto toDto(@Nullable UrbanPropertyConvenienceData data);

}

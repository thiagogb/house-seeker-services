package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.dto.UrbanPropertyLocationDto;
import br.com.houseseeker.domain.proto.UrbanPropertyLocationData;
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
public abstract class UrbanPropertyLocationMapper {

    public abstract UrbanPropertyLocationDto toDto(@Nullable UrbanPropertyLocationData data);

}

package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.dto.UrbanPropertyMediaDto;
import br.com.houseseeker.domain.proto.UrbanPropertyMediaData;
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
public abstract class UrbanPropertyMediaMapper {

    public abstract List<UrbanPropertyMediaDto> toDto(@Nullable List<UrbanPropertyMediaData> data);

    public abstract UrbanPropertyMediaDto toDto(@Nullable UrbanPropertyMediaData data);

}

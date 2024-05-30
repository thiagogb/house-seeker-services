package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.dto.UrbanPropertyDto;
import br.com.houseseeker.domain.proto.UrbanPropertyData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.lang.Nullable;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
        componentModel = SPRING,
        uses = {
                ProtoBoolMapper.class,
                ProtoInt32Mapper.class,
                ProtoDoubleMapper.class,
                ProtoStringMapper.class,
                ProviderMapper.class
        }
)
public abstract class UrbanPropertyMapper {

    public abstract List<UrbanPropertyDto> toDto(@Nullable List<UrbanPropertyData> data);

    @Mapping(target = "priceVariations", ignore = true)
    @Mapping(target = "medias", ignore = true)
    @Mapping(target = "measure", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "conveniences", ignore = true)
    public abstract UrbanPropertyDto toDto(@Nullable UrbanPropertyData data);

}

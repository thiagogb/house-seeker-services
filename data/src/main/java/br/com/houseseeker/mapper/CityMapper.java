package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.projection.City;
import br.com.houseseeker.domain.proto.CityData;
import org.mapstruct.Mapper;
import org.springframework.lang.Nullable;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(
        componentModel = SPRING,
        unmappedTargetPolicy = IGNORE,
        uses = ProtoStringMapper.class
)
public abstract class CityMapper {

    public abstract List<CityData> toProto(@Nullable List<City> cities);

    public abstract CityData toProto(@Nullable City city);

}

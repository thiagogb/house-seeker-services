package br.com.houseseeker.mapper;

import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.entity.UrbanPropertyConvenience;
import org.mapstruct.Mapper;

@Mapper
public interface UrbanPropertyConvenienceMapper {

    UrbanPropertyConvenience createEntity(UrbanProperty urbanProperty, String description);

}

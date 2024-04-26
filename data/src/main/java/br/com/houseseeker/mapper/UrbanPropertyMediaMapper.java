package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.property.AbstractUrbanPropertyMediaMetadata;
import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.entity.UrbanPropertyMedia;
import org.mapstruct.Mapper;

@Mapper
public interface UrbanPropertyMediaMapper {

    UrbanPropertyMedia createEntity(UrbanProperty urbanProperty, AbstractUrbanPropertyMediaMetadata source);

}

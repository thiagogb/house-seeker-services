package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.property.AbstractUrbanPropertyMetadata;
import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.entity.UrbanPropertyLocation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface UrbanPropertyLocationMapper {

    UrbanPropertyLocation createEntity(UrbanProperty urbanProperty, AbstractUrbanPropertyMetadata source);

    void copyToEntity(UrbanProperty urbanProperty, AbstractUrbanPropertyMetadata source, @MappingTarget UrbanPropertyLocation target);

}

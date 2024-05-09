package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.property.AbstractUrbanPropertyMediaMetadata;
import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.entity.UrbanPropertyMedia;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public abstract class UrbanPropertyMediaMapper {

    public abstract UrbanPropertyMedia createEntity(@NotNull UrbanProperty urbanProperty, @NotNull AbstractUrbanPropertyMediaMetadata source);

}

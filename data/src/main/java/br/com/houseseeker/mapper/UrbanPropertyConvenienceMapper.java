package br.com.houseseeker.mapper;

import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.entity.UrbanPropertyConvenience;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public abstract class UrbanPropertyConvenienceMapper {

    public abstract UrbanPropertyConvenience createEntity(@NotNull UrbanProperty urbanProperty, @NotNull String description);

}

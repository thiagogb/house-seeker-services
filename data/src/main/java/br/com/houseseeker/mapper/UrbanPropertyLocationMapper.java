package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.property.AbstractUrbanPropertyMetadata;
import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.entity.UrbanPropertyLocation;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public abstract class UrbanPropertyLocationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "urbanProperty", target = "urbanProperty")
    public abstract UrbanPropertyLocation createEntity(@NotNull UrbanProperty urbanProperty, @NotNull AbstractUrbanPropertyMetadata source);

    @Mapping(target = "id", ignore = true)
    public abstract void copyToEntity(
            @NotNull UrbanProperty urbanProperty,
            @NotNull AbstractUrbanPropertyMetadata source,
            @MappingTarget UrbanPropertyLocation target
    );

}

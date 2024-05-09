package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.property.AbstractUrbanPropertyMetadata;
import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.entity.UrbanPropertyMeasure;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public abstract class UrbanPropertyMeasureMapper {

    public abstract UrbanPropertyMeasure createEntity(@NotNull UrbanProperty urbanProperty, @NotNull AbstractUrbanPropertyMetadata source);

    public abstract void copyToEntity(
            @NotNull UrbanProperty urbanProperty,
            @NotNull AbstractUrbanPropertyMetadata source,
            @MappingTarget UrbanPropertyMeasure target
    );

}

package br.com.houseseeker.mapper;

import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.entity.UrbanPropertyConvenience;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;

@Mapper
public abstract class UrbanPropertyConvenienceMapper {

    public abstract UrbanPropertyConvenience createEntity(@NotNull UrbanProperty urbanProperty, @NotNull String description);

}

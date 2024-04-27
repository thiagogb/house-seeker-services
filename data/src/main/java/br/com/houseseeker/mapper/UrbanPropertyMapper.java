package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.property.AbstractUrbanPropertyMetadata;
import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanProperty;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.time.LocalDateTime;

@Mapper
public abstract class UrbanPropertyMapper {

    private Clock clock;

    @Autowired
    public void setClock(Clock clock) {
        this.clock = clock;
    }

    @Mappings({
            @Mapping(source = "provider", target = "provider"),
            @Mapping(target = "analyzable", ignore = true),
            @Mapping(target = "condominiumPriceVariation", ignore = true),
            @Mapping(target = "creationDate", ignore = true),
            @Mapping(target = "exclusionDate", ignore = true),
            @Mapping(target = "lastAnalysisDate", ignore = true),
            @Mapping(target = "rentPriceVariation", ignore = true),
            @Mapping(target = "sellPriceVariation", ignore = true)
    })
    public abstract UrbanProperty createEntity(@NotNull Provider provider, @NotNull AbstractUrbanPropertyMetadata source);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "provider", ignore = true),
            @Mapping(target = "sellPriceVariation", ignore = true),
            @Mapping(target = "rentPriceVariation", ignore = true),
            @Mapping(target = "condominiumPriceVariation", ignore = true),
            @Mapping(target = "creationDate", ignore = true),
            @Mapping(target = "lastAnalysisDate", ignore = true),
            @Mapping(target = "exclusionDate", ignore = true),
            @Mapping(target = "analyzable", ignore = true),
    })
    public abstract void copyToEntity(@NotNull AbstractUrbanPropertyMetadata source, @MappingTarget UrbanProperty target);

    @AfterMapping
    protected void afterCreatingEntity(@MappingTarget UrbanProperty.UrbanPropertyBuilder builder) {
        builder.creationDate(LocalDateTime.now(clock))
               .lastAnalysisDate(LocalDateTime.now(clock))
               .analyzable(true);
    }

    @AfterMapping
    protected void afterCopyEntity(@MappingTarget UrbanProperty target) {
        target.setLastAnalysisDate(LocalDateTime.now(clock))
              .setExclusionDate(null);
    }

}

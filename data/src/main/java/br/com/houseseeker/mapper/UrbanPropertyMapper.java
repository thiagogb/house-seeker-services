package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.property.AbstractUrbanPropertyMetadata;
import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanProperty;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.time.LocalDateTime;

import static java.util.Objects.isNull;

@Mapper
public interface UrbanPropertyMapper {

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
    UrbanProperty createEntity(Provider provider, AbstractUrbanPropertyMetadata source);

    @Mappings({
            @Mapping(target = "provider", ignore = true),
            @Mapping(target = "sellPriceVariation", ignore = true),
            @Mapping(target = "rentPriceVariation", ignore = true),
            @Mapping(target = "condominiumPriceVariation", ignore = true),
            @Mapping(target = "creationDate", ignore = true),
            @Mapping(target = "lastAnalysisDate", ignore = true),
            @Mapping(target = "exclusionDate", ignore = true),
            @Mapping(target = "analyzable", ignore = true)
    })
    void copyToEntity(AbstractUrbanPropertyMetadata source, @MappingTarget UrbanProperty target);


    @AfterMapping
    default void afterMappingEntity(@MappingTarget UrbanProperty urbanProperty) {
        if (isNull(urbanProperty.getCreationDate()))
            urbanProperty.setCreationDate(LocalDateTime.now());

        if (isNull(urbanProperty.getAnalyzable()))
            urbanProperty.setAnalyzable(true);

        urbanProperty.setLastAnalysisDate(LocalDateTime.now())
                     .setExclusionDate(null);
    }

}

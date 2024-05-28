package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.property.AbstractUrbanPropertyMetadata;
import br.com.houseseeker.domain.proto.UrbanPropertyData;
import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanProperty;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(
        componentModel = SPRING,
        unmappedTargetPolicy = IGNORE,
        uses = {
                ProtoInt32Mapper.class,
                ProtoStringMapper.class,
                ProtoDoubleMapper.class,
                ProtoBoolMapper.class,
                ProviderMapper.class
        }
)
public abstract class UrbanPropertyMapper {

    private Clock clock;

    @Autowired
    public void setClock(Clock clock) {
        this.clock = clock;
    }

    public abstract List<UrbanPropertyData> toProto(@Nullable List<UrbanProperty> entities);

    @Mapping(
            source = "provider",
            target = "provider",
            conditionExpression = "java(!(entity.getProvider() instanceof org.hibernate.proxy.HibernateProxy) && entity.getProvider() != null)",
            defaultExpression = "java(br.com.houseseeker.domain.proto.ProviderData.getDefaultInstance())"
    )
    public abstract UrbanPropertyData toProto(@Nullable UrbanProperty entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "provider", target = "provider")
    @Mapping(target = "analyzable", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "exclusionDate", ignore = true)
    @Mapping(target = "lastAnalysisDate", ignore = true)
    public abstract UrbanProperty toEntity(@Nullable Provider provider, @Nullable AbstractUrbanPropertyMetadata source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "lastAnalysisDate", ignore = true)
    @Mapping(target = "exclusionDate", ignore = true)
    @Mapping(target = "analyzable", ignore = true)
    public abstract void toEntity(@Nullable AbstractUrbanPropertyMetadata source, @MappingTarget UrbanProperty target);

    @AfterMapping
    protected void afterCreatingEntity(@MappingTarget UrbanProperty.UrbanPropertyBuilder builder) {
        builder.creationDate(LocalDateTime.now(clock))
               .lastAnalysisDate(LocalDateTime.now(clock))
               .analyzable(true);
    }

    @AfterMapping
    protected void afterCopyEntity(@MappingTarget UrbanProperty target) {
        target.setLastAnalysisDate(LocalDateTime.now(clock));
        target.setExclusionDate(null);
    }

}

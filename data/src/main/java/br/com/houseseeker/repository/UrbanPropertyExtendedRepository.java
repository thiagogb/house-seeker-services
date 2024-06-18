package br.com.houseseeker.repository;

import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.service.proto.GetSubTypesRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertiesRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;

public interface UrbanPropertyExtendedRepository {

    Page<UrbanProperty> findBy(@NotNull GetUrbanPropertiesRequest request);

    Page<String> findDistinctSubTypesBy(@NotNull GetSubTypesRequest request);

}

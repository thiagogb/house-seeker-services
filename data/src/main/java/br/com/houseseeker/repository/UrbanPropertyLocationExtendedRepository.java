package br.com.houseseeker.repository;

import br.com.houseseeker.domain.projection.City;
import br.com.houseseeker.entity.UrbanPropertyLocation;
import br.com.houseseeker.service.proto.GetCitiesRequest;
import br.com.houseseeker.service.proto.GetStatesRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertyLocationsRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;

public interface UrbanPropertyLocationExtendedRepository {

    Page<UrbanPropertyLocation> findBy(@NotNull GetUrbanPropertyLocationsRequest request);

    Page<String> findDistinctStatesBy(@NotNull GetStatesRequest request);

    Page<City> findDistinctCitiesBy(@NotNull GetCitiesRequest request);

}

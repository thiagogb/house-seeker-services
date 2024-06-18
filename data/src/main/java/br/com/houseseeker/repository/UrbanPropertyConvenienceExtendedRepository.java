package br.com.houseseeker.repository;

import br.com.houseseeker.entity.UrbanPropertyConvenience;
import br.com.houseseeker.service.proto.GetConveniencesRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertyConveniencesRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;

public interface UrbanPropertyConvenienceExtendedRepository {

    Page<UrbanPropertyConvenience> findBy(@NotNull GetUrbanPropertyConveniencesRequest request);

    Page<String> findDistinctConveniencesBy(@NotNull GetConveniencesRequest request);

}

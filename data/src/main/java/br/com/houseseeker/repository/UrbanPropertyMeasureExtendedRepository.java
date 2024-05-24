package br.com.houseseeker.repository;

import br.com.houseseeker.entity.UrbanPropertyMeasure;
import br.com.houseseeker.service.proto.GetUrbanPropertyMeasuresRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;

public interface UrbanPropertyMeasureExtendedRepository {

    Page<UrbanPropertyMeasure> findBy(@NotNull GetUrbanPropertyMeasuresRequest request);

}

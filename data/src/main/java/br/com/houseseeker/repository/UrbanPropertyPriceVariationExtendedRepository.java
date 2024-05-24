package br.com.houseseeker.repository;

import br.com.houseseeker.entity.UrbanPropertyPriceVariation;
import br.com.houseseeker.service.proto.GetUrbanPropertyPriceVariationsRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;

public interface UrbanPropertyPriceVariationExtendedRepository {

    Page<UrbanPropertyPriceVariation> findBy(@NotNull GetUrbanPropertyPriceVariationsRequest request);

}

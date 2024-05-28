package br.com.houseseeker.repository;

import br.com.houseseeker.entity.UrbanPropertyMedia;
import br.com.houseseeker.service.proto.GetUrbanPropertyMediasRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;

public interface UrbanPropertyMediaExtendedRepository {

    Page<UrbanPropertyMedia> findBy(@NotNull GetUrbanPropertyMediasRequest request);

}

package br.com.houseseeker.service;

import br.com.houseseeker.domain.argument.UrbanPropertyInput;
import br.com.houseseeker.service.builder.UrbanPropertiesDataRequestBuilder;
import br.com.houseseeker.service.proto.GetUrbanPropertiesRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertiesResponse;
import br.com.houseseeker.service.proto.UrbanPropertyDataServiceGrpc.UrbanPropertyDataServiceBlockingStub;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrbanPropertyService {

    @GrpcClient("GLOBAL")
    private UrbanPropertyDataServiceBlockingStub urbanPropertyDataServiceBlockingStub;

    public GetUrbanPropertiesResponse findBy(@NotNull Set<String> projections, @Nullable UrbanPropertyInput input) {
        GetUrbanPropertiesRequest request = UrbanPropertiesDataRequestBuilder.newInstance()
                                                                             .withProjections(projections)
                                                                             .withInput(input)
                                                                             .build();

        return urbanPropertyDataServiceBlockingStub.getUrbanProperties(request);
    }

}

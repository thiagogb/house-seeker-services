package br.com.houseseeker.service;

import br.com.houseseeker.service.builder.UrbanPropertyLocationsDataRequestBuilder;
import br.com.houseseeker.service.proto.GetUrbanPropertyLocationsRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertyLocationsResponse;
import br.com.houseseeker.service.proto.UrbanPropertyLocationDataServiceGrpc.UrbanPropertyLocationDataServiceBlockingStub;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrbanPropertyLocationService {

    @GrpcClient("GLOBAL")
    private UrbanPropertyLocationDataServiceBlockingStub urbanPropertyLocationDataServiceBlockingStub;

    public GetUrbanPropertyLocationsResponse findByUrbanProperties(@NotNull List<Integer> ids) {
        GetUrbanPropertyLocationsRequest request = UrbanPropertyLocationsDataRequestBuilder.newInstance()
                                                                                           .byUrbanProperties(ids)
                                                                                           .build();
        return urbanPropertyLocationDataServiceBlockingStub.getUrbanPropertyLocations(request);
    }

}

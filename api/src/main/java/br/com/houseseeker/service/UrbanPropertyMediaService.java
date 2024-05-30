package br.com.houseseeker.service;

import br.com.houseseeker.service.builder.UrbanPropertyMediasDataRequestBuilder;
import br.com.houseseeker.service.proto.GetUrbanPropertyMediasRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertyMediasResponse;
import br.com.houseseeker.service.proto.UrbanPropertyMediaDataServiceGrpc.UrbanPropertyMediaDataServiceBlockingStub;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrbanPropertyMediaService {

    @GrpcClient("GLOBAL")
    private UrbanPropertyMediaDataServiceBlockingStub urbanPropertyMediaDataServiceBlockingStub;

    public GetUrbanPropertyMediasResponse findByUrbanProperties(@NotNull List<Integer> ids) {
        GetUrbanPropertyMediasRequest request = UrbanPropertyMediasDataRequestBuilder.newInstance()
                                                                                     .byUrbanProperties(ids)
                                                                                     .build();

        return urbanPropertyMediaDataServiceBlockingStub.getUrbanPropertyMedias(request);
    }

}

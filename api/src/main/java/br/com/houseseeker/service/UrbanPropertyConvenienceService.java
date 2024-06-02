package br.com.houseseeker.service;

import br.com.houseseeker.service.builder.UrbanPropertyConveniencesDataRequestBuilder;
import br.com.houseseeker.service.proto.GetUrbanPropertyConveniencesRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertyConveniencesResponse;
import br.com.houseseeker.service.proto.UrbanPropertyConvenienceDataServiceGrpc.UrbanPropertyConvenienceDataServiceBlockingStub;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrbanPropertyConvenienceService {

    @GrpcClient("GLOBAL")
    private UrbanPropertyConvenienceDataServiceBlockingStub urbanPropertyConvenienceDataServiceBlockingStub;

    public GetUrbanPropertyConveniencesResponse findByUrbanProperties(@NotNull List<Integer> ids) {
        GetUrbanPropertyConveniencesRequest request = UrbanPropertyConveniencesDataRequestBuilder.newInstance()
                                                                                                 .byUrbanProperties(ids)
                                                                                                 .build();

        return urbanPropertyConvenienceDataServiceBlockingStub.getUrbanPropertyConveniences(request);
    }

}

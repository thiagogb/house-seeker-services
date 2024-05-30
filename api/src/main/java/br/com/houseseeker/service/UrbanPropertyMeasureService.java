package br.com.houseseeker.service;

import br.com.houseseeker.service.builder.UrbanPropertyMeasuresDataRequestBuilder;
import br.com.houseseeker.service.proto.GetUrbanPropertyMeasuresRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertyMeasuresResponse;
import br.com.houseseeker.service.proto.UrbanPropertyMeasureDataServiceGrpc.UrbanPropertyMeasureDataServiceBlockingStub;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrbanPropertyMeasureService {

    @GrpcClient("GLOBAL")
    private UrbanPropertyMeasureDataServiceBlockingStub urbanPropertyMeasureDataServiceBlockingStub;

    public GetUrbanPropertyMeasuresResponse findByUrbanProperties(@NotNull List<Integer> ids) {
        GetUrbanPropertyMeasuresRequest request = UrbanPropertyMeasuresDataRequestBuilder.newInstance()
                                                                                         .byUrbanProperties(ids)
                                                                                         .build();
        return urbanPropertyMeasureDataServiceBlockingStub.getUrbanPropertyMeasures(request);
    }

}

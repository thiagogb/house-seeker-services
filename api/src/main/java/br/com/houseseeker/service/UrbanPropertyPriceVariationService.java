package br.com.houseseeker.service;

import br.com.houseseeker.service.builder.UrbanPropertyPriceVariationsDataRequestBuilder;
import br.com.houseseeker.service.proto.GetUrbanPropertyPriceVariationsRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertyPriceVariationsResponse;
import br.com.houseseeker.service.proto.UrbanPropertyPriceVariationDataServiceGrpc.UrbanPropertyPriceVariationDataServiceBlockingStub;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrbanPropertyPriceVariationService {

    @GrpcClient("GLOBAL")
    private UrbanPropertyPriceVariationDataServiceBlockingStub urbanPropertyPriceVariationDataServiceBlockingStub;

    public GetUrbanPropertyPriceVariationsResponse findByUrbanProperties(@NotNull List<Integer> ids) {
        GetUrbanPropertyPriceVariationsRequest request = UrbanPropertyPriceVariationsDataRequestBuilder.newInstance()
                                                                                                       .byUrbanProperties(ids)
                                                                                                       .build();

        return urbanPropertyPriceVariationDataServiceBlockingStub.getUrbanPropertyPriceVariations(request);
    }

}

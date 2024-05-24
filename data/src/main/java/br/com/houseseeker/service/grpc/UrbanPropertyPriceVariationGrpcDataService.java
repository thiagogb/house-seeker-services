package br.com.houseseeker.service.grpc;

import br.com.houseseeker.entity.UrbanPropertyPriceVariation;
import br.com.houseseeker.mapper.UrbanPropertyPriceVariationMapper;
import br.com.houseseeker.service.UrbanPropertyPriceVariationService;
import br.com.houseseeker.service.proto.GetUrbanPropertyPriceVariationsRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertyPriceVariationsResponse;
import br.com.houseseeker.service.proto.UrbanPropertyPriceVariationDataServiceGrpc;
import br.com.houseseeker.util.PaginationUtils;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.Page;

import static br.com.houseseeker.util.GrpcServiceUtils.execute;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class UrbanPropertyPriceVariationGrpcDataService extends UrbanPropertyPriceVariationDataServiceGrpc.UrbanPropertyPriceVariationDataServiceImplBase {

    private final UrbanPropertyPriceVariationService urbanPropertyPriceVariationService;
    private final UrbanPropertyPriceVariationMapper urbanPropertyPriceVariationMapper;

    @Override
    public void getUrbanPropertyPriceVariations(
            GetUrbanPropertyPriceVariationsRequest request,
            StreamObserver<GetUrbanPropertyPriceVariationsResponse> responseObserver
    ) {
        log.info("GRPC: executing getUrbanPropertyPriceVariations ...");
        execute(responseObserver, () -> {
            Page<UrbanPropertyPriceVariation> result = urbanPropertyPriceVariationService.findBy(request);
            return GetUrbanPropertyPriceVariationsResponse.newBuilder()
                                                          .addAllUrbanPropertyPriceVariations(urbanPropertyPriceVariationMapper.toProto(result.getContent()))
                                                          .setPagination(PaginationUtils.toPaginationResponseData(result))
                                                          .build();
        });
    }

}

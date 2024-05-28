package br.com.houseseeker.service.grpc;

import br.com.houseseeker.entity.UrbanPropertyMeasure;
import br.com.houseseeker.mapper.UrbanPropertyMeasureMapper;
import br.com.houseseeker.service.UrbanPropertyMeasureService;
import br.com.houseseeker.service.proto.GetUrbanPropertyMeasuresRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertyMeasuresResponse;
import br.com.houseseeker.service.proto.UrbanPropertyMeasureDataServiceGrpc;
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
public class UrbanPropertyMeasureGrpcDataService extends UrbanPropertyMeasureDataServiceGrpc.UrbanPropertyMeasureDataServiceImplBase {

    private final UrbanPropertyMeasureService urbanPropertyMeasureService;
    private final UrbanPropertyMeasureMapper urbanPropertyMeasureMapper;

    @Override
    public void getUrbanPropertyMeasures(
            GetUrbanPropertyMeasuresRequest request,
            StreamObserver<GetUrbanPropertyMeasuresResponse> responseObserver
    ) {
        log.info("GRPC: executing getUrbanPropertyMeasures ...");
        execute(responseObserver, () -> {
            Page<UrbanPropertyMeasure> result = urbanPropertyMeasureService.findBy(request);
            return GetUrbanPropertyMeasuresResponse.newBuilder()
                                                   .addAllUrbanPropertyMeasures(urbanPropertyMeasureMapper.toProto(result.getContent()))
                                                   .setPagination(PaginationUtils.toPaginationResponseData(result))
                                                   .build();
        });
    }

}

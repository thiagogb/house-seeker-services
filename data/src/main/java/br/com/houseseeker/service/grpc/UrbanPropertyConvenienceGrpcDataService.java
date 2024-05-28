package br.com.houseseeker.service.grpc;

import br.com.houseseeker.entity.UrbanPropertyConvenience;
import br.com.houseseeker.mapper.UrbanPropertyConvenienceMapper;
import br.com.houseseeker.service.UrbanPropertyConvenienceService;
import br.com.houseseeker.service.proto.GetUrbanPropertyConveniencesRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertyConveniencesResponse;
import br.com.houseseeker.service.proto.UrbanPropertyConvenienceDataServiceGrpc;
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
public class UrbanPropertyConvenienceGrpcDataService extends UrbanPropertyConvenienceDataServiceGrpc.UrbanPropertyConvenienceDataServiceImplBase {

    private final UrbanPropertyConvenienceService urbanPropertyConvenienceService;
    private final UrbanPropertyConvenienceMapper urbanPropertyConvenienceMapper;

    @Override
    public void getUrbanPropertyConveniences(
            GetUrbanPropertyConveniencesRequest request,
            StreamObserver<GetUrbanPropertyConveniencesResponse> responseObserver
    ) {
        log.info("GRPC: executing getUrbanPropertyConveniences ...");
        execute(responseObserver, () -> {
            Page<UrbanPropertyConvenience> result = urbanPropertyConvenienceService.findBy(request);
            return GetUrbanPropertyConveniencesResponse.newBuilder()
                                                       .addAllUrbanPropertyConveniences(urbanPropertyConvenienceMapper.toProto(result.getContent()))
                                                       .setPagination(PaginationUtils.toPaginationResponseData(result))
                                                       .build();
        });
    }

}

package br.com.houseseeker.service.grpc;

import br.com.houseseeker.entity.UrbanPropertyMedia;
import br.com.houseseeker.mapper.UrbanPropertyMediaMapper;
import br.com.houseseeker.service.UrbanPropertyMediaService;
import br.com.houseseeker.service.proto.GetUrbanPropertyMediasRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertyMediasResponse;
import br.com.houseseeker.service.proto.UrbanPropertyMediaDataServiceGrpc;
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
public class UrbanPropertyMediaGrpcDataService extends UrbanPropertyMediaDataServiceGrpc.UrbanPropertyMediaDataServiceImplBase {

    private final UrbanPropertyMediaService urbanPropertyMediaService;
    private final UrbanPropertyMediaMapper urbanPropertyMediaMapper;

    @Override
    public void getUrbanPropertyMedias(
            GetUrbanPropertyMediasRequest request,
            StreamObserver<GetUrbanPropertyMediasResponse> responseObserver
    ) {
        log.info("GRPC: executing getUrbanPropertyMedias ...");
        execute(responseObserver, () -> {
            Page<UrbanPropertyMedia> result = urbanPropertyMediaService.findBy(request);
            return GetUrbanPropertyMediasResponse.newBuilder()
                                                 .addAllUrbanPropertyMedias(urbanPropertyMediaMapper.toProto(result.getContent()))
                                                 .setPagination(PaginationUtils.toPaginationResponseData(result))
                                                 .build();
        });
    }

}

package br.com.houseseeker.service.grpc;

import br.com.houseseeker.entity.UrbanPropertyLocation;
import br.com.houseseeker.mapper.UrbanPropertyLocationMapper;
import br.com.houseseeker.service.UrbanPropertyLocationService;
import br.com.houseseeker.service.proto.GetUrbanPropertyLocationsRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertyLocationsResponse;
import br.com.houseseeker.service.proto.UrbanPropertyLocationDataServiceGrpc;
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
public class UrbanPropertyLocationGrpcDataService extends UrbanPropertyLocationDataServiceGrpc.UrbanPropertyLocationDataServiceImplBase {

    private final UrbanPropertyLocationService urbanPropertyLocationService;
    private final UrbanPropertyLocationMapper urbanPropertyLocationMapper;

    @Override
    public void getUrbanPropertyLocations(
            GetUrbanPropertyLocationsRequest request,
            StreamObserver<GetUrbanPropertyLocationsResponse> responseObserver
    ) {
        log.info("GRPC: executing getUrbanPropertyLocations ...");
        execute(responseObserver, () -> {
            Page<UrbanPropertyLocation> result = urbanPropertyLocationService.findBy(request);
            return GetUrbanPropertyLocationsResponse.newBuilder()
                                                    .addAllUrbanPropertyLocations(urbanPropertyLocationMapper.toProto(result.getContent()))
                                                    .setPagination(PaginationUtils.toPaginationResponseData(result))
                                                    .build();
        });
    }

}

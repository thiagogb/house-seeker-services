package br.com.houseseeker.service.grpc;

import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.mapper.UrbanPropertyMapper;
import br.com.houseseeker.service.UrbanPropertyService;
import br.com.houseseeker.service.proto.GetUrbanPropertiesRequest;
import br.com.houseseeker.service.proto.GetUrbanPropertiesResponse;
import br.com.houseseeker.service.proto.UrbanPropertyDataServiceGrpc;
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
public class UrbanPropertyGrpcDataService extends UrbanPropertyDataServiceGrpc.UrbanPropertyDataServiceImplBase {

    private final UrbanPropertyService urbanPropertyService;
    private final UrbanPropertyMapper urbanPropertyMapper;

    @Override
    public void getUrbanProperties(GetUrbanPropertiesRequest request, StreamObserver<GetUrbanPropertiesResponse> responseObserver) {
        log.info("GRPC: executing getUrbanProperties ...");
        execute(responseObserver, () -> {
            Page<UrbanProperty> result = urbanPropertyService.findBy(request);
            return GetUrbanPropertiesResponse.newBuilder()
                                             .addAllUrbanProperties(urbanPropertyMapper.toProto(result.getContent()))
                                             .setPagination(PaginationUtils.toPaginationResponseData(result))
                                             .build();
        });
    }

}

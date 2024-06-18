package br.com.houseseeker.service.grpc;

import br.com.houseseeker.mapper.ProtoStringMapper;
import br.com.houseseeker.service.UrbanPropertyService;
import br.com.houseseeker.service.proto.GetSubTypesRequest;
import br.com.houseseeker.service.proto.GetSubTypesResponse;
import br.com.houseseeker.service.proto.SubTypeDataServiceGrpc;
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
public class SubTypeGrpcDataService extends SubTypeDataServiceGrpc.SubTypeDataServiceImplBase {

    private final UrbanPropertyService urbanPropertyService;
    private final ProtoStringMapper protoStringMapper;

    @Override
    public void getSubTypes(GetSubTypesRequest request, StreamObserver<GetSubTypesResponse> responseObserver) {
        log.info("GRPC: executing getSubTypes ...");
        execute(responseObserver, () -> {
            Page<String> result = urbanPropertyService.findDistinctSubTypesBy(request);
            return GetSubTypesResponse.newBuilder()
                                      .addAllSubTypes(protoStringMapper.toStringValue(result.getContent()))
                                      .setPagination(PaginationUtils.toPaginationResponseData(result))
                                      .build();
        });
    }

}

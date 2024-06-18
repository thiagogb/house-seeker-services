package br.com.houseseeker.service.grpc;

import br.com.houseseeker.mapper.ProtoStringMapper;
import br.com.houseseeker.service.UrbanPropertyLocationService;
import br.com.houseseeker.service.proto.GetStatesRequest;
import br.com.houseseeker.service.proto.GetStatesResponse;
import br.com.houseseeker.service.proto.StateDataServiceGrpc;
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
public class StateGrpcDataService extends StateDataServiceGrpc.StateDataServiceImplBase {

    private final UrbanPropertyLocationService urbanPropertyLocationService;
    private final ProtoStringMapper protoStringMapper;

    @Override
    public void getStates(GetStatesRequest request, StreamObserver<GetStatesResponse> responseObserver) {
        log.info("GRPC: executing getStates ...");
        execute(responseObserver, () -> {
            Page<String> result = urbanPropertyLocationService.findDistinctStatesBy(request);
            return GetStatesResponse.newBuilder()
                                    .addAllStates(protoStringMapper.toStringValue(result.getContent()))
                                    .setPagination(PaginationUtils.toPaginationResponseData(result))
                                    .build();
        });
    }

}

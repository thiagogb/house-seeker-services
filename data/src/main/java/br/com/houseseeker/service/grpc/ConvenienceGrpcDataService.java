package br.com.houseseeker.service.grpc;

import br.com.houseseeker.mapper.ProtoStringMapper;
import br.com.houseseeker.service.UrbanPropertyConvenienceService;
import br.com.houseseeker.service.proto.ConvenienceDataServiceGrpc;
import br.com.houseseeker.service.proto.GetConveniencesRequest;
import br.com.houseseeker.service.proto.GetConveniencesResponse;
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
public class ConvenienceGrpcDataService extends ConvenienceDataServiceGrpc.ConvenienceDataServiceImplBase {

    private final UrbanPropertyConvenienceService urbanPropertyConvenienceService;
    private final ProtoStringMapper protoStringMapper;

    @Override
    public void getConveniences(GetConveniencesRequest request, StreamObserver<GetConveniencesResponse> responseObserver) {
        log.info("GRPC: executing getConveniences ...");
        execute(responseObserver, () -> {
            Page<String> result = urbanPropertyConvenienceService.findDistinctConveniencesBy(request);
            return GetConveniencesResponse.newBuilder()
                                          .addAllConveniences(protoStringMapper.toStringValue(result.getContent()))
                                          .setPagination(PaginationUtils.toPaginationResponseData(result))
                                          .build();
        });
    }

}

package br.com.houseseeker.service.grpc;

import br.com.houseseeker.domain.proto.ProviderData;
import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.mapper.ProviderMapper;
import br.com.houseseeker.service.ProviderService;
import br.com.houseseeker.service.proto.GetProvidersDataRequest;
import br.com.houseseeker.service.proto.GetProvidersDataResponse;
import br.com.houseseeker.service.proto.ProviderDataServiceGrpc;
import br.com.houseseeker.util.GrpcUtils;
import br.com.houseseeker.util.PaginationUtils;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.Page;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class ProviderGrpcDataService extends ProviderDataServiceGrpc.ProviderDataServiceImplBase {

    private final ProviderService providerService;
    private final ProviderMapper providerMapper;

    @Override
    public void getProviders(GetProvidersDataRequest request, StreamObserver<GetProvidersDataResponse> responseObserver) {
        log.info("GRPC: executing getProviders ...");
        try {
            Page<Provider> result = providerService.findBy(request);
            responseObserver.onNext(
                    GetProvidersDataResponse.newBuilder()
                                            .addAllProviders(providerMapper.toProto(result.getContent()))
                                            .setPagination(PaginationUtils.toPaginationResponseData(result))
                                            .build()
            );
            responseObserver.onCompleted();
        } catch (Throwable e) {
            log.error("GRPC: failed executing getProviders", e);
            responseObserver.onError(GrpcUtils.fromThrowable(e));
        }
    }

    @Override
    public void insertProvider(ProviderData request, StreamObserver<ProviderData> responseObserver) {
        log.info("GRPC: executing insertProvider ...");
        try {
            responseObserver.onNext(providerMapper.toProto(providerService.insert(request)));
            responseObserver.onCompleted();
        } catch (Throwable e) {
            log.error("GRPC: failed executing insertProvider", e);
            responseObserver.onError(GrpcUtils.fromThrowable(e));
        }
    }

    @Override
    public void updateProvider(ProviderData request, StreamObserver<ProviderData> responseObserver) {
        log.info("GRPC: executing updateProvider ...");
        try {
            responseObserver.onNext(providerMapper.toProto(providerService.update(request)));
            responseObserver.onCompleted();
        } catch (Throwable e) {
            log.error("GRPC: failed executing updateProvider", e);
            responseObserver.onError(GrpcUtils.fromThrowable(e));
        }
    }

}

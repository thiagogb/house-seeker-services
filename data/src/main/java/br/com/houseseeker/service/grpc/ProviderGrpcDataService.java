package br.com.houseseeker.service.grpc;

import br.com.houseseeker.domain.proto.ProviderData;
import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.mapper.ProviderMapper;
import br.com.houseseeker.service.ProviderService;
import br.com.houseseeker.service.proto.GetProvidersDataRequest;
import br.com.houseseeker.service.proto.GetProvidersDataResponse;
import br.com.houseseeker.service.proto.ProviderDataServiceGrpc;
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
public class ProviderGrpcDataService extends ProviderDataServiceGrpc.ProviderDataServiceImplBase {

    private final ProviderService providerService;
    private final ProviderMapper providerMapper;

    @Override
    public void getProviders(GetProvidersDataRequest request, StreamObserver<GetProvidersDataResponse> responseObserver) {
        log.info("GRPC: executing getProviders ...");
        execute(responseObserver, () -> {
            Page<Provider> result = providerService.findBy(request);
            return GetProvidersDataResponse.newBuilder()
                                           .addAllProviders(providerMapper.toProto(result.getContent()))
                                           .setPagination(PaginationUtils.toPaginationResponseData(result))
                                           .build();
        });
    }

    @Override
    public void insertProvider(ProviderData request, StreamObserver<ProviderData> responseObserver) {
        log.info("GRPC: executing insertProvider ...");
        execute(responseObserver, () -> providerMapper.toProto(providerService.insert(request)));
    }

    @Override
    public void updateProvider(ProviderData request, StreamObserver<ProviderData> responseObserver) {
        log.info("GRPC: executing updateProvider ...");
        execute(responseObserver, () -> providerMapper.toProto(providerService.update(request)));
    }

}

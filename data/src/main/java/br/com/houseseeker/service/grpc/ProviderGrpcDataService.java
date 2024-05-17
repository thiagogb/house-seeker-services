package br.com.houseseeker.service.grpc;

import br.com.houseseeker.domain.proto.ProviderData;
import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.mapper.ProviderMapper;
import br.com.houseseeker.service.ProviderService;
import br.com.houseseeker.service.proto.GetProviderMechanismsResponse;
import br.com.houseseeker.service.proto.GetProvidersDataRequest;
import br.com.houseseeker.service.proto.GetProvidersDataResponse;
import br.com.houseseeker.service.proto.ProviderDataServiceGrpc;
import br.com.houseseeker.util.PaginationUtils;
import com.google.protobuf.Empty;
import com.google.protobuf.Int32Value;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.Page;

import java.util.Arrays;

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
    public void getProviderMechanisms(Empty request, StreamObserver<GetProviderMechanismsResponse> responseObserver) {
        log.info("GRPC: executing getProviderMechanisms ...");
        execute(responseObserver, () ->
                GetProviderMechanismsResponse.newBuilder()
                                             .addAllProviderMechanisms(
                                                     Arrays.stream(ProviderMechanism.values())
                                                           .map(Enum::name)
                                                           .toList()
                                             )
                                             .build()
        );
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

    @Override
    public void wipeProvider(Int32Value request, StreamObserver<Empty> responseObserver) {
        log.info("GRPC: executing writeProvider ...");
        execute(responseObserver, () -> {
            providerService.wipe(request.getValue());
            return Empty.getDefaultInstance();
        });
    }

}

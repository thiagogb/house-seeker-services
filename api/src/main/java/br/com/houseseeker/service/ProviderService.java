package br.com.houseseeker.service;

import br.com.houseseeker.domain.argument.ProviderInput;
import br.com.houseseeker.domain.input.ProviderCreationInput;
import br.com.houseseeker.domain.input.ProviderEditionInput;
import br.com.houseseeker.domain.proto.ProviderData;
import br.com.houseseeker.mapper.ProviderMapper;
import br.com.houseseeker.service.builder.ProvidersDataRequestBuilder;
import br.com.houseseeker.service.proto.GetProvidersDataRequest;
import br.com.houseseeker.service.proto.GetProvidersDataResponse;
import br.com.houseseeker.service.proto.ProviderDataServiceGrpc.ProviderDataServiceBlockingStub;
import com.google.protobuf.Int32Value;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProviderService {

    public static final String PROVIDER_LOGO_CACHE = "providerLogoCache";

    private final ProviderMapper providerMapper;

    @GrpcClient("GLOBAL")
    private ProviderDataServiceBlockingStub providerDataServiceBlockingStub;

    public GetProvidersDataResponse findBy(@NotNull Set<String> projections, @Nullable ProviderInput input) {
        GetProvidersDataRequest request = ProvidersDataRequestBuilder.newInstance()
                                                                     .withProjections(projections)
                                                                     .withInput(input)
                                                                     .build();

        return providerDataServiceBlockingStub.getProviders(request);
    }

    @Cacheable(value = PROVIDER_LOGO_CACHE, key = "#id")
    public byte[] getLogo(int id) {
        return findByIdOrThrowNotFound(id).getLogo().getValue().toByteArray();
    }

    public ProviderData insert(@NotNull ProviderCreationInput input) {
        ProviderData request = providerMapper.toProto(input);
        return providerDataServiceBlockingStub.insertProvider(request);
    }

    @CacheEvict(value = PROVIDER_LOGO_CACHE, key = "#id")
    public ProviderData update(int id, @NotNull ProviderEditionInput input) {
        ProviderData.Builder builder = findByIdOrThrowNotFound(id).toBuilder();
        providerMapper.copyToProto(input, builder);
        return providerDataServiceBlockingStub.updateProvider(builder.build());
    }

    public void wipe(int id) {
        providerDataServiceBlockingStub.wipeProvider(Int32Value.of(id));
    }

    private ProviderData findByIdOrThrowNotFound(int id) {
        GetProvidersDataRequest request = ProvidersDataRequestBuilder.newInstance()
                                                                     .byId(id)
                                                                     .build();

        return providerDataServiceBlockingStub.getProviders(request)
                                              .getProvidersList()
                                              .stream()
                                              .findFirst()
                                              .orElseThrow(() -> new ResponseStatusException(
                                                      HttpStatusCode.valueOf(404),
                                                      String.format("Provider with id %s not found", id)
                                              ));
    }

}

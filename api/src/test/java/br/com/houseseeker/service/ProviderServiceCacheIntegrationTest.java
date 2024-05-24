package br.com.houseseeker.service;

import br.com.houseseeker.domain.input.ProviderEditionInput;
import br.com.houseseeker.domain.proto.ProviderData;
import br.com.houseseeker.service.proto.GetProvidersDataResponse;
import br.com.houseseeker.service.proto.ProviderDataServiceGrpc;
import com.google.protobuf.ByteString;
import com.google.protobuf.BytesValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import graphql.schema.DataFetchingEnvironment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static br.com.houseseeker.service.ProviderService.PROVIDER_LOGO_CACHE;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ProviderServiceCacheIntegrationTest {

    private static final String DEFAULT_LOGO_CONTENT = "content";

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ProviderService providerService;

    @Mock
    private ProviderDataServiceGrpc.ProviderDataServiceBlockingStub mockedProviderDataServiceBlockingStub;

    @Mock
    private DataFetchingEnvironment mockedDataFetchingEnvironment;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(providerService, "providerDataServiceBlockingStub", mockedProviderDataServiceBlockingStub);

        Optional.ofNullable(cacheManager.getCache(PROVIDER_LOGO_CACHE)).ifPresent(Cache::clear);
    }

    @Test
    @DisplayName("given a cached logo when calls getLogo then expects to not call grpc service")
    void givenACachedLogo_whenCallsGetLogo_thenExpectsToNotCallGrpcService() {
        requireNonNull(cacheManager.getCache(PROVIDER_LOGO_CACHE)).put(1, DEFAULT_LOGO_CONTENT.getBytes());

        assertThat(providerService.getLogo(1))
                .asString(StandardCharsets.UTF_8)
                .isEqualTo(DEFAULT_LOGO_CONTENT);

        verifyNoInteractions(mockedProviderDataServiceBlockingStub);
    }

    @Test
    @DisplayName("given a logo not cached when calls getLogo then expects to call grpc service and store and cache")
    void givenALogoNotCached_whenCallsGetLogo_thenExpectsToCallGrpcServiceAndStoreInCache() {
        when(mockedProviderDataServiceBlockingStub.getProviders(any())).thenReturn(
                GetProvidersDataResponse.newBuilder()
                                        .addAllProviders(List.of(
                                                ProviderData.newBuilder()
                                                            .setLogo(
                                                                    BytesValue.of(
                                                                            ByteString.copyFrom(DEFAULT_LOGO_CONTENT, StandardCharsets.UTF_8)
                                                                    )
                                                            )
                                                            .build()
                                        ))
                                        .build()
        );

        assertThat(providerService.getLogo(1))
                .asString(StandardCharsets.UTF_8)
                .isEqualTo(DEFAULT_LOGO_CONTENT);

        assertThat(cacheManager.getCache(PROVIDER_LOGO_CACHE))
                .isNotNull()
                .satisfies(cache -> assertThat(cache.get(1, byte[].class))
                        .asString(StandardCharsets.UTF_8)
                        .isEqualTo(DEFAULT_LOGO_CONTENT)
                );

        verify(mockedProviderDataServiceBlockingStub, times(1)).getProviders(any());
        verifyNoMoreInteractions(mockedProviderDataServiceBlockingStub);
    }

    @Test
    @DisplayName("given a cached logo when calls update then expects to evict cache")
    void givenACachedLogo_whenCallsUpdate_thenExpectsToEvictCache() {
        var input = ProviderEditionInput.builder()
                                        .name("Test provider")
                                        .build();

        requireNonNull(cacheManager.getCache(PROVIDER_LOGO_CACHE)).put(1, DEFAULT_LOGO_CONTENT.getBytes());

        when(mockedProviderDataServiceBlockingStub.getProviders(any())).thenReturn(
                GetProvidersDataResponse.newBuilder()
                                        .addAllProviders(List.of(
                                                ProviderData.newBuilder()
                                                            .setId(Int32Value.of(1))
                                                            .build()
                                        ))
                                        .build()
        );

        when(mockedProviderDataServiceBlockingStub.updateProvider(any()))
                .thenAnswer(a -> a.getArgument(0, ProviderData.class));

        when(mockedDataFetchingEnvironment.getArgument("input")).thenReturn(Map.of("name", true));

        input.detectedChangedArguments(mockedDataFetchingEnvironment);

        assertThat(providerService.update(1, input))
                .extracting("id", "name")
                .containsExactly(Int32Value.of(1), StringValue.of(input.getName()));

        assertThat(cacheManager.getCache(PROVIDER_LOGO_CACHE))
                .isNotNull()
                .satisfies(cache -> assertThat(cache.get(1, byte[].class)).isNull());

        verify(mockedProviderDataServiceBlockingStub, times(1)).getProviders(any());
        verify(mockedProviderDataServiceBlockingStub, times(1)).updateProvider(any());
        verifyNoMoreInteractions(mockedProviderDataServiceBlockingStub);
    }

}
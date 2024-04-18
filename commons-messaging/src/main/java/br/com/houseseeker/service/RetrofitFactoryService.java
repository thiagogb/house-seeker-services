package br.com.houseseeker.service;

import br.com.houseseeker.domain.provider.ProviderMetadata;
import br.com.houseseeker.domain.provider.ProviderParameters;
import br.com.houseseeker.util.RetrofitUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RetrofitFactoryService {

    private final ObjectMapper objectMapper;
    private final OkHttpClientFactoryService okHttpClientFactoryService;

    public Retrofit configure(
            @NotNull ProviderMetadata providerMetadata,
            @NotNull ProviderParameters providerParameters
    ) {
        return configureRetrofit(providerMetadata, providerParameters);
    }

    private Retrofit configureRetrofit(ProviderMetadata providerMetadata, ProviderParameters providerParameters) {
        String baseUrl = Optional.ofNullable(providerMetadata.getDataUrl())
                                 .orElse(providerMetadata.getSiteUrl());

        return new Retrofit.Builder()
                .baseUrl(RetrofitUtils.normalizeBaseUrl(baseUrl))
                .client(okHttpClientFactoryService.configure(providerMetadata, providerParameters))
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();
    }

}

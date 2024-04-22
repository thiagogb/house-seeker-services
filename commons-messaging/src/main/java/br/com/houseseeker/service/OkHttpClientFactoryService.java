package br.com.houseseeker.service;

import br.com.houseseeker.domain.provider.ProviderMetadata;
import br.com.houseseeker.domain.provider.ProviderParameters;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.stereotype.Component;

import static java.util.concurrent.TimeUnit.SECONDS;

@Component
@Slf4j
public class OkHttpClientFactoryService {

    public OkHttpClient configure(@NotNull ProviderMetadata providerMetadata, @NotNull ProviderParameters providerParameters) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(providerParameters.getConnection().getConnectionTimeout(), SECONDS)
                .readTimeout(providerParameters.getConnection().getReadTimeout(), SECONDS)
                .addInterceptor(createRetryInterceptor(providerMetadata, providerParameters));

        providerParameters.getConnection().getLogLevels().forEach(logLevel -> {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(logLevel);
            builder.addInterceptor(interceptor);
        });

        return builder.build();
    }

    private Interceptor createRetryInterceptor(ProviderMetadata providerMetadata, ProviderParameters providerParameters) {
        return chain -> {
            Request request = chain.request();
            Response response = chain.proceed(request);

            int attemptNumber = 1;
            while (!response.isSuccessful() && attemptNumber <= providerParameters.getConnection().getRetryCount()) {
                log.info(
                        "Failed request to {} for provider {}: attempt {} of {}",
                        request.url(),
                        providerMetadata.getName(),
                        attemptNumber,
                        providerParameters.getConnection().getRetryCount()
                );

                waitIfTheresRetryRemaining(providerMetadata, providerParameters, ++attemptNumber);

                response.close();
                response = chain.proceed(request);
            }
            return response;
        };
    }

    private void waitIfTheresRetryRemaining(ProviderMetadata providerMetadata, ProviderParameters providerParameters, int attemptNumber) {
        if (attemptNumber >= providerParameters.getConnection().getRetryCount())
            return;

        try {
            Thread.sleep(providerParameters.getConnection().getRetryWait());
        } catch (InterruptedException e) {
            log.error("Retry wait time failed for provider {}", providerMetadata.getName(), e);
            Thread.currentThread().interrupt();
        }
    }

}

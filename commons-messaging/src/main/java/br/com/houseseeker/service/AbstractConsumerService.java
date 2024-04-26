package br.com.houseseeker.service;

import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.domain.provider.ProviderMetadata;
import br.com.houseseeker.domain.provider.ProviderParameters;
import br.com.houseseeker.domain.provider.ProviderScraperResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.messaging.handler.annotation.Payload;
import retrofit2.Retrofit;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractConsumerService {

    private final PersistenceProducerService persistenceProducerService;
    private final ProviderInspectorService providerInspectorService;
    private final RetrofitFactoryService retrofitFactoryService;

    public abstract void consume(@Valid @Payload ProviderMetadata payload);

    protected final void validateProviderMechanism(
            @NotNull ProviderMetadata providerMetadata,
            @NotNull ProviderMechanism acceptedMechanism
    ) {
        if (!acceptedMechanism.equals(providerMetadata.getMechanism()))
            throw new AmqpRejectAndDontRequeueException(String.format(
                    "Unaccepted provider mechanism %s: accepted value are (%s)",
                    providerMetadata.getMechanism(),
                    acceptedMechanism
            ));
    }

    protected final void executeScraper(
            @NotNull ProviderMetadata providerMetadata,
            @NotNull AbstractDataScraperService abstractDataScraperService
    ) {
        CompletableFuture.runAsync(() -> {
            ProviderParameters providerParameters = providerInspectorService.getParameters(providerMetadata);
            Retrofit retrofit = retrofitFactoryService.configure(providerMetadata, providerParameters);
            ProviderScraperResponse response = abstractDataScraperService.scrap(providerMetadata, providerParameters, retrofit);
            persistenceProducerService.produce(response);
        });
    }

}

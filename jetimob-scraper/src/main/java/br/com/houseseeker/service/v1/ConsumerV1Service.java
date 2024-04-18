package br.com.houseseeker.service.v1;

import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.domain.provider.ProviderMetadata;
import br.com.houseseeker.service.AbstractConsumerService;
import br.com.houseseeker.service.PersistenceProducerService;
import br.com.houseseeker.service.ProviderInspectorService;
import br.com.houseseeker.service.RetrofitFactoryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConsumerV1Service extends AbstractConsumerService {

    public static final String LISTENER_ID = "scraperJetimobQueueV1Queue";

    private final DataScraperV1Service dataScraperV1Service;

    public ConsumerV1Service(
            PersistenceProducerService persistenceProducerService,
            ProviderInspectorService providerInspectorService,
            RetrofitFactoryService retrofitFactoryService,
            DataScraperV1Service dataScraperV1Service
    ) {
        super(persistenceProducerService, providerInspectorService, retrofitFactoryService);
        this.dataScraperV1Service = dataScraperV1Service;
    }

    @Override
    @RabbitListener(id = LISTENER_ID, queues = "#{scraperJetimobQueueV1Queue.getName()}")
    public void consume(@Valid @Payload ProviderMetadata payload) {
        log.info("Received message: {}", payload);
        validateProviderMechanism(payload, ProviderMechanism.JETIMOB_V1);
        executeScraper(payload, dataScraperV1Service);
    }

}

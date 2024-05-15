package br.com.houseseeker.service.v2;

import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.domain.provider.ProviderMetadata;
import br.com.houseseeker.service.AbstractConsumerService;
import br.com.houseseeker.service.PersistenceProducerService;
import br.com.houseseeker.service.ProviderInspectorService;
import br.com.houseseeker.service.RetrofitFactoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConsumerV2Service extends AbstractConsumerService {

    public static final String LISTENER_ID = "scraperJetimobQueueV2Queue";

    private final DataScraperV2Service dataScraperV2Service;

    protected ConsumerV2Service(
            PersistenceProducerService persistenceProducerService,
            ProviderInspectorService providerInspectorService,
            RetrofitFactoryService retrofitFactoryService,
            DataScraperV2Service dataScraperV2Service
    ) {
        super(persistenceProducerService, providerInspectorService, retrofitFactoryService);
        this.dataScraperV2Service = dataScraperV2Service;
    }

    @Override
    @RabbitListener(id = LISTENER_ID, queues = "#{scraperJetimobQueueV2Queue.getName()}")
    public void consume(ProviderMetadata payload) {
        log.info("Received message: {}", payload);
        validateProviderMechanism(payload, ProviderMechanism.JETIMOB_V2);
        executeScraper(payload, dataScraperV2Service);
    }

}

package br.com.houseseeker.service.v4;

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
public class ConsumerServiceV4 extends AbstractConsumerService {

    private final DataScraperV4Service dataScraperV4Service;

    protected ConsumerServiceV4(
            PersistenceProducerService persistenceProducerService,
            ProviderInspectorService providerInspectorService,
            RetrofitFactoryService retrofitFactoryService,
            DataScraperV4Service dataScraperV4Service
    ) {
        super(persistenceProducerService, providerInspectorService, retrofitFactoryService);
        this.dataScraperV4Service = dataScraperV4Service;
    }

    @Override
    @RabbitListener(queues = "#{scraperJetimobQueueV4Queue.getName()}")
    public void consume(ProviderMetadata payload) {
        log.info("Received message: {}", payload);
        validateProviderMechanism(payload, ProviderMechanism.JETIMOB_V4);
        executeScraper(payload, dataScraperV4Service);
    }

}

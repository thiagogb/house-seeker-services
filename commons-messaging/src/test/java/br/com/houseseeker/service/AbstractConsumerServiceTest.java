package br.com.houseseeker.service;

import br.com.houseseeker.configuration.ObjectMapperConfiguration;
import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.domain.provider.ProviderMetadata;
import br.com.houseseeker.domain.provider.ProviderScraperResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static br.com.houseseeker.mock.ProviderMetadataMocks.withMechanism;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {
        ObjectMapperConfiguration.class,
        ProviderInspectorService.class,
        OkHttpClientFactoryService.class,
        RetrofitFactoryService.class
})
@ExtendWith(MockitoExtension.class)
class AbstractConsumerServiceTest {

    @Mock
    private PersistenceProducerService persistenceProducerService;

    @Autowired
    private ProviderInspectorService providerInspectorService;

    @Autowired
    private RetrofitFactoryService retrofitFactoryService;

    @Mock
    private AbstractDataScraperService abstractDataScraperService;

    @Mock
    private ProviderScraperResponse providerScraperResponse;

    private TestConsumerService testConsumerService;

    @BeforeEach
    void setup() {
        testConsumerService = new TestConsumerService(
                persistenceProducerService,
                providerInspectorService,
                retrofitFactoryService,
                abstractDataScraperService
        );
    }

    @Test
    @DisplayName("given a provider metadata with unacceptable mechanism when calls consume then expects exception")
    void givenAProviderMetadataWithUnacceptableMechanism_whenCallsConsume_thenExpectsException() {
        ProviderMetadata payload = withMechanism(ProviderMechanism.JETIMOB_V2);

        assertThatThrownBy(() -> testConsumerService.consume(payload))
                .isInstanceOf(AmqpRejectAndDontRequeueException.class)
                .hasMessage("Unaccepted provider mechanism JETIMOB_V2: accepted value are (JETIMOB_V1)");
    }

    @Test
    @DisplayName("given a provider metadata with accepted mechanism when calls consume and process without errors then expects")
    void givenAProviderMetadataWithAcceptedMechanism_whenCallsConsumeAndProcessWithoutErrors_thenExpects() {
        ProviderMetadata payload = withMechanism(ProviderMechanism.JETIMOB_V1);

        when(abstractDataScraperService.scrap(eq(payload), any(), any())).thenReturn(providerScraperResponse);

        testConsumerService.consume(payload);

        await().atMost(Duration.ofSeconds(3))
               .untilAsserted(() -> {
                   verify(abstractDataScraperService, times(1)).scrap(eq(payload), any(), any());
                   verifyNoMoreInteractions(abstractDataScraperService);

                   verify(persistenceProducerService, times(1)).produce(providerScraperResponse);
                   verifyNoMoreInteractions(persistenceProducerService);
               });
    }

    private static final class TestConsumerService extends AbstractConsumerService {

        private final AbstractDataScraperService abstractDataScraperService;

        private TestConsumerService(
                PersistenceProducerService persistenceProducerService,
                ProviderInspectorService providerInspectorService,
                RetrofitFactoryService retrofitFactoryService,
                AbstractDataScraperService abstractDataScraperService
        ) {
            super(persistenceProducerService, providerInspectorService, retrofitFactoryService);
            this.abstractDataScraperService = abstractDataScraperService;
        }

        @Override
        public void consume(ProviderMetadata payload) {
            validateProviderMechanism(payload, ProviderMechanism.JETIMOB_V1);
            executeScraper(payload, abstractDataScraperService);
        }

    }

}
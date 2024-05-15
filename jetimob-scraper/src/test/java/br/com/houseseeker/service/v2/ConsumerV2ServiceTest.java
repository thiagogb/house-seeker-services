package br.com.houseseeker.service.v2;

import br.com.houseseeker.Application;
import br.com.houseseeker.RabbitMqIntegrationTest;
import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.domain.provider.ProviderMetadata;
import br.com.houseseeker.domain.provider.ProviderScraperResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static br.com.houseseeker.mock.ProviderMetadataMocks.withMechanism;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest(classes = Application.class)
@ExtendWith(MockitoExtension.class)
class ConsumerV2ServiceTest extends RabbitMqIntegrationTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    @Autowired
    private Binding persistenceQueueBinding;

    @Autowired
    private Binding deadLetterQueueBinding;

    @Autowired
    private Binding scraperJetimobQueueV2QueueBinding;

    @MockBean
    private DataScraperV2Service dataScraperV2Service;

    @BeforeEach
    void setup() {
        startListener(rabbitListenerEndpointRegistry, ConsumerV2Service.LISTENER_ID);
    }

    @AfterEach
    void finish() {
        stopListener(rabbitListenerEndpointRegistry, ConsumerV2Service.LISTENER_ID);
    }

    @Test
    @DisplayName("given a message for jetimob scraper v2 with successful consume when consume then expects to save in persistence queue")
    void givenAMessageForJetimobScraperV2WithSuccessfulConsume_whenConsume_thenExpectsToSaveInPersistenceQueue() {
        ProviderMetadata providerMetadata = withMechanism(ProviderMechanism.JETIMOB_V2);

        ProviderScraperResponse response = ProviderScraperResponse.builder()
                                                                  .providerMetadata(providerMetadata)
                                                                  .extractedData(Collections.emptyList())
                                                                  .build();

        when(dataScraperV2Service.scrap(eq(providerMetadata), any(), any())).thenReturn(response);

        sendMessage(rabbitTemplate, scraperJetimobQueueV2QueueBinding, providerMetadata);

        await().timeout(THREE_SECONDS_WAIT, TimeUnit.SECONDS)
               .untilAsserted(() -> verify(dataScraperV2Service, times(1)).scrap(eq(providerMetadata), any(), any()));

        assertThat(receivePersistenceMessage(rabbitTemplate, persistenceQueueBinding)).isEqualTo(response);
        assertThat(receiveMessage(rabbitTemplate, deadLetterQueueBinding)).isNull();
    }

    @Test
    @DisplayName("given a invalid message mechanism for jetimob scraper v2 with acceptance fail when consume then expects to save dead letter queue")
    void givenAInvalidMessageMechanismForJetimobScraperV2WithAcceptanceFail_whenConsume_thenExpectsToSaveDeadLetterQueue() {
        ProviderMetadata providerMetadata = withMechanism(ProviderMechanism.JETIMOB_V1);

        sendMessage(rabbitTemplate, scraperJetimobQueueV2QueueBinding, providerMetadata);

        await().timeout(THREE_SECONDS_WAIT, TimeUnit.SECONDS)
               .untilAsserted(() -> verifyNoInteractions(dataScraperV2Service));

        assertThat(receivePersistenceMessage(rabbitTemplate, persistenceQueueBinding)).isNull();
        assertThat(receiveMessage(rabbitTemplate, deadLetterQueueBinding)).isNotNull();
    }

    @Test
    @DisplayName("given a invalid message for jetimob scraper v2 with failed consume when consume then expects to save dead letter queue")
    void givenAInvalidMessageForJetimobScraperV2WithFailedConsume_whenConsume_thenExpectsToSaveDeadLetterQueue() {
        ProviderMetadata providerMetadata = ProviderMetadata.builder().build();

        sendMessage(rabbitTemplate, scraperJetimobQueueV2QueueBinding, providerMetadata);

        await().timeout(THREE_SECONDS_WAIT, TimeUnit.SECONDS)
               .untilAsserted(() -> verifyNoInteractions(dataScraperV2Service));

        assertThat(receivePersistenceMessage(rabbitTemplate, persistenceQueueBinding)).isNull();
        assertThat(receiveMessage(rabbitTemplate, deadLetterQueueBinding)).isNotNull();
    }

}
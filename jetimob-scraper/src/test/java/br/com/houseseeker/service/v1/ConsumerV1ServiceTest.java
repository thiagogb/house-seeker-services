package br.com.houseseeker.service.v1;

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
class ConsumerV1ServiceTest extends RabbitMqIntegrationTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    @Autowired
    private Binding persistenceQueueBinding;

    @Autowired
    private Binding deadLetterQueueBinding;

    @Autowired
    private Binding scraperJetimobQueueV1QueueBinding;

    @MockBean
    private DataScraperV1Service dataScraperV1Service;

    @BeforeEach
    void setup() {
        startListener(rabbitListenerEndpointRegistry, ConsumerV1Service.LISTENER_ID);
    }

    @AfterEach
    void finish() {
        stopListener(rabbitListenerEndpointRegistry, ConsumerV1Service.LISTENER_ID);
    }

    @Test
    @DisplayName("given a message for jetimob scraper v1 with successful consume when consume then expects to save in persistence queue")
    void givenAMessageForJetimobScraperV1WithSuccessfulConsume_whenConsume_thenExpectsToSaveInPersistenceQueue() {
        ProviderMetadata providerMetadata = withMechanism(ProviderMechanism.JETIMOB_V1);

        ProviderScraperResponse response = ProviderScraperResponse.builder()
                                                                  .providerMetadata(providerMetadata)
                                                                  .extractedData(Collections.emptyList())
                                                                  .build();

        when(dataScraperV1Service.scrap(eq(providerMetadata), any(), any())).thenReturn(response);

        sendMessage(rabbitTemplate, scraperJetimobQueueV1QueueBinding, providerMetadata);

        await().timeout(THREE_SECONDS_WAIT, TimeUnit.SECONDS)
               .untilAsserted(() -> verify(dataScraperV1Service, times(1)).scrap(eq(providerMetadata), any(), any()));

        assertThat(receivePersistenceMessage(rabbitTemplate, persistenceQueueBinding)).isEqualTo(response);
        assertThat(receiveMessage(rabbitTemplate, deadLetterQueueBinding)).isNull();
    }

    @Test
    @DisplayName("given a invalid message mechanism for jetimob scraper v1 with acceptance fail when consume then expects to save dead letter queue")
    void givenAInvalidMessageMechanismForJetimobScraperV1WithAcceptanceFail_whenConsume_thenExpectsToSaveDeadLetterQueue() {
        ProviderMetadata providerMetadata = withMechanism(ProviderMechanism.JETIMOB_V2);

        sendMessage(rabbitTemplate, scraperJetimobQueueV1QueueBinding, providerMetadata);

        await().timeout(THREE_SECONDS_WAIT, TimeUnit.SECONDS)
               .untilAsserted(() -> verifyNoInteractions(dataScraperV1Service));

        assertThat(receivePersistenceMessage(rabbitTemplate, persistenceQueueBinding)).isNull();
        assertThat(receiveMessage(rabbitTemplate, deadLetterQueueBinding)).isNotNull();
    }

    @Test
    @DisplayName("given a invalid message for jetimob scraper v1 with failed consume when consume then expects to save dead letter queue")
    void givenAInvalidMessageForJetimobScraperV1WithFailedConsume_whenConsume_thenExpectsToSaveDeadLetterQueue() {
        ProviderMetadata providerMetadata = ProviderMetadata.builder().build();

        sendMessage(rabbitTemplate, scraperJetimobQueueV1QueueBinding, providerMetadata);

        await().timeout(THREE_SECONDS_WAIT, TimeUnit.SECONDS)
               .untilAsserted(() -> verifyNoInteractions(dataScraperV1Service));

        assertThat(receivePersistenceMessage(rabbitTemplate, persistenceQueueBinding)).isNull();
        assertThat(receiveMessage(rabbitTemplate, deadLetterQueueBinding)).isNotNull();
    }

}
package br.com.houseseeker.service.v1;

import br.com.houseseeker.JetimobScraperApplication;
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
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest
@ContextConfiguration(classes = JetimobScraperApplication.class)
@ExtendWith(MockitoExtension.class)
class ConsumerV1ServiceTest implements RabbitMqIntegrationTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    @Autowired
    private Binding persistenceQueueBinding;

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
    @DisplayName("given a message for jetimob scraper v1 when consume then expects")
    void givenAMessageForJetimobScraperV1_whenConsume_thenExpects() {
        ProviderMetadata providerMetadata = ProviderMetadata.builder()
                                                            .id(1)
                                                            .name("Test Provider")
                                                            .mechanism(ProviderMechanism.JETIMOB_V1)
                                                            .siteUrl("http://test.com")
                                                            .build();

        ProviderScraperResponse response = ProviderScraperResponse.builder()
                                                                  .providerMetadata(providerMetadata)
                                                                  .extractedData(Collections.emptyList())
                                                                  .build();

        when(dataScraperV1Service.scrap(eq(providerMetadata), any(), any())).thenReturn(response);

        sendMessage(rabbitTemplate, scraperJetimobQueueV1QueueBinding, providerMetadata);

        await().timeout(10, TimeUnit.SECONDS)
               .untilAsserted(() -> verify(dataScraperV1Service, times(1)).scrap(eq(providerMetadata), any(), any()));

        assertThat(receivePersistenceMessage(rabbitTemplate, persistenceQueueBinding)).isEqualTo(response);
    }

    @Test
    @DisplayName("given a invalid message for jetimob scraper v1 when consume then expects exception")
    void givenAInvalidMessageForJetimobScraperV1_whenConsume_thenExpectException() {
        ProviderMetadata providerMetadata = ProviderMetadata.builder().build();

        sendMessage(rabbitTemplate, scraperJetimobQueueV1QueueBinding, providerMetadata);

        await().timeout(10, TimeUnit.SECONDS)
               .untilAsserted(() -> verifyNoInteractions(dataScraperV1Service));

        assertThat(receivePersistenceMessage(rabbitTemplate, persistenceQueueBinding)).isNull();
    }

}
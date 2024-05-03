package br.com.houseseeker.service;

import br.com.houseseeker.RabbitMqIntegrationTest;
import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.domain.provider.ProviderScraperResponse;
import br.com.houseseeker.domain.provider.ProviderScraperResponse.ErrorInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static br.com.houseseeker.mock.ProviderMetadataMocks.withUrlAndMechanism;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest
class PersistenceProducerServiceTest extends RabbitMqIntegrationTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private PersistenceProducerService persistenceProducerService;

    @Autowired
    private Binding persistenceQueueBinding;

    @Test
    @DisplayName("given a scrapper error response when calls produce then expects to send message to broker")
    void givenAScrapperErrorResponse_whenCallsProduce_thenSendMessageToBroker() {
        ProviderScraperResponse providerScraperResponse = ProviderScraperResponse.builder()
                                                                                 .providerMetadata(
                                                                                         withUrlAndMechanism(
                                                                                                 "http://localhost",
                                                                                                 ProviderMechanism.JETIMOB_V1
                                                                                         )
                                                                                 )
                                                                                 .errorInfo(
                                                                                         ErrorInfo.builder()
                                                                                                  .message("error message")
                                                                                                  .className("java.lang.RuntimeException")
                                                                                                  .stackTrace("stack trace")
                                                                                                  .build()
                                                                                 )
                                                                                 .build();

        persistenceProducerService.produce(providerScraperResponse);

        await().pollDelay(THREE_SECONDS_WAIT, TimeUnit.SECONDS)
               .untilAsserted(() -> assertThat(receivePersistenceMessage(rabbitTemplate, persistenceQueueBinding)).isEqualTo(providerScraperResponse));
    }

}
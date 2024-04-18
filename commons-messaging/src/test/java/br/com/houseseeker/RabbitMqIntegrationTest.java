package br.com.houseseeker;

import br.com.houseseeker.domain.provider.ProviderScraperResponse;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public interface RabbitMqIntegrationTest {

    @Container
    RabbitMQContainer RABBIT_MQ_CONTAINER = new RabbitMQContainer("rabbitmq:3.13.0-alpine")
            .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger(RabbitMqIntegrationTest.class)));

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", RABBIT_MQ_CONTAINER::getHost);
        registry.add("spring.rabbitmq.port", RABBIT_MQ_CONTAINER::getAmqpPort);
        registry.add("spring.rabbitmq.username", RABBIT_MQ_CONTAINER::getAdminUsername);
        registry.add("spring.rabbitmq.password", RABBIT_MQ_CONTAINER::getAdminPassword);
        registry.add("spring.rabbitmq.listener.simple.auto-startup", () -> "false");
    }

    default void startListener(RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry, String id) {
        rabbitListenerEndpointRegistry.getListenerContainer(id).start();
    }

    default void stopListener(RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry, String id) {
        rabbitListenerEndpointRegistry.getListenerContainer(id).stop();
    }

    default <T> void sendMessage(RabbitTemplate rabbitTemplate, Binding binding, T message) {
        rabbitTemplate.convertAndSend(binding.getExchange(), binding.getRoutingKey(), message);
    }

    default ProviderScraperResponse receivePersistenceMessage(RabbitTemplate rabbitTemplate, Binding binding) {
        return rabbitTemplate.receiveAndConvert(binding.getDestination(), 5000, new ParameterizedTypeReference<>() {
        });
    }

}

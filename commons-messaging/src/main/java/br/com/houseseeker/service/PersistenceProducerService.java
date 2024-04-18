package br.com.houseseeker.service;

import br.com.houseseeker.domain.provider.ProviderScraperResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersistenceProducerService {

    private final RabbitTemplate rabbitTemplate;
    private final TopicExchange topicExchange;
    private final Binding persistenceQueueBinding;

    public void produce(@NotNull ProviderScraperResponse response) {
        log.info("Sending message for routingKey: {}", persistenceQueueBinding.getRoutingKey());
        rabbitTemplate.convertAndSend(topicExchange.getName(), persistenceQueueBinding.getRoutingKey(), response);
    }

}

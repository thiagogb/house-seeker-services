package br.com.houseseeker.configuration;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static br.com.houseseeker.util.RabbitMqUtils.getDeadLetterParamsUsingBinding;

@Configuration
@Slf4j
public class RabbitMqJetimobConfiguration {

    @Bean
    public Queue scraperJetimobQueueV1Queue(Binding deadLetterQueueBinding) {
        return new Queue("scraper.jetimob.v1.queue", true, false, false, getDeadLetterParamsUsingBinding(deadLetterQueueBinding));
    }

    @Bean
    public Binding scraperJetimobQueueV1QueueBinding(TopicExchange topicExchange, Queue scraperJetimobQueueV1Queue) {
        return BindingBuilder.bind(scraperJetimobQueueV1Queue).to(topicExchange).with("scraper.jetimob.v1");
    }

    @Bean
    public Queue scraperJetimobQueueV4Queue(Binding deadLetterQueueBinding) {
        return new Queue("scraper.jetimob.v4.queue", true, false, false, getDeadLetterParamsUsingBinding(deadLetterQueueBinding));
    }

    @Bean
    public Binding scraperJetimobQueueV4QueueBinding(TopicExchange topicExchange, Queue scraperJetimobQueueV4Queue) {
        return BindingBuilder.bind(scraperJetimobQueueV4Queue).to(topicExchange).with("scraper.jetimob.v4");
    }

    @PostConstruct
    public void configure() {
        log.info("Initialized Jetimob queue's!");
    }

}

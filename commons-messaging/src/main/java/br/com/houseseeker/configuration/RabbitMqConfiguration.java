package br.com.houseseeker.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@EnableRabbit
@Slf4j
public class RabbitMqConfiguration {

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("house.seeker.exchange", true, false);
    }

    @Bean
    public Queue persistenceQueue() {
        return new Queue("persistence.queue", true, false, false);
    }

    @Bean
    public Binding persistenceQueueBinding(TopicExchange topicExchange, Queue persistenceQueue) {
        return BindingBuilder.bind(persistenceQueue).to(topicExchange).with("persistence.*");
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue("deadLetter.queue", true, false, false);
    }

    @Bean
    public Binding deadLetterQueueBinding(TopicExchange topicExchange, Queue deadLetterQueue) {
        return BindingBuilder.bind(deadLetterQueue).to(topicExchange).with("deadLetter.*");
    }

    @Bean
    public DefaultMessageHandlerMethodFactory defaultMessageHandlerMethodFactory(LocalValidatorFactoryBean localValidatorFactoryBean) {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setValidator(localValidatorFactoryBean);
        return factory;
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public AmqpTemplate amqpTemplate(
            ConnectionFactory connectionFactory,
            MessageConverter jackson2JsonMessageConverter
    ) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);
        return rabbitTemplate;
    }

    @PostConstruct
    public void configure() {
        log.info("Initialized RabbitMQ!");
    }

}

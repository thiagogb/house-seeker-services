package br.com.houseseeker.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RabbitMqListenerConfiguration implements RabbitListenerConfigurer {

    private final LocalValidatorFactoryBean localValidatorFactoryBean;

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {
        log.info("Successfully assign validation bean on RabbitMQ listeners!");
        rabbitListenerEndpointRegistrar.setValidator(localValidatorFactoryBean);
    }

}

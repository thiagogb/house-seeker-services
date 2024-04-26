package br.com.houseseeker.service;

import br.com.houseseeker.domain.provider.ProviderScraperResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersistenceConsumerService {

    public static final String LISTENER_ID = "persistenceQueue";

    private final ScraperPersistenceService scraperPersistenceService;

    @RabbitListener(id = LISTENER_ID, queues = "#{persistenceQueue.getName()}")
    public void consume(@Valid @Payload ProviderScraperResponse payload) {
        log.info("Received message: {}", payload);
        CompletableFuture.runAsync(() -> scraperPersistenceService.persists(payload));
    }

}

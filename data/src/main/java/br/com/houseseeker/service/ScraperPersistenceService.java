package br.com.houseseeker.service;

import br.com.houseseeker.domain.provider.ProviderMetadata;
import br.com.houseseeker.domain.provider.ProviderScraperResponse;
import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.service.ProviderDataCollectorService.UrbanPropertyFullData;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.Map;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScraperPersistenceService {

    private final ProviderService providerService;
    private final ProviderDataCollectorService providerDataCollectorService;
    private final ProviderDataMergeService providerDataMergeService;
    private final ScannerService scannerService;

    @Transactional
    public void persists(@NotNull ProviderScraperResponse providerScraperResponse) {
        Provider provider = getProviderOrReject(providerScraperResponse.getProviderMetadata());
        if (isNull(providerScraperResponse.getErrorInfo())) {
            log.info("Starting provider {} persistence", provider.getName());
            collectDataAndMerge(provider, providerScraperResponse);
        } else {
            log.info("Registering provider {} scraper error", provider.getName());
            scannerService.failed(provider, providerScraperResponse.getStartAt(), providerScraperResponse.getErrorInfo());
        }
    }

    private void collectDataAndMerge(Provider provider, ProviderScraperResponse providerScraperResponse) {
        StopWatch stopWatch = new StopWatch(getClass().getName());
        stopWatch.start();
        try {
            Map<UrbanProperty, UrbanPropertyFullData> propertyFullDataMap = providerDataCollectorService.collect(provider);
            providerDataMergeService.merge(provider, propertyFullDataMap, providerScraperResponse.getExtractedData());
            scannerService.successful(provider, providerScraperResponse.getStartAt());
        } catch (Exception e) {
            log.error(String.format("Failed provider %s persistence", provider.getName()), e);
            scannerService.failed(provider, providerScraperResponse.getStartAt(), e);
        } finally {
            stopWatch.stop();
            log.info("Finished provider {} persistence in {}", provider.getName(), stopWatch.shortSummary());
        }
    }

    private Provider getProviderOrReject(ProviderMetadata providerMetadata) {
        return providerService.findById(providerMetadata.getId())
                              .orElseThrow(() -> new AmqpRejectAndDontRequeueException(
                                      String.format("Provider with id %s not found", providerMetadata.getId())
                              ));
    }

}

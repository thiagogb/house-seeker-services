package br.com.houseseeker.service;

import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import br.com.houseseeker.domain.property.AbstractUrbanPropertyMetadata;
import br.com.houseseeker.domain.provider.ProviderMetadata;
import br.com.houseseeker.domain.provider.ProviderParameters;
import br.com.houseseeker.domain.provider.ProviderScraperResponse;
import br.com.houseseeker.domain.provider.ProviderScraperResponse.ErrorInfo;
import br.com.houseseeker.util.StopWatchUtils;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.util.StopWatch;
import retrofit2.Retrofit;

import java.time.Clock;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public abstract class AbstractDataScraperService {

    private final Clock clock;
    private final Map<Integer, UUID> providersExecuting = new HashMap<>();

    public final ProviderScraperResponse scrap(
            @NotNull ProviderMetadata providerMetadata,
            @NotNull ProviderParameters providerParameters,
            @NotNull Retrofit retrofit
    ) {
        UUID executorId = UUID.randomUUID();
        log.info("Starting data scrapping on provider {} with execution id {} ...", providerMetadata.getName(), executorId);
        ProviderScraperResponse response;
        StopWatch stopWatch = new StopWatch(getClass().getName());
        stopWatch.start();
        try {
            validateAndPrepareProviderExecution(providerMetadata, executorId);
            response = execute(providerMetadata, providerParameters, retrofit);
        } catch (Exception e) {
            log.error(String.format("Failed data scrapping for provider %s", providerMetadata.getName()), e);
            response = buildScrappingResponseError(providerMetadata, e);
        } finally {
            stopWatch.stop();
            clearProviderExecution(providerMetadata, executorId);
        }
        log.info("Finished data scrapping on provider {}: {}", providerMetadata.getName(), stopWatch.shortSummary());
        response.setStartAt(StopWatchUtils.getStart(clock, stopWatch));
        return response;
    }

    protected abstract ProviderScraperResponse execute(
            ProviderMetadata providerMetadata,
            ProviderParameters providerParameters,
            Retrofit retrofit
    );

    protected final <T> ProviderScraperResponse generateResponse(
            @NotNull ProviderMetadata providerMetadata,
            @NotNull List<T> scrappedDataList,
            @NotNull Function<T, AbstractUrbanPropertyMetadata> resultMapper
    ) {
        return ProviderScraperResponse.builder()
                                      .providerMetadata(providerMetadata)
                                      .extractedData(scrappedDataList.stream().map(resultMapper).toList())
                                      .build();
    }

    private synchronized void validateAndPrepareProviderExecution(ProviderMetadata providerMetadata, UUID uuid) {
        if (providersExecuting.containsKey(providerMetadata.getId()))
            throw new ExtendedRuntimeException("Provider %s is already in execution state", providerMetadata.getName());

        providersExecuting.put(providerMetadata.getId(), uuid);
    }

    private synchronized void clearProviderExecution(ProviderMetadata providerMetadata, UUID uuid) {
        if (uuid.equals(providersExecuting.get(providerMetadata.getId())))
            providersExecuting.remove(providerMetadata.getId());
    }

    private ProviderScraperResponse buildScrappingResponseError(ProviderMetadata providerMetadata, Exception exception) {
        return ProviderScraperResponse.builder()
                                      .providerMetadata(providerMetadata)
                                      .errorInfo(
                                              ErrorInfo.builder()
                                                       .className(exception.getClass().getName())
                                                       .message(exception.getMessage())
                                                       .stackTrace(ExceptionUtils.getStackTrace(exception))
                                                       .build()
                                      )
                                      .build();
    }

}

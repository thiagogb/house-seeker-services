package br.com.houseseeker.service;

import br.com.houseseeker.domain.property.AbstractUrbanPropertyMetadata;
import br.com.houseseeker.domain.provider.ProviderMetadata;
import br.com.houseseeker.domain.provider.ProviderParameters;
import br.com.houseseeker.domain.provider.ProviderScraperResponse;
import br.com.houseseeker.domain.provider.ProviderScraperResponse.ErrorInfo;
import br.com.houseseeker.util.StopWatchUtils;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.util.StopWatch;
import retrofit2.Retrofit;

import java.util.List;
import java.util.function.Function;

@Slf4j
public abstract class AbstractDataScraperService {

    public final ProviderScraperResponse scrap(
            @NotNull ProviderMetadata providerMetadata,
            @NotNull ProviderParameters providerParameters,
            @NotNull Retrofit retrofit
    ) {
        log.info("Starting data scrapping on provider {} ...", providerMetadata.getName());
        ProviderScraperResponse response;
        StopWatch stopWatch = new StopWatch(getClass().getName());
        stopWatch.start();
        try {
            response = execute(providerMetadata, providerParameters, retrofit);
        } catch (Exception e) {
            log.error(String.format("Failed data scrapping for provider %s", providerMetadata.getName()), e);
            response = buildScrappingResponseError(providerMetadata, e);
        } finally {
            stopWatch.stop();
        }
        log.info("Finished data scrapping on provider {}: {}", providerMetadata.getName(), stopWatch.shortSummary());
        response.setStartAt(StopWatchUtils.getStart(stopWatch));
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

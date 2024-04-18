package br.com.houseseeker.domain.provider;

import br.com.houseseeker.domain.urbanProperty.AbstractUrbanPropertyMetadata;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProviderScraperResponse {

    private final ProviderMetadata providerMetadata;
    private final ErrorInfo errorInfo;
    private List<AbstractUrbanPropertyMetadata> extractedData;

    @Data
    @Builder
    public static final class ErrorInfo {

        private String className;
        private String message;
        private String stackTrace;

    }

}

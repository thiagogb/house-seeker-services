package br.com.houseseeker.domain.provider;

import br.com.houseseeker.domain.urbanProperty.AbstractUrbanPropertyMetadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProviderScraperResponse {

    private ProviderMetadata providerMetadata;
    private ErrorInfo errorInfo;
    @Builder.Default
    private List<AbstractUrbanPropertyMetadata> extractedData = new LinkedList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static final class ErrorInfo {

        private String className;
        private String message;
        private String stackTrace;

    }

}

package br.com.houseseeker.domain.provider;

import br.com.houseseeker.deserializer.AbstractUrbanPropertyMetadataDeserializer;
import br.com.houseseeker.domain.property.AbstractUrbanPropertyMetadata;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProviderScraperResponse {

    @NotNull
    private ProviderMetadata providerMetadata;
    private ErrorInfo errorInfo;
    @Builder.Default
    @JsonDeserialize(using = AbstractUrbanPropertyMetadataDeserializer.class)
    private List<AbstractUrbanPropertyMetadata> extractedData = new LinkedList<>();
    private LocalDateTime startAt;

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

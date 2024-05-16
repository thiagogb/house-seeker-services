package br.com.houseseeker.domain.jetimob.v2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Data
@Builder
public class ScraperAnalysisProperties {

    public static final ScraperAnalysisProperties DEFAULT = ScraperAnalysisProperties.builder().build();

    @Builder.Default
    private Scope cities = Scope.builder().build();
    @Builder.Default
    private Scope subTypes = Scope.builder().build();

    @JsonCreator
    public ScraperAnalysisProperties(@JsonProperty("cities") Scope cities, @JsonProperty("subTypes") Scope subTypes) {
        this.cities = Optional.ofNullable(cities).orElse(Scope.builder().build());
        this.subTypes = Optional.ofNullable(subTypes).orElse(Scope.builder().build());
    }

    public List<String> applyCitiesFilter(List<String> values) {
        if (CollectionUtils.isEmpty(cities.values))
            return values;

        return values.stream()
                     .filter(v -> (cities.operation == Operation.IN) == cities.values.contains(v))
                     .toList();
    }

    public List<String> applySubTypesFilter(List<String> values) {
        if (CollectionUtils.isEmpty(subTypes.values))
            return values;

        return values.stream()
                     .filter(v -> (subTypes.operation == Operation.IN) == subTypes.values.contains(v))
                     .toList();
    }

    public enum Operation {

        IN, NOT_IN

    }

    @Data
    @Builder
    public static final class Scope {

        @Builder.Default
        private Operation operation = Operation.NOT_IN;
        @Builder.Default
        private List<String> values = new LinkedList<>();

        @JsonCreator
        public Scope(@JsonProperty("operation") Operation operation, @JsonProperty("values") List<String> values) {
            this.operation = Optional.ofNullable(operation).orElse(Operation.NOT_IN);
            this.values = Optional.ofNullable(values).orElse(Collections.emptyList());
        }
    }

}

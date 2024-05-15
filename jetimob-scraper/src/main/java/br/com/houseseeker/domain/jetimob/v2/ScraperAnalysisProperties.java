package br.com.houseseeker.domain.jetimob.v2;

import lombok.Builder;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

@Data
@Builder
public class ScraperAnalysisProperties {

    public static final ScraperAnalysisProperties DEFAULT = ScraperAnalysisProperties.builder().build();

    @Builder.Default
    private Scope cities = Scope.builder().build();
    @Builder.Default
    private Scope subTypes = Scope.builder().build();

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

    }

}

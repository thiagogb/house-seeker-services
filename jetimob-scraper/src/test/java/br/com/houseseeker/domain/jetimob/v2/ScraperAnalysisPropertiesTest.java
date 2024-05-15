package br.com.houseseeker.domain.jetimob.v2;

import br.com.houseseeker.domain.jetimob.v2.ScraperAnalysisProperties.Operation;
import br.com.houseseeker.domain.jetimob.v2.ScraperAnalysisProperties.Scope;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ScraperAnalysisPropertiesTest {

    private static final List<String> SAMPLE_CITIES = List.of("Porto Alegre", "Santa Maria");
    private static final List<String> SAMPLE_SUBTYPES = List.of("Apartamento", "Casa", "Sobrado");

    @Test
    @DisplayName("given a empty analysis scope when calls applyCitiesFilter then expects")
    void givenAEmptyAnalysisScope_whenCallsApplyCitiesFilter_thenExpects() {
        assertThat(ScraperAnalysisProperties.DEFAULT.applyCitiesFilter(SAMPLE_CITIES))
                .isEqualTo(SAMPLE_CITIES);
    }

    @Test
    @DisplayName("given a analysis scope for cities with in operator when calls applyCitiesFilter then expects")
    void givenAAnalysisScopeForCitiesWithInOperator_whenCallApplyCitiesFilter_thenExpects() {
        var analysisProperties = ScraperAnalysisProperties.builder()
                                                          .cities(
                                                                  Scope.builder()
                                                                       .operation(Operation.IN)
                                                                       .values(SAMPLE_CITIES)
                                                                       .build()
                                                          )
                                                          .build();

        assertThat(analysisProperties.applyCitiesFilter(List.of("Caxias do Sul", "Pelotas", "Porto Alegre", "Santa Maria")))
                .isEqualTo(SAMPLE_CITIES);
    }

    @Test
    @DisplayName("given a analysis scope for cities with not in operator when calls applyCitiesFilter then expects")
    void givenAAnalysisScopeForCitiesWithNotInOperator_whenCallApplyCitiesFilter_thenExpects() {
        var analysisProperties = ScraperAnalysisProperties.builder()
                                                          .cities(
                                                                  Scope.builder()
                                                                       .operation(Operation.NOT_IN)
                                                                       .values(SAMPLE_CITIES)
                                                                       .build()
                                                          )
                                                          .build();

        assertThat(analysisProperties.applyCitiesFilter(List.of("Caxias do Sul", "Pelotas", "Porto Alegre", "Santa Maria")))
                .doesNotContainAnyElementsOf(SAMPLE_CITIES);
    }

    @Test
    @DisplayName("given a empty analysis scope when calls applySubTypesFilter then expects")
    void givenAEmptyAnalysisScope_whenCallsApplySubTypesFilter_thenExpects() {
        assertThat(ScraperAnalysisProperties.DEFAULT.applySubTypesFilter(SAMPLE_SUBTYPES))
                .isEqualTo(SAMPLE_SUBTYPES);
    }

    @Test
    @DisplayName("given a analysis scope for subTypes with in operator when calls applySubTypesFilter then expects")
    void givenAAnalysisScopeForSubtypesWithInOperator_whenCallApplySubTypesFilter_thenExpects() {
        var analysisProperties = ScraperAnalysisProperties.builder()
                                                          .subTypes(
                                                                  Scope.builder()
                                                                       .operation(Operation.IN)
                                                                       .values(SAMPLE_SUBTYPES)
                                                                       .build()
                                                          )
                                                          .build();

        assertThat(analysisProperties.applySubTypesFilter(List.of("Apartamento", "Casa", "Kitnet", "Sobrado", "Terreno")))
                .isEqualTo(SAMPLE_SUBTYPES);
    }

    @Test
    @DisplayName("given a analysis scope for subTypes with not in operator when calls applySubTypesFilter then expects")
    void givenAAnalysisScopeForSubtypesWithNotInOperator_whenCallApplySubTypesFilter_thenExpects() {
        var analysisProperties = ScraperAnalysisProperties.builder()
                                                          .subTypes(
                                                                  Scope.builder()
                                                                       .operation(Operation.NOT_IN)
                                                                       .values(SAMPLE_SUBTYPES)
                                                                       .build()
                                                          )
                                                          .build();

        assertThat(analysisProperties.applySubTypesFilter(List.of("Apartamento", "Casa", "Kitnet", "Sobrado", "Terreno")))
                .doesNotContainAnyElementsOf(SAMPLE_SUBTYPES);
    }

}
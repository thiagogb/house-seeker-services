package br.com.houseseeker.service.v2;

import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static br.com.houseseeker.TestUtils.getTextFromResources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = FilterOptionsV2ScraperService.class)
class FilterOptionsV2ScraperServiceTest {

    private static final String SAMPLE_WITH_MODAL_FULL_DATA = "samples/v2/filter/with-modal-full-data.html";
    private static final String SAMPLE_WITHOUT_MODAL = "samples/v2/filter/without-modal.html";
    private static final String SAMPLE_WITH_MODAL_EMPTY_DATA = "samples/v2/filter/with-modal-empty-data.html";

    @Autowired
    private FilterOptionsV2ScraperService filterOptionsV2ScraperService;

    @Test
    @DisplayName("given a sample page with full data when calls scrap then expects")
    void givenASamplePageWithFullData_whenCallsScrap_ThenExpects() {
        assertThat(filterOptionsV2ScraperService.scrap(getTextFromResources(SAMPLE_WITH_MODAL_FULL_DATA)))
                .extracting("cities", "types")
                .containsExactly(
                        List.of("Agudo", "Camboriú", "Caxias do Sul", "Gramado", "Santa Maria"),
                        List.of("Apartamento", "Casa", "Casa Comercial", "Casa de Condomínio", "Chácara", "Cobertura",
                                "Sala Comercial", "Sobrado", "Sítio", "Terreno")
                );
    }

    @Test
    @DisplayName("given a sample page without modal element then calls scrap then expects exception")
    void givenASamplePageWithoutModalElement_whenCallsScrap_ThenExpectsException() {
        String document = getTextFromResources(SAMPLE_WITHOUT_MODAL);
        assertThatThrownBy(() -> filterOptionsV2ScraperService.scrap(document))
                .isInstanceOf(ExtendedRuntimeException.class)
                .hasMessage("Filter modal element not found");
    }

    @Test
    @DisplayName("given a sample page with empty data when calls scrap then expects")
    void givenASamplePageWithEmptyData_whenCallsScrap_ThenExpects() {
        assertThat(filterOptionsV2ScraperService.scrap(getTextFromResources(SAMPLE_WITH_MODAL_EMPTY_DATA)))
                .hasFieldOrPropertyWithValue("cities", Collections.emptyList())
                .hasFieldOrPropertyWithValue("types", Collections.emptyList());
    }

}
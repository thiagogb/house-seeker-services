package br.com.houseseeker.service.v2;

import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import br.com.houseseeker.domain.jetimob.PropertyCharacteristic;
import br.com.houseseeker.domain.jetimob.PropertyDetail;
import br.com.houseseeker.domain.jetimob.v2.PropertyInfoMetadata.Location;
import br.com.houseseeker.domain.jetimob.v2.PropertyInfoMetadata.Media;
import br.com.houseseeker.domain.jetimob.v2.PropertyInfoMetadata.Pricing;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static br.com.houseseeker.TestUtils.getTextFromResources;
import static br.com.houseseeker.domain.jetimob.PropertyCharacteristic.Type.BATHROOMS;
import static br.com.houseseeker.domain.jetimob.PropertyCharacteristic.Type.DORMITORIES;
import static br.com.houseseeker.domain.jetimob.PropertyCharacteristic.Type.GARAGES;
import static br.com.houseseeker.domain.jetimob.PropertyCharacteristic.Type.PRIVATE_AREA;
import static br.com.houseseeker.domain.jetimob.PropertyCharacteristic.Type.TOTAL_AREA;
import static br.com.houseseeker.domain.jetimob.PropertyDetail.Type.FLOOR_TYPE;
import static br.com.houseseeker.domain.jetimob.PropertyDetail.Type.MEASURES;
import static br.com.houseseeker.domain.jetimob.PropertyDetail.Type.POSITION;
import static br.com.houseseeker.domain.jetimob.PropertyDetail.Type.SOLAR_POSITION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@SpringBootTest(classes = PropertyPageV2ScraperService.class)
class PropertyPageV2ScraperServiceTest {

    private static final String SAMPLE_WITH_FULL_DATA = "samples/v2/property/with-full-data.html";
    private static final String SAMPLE_WITHOUT_DATA = "samples/v2/property/without-data.html";

    @Autowired
    private PropertyPageV2ScraperService propertyPageV2ScraperService;

    @Test
    @DisplayName("given a property page without data when scrap then expects")
    void givenAPropertyPageWithoutData_whenScrap_thenExpects() {
        assertThat(propertyPageV2ScraperService.scrap(getTextFromResources(SAMPLE_WITHOUT_DATA)))
                .hasFieldOrPropertyWithValue("description", null)
                .hasFieldOrPropertyWithValue("location", Location.builder().build())
                .hasFieldOrPropertyWithValue("pricing", Pricing.builder().build())
                .hasFieldOrPropertyWithValue("characteristics", Collections.emptyList())
                .hasFieldOrPropertyWithValue("medias", Collections.emptyList())
                .hasFieldOrPropertyWithValue("details", Collections.emptyList())
                .hasFieldOrPropertyWithValue("comforts", Collections.emptyList());
    }

    @Test
    @DisplayName("given a property page with full data when scrap then expects")
    void givenAPropertyPageWithFullData_whenScrap_thenExpects() {
        assertThat(propertyPageV2ScraperService.scrap(getTextFromResources(SAMPLE_WITH_FULL_DATA)))
                .extracting("description", "location", "pricing", "characteristics", "medias", "details", "comforts")
                .containsExactly(
                        "Casa com dois pavimentos sendo o, Térreo com dois dormitórios, banheiro, ampla sala de estar, " +
                                "cozinha e área de serviço com churrasqueira.",
                        Location.builder()
                                .state("RS")
                                .city("Santa Maria")
                                .district("Juscelino Kubitschek")
                                .latitude("-29.695085")
                                .longitude("-53.848229")
                                .build(),
                        Pricing.builder()
                               .sellPrice("R$ 850.000,00")
                               .rentPrice("R$ 8.500,00")
                               .condominiumPrice("R$ 850,00")
                               .build(),
                        List.of(
                                PropertyCharacteristic.builder().type(DORMITORIES).value("4 dormitórios").build(),
                                PropertyCharacteristic.builder().type(TOTAL_AREA).value("300.0 m² total").build(),
                                PropertyCharacteristic.builder().type(PRIVATE_AREA).value("186 m² privativa").build(),
                                PropertyCharacteristic.builder().type(BATHROOMS).value("2 banheiros").build(),
                                PropertyCharacteristic.builder().type(GARAGES).value("2 vagas").build()
                        ),
                        List.of(
                                Media.builder().link("https://s01.jetimgs.com/1.jpeg").extension("jpeg").build(),
                                Media.builder().link("https://s01.jetimgs.com/2.jpeg").extension("jpeg").build(),
                                Media.builder().link("https://s01.jetimgs.com/3.jpeg").extension("jpeg").build()
                        ),
                        List.of(
                                PropertyDetail.builder().type(FLOOR_TYPE).items(List.of("Cerâmico", "Laminado")).build(),
                                PropertyDetail.builder().type(SOLAR_POSITION).items(List.of("Norte")).build(),
                                PropertyDetail.builder().type(POSITION).items(List.of("Frente")).build(),
                                PropertyDetail.builder().type(MEASURES).items(List.of("Frente 10m", "Fundos 10m", "Direita 30m", "Esquerda 30m")).build()
                        ),
                        List.of(
                                "Alarme",
                                "Ar condicionado",
                                "Cerca elétrica",
                                "Churrasqueira",
                                "Pátio",
                                "Piscina",
                                "Portão eletrônico",
                                "Sacada",
                                "Interfone",
                                "Muro",
                                "Área de serviço",
                                "Cozinha",
                                "Varanda",
                                "Sala de estar",
                                "Banheiro social",
                                "Estar social",
                                "Closet",
                                "Jardim",
                                "Deck molhado",
                                "Vista panorâmica",
                                "Lareira",
                                "Espera para split",
                                "Internet",
                                "Móveis planejados",
                                "Armário cozinha"
                        )
                );
    }

    @Test
    @DisplayName("given a fake document without element container when calls scrap then expects exception")
    void givenAFakeDocumentWithoutElementContainer_whenCallsScrap_thenExpectsException() {
        try (MockedStatic<Jsoup> mockedJsoup = mockStatic(Jsoup.class)) {
            Document mockedDocument = mock(Document.class);

            mockedJsoup.when(() -> Jsoup.parse(anyString())).thenReturn(mockedDocument);

            assertThatThrownBy(() -> propertyPageV2ScraperService.scrap(StringUtils.EMPTY))
                    .isInstanceOf(ExtendedRuntimeException.class)
                    .hasMessage("Element container not found");

            verify(mockedDocument, times(1)).selectFirst("div[data-phx-main] div.container");
            verifyNoMoreInteractions(mockedDocument);
        }
    }

}
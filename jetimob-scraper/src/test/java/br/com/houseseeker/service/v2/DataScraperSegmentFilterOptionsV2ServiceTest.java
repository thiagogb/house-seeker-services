package br.com.houseseeker.service.v2;

import br.com.houseseeker.AbstractMockWebServerTest;
import br.com.houseseeker.configuration.ObjectMapperConfiguration;
import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import br.com.houseseeker.domain.jetimob.v2.ScraperAnalysisProperties;
import br.com.houseseeker.domain.jetimob.v2.ScraperAnalysisProperties.Scope;
import br.com.houseseeker.domain.property.UrbanPropertyContract;
import br.com.houseseeker.domain.provider.ProviderMetadata;
import br.com.houseseeker.domain.provider.ProviderParameters;
import br.com.houseseeker.domain.provider.ProviderParameters.Connection;
import br.com.houseseeker.service.OkHttpClientFactoryService;
import br.com.houseseeker.service.RetrofitFactoryService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;

import static br.com.houseseeker.TestUtils.getTextFromResources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = {
        ObjectMapperConfiguration.class,
        FilterOptionsV2ScraperService.class,
        DataScraperSegmentFilterOptionsV2Service.class,
        OkHttpClientFactoryService.class,
        RetrofitFactoryService.class
})
class DataScraperSegmentFilterOptionsV2ServiceTest extends AbstractMockWebServerTest {

    private static final ProviderParameters PARAMETER_NO_RETRY = ProviderParameters.builder()
                                                                                   .connection(
                                                                                           Connection.builder()
                                                                                                     .retryCount(0)
                                                                                                     .build()
                                                                                   )
                                                                                   .build();

    private static final String DEFAULT_SEGMENT = "imoveis-urbanos-alugar";
    private static final UrbanPropertyContract DEFAULT_CONTRACT = UrbanPropertyContract.SELL;
    private static final String SAMPLE_WITH_MODAL_FULL_DATA = "samples/v2/filter/with-modal-full-data.html";

    @Autowired
    private DataScraperSegmentFilterOptionsV2Service dataScraperSegmentFilterOptionsV2Service;

    @Autowired
    private RetrofitFactoryService retrofitFactoryService;

    private ProviderMetadata providerMetadata;

    private Retrofit retrofit;

    @BeforeEach
    @Override
    public void setup() throws IOException {
        super.setup();

        providerMetadata = ProviderMetadata.builder()
                                           .siteUrl(getBaseUrl())
                                           .build();

        retrofit = retrofitFactoryService.configure(providerMetadata, PARAMETER_NO_RETRY);
    }

    @Test
    @DisplayName("given a error response when calls fetch then expects exception")
    void givenAErrorResponse_whenCallsFetch_thenExpectsException() {
        whenDispatch(recordedRequest -> new MockResponse().setResponseCode(404).setBody("page not found"));

        assertThatThrownBy(() -> dataScraperSegmentFilterOptionsV2Service.fetch(
                retrofit,
                providerMetadata,
                ScraperAnalysisProperties.DEFAULT,
                DEFAULT_SEGMENT,
                DEFAULT_CONTRACT
        )).isInstanceOf(ResponseStatusException.class)
          .hasMessage("404 NOT_FOUND \"Request failed with error: page not found\"");

        assertRecordedRequests(RecordedRequest::getPath)
                .hasSize(1)
                .containsExactly("/" + DEFAULT_SEGMENT);
    }

    @Test
    @DisplayName("given a invalid url when calls fetch then expects exception")
    void givenAInvalidUrl_whenCallsFetch_thenExpectsException() {
        providerMetadata.setDataUrl("h_t_t_p://localhost");

        assertThatThrownBy(() -> dataScraperSegmentFilterOptionsV2Service.fetch(
                retrofit,
                providerMetadata,
                ScraperAnalysisProperties.DEFAULT,
                DEFAULT_SEGMENT,
                DEFAULT_CONTRACT
        )).isInstanceOf(ExtendedRuntimeException.class)
          .hasMessage("Failed to fetch filter options for segment imoveis-urbanos-alugar");

        assertRecordedRequests(RecordedRequest::getPath).isEmpty();
    }

    @Test
    @DisplayName("given a filter page without analysis scope when calls fetch then expects")
    void givenAFilterPageWithoutAnalysisScope_whenCallsFetch_thenExpects() {
        whenDispatch(recordedRequest -> new MockResponse()
                .setResponseCode(200)
                .setBody(getTextFromResources(SAMPLE_WITH_MODAL_FULL_DATA))
        );

        var response = dataScraperSegmentFilterOptionsV2Service.fetch(
                retrofit,
                providerMetadata,
                ScraperAnalysisProperties.DEFAULT,
                DEFAULT_SEGMENT,
                DEFAULT_CONTRACT
        );

        assertThat(response)
                .hasFieldOrPropertyWithValue("contract", DEFAULT_CONTRACT)
                .hasFieldOrPropertyWithValue("cities", List.of("Agudo", "Camboriú", "Caxias do Sul", "Gramado", "Santa Maria"))
                .hasFieldOrPropertyWithValue("types", List.of("Apartamento", "Casa", "Casa Comercial", "Casa de Condomínio", "Chácara",
                                                              "Cobertura", "Sala Comercial", "Sobrado", "Sítio", "Terreno"));
    }

    @Test
    @DisplayName("given a filter page with analysis scope when calls fetch then expects")
    void givenAFilterPageWithAnalysisScope_whenCallsFetch_thenExpects() {
        var analysisScope = ScraperAnalysisProperties.builder()
                                                     .cities(
                                                             Scope.builder()
                                                                  .operation(ScraperAnalysisProperties.Operation.IN)
                                                                  .values(List.of("Santa Maria"))
                                                                  .build()
                                                     )
                                                     .subTypes(
                                                             Scope.builder()
                                                                  .operation(ScraperAnalysisProperties.Operation.NOT_IN)
                                                                  .values(List.of("Apartamento", "Casa", "Casa Comercial", "Casa de Condomínio", "Chácara"))
                                                                  .build()
                                                     )
                                                     .build();

        whenDispatch(recordedRequest -> new MockResponse()
                .setResponseCode(200)
                .setBody(getTextFromResources(SAMPLE_WITH_MODAL_FULL_DATA))
        );

        var response = dataScraperSegmentFilterOptionsV2Service.fetch(
                retrofit,
                providerMetadata,
                analysisScope,
                DEFAULT_SEGMENT,
                DEFAULT_CONTRACT
        );

        assertThat(response)
                .hasFieldOrPropertyWithValue("contract", DEFAULT_CONTRACT)
                .hasFieldOrPropertyWithValue("cities", List.of("Santa Maria"))
                .hasFieldOrPropertyWithValue("types", List.of("Cobertura", "Sala Comercial", "Sobrado", "Sítio", "Terreno"));
    }

}
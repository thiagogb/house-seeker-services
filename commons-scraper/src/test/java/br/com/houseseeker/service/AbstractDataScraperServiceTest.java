package br.com.houseseeker.service;

import br.com.houseseeker.configuration.TimeZoneConfiguration;
import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.domain.provider.ProviderMetadata;
import br.com.houseseeker.domain.provider.ProviderParameters;
import br.com.houseseeker.domain.provider.ProviderScraperResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import retrofit2.Retrofit;

import java.time.Clock;
import java.util.Collections;

import static br.com.houseseeker.domain.provider.ProviderParameters.DEFAULT;
import static br.com.houseseeker.mock.ProviderMetadataMocks.withMechanism;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TimeZoneConfiguration.class)
@ExtendWith(MockitoExtension.class)
class AbstractDataScraperServiceTest {

    private static final ProviderMetadata DEFAULT_PROVIDER = withMechanism(ProviderMechanism.JETIMOB_V1);

    private SuccessDataScraperService successDataScraperService;
    private FailDataScraperService failDataScraperService;

    @Autowired
    private Clock clock;

    @Mock
    private Retrofit retrofit;

    @BeforeEach
    void setup() {
        successDataScraperService = new SuccessDataScraperService(clock);
        failDataScraperService = new FailDataScraperService(clock);
    }

    @Test
    @DisplayName("given a service with successful execution when calls scrap then expects")
    void givenAServiceWithSuccessfulExecution_whenCallsScrap_thenExpects() {
        assertThat(successDataScraperService.scrap(DEFAULT_PROVIDER, DEFAULT, retrofit))
                .extracting("providerMetadata", "errorInfo", "extractedData")
                .containsExactly(DEFAULT_PROVIDER, null, Collections.emptyList());
    }

    @Test
    @DisplayName("given a service with fail execution when calls scrap then expects")
    void givenAServiceWithFailExecution_whenCallsScrap_thenExpects() {
        assertThat(failDataScraperService.scrap(DEFAULT_PROVIDER, DEFAULT, retrofit))
                .hasFieldOrPropertyWithValue("providerMetadata", DEFAULT_PROVIDER)
                .hasFieldOrPropertyWithValue("extractedData", Collections.emptyList())
                .extracting(ProviderScraperResponse::getErrorInfo)
                .extracting("message", "className")
                .containsExactly("Execution failure", "java.lang.RuntimeException");
    }

    private static final class SuccessDataScraperService extends AbstractDataScraperService {

        SuccessDataScraperService(Clock clock) {
            super(clock);
        }

        @Override
        protected ProviderScraperResponse execute(ProviderMetadata providerMetadata, ProviderParameters providerParameters, Retrofit retrofit) {
            return generateResponse(providerMetadata, Collections.emptyList(), result -> null);
        }

    }

    private static final class FailDataScraperService extends AbstractDataScraperService {

        FailDataScraperService(Clock clock) {
            super(clock);
        }

        @Override
        protected ProviderScraperResponse execute(ProviderMetadata providerMetadata, ProviderParameters providerParameters, Retrofit retrofit) {
            throw new RuntimeException("Execution failure");
        }

    }

}
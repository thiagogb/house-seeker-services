package br.com.houseseeker.service;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.domain.provider.ProviderScraperResponse;
import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScannerServiceIntegrationTest extends AbstractJpaIntegrationTest {

    private static final int TEST_PROVIDER = 10001;

    @Autowired
    private ScannerService scannerService;

    @MockBean
    private Clock clock;

    @Mock
    private ProviderScraperResponse.ErrorInfo mockedErrorInfo;

    @BeforeEach
    @Override
    public void setup() {
        super.setup();
        when(clock.instant()).thenReturn(Instant.parse("2024-01-01T12:30:45Z"));
        when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    }

    @Test
    @DisplayName("given a provider and creation date when calls successful then expects")
    void givenAProviderAndCreationDate_whenCallsSuccessful_thenExpects() {
        Provider provider = findProviderById(TEST_PROVIDER);
        LocalDateTime startAt = LocalDateTime.of(2024, 1, 1, 12, 0, 0);

        assertThat(scannerService.successful(provider, startAt))
                .extracting("provider", "creationDate", "endDate", "status")
                .containsExactly(
                        provider,
                        startAt,
                        LocalDateTime.of(2024, 1, 1, 12, 30, 45),
                        Scanner.ScannerStatus.SUCCESS
                );
    }

    @Test
    @DisplayName("given a provider, creation date and error info when calls failed then expects")
    void givenAProviderAndCreationDateAndErrorInfo_whenCallsFailed_thenExpects() {
        Provider provider = findProviderById(TEST_PROVIDER);
        LocalDateTime startAt = LocalDateTime.of(2024, 1, 1, 12, 0, 0);

        when(mockedErrorInfo.getClassName()).thenReturn("ExampleClass");
        when(mockedErrorInfo.getMessage()).thenReturn("Scraper failed");
        when(mockedErrorInfo.getStackTrace()).thenReturn("Example stacktrace");

        assertThat(scannerService.failed(provider, startAt, mockedErrorInfo))
                .extracting("provider", "creationDate", "endDate", "errorMessage", "stackTrace", "status")
                .containsExactly(
                        provider,
                        startAt,
                        LocalDateTime.of(2024, 1, 1, 12, 30, 45),
                        "ExampleClass: Scraper failed",
                        "Example stacktrace",
                        Scanner.ScannerStatus.FAILED
                );
    }

    @Test
    @DisplayName("given a provider, creation date and throwable when calls failed then expects")
    void givenAProviderAndCreationDateAndThrowable_whenCallsFailed_thenExpects() {
        Provider provider = findProviderById(TEST_PROVIDER);
        LocalDateTime startAt = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        RuntimeException throwable = new RuntimeException("Test exception");

        assertThat(scannerService.failed(provider, startAt, throwable))
                .satisfies(s -> assertThat(s.getStackTrace()).isNotEmpty())
                .extracting("provider", "creationDate", "endDate", "errorMessage", "status")
                .containsExactly(
                        provider,
                        startAt,
                        LocalDateTime.of(2024, 1, 1, 12, 30, 45),
                        "Test exception",
                        Scanner.ScannerStatus.FAILED
                );
    }

}
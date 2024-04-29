package br.com.houseseeker.service;

import br.com.houseseeker.domain.property.AbstractUrbanPropertyMetadata;
import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.domain.provider.ProviderMetadata;
import br.com.houseseeker.domain.provider.ProviderScraperResponse;
import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static br.com.houseseeker.mock.ProviderMetadataMocks.withMechanism;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScraperPersistenceServiceTest {

    private static final ProviderMetadata DEFAULT_PROVIDER_METADATA = withMechanism(ProviderMechanism.JETIMOB_V1);

    @InjectMocks
    private ScraperPersistenceService scraperPersistenceService;

    @Mock
    private ProviderService providerService;

    @Mock
    private ProviderDataCollectorService providerDataCollectorService;

    @Mock
    private ProviderDataMergeService providerDataMergeService;

    @Mock
    private ScannerService scannerService;

    @Mock
    private List<AbstractUrbanPropertyMetadata> mockedExtractedData;

    @Mock
    private ProviderScraperResponse.ErrorInfo mockedErrorInfo;

    @Mock
    private Provider mockedProvider;

    @Mock
    private Map<UrbanProperty, ProviderDataCollectorService.UrbanPropertyFullData> mockProviderCollectedData;

    @BeforeEach
    void setup() {
        lenient().when(mockedProvider.getName()).thenReturn("Test Provider");
    }

    @Test
    @DisplayName("given a response with non existing provider when calls persists then expects exception")
    void givenAResponseWithNonExistingProvider_whenCallsPersists_thenExpectsException() {
        ProviderScraperResponse response = ProviderScraperResponse.builder()
                                                                  .providerMetadata(DEFAULT_PROVIDER_METADATA)
                                                                  .build();

        when(providerService.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> scraperPersistenceService.persists(response))
                .isInstanceOf(AmqpRejectAndDontRequeueException.class)
                .hasMessage("Provider with id 1 not found");

        verify(providerService, times(1)).findById(1);
        verifyNoMoreInteractions(providerService);
        verifyNoInteractions(providerDataCollectorService);
        verifyNoInteractions(providerDataMergeService);
        verifyNoInteractions(scannerService);
    }

    @Test
    @DisplayName("given a response with existing provider and extracted data when calls persists without errors then expects to register successful scanner")
    void givenAResponseWithExistingProviderAndExtractedData_whenCallsPersistsWithoutErrors_thenExpectsToRegisterSuccessfulScanner() {
        LocalDateTime startAt = LocalDateTime.now();

        ProviderScraperResponse response = ProviderScraperResponse.builder()
                                                                  .providerMetadata(DEFAULT_PROVIDER_METADATA)
                                                                  .extractedData(mockedExtractedData)
                                                                  .startAt(startAt)
                                                                  .build();

        when(providerService.findById(1)).thenReturn(Optional.of(mockedProvider));
        when(providerDataCollectorService.collect(mockedProvider)).thenReturn(mockProviderCollectedData);

        scraperPersistenceService.persists(response);

        verify(providerService, times(1)).findById(1);
        verifyNoMoreInteractions(providerService);

        verify(providerDataCollectorService, times(1)).collect(mockedProvider);
        verifyNoMoreInteractions(providerDataCollectorService);

        verify(providerDataMergeService, times(1)).merge(mockedProvider, mockProviderCollectedData, mockedExtractedData);
        verifyNoMoreInteractions(providerDataMergeService);

        verify(scannerService, times(1)).successful(mockedProvider, startAt);
        verifyNoMoreInteractions(scannerService);
    }

    @Test
    @DisplayName("given a response with existing provider and extracted data when calls persists with errors then expects to register failed scanner")
    void givenAResponseWithExistingProviderAndExtractedData_whenCallsPersistsWithErrors_thenExpectsToRegisterFailedScanner() {
        LocalDateTime startAt = LocalDateTime.now();
        RuntimeException throwable = new RuntimeException("Failed to collect data");

        ProviderScraperResponse response = ProviderScraperResponse.builder()
                                                                  .providerMetadata(DEFAULT_PROVIDER_METADATA)
                                                                  .extractedData(mockedExtractedData)
                                                                  .startAt(startAt)
                                                                  .build();

        when(providerService.findById(1)).thenReturn(Optional.of(mockedProvider));
        doThrow(throwable).when(providerDataCollectorService).collect(mockedProvider);

        scraperPersistenceService.persists(response);

        verify(providerService, times(1)).findById(1);
        verifyNoMoreInteractions(providerService);

        verify(providerDataCollectorService, times(1)).collect(mockedProvider);
        verifyNoMoreInteractions(providerDataCollectorService);

        verifyNoInteractions(providerDataMergeService);

        verify(scannerService, times(1)).failed(mockedProvider, startAt, throwable);
        verifyNoMoreInteractions(scannerService);
    }

    @Test
    @DisplayName("given a response with error on scrap when calls persists then expects to register failed scanner")
    void givenAResponseWithErrorOnScrap_whenCallsPersists_thenExpectsToRegisterFailedScanner() {
        LocalDateTime startAt = LocalDateTime.now();

        ProviderScraperResponse response = ProviderScraperResponse.builder()
                                                                  .providerMetadata(DEFAULT_PROVIDER_METADATA)
                                                                  .errorInfo(mockedErrorInfo)
                                                                  .startAt(startAt)
                                                                  .build();

        when(providerService.findById(1)).thenReturn(Optional.of(mockedProvider));

        scraperPersistenceService.persists(response);

        verify(providerService, times(1)).findById(1);
        verifyNoMoreInteractions(providerService);

        verifyNoInteractions(providerDataCollectorService);
        verifyNoInteractions(providerDataMergeService);

        verify(scannerService, times(1)).failed(mockedProvider, startAt, mockedErrorInfo);
        verifyNoMoreInteractions(scannerService);
    }

}
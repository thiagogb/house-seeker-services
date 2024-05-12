package br.com.houseseeker.service.messaging;

import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.entity.UrbanPropertyConvenience;
import br.com.houseseeker.entity.UrbanPropertyLocation;
import br.com.houseseeker.entity.UrbanPropertyMeasure;
import br.com.houseseeker.entity.UrbanPropertyMedia;
import br.com.houseseeker.entity.UrbanPropertyPriceVariation;
import br.com.houseseeker.service.UrbanPropertyConvenienceService;
import br.com.houseseeker.service.UrbanPropertyLocationService;
import br.com.houseseeker.service.UrbanPropertyMeasureService;
import br.com.houseseeker.service.UrbanPropertyMediaService;
import br.com.houseseeker.service.UrbanPropertyPriceVariationService;
import br.com.houseseeker.service.UrbanPropertyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProviderDataCollectorServiceTest {

    @InjectMocks
    private ProviderDataCollectorService providerDataCollectorService;

    @Mock
    private UrbanPropertyService urbanPropertyService;

    @Mock
    private UrbanPropertyConvenienceService urbanPropertyConvenienceService;

    @Mock
    private UrbanPropertyLocationService urbanPropertyLocationService;

    @Mock
    private UrbanPropertyMeasureService urbanPropertyMeasureService;

    @Mock
    private UrbanPropertyMediaService urbanPropertyMediaService;

    @Mock
    private UrbanPropertyPriceVariationService urbanPropertyPriceVariationService;

    @Mock
    private Provider mockedProvider;

    @Test
    @DisplayName("given a provider without associated data when calls collect then expects")
    void givenAProviderWithoutAssociatedData_whenCallsCollect_thenExpects() {
        when(urbanPropertyService.findAllByProvider(mockedProvider)).thenReturn(Collections.emptyList());
        when(urbanPropertyConvenienceService.findAllByProvider(mockedProvider)).thenReturn(Collections.emptyList());
        when(urbanPropertyLocationService.findAllByProvider(mockedProvider)).thenReturn(Collections.emptyList());
        when(urbanPropertyMeasureService.findAllByProvider(mockedProvider)).thenReturn(Collections.emptyList());
        when(urbanPropertyMediaService.findAllByProvider(mockedProvider)).thenReturn(Collections.emptyList());
        when(urbanPropertyPriceVariationService.findAllByProvider(mockedProvider)).thenReturn(Collections.emptyList());

        assertThat(providerDataCollectorService.collect(mockedProvider)).isEmpty();

        verifyServiceCalls();
    }

    @Test
    @DisplayName("given a provider with associated data when calls collect then expects")
    void givenAProviderWithAssociatedData_whenCallsCollect_thenExpects() {
        UrbanProperty urbanProperty1 = mock(UrbanProperty.class);
        UrbanProperty urbanProperty2 = mock(UrbanProperty.class);
        UrbanPropertyConvenience urbanPropertyConvenience1 = mock(UrbanPropertyConvenience.class);
        UrbanPropertyConvenience urbanPropertyConvenience2 = mock(UrbanPropertyConvenience.class);
        UrbanPropertyLocation urbanPropertyLocation1 = mock(UrbanPropertyLocation.class);
        UrbanPropertyLocation urbanPropertyLocation2 = mock(UrbanPropertyLocation.class);
        UrbanPropertyMeasure urbanPropertyMeasure1 = mock(UrbanPropertyMeasure.class);
        UrbanPropertyMeasure urbanPropertyMeasure2 = mock(UrbanPropertyMeasure.class);
        UrbanPropertyMedia urbanPropertyMedia1 = mock(UrbanPropertyMedia.class);
        UrbanPropertyMedia urbanPropertyMedia2 = mock(UrbanPropertyMedia.class);
        UrbanPropertyPriceVariation urbanPropertyPriceVariation1 = mock(UrbanPropertyPriceVariation.class);
        UrbanPropertyPriceVariation urbanPropertyPriceVariation2 = mock(UrbanPropertyPriceVariation.class);

        when(urbanPropertyConvenience1.getUrbanProperty()).thenReturn(urbanProperty1);
        when(urbanPropertyConvenience2.getUrbanProperty()).thenReturn(urbanProperty2);

        when(urbanPropertyLocation1.getUrbanProperty()).thenReturn(urbanProperty1);
        when(urbanPropertyLocation2.getUrbanProperty()).thenReturn(urbanProperty2);

        when(urbanPropertyMeasure1.getUrbanProperty()).thenReturn(urbanProperty1);
        when(urbanPropertyMeasure2.getUrbanProperty()).thenReturn(urbanProperty2);

        when(urbanPropertyMedia1.getUrbanProperty()).thenReturn(urbanProperty1);
        when(urbanPropertyMedia2.getUrbanProperty()).thenReturn(urbanProperty2);

        when(urbanPropertyPriceVariation1.getUrbanProperty()).thenReturn(urbanProperty1);
        when(urbanPropertyPriceVariation2.getUrbanProperty()).thenReturn(urbanProperty2);

        when(urbanPropertyService.findAllByProvider(mockedProvider))
                .thenReturn(List.of(urbanProperty1, urbanProperty2));
        when(urbanPropertyConvenienceService.findAllByProvider(mockedProvider))
                .thenReturn(List.of(urbanPropertyConvenience1, urbanPropertyConvenience2));
        when(urbanPropertyLocationService.findAllByProvider(mockedProvider))
                .thenReturn(List.of(urbanPropertyLocation1, urbanPropertyLocation2));
        when(urbanPropertyMeasureService.findAllByProvider(mockedProvider))
                .thenReturn(List.of(urbanPropertyMeasure1, urbanPropertyMeasure2));
        when(urbanPropertyMediaService.findAllByProvider(mockedProvider))
                .thenReturn(List.of(urbanPropertyMedia1, urbanPropertyMedia2));
        when(urbanPropertyPriceVariationService.findAllByProvider(mockedProvider))
                .thenReturn(List.of(urbanPropertyPriceVariation1, urbanPropertyPriceVariation2));

        assertThat(providerDataCollectorService.collect(mockedProvider))
                .containsOnlyKeys(urbanProperty1, urbanProperty2)
                .extractingByKeys(urbanProperty1, urbanProperty2)
                .extracting("location", "measure", "conveniences", "medias", "priceVariations")
                .containsExactly(
                        tuple(
                                urbanPropertyLocation1,
                                urbanPropertyMeasure1,
                                List.of(urbanPropertyConvenience1),
                                List.of(urbanPropertyMedia1),
                                List.of(urbanPropertyPriceVariation1)
                        ),
                        tuple(
                                urbanPropertyLocation2,
                                urbanPropertyMeasure2,
                                List.of(urbanPropertyConvenience2),
                                List.of(urbanPropertyMedia2),
                                List.of(urbanPropertyPriceVariation2)
                        )
                );

        verifyServiceCalls();
    }

    private void verifyServiceCalls() {
        verify(urbanPropertyService, times(1)).findAllByProvider(mockedProvider);
        verifyNoMoreInteractions(urbanPropertyService);

        verify(urbanPropertyConvenienceService, times(1)).findAllByProvider(mockedProvider);
        verifyNoMoreInteractions(urbanPropertyConvenienceService);

        verify(urbanPropertyLocationService, times(1)).findAllByProvider(mockedProvider);
        verifyNoMoreInteractions(urbanPropertyLocationService);

        verify(urbanPropertyMeasureService, times(1)).findAllByProvider(mockedProvider);
        verifyNoMoreInteractions(urbanPropertyMeasureService);

        verify(urbanPropertyMediaService, times(1)).findAllByProvider(mockedProvider);
        verifyNoMoreInteractions(urbanPropertyMediaService);

        verify(urbanPropertyPriceVariationService, times(1)).findAllByProvider(mockedProvider);
        verifyNoMoreInteractions(urbanPropertyPriceVariationService);
    }

}
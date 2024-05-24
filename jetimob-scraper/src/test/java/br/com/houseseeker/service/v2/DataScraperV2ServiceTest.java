package br.com.houseseeker.service.v2;

import br.com.houseseeker.configuration.ObjectMapperConfiguration;
import br.com.houseseeker.configuration.TimeZoneConfiguration;
import br.com.houseseeker.domain.jetimob.v2.FilterOptionsMetadata;
import br.com.houseseeker.domain.jetimob.v2.PropertyInfoMetadata;
import br.com.houseseeker.domain.jetimob.v2.ScraperAnalysisProperties;
import br.com.houseseeker.domain.jetimob.v2.SearchPageMetadata;
import br.com.houseseeker.domain.property.UrbanPropertyContract;
import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.domain.provider.ProviderMetadata;
import br.com.houseseeker.domain.provider.ProviderParameters;
import br.com.houseseeker.domain.provider.ProviderScraperResponse;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import retrofit2.Retrofit;

import java.util.List;

import static br.com.houseseeker.mock.ProviderMetadataMocks.withMechanism;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {
        TimeZoneConfiguration.class,
        ObjectMapperConfiguration.class,
        DataScraperV2Service.class,
        PropertyMetadataMergeV2Service.class,
        MetadataTransferV2Service.class
})
@ExtendWith(MockitoExtension.class)
class DataScraperV2ServiceTest {

    private static final ProviderMetadata PROVIDER_METADATA = withMechanism(ProviderMechanism.JETIMOB_V1);

    @Autowired
    private DataScraperV2Service dataScraperV2Service;

    @MockBean
    private DataScraperSegmentFilterOptionsV2Service dataScraperSegmentFilterOptionsV2Service;

    @MockBean
    private DataScraperSegmentSearchV2Service dataScraperSegmentSearchV2Service;

    @MockBean
    private DataScraperSegmentPropertyV2Service dataScraperSegmentPropertyV2Service;

    @Mock
    private Retrofit retrofit;

    @Test
    @DisplayName("given a default analysis scope when calls scrap then expects")
    void givenADefaultAnalysisScope_whenCallsScrap_thenExpects() {
        var filterOptions = FilterOptionsMetadata.builder()
                                                 .cities(List.of("Santa Maria"))
                                                 .types(List.of("Casa"))
                                                 .build();

        var searchResultsSegment1 = List.of(
                SearchPageMetadata.Item.builder()
                                       .providerCode("C01")
                                       .build()
        );

        var searchResultsSegment2 = List.of(
                SearchPageMetadata.Item.builder()
                                       .providerCode("C02")
                                       .build()
        );

        var searchResultsSegment3 = List.of(
                SearchPageMetadata.Item.builder()
                                       .providerCode("C03")
                                       .build()
        );

        var searchResultsSegment4 = List.of(
                SearchPageMetadata.Item.builder()
                                       .providerCode("C04")
                                       .build()
        );

        when(dataScraperSegmentFilterOptionsV2Service.fetch(eq(retrofit), eq(PROVIDER_METADATA), eq(ScraperAnalysisProperties.DEFAULT), anyString(), any()))
                .thenReturn(filterOptions);

        when(dataScraperSegmentSearchV2Service.fetch(PROVIDER_METADATA, "imoveis-plus-comprar", filterOptions))
                .thenReturn(searchResultsSegment1);

        when(dataScraperSegmentSearchV2Service.fetch(PROVIDER_METADATA, "imoveis-plus-alugar", filterOptions))
                .thenReturn(searchResultsSegment2);

        when(dataScraperSegmentSearchV2Service.fetch(PROVIDER_METADATA, "imoveis-urbanos-comprar", filterOptions))
                .thenReturn(searchResultsSegment3);

        when(dataScraperSegmentSearchV2Service.fetch(PROVIDER_METADATA, "imoveis-urbanos-alugar", filterOptions))
                .thenReturn(searchResultsSegment4);

        when(dataScraperSegmentPropertyV2Service.fetch(eq(retrofit), any(), eq(searchResultsSegment1)))
                .thenReturn(List.of(
                        PropertyInfoMetadata.builder()
                                            .providerCode("C01")
                                            .build()
                ));

        when(dataScraperSegmentPropertyV2Service.fetch(eq(retrofit), any(), eq(searchResultsSegment2)))
                .thenReturn(List.of(
                        PropertyInfoMetadata.builder()
                                            .providerCode("C02")
                                            .build()
                ));

        when(dataScraperSegmentPropertyV2Service.fetch(eq(retrofit), any(), eq(searchResultsSegment3)))
                .thenReturn(List.of(
                        PropertyInfoMetadata.builder()
                                            .providerCode("C03")
                                            .build()
                ));

        when(dataScraperSegmentPropertyV2Service.fetch(eq(retrofit), any(), eq(searchResultsSegment4)))
                .thenReturn(List.of(
                        PropertyInfoMetadata.builder()
                                            .providerCode("C04")
                                            .build()
                ));

        assertThat(dataScraperV2Service.scrap(PROVIDER_METADATA, ProviderParameters.DEFAULT, retrofit))
                .extracting(ProviderScraperResponse::getExtractedData, InstanceOfAssertFactories.LIST)
                .hasSize(4)
                .extracting("providerCode")
                .containsExactlyInAnyOrder("C01", "C02", "C03", "C04");

        verify(dataScraperSegmentFilterOptionsV2Service, times(1)).fetch(
                eq(retrofit),
                eq(PROVIDER_METADATA),
                eq(ScraperAnalysisProperties.DEFAULT),
                eq("imoveis-plus-comprar"),
                eq(UrbanPropertyContract.SELL)
        );
        verify(dataScraperSegmentFilterOptionsV2Service, times(1)).fetch(
                eq(retrofit),
                eq(PROVIDER_METADATA),
                eq(ScraperAnalysisProperties.DEFAULT),
                eq("imoveis-plus-alugar"),
                eq(UrbanPropertyContract.RENT)
        );
        verify(dataScraperSegmentFilterOptionsV2Service, times(1)).fetch(
                eq(retrofit),
                eq(PROVIDER_METADATA),
                eq(ScraperAnalysisProperties.DEFAULT),
                eq("imoveis-urbanos-comprar"),
                eq(UrbanPropertyContract.SELL)
        );
        verify(dataScraperSegmentFilterOptionsV2Service, times(1)).fetch(
                eq(retrofit),
                eq(PROVIDER_METADATA),
                eq(ScraperAnalysisProperties.DEFAULT),
                eq("imoveis-urbanos-alugar"),
                eq(UrbanPropertyContract.RENT)
        );
        verifyNoMoreInteractions(dataScraperSegmentFilterOptionsV2Service);

        verify(dataScraperSegmentSearchV2Service, times(1)).fetch(
                PROVIDER_METADATA, "imoveis-plus-comprar", filterOptions
        );
        verify(dataScraperSegmentSearchV2Service, times(1)).fetch(
                PROVIDER_METADATA, "imoveis-plus-alugar", filterOptions
        );
        verify(dataScraperSegmentSearchV2Service, times(1)).fetch(
                PROVIDER_METADATA, "imoveis-urbanos-comprar", filterOptions
        );
        verify(dataScraperSegmentSearchV2Service, times(1)).fetch(
                PROVIDER_METADATA, "imoveis-urbanos-alugar", filterOptions
        );
        verifyNoMoreInteractions(dataScraperSegmentSearchV2Service);

        verify(dataScraperSegmentPropertyV2Service, times(1)).fetch(eq(retrofit), any(), eq(searchResultsSegment1));
        verify(dataScraperSegmentPropertyV2Service, times(1)).fetch(eq(retrofit), any(), eq(searchResultsSegment2));
        verify(dataScraperSegmentPropertyV2Service, times(1)).fetch(eq(retrofit), any(), eq(searchResultsSegment3));
        verify(dataScraperSegmentPropertyV2Service, times(1)).fetch(eq(retrofit), any(), eq(searchResultsSegment4));
        verifyNoMoreInteractions(dataScraperSegmentPropertyV2Service);
    }

}
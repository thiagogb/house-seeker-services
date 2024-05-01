package br.com.houseseeker.service.calculator;

import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.entity.UrbanPropertyPriceVariation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static br.com.houseseeker.entity.UrbanPropertyPriceVariation.Type.CONDOMINIUM;
import static br.com.houseseeker.entity.UrbanPropertyPriceVariation.Type.RENT;
import static br.com.houseseeker.entity.UrbanPropertyPriceVariation.Type.SELL;
import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = PriceVariationCalculator.class)
@ExtendWith(MockitoExtension.class)
class PriceVariationCalculatorTest {

    private static final String[] EXTRACTED_ATTRIBUTES = new String[]{"analysisDate", "type", "price", "variation"};
    private static final LocalDateTime CLOCK_AT_ANALYSIS_DATE = LocalDateTime.of(2024, 1, 7, 12, 30, 45);

    @Autowired
    private PriceVariationCalculator priceVariationCalculator;

    @MockBean
    private Clock clock;

    @Mock
    private UrbanProperty mockedUrbanProperty;

    @BeforeEach
    void setup() {
        when(clock.instant()).thenReturn(Instant.parse("2024-01-07T12:30:45Z"));
        when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    }

    @Test
    @DisplayName("given a property with all values null and without existing pricing variations when calls calculate then expects")
    void givenAPropertyWithAllValuesNullAndWithoutExistingPricingVariations_whenCallsCalculate_thenExpects() {
        List<UrbanPropertyPriceVariation> priceVariations = Collections.emptyList();

        when(mockedUrbanProperty.getSellPrice()).thenReturn(null);
        when(mockedUrbanProperty.getRentPrice()).thenReturn(null);
        when(mockedUrbanProperty.getCondominiumPrice()).thenReturn(null);

        assertThat(priceVariationCalculator.calculate(mockedUrbanProperty, priceVariations)).isEmpty();

        verifyInteractions();
    }

    @Test
    @DisplayName("given a property with all values zero and without existing pricing variations when calls calculate then expects")
    void givenAPropertyWithAllValuesZeroAndWithoutExistingPricingVariations_whenCallsCalculate_thenExpects() {
        List<UrbanPropertyPriceVariation> priceVariations = Collections.emptyList();

        when(mockedUrbanProperty.getSellPrice()).thenReturn(ZERO);
        when(mockedUrbanProperty.getRentPrice()).thenReturn(ZERO);
        when(mockedUrbanProperty.getCondominiumPrice()).thenReturn(ZERO);

        assertThat(priceVariationCalculator.calculate(mockedUrbanProperty, priceVariations)).isEmpty();

        verifyInteractions();
    }

    @Test
    @DisplayName("given a property with only sell value and with unmodified pricing variations when calls calculate then expects")
    void givenAPropertyWithOnlySellValueAndWithUnmodifiedPricingVariations_whenCallsCalculate_thenExpects() {
        LocalDateTime clockAt_20240101_123045 = LocalDateTime.of(2024, 1, 1, 12, 30, 45);
        LocalDateTime clockAt_20240102_123045 = LocalDateTime.of(2024, 1, 2, 12, 30, 45);
        List<UrbanPropertyPriceVariation> priceVariations = List.of(
                UrbanPropertyPriceVariation.builder()
                                           .urbanProperty(mockedUrbanProperty)
                                           .analysisDate(clockAt_20240101_123045)
                                           .type(SELL)
                                           .price(valueOf(100000))
                                           .variation(ZERO)
                                           .build(),
                UrbanPropertyPriceVariation.builder()
                                           .urbanProperty(mockedUrbanProperty)
                                           .analysisDate(clockAt_20240102_123045)
                                           .type(SELL)
                                           .price(valueOf(200000))
                                           .variation(valueOf(100))
                                           .build()
        );

        when(mockedUrbanProperty.getSellPrice()).thenReturn(valueOf(200000));
        when(mockedUrbanProperty.getRentPrice()).thenReturn(null);
        when(mockedUrbanProperty.getCondominiumPrice()).thenReturn(null);

        assertThat(priceVariationCalculator.calculate(mockedUrbanProperty, priceVariations))
                .extracting(EXTRACTED_ATTRIBUTES)
                .containsExactlyInAnyOrder(
                        tuple(clockAt_20240101_123045, SELL, valueOf(100000), ZERO),
                        tuple(clockAt_20240102_123045, SELL, valueOf(200000), valueOf(100))
                );

        verifyInteractions();
    }

    @Test
    @DisplayName("given a property with only sell value and with empty pricing variations when calls calculate then expects")
    void givenAPropertyWithOnlySellValueAndWithEmptyPricingVariations_whenCallsCalculate_thenExpects() {
        List<UrbanPropertyPriceVariation> priceVariations = Collections.emptyList();

        when(mockedUrbanProperty.getSellPrice()).thenReturn(valueOf(200000));
        when(mockedUrbanProperty.getRentPrice()).thenReturn(null);
        when(mockedUrbanProperty.getCondominiumPrice()).thenReturn(null);

        assertThat(priceVariationCalculator.calculate(mockedUrbanProperty, priceVariations))
                .extracting(EXTRACTED_ATTRIBUTES)
                .containsExactlyInAnyOrder(tuple(CLOCK_AT_ANALYSIS_DATE, SELL, valueOf(200000), ZERO));

        verifyInteractions();
    }

    @Test
    @DisplayName("given a property with only sell value and with modified pricing variations when calls calculate then expects")
    void givenAPropertyWithOnlySellValueAndWithModifiedPricingVariations_whenCallsCalculate_thenExpects() {
        LocalDateTime clockAt_20240101_123045 = LocalDateTime.of(2024, 1, 1, 12, 30, 45);
        LocalDateTime clockAt_20240102_123045 = LocalDateTime.of(2024, 1, 2, 12, 30, 45);
        List<UrbanPropertyPriceVariation> priceVariations = List.of(
                UrbanPropertyPriceVariation.builder()
                                           .urbanProperty(mockedUrbanProperty)
                                           .analysisDate(clockAt_20240101_123045)
                                           .type(SELL)
                                           .price(valueOf(100000))
                                           .variation(ZERO)
                                           .build(),
                UrbanPropertyPriceVariation.builder()
                                           .urbanProperty(mockedUrbanProperty)
                                           .analysisDate(clockAt_20240102_123045)
                                           .type(SELL)
                                           .price(valueOf(200000))
                                           .variation(valueOf(100))
                                           .build()
        );

        when(mockedUrbanProperty.getSellPrice()).thenReturn(valueOf(175000));
        when(mockedUrbanProperty.getRentPrice()).thenReturn(null);
        when(mockedUrbanProperty.getCondominiumPrice()).thenReturn(null);

        assertThat(priceVariationCalculator.calculate(mockedUrbanProperty, priceVariations))
                .extracting(EXTRACTED_ATTRIBUTES)
                .containsExactlyInAnyOrder(
                        tuple(clockAt_20240101_123045, SELL, valueOf(100000), ZERO),
                        tuple(clockAt_20240102_123045, SELL, valueOf(200000), valueOf(100)),
                        tuple(CLOCK_AT_ANALYSIS_DATE, SELL, valueOf(175000), new BigDecimal("-12.50"))
                );

        verifyInteractions();
    }

    @Test
    @DisplayName("given a property with only sell value and with modified unordered pricing variations when calls calculate then expects")
    void givenAPropertyWithOnlySellValueAndWithModifiedUnorderedPricingVariations_whenCallsCalculate_thenExpects() {
        LocalDateTime clockAt_20240101_123045 = LocalDateTime.of(2024, 1, 1, 12, 30, 45);
        LocalDateTime clockAt_20240102_123045 = LocalDateTime.of(2024, 1, 2, 12, 30, 45);
        List<UrbanPropertyPriceVariation> priceVariations = List.of(
                UrbanPropertyPriceVariation.builder()
                                           .urbanProperty(mockedUrbanProperty)
                                           .analysisDate(clockAt_20240102_123045)
                                           .type(SELL)
                                           .price(valueOf(200000))
                                           .variation(valueOf(100))
                                           .build(),
                UrbanPropertyPriceVariation.builder()
                                           .urbanProperty(mockedUrbanProperty)
                                           .analysisDate(clockAt_20240101_123045)
                                           .type(SELL)
                                           .price(valueOf(100000))
                                           .variation(ZERO)
                                           .build()
        );

        when(mockedUrbanProperty.getSellPrice()).thenReturn(valueOf(225000));
        when(mockedUrbanProperty.getRentPrice()).thenReturn(null);
        when(mockedUrbanProperty.getCondominiumPrice()).thenReturn(null);

        assertThat(priceVariationCalculator.calculate(mockedUrbanProperty, priceVariations))
                .extracting(EXTRACTED_ATTRIBUTES)
                .containsExactlyInAnyOrder(
                        tuple(clockAt_20240101_123045, SELL, valueOf(100000), ZERO),
                        tuple(clockAt_20240102_123045, SELL, valueOf(200000), valueOf(100)),
                        tuple(CLOCK_AT_ANALYSIS_DATE, SELL, valueOf(225000), new BigDecimal("12.50"))
                );

        verifyInteractions();
    }

    @Test
    @DisplayName("given a property with all values greater than zero and with modified pricing variations when calls calculate then expects")
    void givenAPropertyWithAllValuesGreaterThanZeroAndWithModifiedPricingVariations_whenCallsCalculate_thenExpects() {
        LocalDateTime clockAt_20240101_123045 = LocalDateTime.of(2024, 1, 1, 12, 30, 45);
        LocalDateTime clockAt_20240102_123045 = LocalDateTime.of(2024, 1, 2, 12, 30, 45);
        List<UrbanPropertyPriceVariation> priceVariations = List.of(
                UrbanPropertyPriceVariation.builder()
                                           .urbanProperty(mockedUrbanProperty)
                                           .analysisDate(clockAt_20240101_123045)
                                           .type(SELL)
                                           .price(valueOf(100000))
                                           .variation(ZERO)
                                           .build(),
                UrbanPropertyPriceVariation.builder()
                                           .urbanProperty(mockedUrbanProperty)
                                           .analysisDate(clockAt_20240101_123045)
                                           .type(RENT)
                                           .price(valueOf(2000))
                                           .variation(ZERO)
                                           .build(),
                UrbanPropertyPriceVariation.builder()
                                           .urbanProperty(mockedUrbanProperty)
                                           .analysisDate(clockAt_20240101_123045)
                                           .type(CONDOMINIUM)
                                           .price(valueOf(350))
                                           .variation(ZERO)
                                           .build(),
                UrbanPropertyPriceVariation.builder()
                                           .urbanProperty(mockedUrbanProperty)
                                           .analysisDate(clockAt_20240102_123045)
                                           .type(SELL)
                                           .price(valueOf(200000))
                                           .variation(valueOf(100))
                                           .build(),
                UrbanPropertyPriceVariation.builder()
                                           .urbanProperty(mockedUrbanProperty)
                                           .analysisDate(clockAt_20240102_123045)
                                           .type(CONDOMINIUM)
                                           .price(valueOf(375))
                                           .variation(valueOf(7.14))
                                           .build()
        );

        when(mockedUrbanProperty.getSellPrice()).thenReturn(valueOf(250000));
        when(mockedUrbanProperty.getRentPrice()).thenReturn(valueOf(2125.33));
        when(mockedUrbanProperty.getCondominiumPrice()).thenReturn(valueOf(412.30));

        assertThat(priceVariationCalculator.calculate(mockedUrbanProperty, priceVariations))
                .extracting(EXTRACTED_ATTRIBUTES)
                .containsExactlyInAnyOrder(
                        tuple(clockAt_20240101_123045, RENT, valueOf(2000), ZERO),
                        tuple(CLOCK_AT_ANALYSIS_DATE, RENT, valueOf(2125.33), valueOf(6.27)),
                        tuple(clockAt_20240101_123045, SELL, valueOf(100000), ZERO),
                        tuple(clockAt_20240102_123045, SELL, valueOf(200000), valueOf(100)),
                        tuple(CLOCK_AT_ANALYSIS_DATE, SELL, valueOf(250000), new BigDecimal("25.00")),
                        tuple(clockAt_20240101_123045, CONDOMINIUM, valueOf(350), ZERO),
                        tuple(clockAt_20240102_123045, CONDOMINIUM, valueOf(375), valueOf(7.14)),
                        tuple(CLOCK_AT_ANALYSIS_DATE, CONDOMINIUM, valueOf(412.3), valueOf(9.95))
                );

        verifyInteractions();
    }

    private void verifyInteractions() {
        verify(mockedUrbanProperty, atLeast(1)).getSellPrice();
        verify(mockedUrbanProperty, atLeast(1)).getRentPrice();
        verify(mockedUrbanProperty, atLeast(1)).getCondominiumPrice();
        verifyNoMoreInteractions(mockedUrbanProperty);
    }

}
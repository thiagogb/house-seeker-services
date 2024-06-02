package br.com.houseseeker.service.calculator;

import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.entity.UrbanPropertyPriceVariation;
import br.com.houseseeker.domain.property.UrbanPropertyPriceVariationType;
import br.com.houseseeker.util.BigDecimalUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ZERO;
import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
@Slf4j
public class PriceVariationCalculatorService {

    private final Clock clock;

    public List<UrbanPropertyPriceVariation> calculate(
            @NotNull UrbanProperty urbanProperty,
            @NotNull List<UrbanPropertyPriceVariation> priceVariations
    ) {
        Map<UrbanPropertyPriceVariationType, List<UrbanPropertyPriceVariation>> priceVariationsMap = groupByTypeOrderByAnalysisDate(priceVariations);
        for (var type : UrbanPropertyPriceVariationType.values()) {
            List<UrbanPropertyPriceVariation> currentVariations = priceVariationsMap.getOrDefault(type, Collections.emptyList());

            if (!hasPriceVariationsSinceLastAnalysisDate(currentVariations, getComparatorAttribute(type, urbanProperty)))
                continue;

            priceVariationsMap.computeIfAbsent(type, key -> new LinkedList<>())
                              .add(registerNewPriceVariation(urbanProperty, currentVariations, type));
        }
        return priceVariationsMap.values().stream().flatMap(Collection::stream).toList();
    }

    private BigDecimal getComparatorAttribute(UrbanPropertyPriceVariationType type, UrbanProperty urbanProperty) {
        return switch (type) {
            case SELL -> urbanProperty.getSellPrice();
            case RENT -> urbanProperty.getRentPrice();
            case CONDOMINIUM -> urbanProperty.getCondominiumPrice();
        };
    }

    private Map<UrbanPropertyPriceVariationType, List<UrbanPropertyPriceVariation>> groupByTypeOrderByAnalysisDate(
            List<UrbanPropertyPriceVariation> priceVariations
    ) {
        return priceVariations.stream()
                              .collect(Collectors.groupingBy(
                                      UrbanPropertyPriceVariation::getType,
                                      Collectors.collectingAndThen(Collectors.toCollection(LinkedList::new), this::sortByAnalysisDate)
                              ));
    }

    private List<UrbanPropertyPriceVariation> sortByAnalysisDate(List<UrbanPropertyPriceVariation> priceVariations) {
        priceVariations.sort(Comparator.comparing(UrbanPropertyPriceVariation::getAnalysisDate));
        return priceVariations;
    }

    private boolean hasPriceVariationsSinceLastAnalysisDate(List<UrbanPropertyPriceVariation> priceVariations, BigDecimal currentPrice) {
        if (isNull(currentPrice) || BigDecimalUtils.isZero(currentPrice))
            return false;

        if (priceVariations.isEmpty())
            return true;

        return !BigDecimalUtils.isEqual(priceVariations.getLast().getPrice(), currentPrice);
    }

    private UrbanPropertyPriceVariation registerNewPriceVariation(
            UrbanProperty urbanProperty,
            List<UrbanPropertyPriceVariation> priceVariations,
            UrbanPropertyPriceVariationType type
    ) {
        BigDecimal previousPrice = !priceVariations.isEmpty() ? priceVariations.getLast().getPrice() : ZERO;
        BigDecimal currentPrice = getComparatorAttribute(type, urbanProperty);
        BigDecimal variation = !BigDecimalUtils.isZero(previousPrice)
                ? BigDecimalUtils.divideAndRoundByTwo(currentPrice.multiply(BigDecimalUtils.ONE_HUNDRED), previousPrice)
                                 .subtract(BigDecimalUtils.ONE_HUNDRED)
                : ZERO;
        return UrbanPropertyPriceVariation.builder()
                                          .urbanProperty(urbanProperty)
                                          .analysisDate(LocalDateTime.now(clock))
                                          .type(type)
                                          .price(currentPrice)
                                          .variation(variation)
                                          .build();
    }

}

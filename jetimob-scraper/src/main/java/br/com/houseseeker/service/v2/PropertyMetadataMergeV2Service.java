package br.com.houseseeker.service.v2;

import br.com.houseseeker.domain.jetimob.v2.PropertyInfoMetadata;
import br.com.houseseeker.domain.jetimob.v2.PropertyInfoMetadata.Pricing;
import br.com.houseseeker.util.BigDecimalUtils;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static br.com.houseseeker.util.ConverterUtils.tryToBigDecimalPtBR;
import static br.com.houseseeker.util.StringUtils.keepOnlyNumericSymbols;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@Slf4j
public class PropertyMetadataMergeV2Service {

    public List<PropertyInfoMetadata> merge(@NotNull List<PropertyInfoMetadata> properties) {
        return properties.stream()
                         .collect(Collectors.toMap(
                                 PropertyInfoMetadata::getProviderCode,
                                 Function.identity(),
                                 this::mergePropertyMetadata
                         ))
                         .values()
                         .stream()
                         .toList();
    }

    private PropertyInfoMetadata mergePropertyMetadata(PropertyInfoMetadata first, PropertyInfoMetadata second) {
        if (first.equals(second))
            return first;

        return first.toBuilder()
                    .pricing(mergePricingMetadata(first.getPricing(), second.getPricing()))
                    .build();
    }

    private Pricing mergePricingMetadata(Pricing first, Pricing second) {
        return first.toBuilder()
                    .sellPrice(mergePriceValue(first.getSellPrice(), second.getSellPrice()))
                    .rentPrice(mergePriceValue(first.getRentPrice(), second.getRentPrice()))
                    .condominiumPrice(mergePriceValue(first.getCondominiumPrice(), second.getCondominiumPrice()))
                    .build();
    }

    private String mergePriceValue(String first, String second) {
        if (isBlank(first) && isBlank(second))
            return null;

        if (isNotBlank(first) && isBlank(second))
            return first;

        if (isBlank(first) && isNotBlank(second))
            return second;

        BigDecimal firstValue = tryToBigDecimalPtBR(keepOnlyNumericSymbols(first)).orElse(BigDecimal.ZERO);
        BigDecimal secondValue = tryToBigDecimalPtBR(keepOnlyNumericSymbols(second)).orElse(BigDecimal.ZERO);

        return mergePriceValue(Pair.of(first, firstValue), Pair.of(second, secondValue));
    }

    private String mergePriceValue(Pair<String, BigDecimal> first, Pair<String, BigDecimal> second) {
        return BigDecimalUtils.isGreaterOrEqual(first.getValue(), second.getValue())
                ? first.getKey()
                : second.getKey();
    }

}

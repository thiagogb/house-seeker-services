package br.com.houseseeker.service.v2;

import br.com.houseseeker.domain.jetimob.v2.PropertyInfoMetadata;
import br.com.houseseeker.domain.jetimob.v2.PropertyInfoMetadata.Pricing;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = PropertyMetadataMergeV2Service.class)
class PropertyMetadataMergeV2ServiceTest {

    private static final String DEFAULT_PROVIDER_CODE = "12345";

    @Autowired
    private PropertyMetadataMergeV2Service propertyMetadataMergeV2Service;

    @Test
    @DisplayName("given a duplicated property with same data when calls merge then expects")
    void givenADuplicatedPropertyWithSameData_whenCallsMerge_thenExpects() {
        var property12345_1 = PropertyInfoMetadata.builder().providerCode(DEFAULT_PROVIDER_CODE).build();
        var property12345_2 = PropertyInfoMetadata.builder().providerCode(DEFAULT_PROVIDER_CODE).build();

        assertThat(propertyMetadataMergeV2Service.merge(List.of(property12345_1, property12345_2)))
                .hasSize(1)
                .first()
                .usingRecursiveComparison()
                .isEqualTo(property12345_1);
    }

    @Test
    @DisplayName("given a duplicated property with pricing changes when calls merge then expects")
    void givenADuplicatedPropertyWithPricingChanges_whenCallsMerge_thenExpects() {
        var property12345_1 = PropertyInfoMetadata.builder()
                                                  .providerCode(DEFAULT_PROVIDER_CODE)
                                                  .pricing(
                                                          Pricing.builder()
                                                                 .sellPrice(EMPTY)
                                                                 .rentPrice(EMPTY)
                                                                 .condominiumPrice(EMPTY)
                                                                 .build()
                                                  )
                                                  .build();
        var property12345_2 = PropertyInfoMetadata.builder()
                                                  .providerCode(DEFAULT_PROVIDER_CODE)
                                                  .pricing(
                                                          Pricing.builder()
                                                                 .sellPrice(EMPTY)
                                                                 .rentPrice("R$ 1.000,00")
                                                                 .condominiumPrice("R$ 500,00")
                                                                 .build()
                                                  )
                                                  .build();
        var property12345_3 = PropertyInfoMetadata.builder()
                                                  .providerCode(DEFAULT_PROVIDER_CODE)
                                                  .pricing(
                                                          Pricing.builder()
                                                                 .sellPrice("R$ 100.000,00")
                                                                 .rentPrice(EMPTY)
                                                                 .condominiumPrice("R$ 400,00")
                                                                 .build()
                                                  )
                                                  .build();
        var property12345_4 = PropertyInfoMetadata.builder()
                                                  .providerCode(DEFAULT_PROVIDER_CODE)
                                                  .pricing(
                                                          Pricing.builder()
                                                                 .sellPrice(EMPTY)
                                                                 .rentPrice(EMPTY)
                                                                 .condominiumPrice("R$ 550,00")
                                                                 .build()
                                                  )
                                                  .build();

        assertThat(propertyMetadataMergeV2Service.merge(List.of(property12345_1, property12345_2, property12345_3, property12345_4)))
                .hasSize(1)
                .first()
                .usingRecursiveComparison()
                .isEqualTo(
                        PropertyInfoMetadata.builder()
                                            .providerCode(DEFAULT_PROVIDER_CODE)
                                            .pricing(
                                                    Pricing.builder()
                                                           .sellPrice("R$ 100.000,00")
                                                           .rentPrice("R$ 1.000,00")
                                                           .condominiumPrice("R$ 550,00")
                                                           .build()
                                            )
                                            .build()
                );
    }

}
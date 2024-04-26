package br.com.houseseeker.deserializer;

import br.com.houseseeker.configuration.ObjectMapperConfiguration;
import br.com.houseseeker.domain.provider.ProviderScraperResponse;
import br.com.houseseeker.util.ObjectMapperUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ObjectMapperConfiguration.class)
class AbstractUrbanPropertyMetadataDeserializerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("given a object without extractedData attribute when deserialize then expects null")
    void givenAObjectWithoutExtractedDataAttribute_whenDeserialize_ThenExpectsNull() {
        String json = """
                {
                   "extractedData": null
                 }
                """;

        assertThat(ObjectMapperUtils.deserializeAs(objectMapper, json, ProviderScraperResponse.class))
                .extracting("extractedData")
                .isNull();
    }

    @Test
    @DisplayName("given a object with empty extractedData attribute when deserialize then expects empty list")
    void givenAObjectWithEmptyExtractedDataAttribute_whenDeserialize_ThenExpectsEmptyList() {
        String json = """
                {
                   "extractedData": []
                 }
                """;

        assertThat(ObjectMapperUtils.deserializeAs(objectMapper, json, ProviderScraperResponse.class))
                .extracting("extractedData", InstanceOfAssertFactories.LIST)
                .isEmpty();
    }

    @Test
    @DisplayName("given a object with filled extractedData attribute when deserialize then expects list with two elements")
    void givenAObjectWithFilledExtractedDataAttribute_whenDeserialize_ThenExpectsListWithTwoElements() {
        String json = """
                {
                  "extractedData": [
                    {
                      "providerCode": "PC1"
                    },
                    {
                      "providerCode": "PC2"
                    }
                  ]
                }
                """;

        assertThat(ObjectMapperUtils.deserializeAs(objectMapper, json, ProviderScraperResponse.class))
                .extracting("extractedData", InstanceOfAssertFactories.LIST)
                .hasSize(2)
                .extracting("providerCode")
                .containsExactly("PC1", "PC2");
    }

}
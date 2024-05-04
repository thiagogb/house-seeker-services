package br.com.houseseeker.service.v4;

import br.com.houseseeker.configuration.ObjectMapperConfiguration;
import br.com.houseseeker.domain.jetimob.v4.PropertyInfoResponse;
import br.com.houseseeker.util.ObjectMapperUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static br.com.houseseeker.TestUtils.getTextFromResources;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
        ObjectMapperConfiguration.class,
        MetadataTransferV4Service.class
})
class MetadataTransferV4ServiceTest {

    private static final String SAMPLE_PROPERTY_2367172 = "samples/v4/property/2367172.json";
    private static final String SAMPLE_PROPERTY_2360531 = "samples/v4/property/2360531.json";
    private static final String RESPONSE_PROPERTY_2367172 = "responses/v4/transfer/2367172.json";
    private static final String RESPONSE_PROPERTY_2360531 = "responses/v4/transfer/2360531.json";

    @Autowired
    private MetadataTransferV4Service metadataTransferV4Service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("given sample property data 2367172 when calls transfer then expects")
    void givenSamplePropertyData2367172_whenCallsTransfer_thenExpects() {
        PropertyInfoResponse propertyInfoResponse = ObjectMapperUtils.deserializeAs(
                objectMapper,
                getTextFromResources(SAMPLE_PROPERTY_2367172),
                PropertyInfoResponse.class
        );

        assertThat(metadataTransferV4Service.transfer(propertyInfoResponse))
                .extracting(Object::toString)
                .satisfies(expected -> JSONAssert.assertEquals(expected, getTextFromResources(RESPONSE_PROPERTY_2367172), true));
    }

    @Test
    @DisplayName("given sample property data 2360531 when calls transfer then expects")
    void givenSamplePropertyData2360531_whenCallsTransfer_thenExpects() {
        PropertyInfoResponse propertyInfoResponse = ObjectMapperUtils.deserializeAs(
                objectMapper,
                getTextFromResources(SAMPLE_PROPERTY_2360531),
                PropertyInfoResponse.class
        );

        assertThat(metadataTransferV4Service.transfer(propertyInfoResponse))
                .extracting(Object::toString)
                .satisfies(expected -> JSONAssert.assertEquals(expected, getTextFromResources(RESPONSE_PROPERTY_2360531), true));
    }

}
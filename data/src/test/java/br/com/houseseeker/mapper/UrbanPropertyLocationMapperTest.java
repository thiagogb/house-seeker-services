package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.property.AbstractUrbanPropertyMetadata;
import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.entity.UrbanPropertyLocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static br.com.houseseeker.mock.UrbanPropertyMetadataMocks.residentialSellingHouse;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = UrbanPropertyLocationMapperImpl.class)
class UrbanPropertyLocationMapperTest {

    private static final String[] EXTRACTED_ATTRIBUTES = new String[]{
            "id",
            "urbanProperty",
            "state",
            "city",
            "district",
            "zipCode",
            "streetName",
            "streetNumber",
            "complement",
            "latitude",
            "longitude"
    };

    @Autowired
    private UrbanPropertyLocationMapper urbanPropertyLocationMapper;

    @Test
    @DisplayName("given a null property and extracted metadata when calls createEntity then expects")
    void givenANullPropertyAndExtractedMetadata_whenCallsCreateEntity_thenExpects() {
        assertThat(urbanPropertyLocationMapper.createEntity(null, null)).isNull();
    }

    @Test
    @DisplayName("given a property and extracted metadata when calls createEntity then expects")
    void givenAPropertyAndExtractedMetadata_whenCallsCreateEntity_thenExpects() {
        UrbanProperty urbanProperty = UrbanProperty.builder().build();
        AbstractUrbanPropertyMetadata metadata = residentialSellingHouse();

        assertThat(urbanPropertyLocationMapper.createEntity(urbanProperty, metadata))
                .extracting(EXTRACTED_ATTRIBUTES)
                .containsExactly(
                        null,
                        urbanProperty,
                        "RS",
                        "Santa Maria",
                        "Centro",
                        "97010100",
                        "Rua Venâncio Aires",
                        100,
                        "Esquina com a rua Appel",
                        BigDecimal.valueOf(123456.78),
                        BigDecimal.valueOf(-231654.87)
                );
    }

    @Test
    @DisplayName("given a property, metadata and a target location when calls copyToEntity then expects")
    void givenAPropertyAndTargetLocation_whenCallsCopyToEntity_thenExpects() {
        UrbanProperty urbanProperty = UrbanProperty.builder().build();
        AbstractUrbanPropertyMetadata metadata = residentialSellingHouse();
        UrbanPropertyLocation urbanPropertyLocation = UrbanPropertyLocation.builder().build();

        urbanPropertyLocationMapper.copyToEntity(urbanProperty, metadata, urbanPropertyLocation);

        assertThat(urbanPropertyLocation)
                .extracting(EXTRACTED_ATTRIBUTES)
                .containsExactly(
                        null,
                        urbanProperty,
                        "RS",
                        "Santa Maria",
                        "Centro",
                        "97010100",
                        "Rua Venâncio Aires",
                        100,
                        "Esquina com a rua Appel",
                        BigDecimal.valueOf(123456.78),
                        BigDecimal.valueOf(-231654.87)
                );
    }

}
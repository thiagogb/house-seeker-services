package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.property.AbstractUrbanPropertyMetadata;
import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.entity.UrbanPropertyMeasure;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static br.com.houseseeker.mock.UrbanPropertyMetadataMocks.residentialSellingHouse;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = UrbanPropertyMeasureMapperImpl.class)
class UrbanPropertyMeasureMapperTest {

    private static final String[] EXTRACTED_ATTRIBUTES = new String[]{
            "id",
            "urbanProperty",
            "totalArea",
            "privateArea",
            "usableArea",
            "terrainTotalArea",
            "terrainFront",
            "terrainBack",
            "terrainLeft",
            "terrainRight",
            "areaUnit"
    };

    @Autowired
    private UrbanPropertyMeasureMapper urbanPropertyMeasureMapper;

    @Test
    @DisplayName("given a property and metadata when calls createEntity then expects")
    void givenAPropertyAndMetadata_whenCallsCreateEntity_thenExpects() {
        UrbanProperty urbanProperty = UrbanProperty.builder().build();
        AbstractUrbanPropertyMetadata metadata = residentialSellingHouse();

        assertThat(urbanPropertyMeasureMapper.createEntity(urbanProperty, metadata))
                .extracting(EXTRACTED_ATTRIBUTES)
                .containsExactly(
                        null,
                        urbanProperty,
                        BigDecimal.valueOf(300),
                        BigDecimal.valueOf(250),
                        BigDecimal.valueOf(225),
                        BigDecimal.valueOf(400),
                        BigDecimal.valueOf(20),
                        BigDecimal.valueOf(20),
                        BigDecimal.valueOf(20),
                        BigDecimal.valueOf(20),
                        "mº"
                );
    }

    @Test
    @DisplayName("given a property, metadata and measure when calls copyToEntity then expects")
    void givenAPropertyAndMetadataAndMeasure_whenCallsCopyToEntity_thenExpects() {
        UrbanProperty urbanProperty = UrbanProperty.builder().build();
        AbstractUrbanPropertyMetadata metadata = residentialSellingHouse();
        UrbanPropertyMeasure urbanPropertyMeasure = UrbanPropertyMeasure.builder().build();

        urbanPropertyMeasureMapper.copyToEntity(urbanProperty, metadata, urbanPropertyMeasure);

        assertThat(urbanPropertyMeasure).extracting(EXTRACTED_ATTRIBUTES)
                                        .containsExactly(
                                                null,
                                                urbanProperty,
                                                BigDecimal.valueOf(300),
                                                BigDecimal.valueOf(250),
                                                BigDecimal.valueOf(225),
                                                BigDecimal.valueOf(400),
                                                BigDecimal.valueOf(20),
                                                BigDecimal.valueOf(20),
                                                BigDecimal.valueOf(20),
                                                BigDecimal.valueOf(20),
                                                "mº"
                                        );
    }

}
package br.com.houseseeker.mapper;

import br.com.houseseeker.configuration.TimeZoneConfiguration;
import br.com.houseseeker.domain.proto.UrbanPropertyData;
import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.entity.UrbanPropertyMeasure;
import com.google.protobuf.DoubleValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static br.com.houseseeker.mock.UrbanPropertyMetadataMocks.residentialSellingHouse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@SpringBootTest(classes = {
        TimeZoneConfiguration.class,
        ProtoInt32MapperImpl.class,
        ProtoStringMapperImpl.class,
        ProtoDoubleMapperImpl.class,
        ProtoBoolMapperImpl.class,
        ProtoBytesMapperImpl.class,
        ProviderMapperImpl.class,
        UrbanPropertyMapperImpl.class,
        UrbanPropertyMeasureMapperImpl.class
})
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
    @DisplayName("given entities when calls toProto then expects")
    void givenEntities_whenCallsToProto_thenExpects() {
        var urbanProperty = UrbanProperty.builder().providerCode("PC01").build();
        var entities = List.of(
                UrbanPropertyMeasure.builder()
                                    .id(1)
                                    .urbanProperty(urbanProperty)
                                    .totalArea(BigDecimal.valueOf(300))
                                    .privateArea(BigDecimal.valueOf(200))
                                    .usableArea(BigDecimal.valueOf(180))
                                    .terrainTotalArea(BigDecimal.valueOf(300))
                                    .terrainFront(BigDecimal.valueOf(30))
                                    .terrainBack(BigDecimal.valueOf(20))
                                    .terrainRight(BigDecimal.valueOf(30))
                                    .terrainLeft(BigDecimal.valueOf(30))
                                    .areaUnit("m²")
                                    .build()
        );

        assertThat(urbanPropertyMeasureMapper.toProto(entities))
                .extracting(
                        "id",
                        "urbanProperty.providerCode",
                        "totalArea",
                        "privateArea",
                        "usableArea",
                        "terrainTotalArea",
                        "terrainFront",
                        "terrainBack",
                        "terrainRight",
                        "terrainLeft",
                        "areaUnit"
                )
                .containsExactly(tuple(
                        Int32Value.of(1),
                        StringValue.of("PC01"),
                        DoubleValue.of(300),
                        DoubleValue.of(200),
                        DoubleValue.of(180),
                        DoubleValue.of(300),
                        DoubleValue.of(30),
                        DoubleValue.of(20),
                        DoubleValue.of(30),
                        DoubleValue.of(30),
                        StringValue.of("m²")
                ));
    }

    @Test
    @DisplayName("given entity when calls toProto then expects")
    void givenEntity_whenCallsToProto_thenExpects() {
        var entities = List.of(
                UrbanPropertyMeasure.builder()
                                    .id(1)
                                    .totalArea(BigDecimal.valueOf(300))
                                    .privateArea(BigDecimal.valueOf(200))
                                    .usableArea(BigDecimal.valueOf(180))
                                    .terrainTotalArea(BigDecimal.valueOf(300))
                                    .terrainFront(BigDecimal.valueOf(30))
                                    .terrainBack(BigDecimal.valueOf(20))
                                    .terrainRight(BigDecimal.valueOf(30))
                                    .terrainLeft(BigDecimal.valueOf(30))
                                    .areaUnit("m²")
                                    .build()
        );

        assertThat(urbanPropertyMeasureMapper.toProto(entities))
                .extracting(
                        "id",
                        "urbanProperty",
                        "totalArea",
                        "privateArea",
                        "usableArea",
                        "terrainTotalArea",
                        "terrainFront",
                        "terrainBack",
                        "terrainRight",
                        "terrainLeft",
                        "areaUnit"
                )
                .containsExactly(tuple(
                        Int32Value.of(1),
                        UrbanPropertyData.getDefaultInstance(),
                        DoubleValue.of(300),
                        DoubleValue.of(200),
                        DoubleValue.of(180),
                        DoubleValue.of(300),
                        DoubleValue.of(30),
                        DoubleValue.of(20),
                        DoubleValue.of(30),
                        DoubleValue.of(30),
                        StringValue.of("m²")
                ));
    }

    @Test
    @DisplayName("given a property and metadata when calls toEntity then expects")
    void givenAPropertyAndMetadata_whenCallsToEntity_thenExpects() {
        var urbanProperty = UrbanProperty.builder().build();
        var metadata = residentialSellingHouse();

        assertThat(urbanPropertyMeasureMapper.toEntity(urbanProperty, metadata))
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
    @DisplayName("given a property, metadata and measure when calls toEntity then expects")
    void givenAPropertyAndMetadataAndMeasure_whenCallsToEntity_thenExpects() {
        var urbanProperty = UrbanProperty.builder().build();
        var metadata = residentialSellingHouse();
        var urbanPropertyMeasure = UrbanPropertyMeasure.builder().build();

        urbanPropertyMeasureMapper.toEntity(urbanProperty, metadata, urbanPropertyMeasure);

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
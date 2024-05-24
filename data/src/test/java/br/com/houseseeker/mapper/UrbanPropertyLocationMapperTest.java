package br.com.houseseeker.mapper;

import br.com.houseseeker.configuration.TimeZoneConfiguration;
import br.com.houseseeker.domain.proto.UrbanPropertyData;
import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.entity.UrbanPropertyLocation;
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
        UrbanPropertyLocationMapperImpl.class
})
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
    @DisplayName("given entities when calls toProto then expects")
    void givenEntities_whenCallsToProto_thenExpects() {
        var urbanProperty = UrbanProperty.builder().providerCode("PC01").build();
        var entities = List.of(
                UrbanPropertyLocation.builder()
                                     .id(1)
                                     .urbanProperty(urbanProperty)
                                     .state("RS")
                                     .city("Santa Maria")
                                     .district("Centro")
                                     .zipCode("97010000")
                                     .streetName("Avenida Rio Branco")
                                     .streetNumber(1)
                                     .complement("Esquina com a Rua Andradas")
                                     .latitude(BigDecimal.valueOf(12.13456))
                                     .longitude(BigDecimal.valueOf(-12.123456))
                                     .build()
        );

        assertThat(urbanPropertyLocationMapper.toProto(entities))
                .extracting(
                        "id",
                        "urbanProperty.providerCode",
                        "state",
                        "city",
                        "district",
                        "zipCode",
                        "streetName",
                        "streetNumber",
                        "complement",
                        "latitude",
                        "longitude"
                )
                .containsExactly(tuple(
                        Int32Value.of(1),
                        StringValue.of("PC01"),
                        StringValue.of("RS"),
                        StringValue.of("Santa Maria"),
                        StringValue.of("Centro"),
                        StringValue.of("97010000"),
                        StringValue.of("Avenida Rio Branco"),
                        Int32Value.of(1),
                        StringValue.of("Esquina com a Rua Andradas"),
                        DoubleValue.of(12.13456),
                        DoubleValue.of(-12.123456)
                ));
    }

    @Test
    @DisplayName("given entity when calls toProto then expects")
    void givenEntity_whenCallsToProto_thenExpects() {
        var entity = UrbanPropertyLocation.builder()
                                          .id(1)
                                          .state("RS")
                                          .city("Santa Maria")
                                          .district("Centro")
                                          .zipCode("97010000")
                                          .streetName("Avenida Rio Branco")
                                          .streetNumber(1)
                                          .complement("Esquina com a Rua Andradas")
                                          .latitude(BigDecimal.valueOf(12.13456))
                                          .longitude(BigDecimal.valueOf(-12.123456))
                                          .build();

        assertThat(urbanPropertyLocationMapper.toProto(entity))
                .extracting(
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
                )
                .containsExactly(
                        Int32Value.of(1),
                        UrbanPropertyData.getDefaultInstance(),
                        StringValue.of("RS"),
                        StringValue.of("Santa Maria"),
                        StringValue.of("Centro"),
                        StringValue.of("97010000"),
                        StringValue.of("Avenida Rio Branco"),
                        Int32Value.of(1),
                        StringValue.of("Esquina com a Rua Andradas"),
                        DoubleValue.of(12.13456),
                        DoubleValue.of(-12.123456)
                );
    }

    @Test
    @DisplayName("given a null property and extracted metadata when calls toEntity then expects")
    void givenANullPropertyAndExtractedMetadata_whenCallsToEntity_thenExpects() {
        assertThat(urbanPropertyLocationMapper.toEntity(null, null)).isNull();
    }

    @Test
    @DisplayName("given a property and extracted metadata when calls toEntity then expects")
    void givenAPropertyAndExtractedMetadata_whenCallsToEntity_thenExpects() {
        var urbanProperty = UrbanProperty.builder().build();
        var metadata = residentialSellingHouse();

        assertThat(urbanPropertyLocationMapper.toEntity(urbanProperty, metadata))
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
    @DisplayName("given a property, metadata and a target location when calls toEntity then expects")
    void givenAPropertyAndTargetLocation_whenCallsToEntity_thenExpects() {
        var urbanProperty = UrbanProperty.builder().build();
        var metadata = residentialSellingHouse();
        var urbanPropertyLocation = UrbanPropertyLocation.builder().build();

        urbanPropertyLocationMapper.toEntity(urbanProperty, metadata, urbanPropertyLocation);

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
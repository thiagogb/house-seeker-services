package br.com.houseseeker.mapper;

import br.com.houseseeker.configuration.TimeZoneConfiguration;
import br.com.houseseeker.domain.proto.UrbanPropertyData;
import br.com.houseseeker.domain.proto.UrbanPropertyPriceVariationData;
import br.com.houseseeker.domain.property.UrbanPropertyPriceVariationType;
import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.entity.UrbanPropertyPriceVariation;
import com.google.protobuf.DoubleValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
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
        UrbanPropertyPriceVariationMapperImpl.class
})
class UrbanPropertyPriceVariationMapperTest {

    @Autowired
    private UrbanPropertyPriceVariationMapper urbanPropertyPriceVariationMapper;

    @Test
    @DisplayName("given entities when calls toProto then expects")
    void givenEntities_whenCallsToProto_thenExpects() {
        var urbanProperty = UrbanProperty.builder().providerCode("PC01").build();
        var entities = List.of(
                UrbanPropertyPriceVariation.builder()
                                           .id(1)
                                           .urbanProperty(urbanProperty)
                                           .analysisDate(LocalDateTime.parse("2024-01-01T12:30:45", ISO_LOCAL_DATE_TIME))
                                           .type(UrbanPropertyPriceVariationType.SELL)
                                           .price(BigDecimal.valueOf(1000))
                                           .variation(BigDecimal.valueOf(10))
                                           .build()
        );

        assertThat(urbanPropertyPriceVariationMapper.toProto(entities))
                .extracting(
                        UrbanPropertyPriceVariationData::getId,
                        p -> p.getUrbanProperty().getProviderCode(),
                        UrbanPropertyPriceVariationData::getAnalysisDate,
                        UrbanPropertyPriceVariationData::getType,
                        UrbanPropertyPriceVariationData::getPrice,
                        UrbanPropertyPriceVariationData::getVariation
                )
                .containsExactly(tuple(
                        Int32Value.of(1),
                        StringValue.of("PC01"),
                        StringValue.of("2024-01-01T12:30:45"),
                        StringValue.of(UrbanPropertyPriceVariationType.SELL.name()),
                        DoubleValue.of(1000),
                        DoubleValue.of(10)
                ));
    }

    @Test
    @DisplayName("given entity when calls toProto then expects")
    void givenEntity_whenCallsToProto_thenExpects() {
        var entities = List.of(
                UrbanPropertyPriceVariation.builder()
                                           .id(1)
                                           .analysisDate(LocalDateTime.parse("2024-01-01T12:30:45", ISO_LOCAL_DATE_TIME))
                                           .type(UrbanPropertyPriceVariationType.SELL)
                                           .price(BigDecimal.valueOf(1000))
                                           .variation(BigDecimal.valueOf(10))
                                           .build()
        );

        assertThat(urbanPropertyPriceVariationMapper.toProto(entities))
                .extracting(
                        UrbanPropertyPriceVariationData::getId,
                        UrbanPropertyPriceVariationData::getUrbanProperty,
                        UrbanPropertyPriceVariationData::getAnalysisDate,
                        UrbanPropertyPriceVariationData::getType,
                        UrbanPropertyPriceVariationData::getPrice,
                        UrbanPropertyPriceVariationData::getVariation
                )
                .containsExactly(tuple(
                        Int32Value.of(1),
                        UrbanPropertyData.getDefaultInstance(),
                        StringValue.of("2024-01-01T12:30:45"),
                        StringValue.of(UrbanPropertyPriceVariationType.SELL.name()),
                        DoubleValue.of(1000),
                        DoubleValue.of(10)
                ));
    }

}
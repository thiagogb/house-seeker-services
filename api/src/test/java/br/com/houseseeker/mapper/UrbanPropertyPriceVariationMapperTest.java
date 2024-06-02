package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.property.UrbanPropertyPriceVariationType;
import br.com.houseseeker.domain.proto.UrbanPropertyData;
import br.com.houseseeker.domain.proto.UrbanPropertyPriceVariationData;
import com.google.protobuf.DoubleValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
        ProtoInt32MapperImpl.class,
        ProtoStringMapperImpl.class,
        ProtoDoubleMapperImpl.class,
        UrbanPropertyPriceVariationMapperImpl.class
})
class UrbanPropertyPriceVariationMapperTest {

    @Autowired
    private UrbanPropertyPriceVariationMapper urbanPropertyPriceVariationMapper;

    @Test
    @DisplayName("given a data list when calls toDto then expects")
    void givenADataList_whenCallsToDto_thenExpects() {
        var data = List.of(
                UrbanPropertyPriceVariationData
                        .newBuilder()
                        .setId(Int32Value.of(101))
                        .setUrbanProperty(UrbanPropertyData.getDefaultInstance())
                        .setAnalysisDate(StringValue.of("2024-01-01T12:30:45"))
                        .setType(StringValue.of(UrbanPropertyPriceVariationType.RENT.name()))
                        .setPrice(DoubleValue.of(1000))
                        .setVariation(DoubleValue.of(10))
                        .build()
        );

        assertThat(urbanPropertyPriceVariationMapper.toDto(data))
                .hasToString("[" +
                                     "UrbanPropertyPriceVariationDto(id=101, analysisDate=2024-01-01T12:30:45, " +
                                     "type=RENT, price=1000.0, variation=10.0)" +
                                     "]");
    }

}
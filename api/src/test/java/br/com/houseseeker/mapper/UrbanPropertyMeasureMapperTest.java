package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.proto.UrbanPropertyData;
import br.com.houseseeker.domain.proto.UrbanPropertyMeasureData;
import com.google.protobuf.DoubleValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
        ProtoInt32MapperImpl.class,
        ProtoDoubleMapperImpl.class,
        ProtoStringMapperImpl.class,
        UrbanPropertyMeasureMapperImpl.class
})
class UrbanPropertyMeasureMapperTest {

    @Autowired
    private UrbanPropertyMeasureMapper urbanPropertyMeasureMapper;

    @Test
    @DisplayName("given a data when calls toDto then expects")
    void givenAData_whenCallsToDto_thenExpects() {
        var data = UrbanPropertyMeasureData.newBuilder()
                                           .setId(Int32Value.of(101))
                                           .setUrbanProperty(UrbanPropertyData.getDefaultInstance())
                                           .setTotalArea(DoubleValue.of(300))
                                           .setPrivateArea(DoubleValue.of(250))
                                           .setUsableArea(DoubleValue.of(200))
                                           .setTerrainTotalArea(DoubleValue.of(300))
                                           .setTerrainBack(DoubleValue.of(30))
                                           .setTerrainFront(DoubleValue.of(30))
                                           .setTerrainLeft(DoubleValue.of(10))
                                           .setTerrainFront(DoubleValue.of(10))
                                           .setAreaUnit(StringValue.of("m²"))
                                           .build();

        assertThat(urbanPropertyMeasureMapper.toDto(data))
                .hasToString(
                        "UrbanPropertyMeasureDto(id=101, totalArea=300.0, privateArea=250.0, usableArea=200.0, " +
                                "terrainTotalArea=300.0, terrainFront=10.0, terrainBack=30.0, terrainLeft=10.0, terrainRight=null, " +
                                "areaUnit=m²)"
                );
    }

}
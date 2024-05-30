package br.com.houseseeker.service.batch;

import br.com.houseseeker.domain.dto.UrbanPropertyDto;
import br.com.houseseeker.domain.proto.UrbanPropertyData;
import br.com.houseseeker.domain.proto.UrbanPropertyMeasureData;
import br.com.houseseeker.mapper.ProtoDoubleMapperImpl;
import br.com.houseseeker.mapper.ProtoInt32MapperImpl;
import br.com.houseseeker.mapper.ProtoStringMapperImpl;
import br.com.houseseeker.mapper.UrbanPropertyMeasureMapperImpl;
import br.com.houseseeker.service.UrbanPropertyMeasureService;
import br.com.houseseeker.service.proto.GetUrbanPropertyMeasuresResponse;
import com.google.protobuf.DoubleValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {
        ProtoInt32MapperImpl.class,
        ProtoDoubleMapperImpl.class,
        ProtoStringMapperImpl.class,
        UrbanPropertyMeasureMapperImpl.class,
        UrbanPropertyMeasureBatchMappingService.class
})
@ExtendWith(MockitoExtension.class)
class UrbanPropertyMeasureBatchMappingServiceTest {

    @Autowired
    private UrbanPropertyMeasureBatchMappingService urbanPropertyMeasureBatchMappingService;

    @MockBean
    private UrbanPropertyMeasureService urbanPropertyMeasureService;

    @Test
    @DisplayName("given a empty property list when calls map then expects")
    void givenAEmptyPropertyList_whenCallsMap_thenExpects() {
        when(urbanPropertyMeasureService.findByUrbanProperties(Collections.emptyList()))
                .thenReturn(GetUrbanPropertyMeasuresResponse.getDefaultInstance());

        assertThat(urbanPropertyMeasureBatchMappingService.map(Collections.emptyList()))
                .isEqualTo(Collections.emptyMap());

        verify(urbanPropertyMeasureService, times(1))
                .findByUrbanProperties(Collections.emptyList());
        verifyNoMoreInteractions(urbanPropertyMeasureService);
    }

    @Test
    @DisplayName("given a property list when calls map then expects")
    void givenAPropertyList_whenCallsMap_thenExpects() {
        var urbanProperty1 = UrbanPropertyDto.builder().id(1).build();
        var urbanProperty2 = UrbanPropertyDto.builder().id(2).build();
        var urbanProperties = List.of(urbanProperty1, urbanProperty2);

        when(urbanPropertyMeasureService.findByUrbanProperties(List.of(1, 2)))
                .thenReturn(
                        GetUrbanPropertyMeasuresResponse.newBuilder()
                                                        .addAllUrbanPropertyMeasures(List.of(
                                                                UrbanPropertyMeasureData.newBuilder()
                                                                                        .setId(Int32Value.of(100))
                                                                                        .setUrbanProperty(
                                                                                                UrbanPropertyData.newBuilder()
                                                                                                                 .setId(Int32Value.of(1))
                                                                                                                 .build()
                                                                                        )
                                                                                        .setTotalArea(DoubleValue.of(200))
                                                                                        .setPrivateArea(DoubleValue.of(150))
                                                                                        .setUsableArea(DoubleValue.of(100))
                                                                                        .setTerrainTotalArea(DoubleValue.of(200))
                                                                                        .setTerrainBack(DoubleValue.of(20))
                                                                                        .setTerrainFront(DoubleValue.of(20))
                                                                                        .setTerrainLeft(DoubleValue.of(10))
                                                                                        .setTerrainFront(DoubleValue.of(10))
                                                                                        .setAreaUnit(StringValue.of("m²"))
                                                                                        .build(),
                                                                UrbanPropertyMeasureData.newBuilder()
                                                                                        .setId(Int32Value.of(101))
                                                                                        .setUrbanProperty(
                                                                                                UrbanPropertyData.newBuilder()
                                                                                                                 .setId(Int32Value.of(2))
                                                                                                                 .build()
                                                                                        )
                                                                                        .setTotalArea(DoubleValue.of(300))
                                                                                        .setPrivateArea(DoubleValue.of(250))
                                                                                        .setUsableArea(DoubleValue.of(200))
                                                                                        .setTerrainTotalArea(DoubleValue.of(300))
                                                                                        .setTerrainBack(DoubleValue.of(30))
                                                                                        .setTerrainFront(DoubleValue.of(30))
                                                                                        .setTerrainLeft(DoubleValue.of(10))
                                                                                        .setTerrainFront(DoubleValue.of(10))
                                                                                        .setAreaUnit(StringValue.of("m²"))
                                                                                        .build()
                                                        ))
                                                        .build()
                );

        assertThat(urbanPropertyMeasureBatchMappingService.map(urbanProperties))
                .hasSize(2)
                .extractingByKeys(urbanProperty1, urbanProperty2)
                .hasToString("[" +
                                     "UrbanPropertyMeasureDto(id=100, totalArea=200.0, privateArea=150.0, usableArea=100.0, " +
                                     "terrainTotalArea=200.0, terrainFront=10.0, terrainBack=20.0, terrainLeft=10.0, terrainRight=null, areaUnit=m²), " +
                                     "UrbanPropertyMeasureDto(id=101, totalArea=300.0, privateArea=250.0, usableArea=200.0, terrainTotalArea=300.0, " +
                                     "terrainFront=10.0, terrainBack=30.0, terrainLeft=10.0, terrainRight=null, areaUnit=m²)" +
                                     "]");

        verify(urbanPropertyMeasureService, times(1))
                .findByUrbanProperties(List.of(1, 2));
        verifyNoMoreInteractions(urbanPropertyMeasureService);
    }

}
package br.com.houseseeker.service;

import br.com.houseseeker.domain.proto.UrbanPropertyData;
import br.com.houseseeker.domain.proto.UrbanPropertyMeasureData;
import br.com.houseseeker.service.proto.GetUrbanPropertyMeasuresRequest.OrdersData;
import br.com.houseseeker.service.proto.GetUrbanPropertyMeasuresResponse;
import br.com.houseseeker.service.proto.UrbanPropertyMeasureDataServiceGrpc.UrbanPropertyMeasureDataServiceBlockingStub;
import com.google.protobuf.DoubleValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.normalizeSpace;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = UrbanPropertyMeasureService.class)
@ExtendWith(MockitoExtension.class)
class UrbanPropertyMeasureServiceTest {

    @Autowired
    private UrbanPropertyMeasureService urbanPropertyMeasureService;

    @Mock
    private UrbanPropertyMeasureDataServiceBlockingStub mockedUrbanPropertyMeasureDataServiceBlockingStub;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(
                urbanPropertyMeasureService,
                "urbanPropertyMeasureDataServiceBlockingStub",
                mockedUrbanPropertyMeasureDataServiceBlockingStub
        );
    }

    @Test
    @DisplayName("given a list of property ids when calls findByUrbanProperties then expects")
    void givenAListOfPropertyIds_whenCallsFindByUrbanProperties_thenExpects() {
        var response = GetUrbanPropertyMeasuresResponse.newBuilder()
                                                       .addAllUrbanPropertyMeasures(List.of(
                                                               UrbanPropertyMeasureData.newBuilder()
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
                                                                                       .build()
                                                       ))
                                                       .build();

        when(mockedUrbanPropertyMeasureDataServiceBlockingStub.getUrbanPropertyMeasures(any()))
                .thenReturn(response);

        assertThat(urbanPropertyMeasureService.findByUrbanProperties(List.of(1, 2))).isEqualTo(response);

        verify(mockedUrbanPropertyMeasureDataServiceBlockingStub, times(1)).getUrbanPropertyMeasures(
                assertArg(a -> assertThat(a)
                        .satisfies(r -> assertThat(r.getProjections())
                                .extracting(p -> normalizeSpace(p.toString()))
                                .isEqualTo(
                                        "id: true urban_property: true total_area: true private_area: true " +
                                                "usable_area: true terrain_total_area: true terrain_front: true " +
                                                "terrain_back: true terrain_left: true terrain_right: true area_unit: true"
                                )
                        )
                        .satisfies(r -> assertThat(r.getClauses())
                                .extracting(p -> normalizeSpace(p.toString()))
                                .isEqualTo("urban_property_id { is_in { values: 1 values: 2 } }")
                        )
                        .satisfies(r -> assertThat(r.getOrders()).isEqualTo(OrdersData.getDefaultInstance()))
                        .satisfies(r -> assertThat(r.getPagination())
                                .extracting(p -> normalizeSpace(p.toString()))
                                .isEqualTo("pageSize: 2 pageNumber: 1")
                        )
                )
        );
        verifyNoMoreInteractions(mockedUrbanPropertyMeasureDataServiceBlockingStub);
    }

}
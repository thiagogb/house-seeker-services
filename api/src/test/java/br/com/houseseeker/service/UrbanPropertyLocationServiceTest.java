package br.com.houseseeker.service;

import br.com.houseseeker.domain.proto.UrbanPropertyData;
import br.com.houseseeker.domain.proto.UrbanPropertyLocationData;
import br.com.houseseeker.service.proto.GetUrbanPropertyLocationsRequest.OrdersData;
import br.com.houseseeker.service.proto.GetUrbanPropertyLocationsResponse;
import br.com.houseseeker.service.proto.UrbanPropertyLocationDataServiceGrpc.UrbanPropertyLocationDataServiceBlockingStub;
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

@SpringBootTest(classes = UrbanPropertyLocationService.class)
@ExtendWith(MockitoExtension.class)
class UrbanPropertyLocationServiceTest {

    @Autowired
    private UrbanPropertyLocationService urbanPropertyLocationService;

    @Mock
    private UrbanPropertyLocationDataServiceBlockingStub mockedUrbanPropertyLocationDataServiceBlockingStub;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(
                urbanPropertyLocationService,
                "urbanPropertyLocationDataServiceBlockingStub",
                mockedUrbanPropertyLocationDataServiceBlockingStub
        );
    }

    @Test
    @DisplayName("given a list of property ids when calls findByUrbanProperties then expects")
    void givenAListOfPropertyIds_whenCallsFindByUrbanProperties_thenExpects() {
        var response = GetUrbanPropertyLocationsResponse.newBuilder()
                                                        .addAllUrbanPropertyLocations(List.of(
                                                                UrbanPropertyLocationData.newBuilder()
                                                                                         .setId(Int32Value.of(101))
                                                                                         .setUrbanProperty(UrbanPropertyData.getDefaultInstance())
                                                                                         .setState(StringValue.of("RS"))
                                                                                         .setCity(StringValue.of("Santa Maria"))
                                                                                         .setDistrict(StringValue.of("Centro"))
                                                                                         .setZipCode(StringValue.of("97010-420"))
                                                                                         .setStreetName(StringValue.of("Avenida Rio Branco"))
                                                                                         .setStreetNumber(Int32Value.of(1))
                                                                                         .setComplement(StringValue.of("N/D"))
                                                                                         .setLatitude(DoubleValue.of(33.3333))
                                                                                         .setLongitude(DoubleValue.of(44.4444))
                                                                                         .build()
                                                        ))
                                                        .build();

        when(mockedUrbanPropertyLocationDataServiceBlockingStub.getUrbanPropertyLocations(any()))
                .thenReturn(response);

        assertThat(urbanPropertyLocationService.findByUrbanProperties(List.of(1, 2))).isEqualTo(response);

        verify(mockedUrbanPropertyLocationDataServiceBlockingStub, times(1)).getUrbanPropertyLocations(
                assertArg(a -> assertThat(a)
                        .satisfies(r -> assertThat(r.getProjections())
                                .extracting(p -> normalizeSpace(p.toString()))
                                .isEqualTo(
                                        "id: true urban_property: true state: true city: true district: true " +
                                                "zip_code: true street_name: true street_number: true complement: true " +
                                                "latitude: true longitude: true")
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
        verifyNoMoreInteractions(mockedUrbanPropertyLocationDataServiceBlockingStub);
    }

}
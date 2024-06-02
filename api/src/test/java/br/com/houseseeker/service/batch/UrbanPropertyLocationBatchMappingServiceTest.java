package br.com.houseseeker.service.batch;

import br.com.houseseeker.domain.dto.UrbanPropertyDto;
import br.com.houseseeker.domain.proto.UrbanPropertyData;
import br.com.houseseeker.domain.proto.UrbanPropertyLocationData;
import br.com.houseseeker.mapper.ProtoDoubleMapperImpl;
import br.com.houseseeker.mapper.ProtoInt32MapperImpl;
import br.com.houseseeker.mapper.ProtoStringMapperImpl;
import br.com.houseseeker.mapper.UrbanPropertyLocationMapperImpl;
import br.com.houseseeker.service.UrbanPropertyLocationService;
import br.com.houseseeker.service.proto.GetUrbanPropertyLocationsResponse;
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
        UrbanPropertyLocationMapperImpl.class,
        UrbanPropertyLocationBatchMappingService.class
})
@ExtendWith(MockitoExtension.class)
class UrbanPropertyLocationBatchMappingServiceTest {

    @Autowired
    private UrbanPropertyLocationBatchMappingService urbanPropertyLocationBatchMappingService;

    @MockBean
    private UrbanPropertyLocationService urbanPropertyLocationService;

    @Test
    @DisplayName("given a empty property list when calls map then expects")
    void givenAEmptyPropertyList_whenCallsMap_thenExpects() {
        when(urbanPropertyLocationService.findByUrbanProperties(Collections.emptyList()))
                .thenReturn(GetUrbanPropertyLocationsResponse.getDefaultInstance());

        assertThat(urbanPropertyLocationBatchMappingService.map(Collections.emptyList()))
                .isEqualTo(Collections.emptyMap());

        verify(urbanPropertyLocationService, times(1))
                .findByUrbanProperties(Collections.emptyList());
        verifyNoMoreInteractions(urbanPropertyLocationService);
    }

    @Test
    @DisplayName("given a property list when calls map then expects")
    void givenAPropertyList_whenCallsMap_thenExpects() {
        var urbanProperty1 = UrbanPropertyDto.builder().id(1).build();
        var urbanProperty2 = UrbanPropertyDto.builder().id(2).build();
        var urbanProperties = List.of(urbanProperty1, urbanProperty2);

        when(urbanPropertyLocationService.findByUrbanProperties(List.of(1, 2)))
                .thenReturn(
                        GetUrbanPropertyLocationsResponse.newBuilder()
                                                         .addAllUrbanPropertyLocations(List.of(
                                                                 UrbanPropertyLocationData.newBuilder()
                                                                                          .setId(Int32Value.of(100))
                                                                                          .setUrbanProperty(
                                                                                                  UrbanPropertyData.newBuilder()
                                                                                                                   .setId(Int32Value.of(1))
                                                                                                                   .build()
                                                                                          )
                                                                                          .setState(StringValue.of("RS"))
                                                                                          .setCity(StringValue.of("Porto Alegre"))
                                                                                          .setDistrict(StringValue.of("Jardim Planalto"))
                                                                                          .setZipCode(StringValue.of("91170-730"))
                                                                                          .setStreetName(StringValue.of("Rua Rubem Berta"))
                                                                                          .setStreetNumber(Int32Value.of(1))
                                                                                          .setComplement(StringValue.of("N/D"))
                                                                                          .setLatitude(DoubleValue.of(33.3333))
                                                                                          .setLongitude(DoubleValue.of(44.4444))
                                                                                          .build(),
                                                                 UrbanPropertyLocationData.newBuilder()
                                                                                          .setId(Int32Value.of(101))
                                                                                          .setUrbanProperty(
                                                                                                  UrbanPropertyData.newBuilder()
                                                                                                                   .setId(Int32Value.of(2))
                                                                                                                   .build()
                                                                                          )
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
                                                         .build()
                );

        assertThat(urbanPropertyLocationBatchMappingService.map(urbanProperties))
                .hasSize(2)
                .extractingByKeys(urbanProperty1, urbanProperty2)
                .hasToString("[" +
                                     "UrbanPropertyLocationDto(id=100, state=RS, city=Porto Alegre, district=Jardim Planalto, zipCode=91170-730, " +
                                     "streetName=Rua Rubem Berta, streetNumber=1, complement=N/D, latitude=33.3333, longitude=44.4444), " +
                                     "UrbanPropertyLocationDto(id=101, state=RS, city=Santa Maria, district=Centro, zipCode=97010-420, " +
                                     "streetName=Avenida Rio Branco, streetNumber=1, complement=N/D, latitude=33.3333, longitude=44.4444)" +
                                     "]");

        verify(urbanPropertyLocationService, times(1))
                .findByUrbanProperties(List.of(1, 2));
        verifyNoMoreInteractions(urbanPropertyLocationService);
    }

}
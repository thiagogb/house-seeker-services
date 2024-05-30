package br.com.houseseeker.service.batch;

import br.com.houseseeker.domain.dto.UrbanPropertyDto;
import br.com.houseseeker.domain.property.UrbanPropertyPriceVariationType;
import br.com.houseseeker.domain.proto.UrbanPropertyData;
import br.com.houseseeker.domain.proto.UrbanPropertyPriceVariationData;
import br.com.houseseeker.mapper.ProtoDoubleMapperImpl;
import br.com.houseseeker.mapper.ProtoInt32MapperImpl;
import br.com.houseseeker.mapper.ProtoStringMapperImpl;
import br.com.houseseeker.mapper.UrbanPropertyPriceVariationMapperImpl;
import br.com.houseseeker.service.UrbanPropertyPriceVariationService;
import br.com.houseseeker.service.proto.GetUrbanPropertyPriceVariationsResponse;
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
        ProtoStringMapperImpl.class,
        ProtoDoubleMapperImpl.class,
        UrbanPropertyPriceVariationMapperImpl.class,
        UrbanPropertyPriceVariationBatchMappingService.class
})
@ExtendWith(MockitoExtension.class)
class UrbanPropertyPriceVariationBatchMappingServiceTest {

    @Autowired
    private UrbanPropertyPriceVariationBatchMappingService urbanPropertyPriceVariationBatchMappingService;

    @MockBean
    private UrbanPropertyPriceVariationService urbanPropertyPriceVariationService;

    @Test
    @DisplayName("given a empty property list when calls map then expects")
    void givenAEmptyPropertyList_whenCallsMap_thenExpects() {
        when(urbanPropertyPriceVariationService.findByUrbanProperties(Collections.emptyList()))
                .thenReturn(GetUrbanPropertyPriceVariationsResponse.getDefaultInstance());

        assertThat(urbanPropertyPriceVariationBatchMappingService.map(Collections.emptyList()))
                .isEqualTo(Collections.emptyMap());

        verify(urbanPropertyPriceVariationService, times(1))
                .findByUrbanProperties(Collections.emptyList());
        verifyNoMoreInteractions(urbanPropertyPriceVariationService);
    }

    @Test
    @DisplayName("given a property list when calls map then expects")
    void givenAPropertyList_whenCallsMap_thenExpects() {
        var urbanProperty1 = UrbanPropertyDto.builder().id(1).build();
        var urbanProperty2 = UrbanPropertyDto.builder().id(2).build();
        var urbanProperties = List.of(urbanProperty1, urbanProperty2);

        when(urbanPropertyPriceVariationService.findByUrbanProperties(List.of(1, 2)))
                .thenReturn(
                        GetUrbanPropertyPriceVariationsResponse.newBuilder()
                                                               .addAllUrbanPropertyPriceVariations(List.of(
                                                                       UrbanPropertyPriceVariationData
                                                                               .newBuilder()
                                                                               .setId(Int32Value.of(100))
                                                                               .setUrbanProperty(
                                                                                       UrbanPropertyData.newBuilder()
                                                                                                        .setId(Int32Value.of(1))
                                                                                                        .build()
                                                                               )
                                                                               .setAnalysisDate(StringValue.of("2024-01-01T12:30:45"))
                                                                               .setType(StringValue.of(UrbanPropertyPriceVariationType.RENT.name()))
                                                                               .setPrice(DoubleValue.of(1000))
                                                                               .setVariation(DoubleValue.of(0))
                                                                               .build(),
                                                                       UrbanPropertyPriceVariationData
                                                                               .newBuilder()
                                                                               .setId(Int32Value.of(101))
                                                                               .setUrbanProperty(
                                                                                       UrbanPropertyData.newBuilder()
                                                                                                        .setId(Int32Value.of(2))
                                                                                                        .build()
                                                                               )
                                                                               .setAnalysisDate(StringValue.of("2024-01-01T12:30:45"))
                                                                               .setType(StringValue.of(UrbanPropertyPriceVariationType.SELL.name()))
                                                                               .setPrice(DoubleValue.of(100000))
                                                                               .setVariation(DoubleValue.of(10))
                                                                               .build()
                                                               ))
                                                               .build()
                );

        assertThat(urbanPropertyPriceVariationBatchMappingService.map(urbanProperties))
                .hasSize(2)
                .extractingByKeys(urbanProperty1, urbanProperty2)
                .hasToString("[" +
                                     "[UrbanPropertyPriceVariationDto(id=100, analysisDate=2024-01-01T12:30:45, type=RENT, " +
                                     "price=1000.0, variation=0.0)], " +
                                     "[UrbanPropertyPriceVariationDto(id=101, analysisDate=2024-01-01T12:30:45, type=SELL, " +
                                     "price=100000.0, variation=10.0)]" +
                                     "]");

        verify(urbanPropertyPriceVariationService, times(1))
                .findByUrbanProperties(List.of(1, 2));
        verifyNoMoreInteractions(urbanPropertyPriceVariationService);
    }

}
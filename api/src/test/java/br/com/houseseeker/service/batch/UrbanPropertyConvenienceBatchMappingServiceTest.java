package br.com.houseseeker.service.batch;

import br.com.houseseeker.domain.dto.UrbanPropertyDto;
import br.com.houseseeker.domain.proto.UrbanPropertyConvenienceData;
import br.com.houseseeker.domain.proto.UrbanPropertyData;
import br.com.houseseeker.mapper.ProtoInt32MapperImpl;
import br.com.houseseeker.mapper.ProtoStringMapperImpl;
import br.com.houseseeker.mapper.UrbanPropertyConvenienceMapperImpl;
import br.com.houseseeker.service.UrbanPropertyConvenienceService;
import br.com.houseseeker.service.proto.GetUrbanPropertyConveniencesResponse;
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
        UrbanPropertyConvenienceMapperImpl.class,
        UrbanPropertyConvenienceBatchMappingService.class
})
@ExtendWith(MockitoExtension.class)
class UrbanPropertyConvenienceBatchMappingServiceTest {

    @Autowired
    private UrbanPropertyConvenienceBatchMappingService urbanPropertyConvenienceBatchMappingService;

    @MockBean
    private UrbanPropertyConvenienceService urbanPropertyConvenienceService;

    @Test
    @DisplayName("given a empty property list when calls map then expects")
    void givenAEmptyPropertyList_whenCallsMap_thenExpects() {
        when(urbanPropertyConvenienceService.findByUrbanProperties(Collections.emptyList()))
                .thenReturn(GetUrbanPropertyConveniencesResponse.getDefaultInstance());

        assertThat(urbanPropertyConvenienceBatchMappingService.map(Collections.emptyList()))
                .isEqualTo(Collections.emptyMap());

        verify(urbanPropertyConvenienceService, times(1))
                .findByUrbanProperties(Collections.emptyList());
        verifyNoMoreInteractions(urbanPropertyConvenienceService);
    }

    @Test
    @DisplayName("given a property list when calls map then expects")
    void givenAPropertyList_whenCallsMap_thenExpects() {
        var urbanProperty1 = UrbanPropertyDto.builder().id(1).build();
        var urbanProperty2 = UrbanPropertyDto.builder().id(2).build();
        var urbanProperties = List.of(urbanProperty1, urbanProperty2);

        when(urbanPropertyConvenienceService.findByUrbanProperties(List.of(1, 2)))
                .thenReturn(
                        GetUrbanPropertyConveniencesResponse.newBuilder()
                                                            .addAllUrbanPropertyConveniences(List.of(
                                                                    UrbanPropertyConvenienceData.newBuilder()
                                                                                                .setId(Int32Value.of(100))
                                                                                                .setUrbanProperty(
                                                                                                        UrbanPropertyData.newBuilder()
                                                                                                                         .setId(Int32Value.of(1))
                                                                                                                         .build()
                                                                                                )
                                                                                                .setDescription(StringValue.of("PISCINA"))
                                                                                                .build(),
                                                                    UrbanPropertyConvenienceData.newBuilder()
                                                                                                .setId(Int32Value.of(101))
                                                                                                .setUrbanProperty(
                                                                                                        UrbanPropertyData.newBuilder()
                                                                                                                         .setId(Int32Value.of(2))
                                                                                                                         .build()
                                                                                                )
                                                                                                .setDescription(StringValue.of("CHURRASQUEIRA"))
                                                                                                .build()
                                                            ))
                                                            .build()
                );

        assertThat(urbanPropertyConvenienceBatchMappingService.map(urbanProperties))
                .hasSize(2)
                .extractingByKeys(urbanProperty1, urbanProperty2)
                .hasToString("[" +
                                     "[UrbanPropertyConvenienceDto(id=100, description=PISCINA)], " +
                                     "[UrbanPropertyConvenienceDto(id=101, description=CHURRASQUEIRA)]" +
                                     "]");

        verify(urbanPropertyConvenienceService, times(1))
                .findByUrbanProperties(List.of(1, 2));
        verifyNoMoreInteractions(urbanPropertyConvenienceService);
    }

}
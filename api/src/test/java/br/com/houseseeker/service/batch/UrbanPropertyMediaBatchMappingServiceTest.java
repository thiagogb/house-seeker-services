package br.com.houseseeker.service.batch;

import br.com.houseseeker.domain.dto.UrbanPropertyDto;
import br.com.houseseeker.domain.property.UrbanPropertyMediaType;
import br.com.houseseeker.domain.proto.UrbanPropertyData;
import br.com.houseseeker.domain.proto.UrbanPropertyMediaData;
import br.com.houseseeker.mapper.ProtoInt32MapperImpl;
import br.com.houseseeker.mapper.ProtoStringMapperImpl;
import br.com.houseseeker.mapper.UrbanPropertyMediaMapperImpl;
import br.com.houseseeker.service.UrbanPropertyMediaService;
import br.com.houseseeker.service.proto.GetUrbanPropertyMediasResponse;
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
        UrbanPropertyMediaMapperImpl.class,
        UrbanPropertyMediaBatchMappingService.class
})
@ExtendWith(MockitoExtension.class)
class UrbanPropertyMediaBatchMappingServiceTest {

    @Autowired
    private UrbanPropertyMediaBatchMappingService urbanPropertyMediaBatchMappingService;

    @MockBean
    private UrbanPropertyMediaService urbanPropertyMediaService;

    @Test
    @DisplayName("given a empty property list when calls map then expects")
    void givenAEmptyPropertyList_whenCallsMap_thenExpects() {
        when(urbanPropertyMediaService.findByUrbanProperties(Collections.emptyList()))
                .thenReturn(GetUrbanPropertyMediasResponse.getDefaultInstance());

        assertThat(urbanPropertyMediaBatchMappingService.map(Collections.emptyList()))
                .isEqualTo(Collections.emptyMap());

        verify(urbanPropertyMediaService, times(1))
                .findByUrbanProperties(Collections.emptyList());
        verifyNoMoreInteractions(urbanPropertyMediaService);
    }

    @Test
    @DisplayName("given a property list when calls map then expects")
    void givenAPropertyList_whenCallsMap_thenExpects() {
        var urbanProperty1 = UrbanPropertyDto.builder().id(1).build();
        var urbanProperty2 = UrbanPropertyDto.builder().id(2).build();
        var urbanProperties = List.of(urbanProperty1, urbanProperty2);

        when(urbanPropertyMediaService.findByUrbanProperties(List.of(1, 2)))
                .thenReturn(
                        GetUrbanPropertyMediasResponse.newBuilder()
                                                      .addAllUrbanPropertyMedias(List.of(
                                                              UrbanPropertyMediaData.newBuilder()
                                                                                    .setId(Int32Value.of(100))
                                                                                    .setUrbanProperty(
                                                                                            UrbanPropertyData.newBuilder()
                                                                                                             .setId(Int32Value.of(1))
                                                                                                             .build()
                                                                                    )
                                                                                    .setLink(StringValue.of("http://localhost/img.jpg"))
                                                                                    .setLinkThumb(StringValue.of("http://localhost/img-thumb.jpg"))
                                                                                    .setMediaType(StringValue.of(UrbanPropertyMediaType.IMAGE.name()))
                                                                                    .setExtension(StringValue.of("jpg"))
                                                                                    .build(),
                                                              UrbanPropertyMediaData.newBuilder()
                                                                                    .setId(Int32Value.of(101))
                                                                                    .setUrbanProperty(
                                                                                            UrbanPropertyData.newBuilder()
                                                                                                             .setId(Int32Value.of(2))
                                                                                                             .build()
                                                                                    )
                                                                                    .setLink(StringValue.of("http://localhost/img2.jpg"))
                                                                                    .setLinkThumb(StringValue.of("http://localhost/img2-thumb.jpg"))
                                                                                    .setMediaType(StringValue.of(UrbanPropertyMediaType.IMAGE.name()))
                                                                                    .setExtension(StringValue.of("jpg"))
                                                                                    .build()
                                                      ))
                                                      .build()
                );

        assertThat(urbanPropertyMediaBatchMappingService.map(urbanProperties))
                .hasSize(2)
                .extractingByKeys(urbanProperty1, urbanProperty2)
                .hasToString("[" +
                                     "[UrbanPropertyMediaDto(id=100, link=http://localhost/img.jpg, linkThumb=http://localhost/img-thumb.jpg, " +
                                     "mediaType=IMAGE, extension=jpg)], " +
                                     "[UrbanPropertyMediaDto(id=101, link=http://localhost/img2.jpg, linkThumb=http://localhost/img2-thumb.jpg, " +
                                     "mediaType=IMAGE, extension=jpg)]" +
                                     "]");

        verify(urbanPropertyMediaService, times(1))
                .findByUrbanProperties(List.of(1, 2));
        verifyNoMoreInteractions(urbanPropertyMediaService);
    }

}
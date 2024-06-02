package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.property.UrbanPropertyMediaType;
import br.com.houseseeker.domain.proto.UrbanPropertyData;
import br.com.houseseeker.domain.proto.UrbanPropertyMediaData;
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
        UrbanPropertyMediaMapperImpl.class
})
class UrbanPropertyMediaMapperTest {

    @Autowired
    private UrbanPropertyMediaMapper urbanPropertyMediaMapper;

    @Test
    @DisplayName("given a data list when calls toDto then expects")
    void givenADataList_whenCallsToDto_thenExpects() {
        var data = List.of(
                UrbanPropertyMediaData.newBuilder()
                                      .setId(Int32Value.of(101))
                                      .setUrbanProperty(UrbanPropertyData.getDefaultInstance())
                                      .setLink(StringValue.of("http://localhost/img.jpg"))
                                      .setLinkThumb(StringValue.of("http://localhost/img-thumb.jpg"))
                                      .setMediaType(StringValue.of(UrbanPropertyMediaType.IMAGE.name()))
                                      .setExtension(StringValue.of("jpg"))
                                      .build()
        );

        assertThat(urbanPropertyMediaMapper.toDto(data))
                .hasToString("[" +
                                     "UrbanPropertyMediaDto(id=101, link=http://localhost/img.jpg, linkThumb=http://localhost/img-thumb.jpg, " +
                                     "mediaType=IMAGE, extension=jpg)" +
                                     "]");
    }

}
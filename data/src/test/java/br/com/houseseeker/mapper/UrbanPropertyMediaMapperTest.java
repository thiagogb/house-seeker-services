package br.com.houseseeker.mapper;

import br.com.houseseeker.configuration.TimeZoneConfiguration;
import br.com.houseseeker.domain.property.UrbanPropertyMediaType;
import br.com.houseseeker.domain.proto.UrbanPropertyData;
import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.entity.UrbanPropertyMedia;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static br.com.houseseeker.mock.UrbanPropertyMetadataMocks.residentialSellingHouse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@SpringBootTest(classes = {
        TimeZoneConfiguration.class,
        ProtoInt32MapperImpl.class,
        ProtoStringMapperImpl.class,
        ProtoDoubleMapperImpl.class,
        ProtoBoolMapperImpl.class,
        ProtoBytesMapperImpl.class,
        ProviderMapperImpl.class,
        UrbanPropertyMapperImpl.class,
        UrbanPropertyMediaMapperImpl.class
})
class UrbanPropertyMediaMapperTest {

    private static final String[] EXTRACTED_ATTRIBUTES = new String[]{
            "id",
            "urbanProperty",
            "link",
            "linkThumb",
            "mediaType",
            "extension"
    };

    @Autowired
    private UrbanPropertyMediaMapper urbanPropertyMediaMapper;

    @Test
    @DisplayName("given entities when calls toProto then expects")
    void givenEntities_whenCallsToProto_thenExpects() {
        var urbanProperty = UrbanProperty.builder().providerCode("PC01").build();
        var entities = List.of(
                UrbanPropertyMedia.builder()
                                  .id(1)
                                  .urbanProperty(urbanProperty)
                                  .link("http://image/1")
                                  .linkThumb("http://image/1/thumb")
                                  .mediaType(UrbanPropertyMediaType.IMAGE)
                                  .extension("png")
                                  .build()
        );

        assertThat(urbanPropertyMediaMapper.toProto(entities))
                .extracting(
                        "id",
                        "urbanProperty.providerCode",
                        "link",
                        "linkThumb",
                        "mediaType",
                        "extension"
                )
                .containsExactly(tuple(
                        Int32Value.of(1),
                        StringValue.of("PC01"),
                        StringValue.of("http://image/1"),
                        StringValue.of("http://image/1/thumb"),
                        StringValue.of(UrbanPropertyMediaType.IMAGE.name()),
                        StringValue.of("png")
                ));
    }

    @Test
    @DisplayName("given entity when calls toProto then expects")
    void givenEntity_whenCallsToProto_thenExpects() {
        var entities = List.of(
                UrbanPropertyMedia.builder()
                                  .id(1)
                                  .link("http://image/1")
                                  .linkThumb("http://image/1/thumb")
                                  .mediaType(UrbanPropertyMediaType.IMAGE)
                                  .extension("png")
                                  .build()
        );

        assertThat(urbanPropertyMediaMapper.toProto(entities))
                .extracting(
                        "id",
                        "urbanProperty",
                        "link",
                        "linkThumb",
                        "mediaType",
                        "extension"
                )
                .containsExactly(tuple(
                        Int32Value.of(1),
                        UrbanPropertyData.getDefaultInstance(),
                        StringValue.of("http://image/1"),
                        StringValue.of("http://image/1/thumb"),
                        StringValue.of(UrbanPropertyMediaType.IMAGE.name()),
                        StringValue.of("png")
                ));
    }

    @Test
    @DisplayName("given a property and metadata when calls toEntity then expects")
    void givenAPropertyAndMetadata_whenCallsToEntity_thenExpects() {
        var urbanProperty = UrbanProperty.builder().build();
        var metadata = residentialSellingHouse().getMedias().getFirst();

        assertThat(urbanPropertyMediaMapper.toEntity(urbanProperty, metadata))
                .extracting(EXTRACTED_ATTRIBUTES)
                .containsExactly(
                        null,
                        urbanProperty,
                        "http://property/123/img1.jpg",
                        "http://property/123/img1-thumb.jpg",
                        UrbanPropertyMediaType.IMAGE,
                        "jpg"
                );
    }

}
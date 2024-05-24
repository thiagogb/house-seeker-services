package br.com.houseseeker.mapper;

import br.com.houseseeker.configuration.TimeZoneConfiguration;
import br.com.houseseeker.domain.proto.UrbanPropertyData;
import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.entity.UrbanPropertyConvenience;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
        UrbanPropertyConvenienceMapperImpl.class
})
class UrbanPropertyConvenienceMapperTest {

    private static final String[] EXTRACTED_ATTRIBUTES = new String[]{"id", "urbanProperty", "description"};

    @Autowired
    private UrbanPropertyConvenienceMapper urbanPropertyConvenienceMapper;

    @Test
    @DisplayName("given entities when calls toProto then expects")
    void givenEntities_whenCallsToProto_thenExpects() {
        var urbanPropertyPC01 = UrbanProperty.builder().providerCode("PC01").build();
        var urbanPropertyPC02 = UrbanProperty.builder().providerCode("PC02").build();

        var entities = List.of(
                UrbanPropertyConvenience.builder().id(1).urbanProperty(urbanPropertyPC01).description("PISCINA").build(),
                UrbanPropertyConvenience.builder().id(2).urbanProperty(urbanPropertyPC02).description("CHURRASQUEIRA").build()
        );

        assertThat(urbanPropertyConvenienceMapper.toProto(entities))
                .extracting("id", "urbanProperty.providerCode", "description")
                .containsExactly(
                        tuple(Int32Value.of(1), StringValue.of("PC01"), StringValue.of("PISCINA")),
                        tuple(Int32Value.of(2), StringValue.of("PC02"), StringValue.of("CHURRASQUEIRA"))
                );
    }

    @Test
    @DisplayName("given entities when calls toProto then expects")
    void givenEntity_whenCallsToProto_thenExpects() {
        var entity = UrbanPropertyConvenience.builder().id(1).description("PISCINA").build();

        assertThat(urbanPropertyConvenienceMapper.toProto(entity))
                .extracting("id", "urbanProperty", "description")
                .containsExactly(Int32Value.of(1), UrbanPropertyData.getDefaultInstance(), StringValue.of("PISCINA"));
    }

    @Test
    @DisplayName("given a null property and description when calls toEntity then expects")
    void givenANullPropertyAndDescription_whenCallsToEntity_thenExpects() {
        assertThat(urbanPropertyConvenienceMapper.toEntity(null, null)).isNull();
    }

    @Test
    @DisplayName("given a property and description not null when calls toEntity then expects")
    void givenAPropertyAndDescriptionNotNull_whenCallsToEntity_thenExpects() {
        var urbanProperty = UrbanProperty.builder().build();
        var description = "Example convenience";

        assertThat(urbanPropertyConvenienceMapper.toEntity(urbanProperty, description))
                .extracting(EXTRACTED_ATTRIBUTES)
                .containsExactly(null, urbanProperty, "Example convenience");
    }

}
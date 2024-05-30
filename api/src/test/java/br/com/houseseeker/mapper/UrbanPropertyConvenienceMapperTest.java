package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.dto.UrbanPropertyConvenienceDto;
import br.com.houseseeker.domain.proto.UrbanPropertyConvenienceData;
import br.com.houseseeker.domain.proto.UrbanPropertyData;
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
        ProtoInt32MapperImpl.class,
        ProtoStringMapperImpl.class,
        UrbanPropertyConvenienceMapperImpl.class
})
class UrbanPropertyConvenienceMapperTest {

    @Autowired
    private UrbanPropertyConvenienceMapper urbanPropertyConvenienceMapper;

    @Test
    @DisplayName("given a data list when calls toDto then expects")
    void givenADataList_whenCallsToDto_thenExpects() {
        var data = List.of(
                UrbanPropertyConvenienceData.newBuilder()
                                            .setId(Int32Value.of(1))
                                            .setUrbanProperty(UrbanPropertyData.getDefaultInstance())
                                            .setDescription(StringValue.of("PISCINA"))
                                            .build(),
                UrbanPropertyConvenienceData.newBuilder()
                                            .setId(Int32Value.of(2))
                                            .setUrbanProperty(UrbanPropertyData.getDefaultInstance())
                                            .setDescription(StringValue.of("CHURRASQUEIRA"))
                                            .build()
        );

        assertThat(urbanPropertyConvenienceMapper.toDto(data))
                .extracting(UrbanPropertyConvenienceDto::getId, UrbanPropertyConvenienceDto::getDescription)
                .containsExactly(
                        tuple(1, "PISCINA"),
                        tuple(2, "CHURRASQUEIRA")
                );
    }

}
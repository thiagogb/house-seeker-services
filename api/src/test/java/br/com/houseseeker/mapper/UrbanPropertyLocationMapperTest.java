package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.dto.UrbanPropertyLocationDto;
import br.com.houseseeker.domain.proto.UrbanPropertyData;
import br.com.houseseeker.domain.proto.UrbanPropertyLocationData;
import com.google.protobuf.DoubleValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
        ProtoInt32MapperImpl.class,
        ProtoDoubleMapperImpl.class,
        ProtoStringMapperImpl.class,
        UrbanPropertyLocationMapperImpl.class
})
class UrbanPropertyLocationMapperTest {

    @Autowired
    private UrbanPropertyLocationMapper urbanPropertyLocationMapper;

    @Test
    @DisplayName("given a data when calls toDto then expects")
    void givenAData_whenCallsToDto_thenExpects() {
        var data = UrbanPropertyLocationData.newBuilder()
                                            .setId(Int32Value.of(1))
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
                                            .build();

        assertThat(urbanPropertyLocationMapper.toDto(data))
                .extracting(
                        UrbanPropertyLocationDto::getId,
                        UrbanPropertyLocationDto::getState,
                        UrbanPropertyLocationDto::getCity,
                        UrbanPropertyLocationDto::getDistrict,
                        UrbanPropertyLocationDto::getZipCode,
                        UrbanPropertyLocationDto::getStreetName,
                        UrbanPropertyLocationDto::getStreetNumber,
                        UrbanPropertyLocationDto::getComplement,
                        UrbanPropertyLocationDto::getLatitude,
                        UrbanPropertyLocationDto::getLongitude
                )
                .containsExactly(
                        1,
                        "RS",
                        "Santa Maria",
                        "Centro",
                        "97010-420",
                        "Avenida Rio Branco",
                        1,
                        "N/D",
                        BigDecimal.valueOf(33.3333),
                        BigDecimal.valueOf(44.4444)
                );
    }

}
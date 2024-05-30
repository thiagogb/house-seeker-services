package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.property.UrbanPropertyContract;
import br.com.houseseeker.domain.property.UrbanPropertyStatus;
import br.com.houseseeker.domain.property.UrbanPropertyType;
import br.com.houseseeker.domain.proto.ProviderData;
import br.com.houseseeker.domain.proto.UrbanPropertyData;
import br.com.houseseeker.domain.provider.ProviderMechanism;
import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import com.google.protobuf.BytesValue;
import com.google.protobuf.DoubleValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
        ProtoBoolMapperImpl.class,
        ProtoInt32MapperImpl.class,
        ProtoDoubleMapperImpl.class,
        ProtoStringMapperImpl.class,
        ProtoBytesMapperImpl.class,
        ProviderMapperImpl.class,
        UrbanPropertyMapperImpl.class
})
class UrbanPropertyMapperTest {

    @Autowired
    private UrbanPropertyMapper urbanPropertyMapper;

    @Test
    @DisplayName("given a data list when calls toDto then expects")
    void givenADataList_whenCallsToDto_thenExpects() {
        var data = List.of(
                UrbanPropertyData.newBuilder()
                                 .setId(Int32Value.of(1))
                                 .setProvider(
                                         ProviderData.newBuilder()
                                                     .setId(Int32Value.of(1))
                                                     .setName(StringValue.of("Test provider"))
                                                     .setSiteUrl(StringValue.of("http://localhost"))
                                                     .setDataUrl(StringValue.of("http://localhost/api"))
                                                     .setMechanism(StringValue.of(ProviderMechanism.ALAN_WGT.name()))
                                                     .setParams(StringValue.of("{}"))
                                                     .setCronExpression(StringValue.of("* * *"))
                                                     .setLogo(BytesValue.of(ByteString.copyFrom("content", StandardCharsets.UTF_8)))
                                                     .setActive(BoolValue.of(true))
                                                     .build()
                                 )
                                 .setProviderCode(StringValue.of("PC01"))
                                 .setUrl(StringValue.of("http://localhost"))
                                 .setContract(StringValue.of(UrbanPropertyContract.SELL.name()))
                                 .setType(StringValue.of(UrbanPropertyType.RESIDENTIAL.name()))
                                 .setSubType(StringValue.of("Casa"))
                                 .setDormitories(Int32Value.of(2))
                                 .setSuites(Int32Value.of(1))
                                 .setBathrooms(Int32Value.of(2))
                                 .setGarages(Int32Value.of(1))
                                 .setSellPrice(DoubleValue.of(500000))
                                 .setRentPrice(DoubleValue.of(5000))
                                 .setCondominiumPrice(DoubleValue.of(500))
                                 .setCondominiumName(StringValue.of("Condomínio A"))
                                 .setExchangeable(BoolValue.of(true))
                                 .setStatus(StringValue.of(UrbanPropertyStatus.UNUSED.name()))
                                 .setFinanceable(BoolValue.of(true))
                                 .setOccupied(BoolValue.of(false))
                                 .setNotes(StringValue.of("Sem descrição"))
                                 .setCreationDate(StringValue.of("2024-01-01T12:30:45"))
                                 .setLastAnalysisDate(StringValue.of("2024-01-01T12:30:45"))
                                 .setExclusionDate(StringValue.of("2024-01-01T12:30:45"))
                                 .setAnalyzable(BoolValue.of(true))
                                 .build()
        );

        assertThat(urbanPropertyMapper.toDto(data))
                .hasToString("[" +
                                     "UrbanPropertyDto(id=1, provider=ProviderDto(id=1, name=Test provider, siteUrl=http://localhost, " +
                                     "dataUrl=http://localhost/api, mechanism=ALAN_WGT, params={}, cronExpression=* * *, " +
                                     "logoUrl=http://localhost/api/rest/providers/1/logo, active=true), providerCode=PC01, " +
                                     "url=http://localhost, contract=SELL, type=RESIDENTIAL, subType=Casa, dormitories=2, suites=1, " +
                                     "bathrooms=2, garages=1, sellPrice=500000.0, rentPrice=5000.0, condominiumPrice=500.0, " +
                                     "condominiumName=Condomínio A, exchangeable=true, status=UNUSED, financeable=true, " +
                                     "occupied=false, notes=Sem descrição, creationDate=2024-01-01T12:30:45, " +
                                     "lastAnalysisDate=2024-01-01T12:30:45, exclusionDate=2024-01-01T12:30:45, analyzable=true, " +
                                     "location=null, measure=null, conveniences=null, medias=null, priceVariations=null)" +
                                     "]");
    }

}
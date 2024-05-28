package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.property.AbstractUrbanPropertyMetadata;
import br.com.houseseeker.domain.property.UrbanPropertyContract;
import br.com.houseseeker.domain.property.UrbanPropertyStatus;
import br.com.houseseeker.domain.property.UrbanPropertyType;
import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanProperty;
import br.com.houseseeker.mock.ProviderMocks;
import com.google.protobuf.BoolValue;
import com.google.protobuf.DoubleValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static br.com.houseseeker.mock.UrbanPropertyMetadataMocks.residentialSellingHouse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {
        ProtoInt32MapperImpl.class,
        ProtoStringMapperImpl.class,
        ProtoDoubleMapperImpl.class,
        ProtoBoolMapperImpl.class,
        ProtoBytesMapperImpl.class,
        ProviderMapperImpl.class,
        UrbanPropertyMapperImpl.class
})
@ExtendWith(MockitoExtension.class)
class UrbanPropertyMapperTest {

    private static final String[] EXTRACTED_ATTRIBUTES = new String[]{
            "id",
            "provider",
            "providerCode",
            "url",
            "contract",
            "type",
            "subType",
            "dormitories",
            "suites",
            "bathrooms",
            "garages",
            "sellPrice",
            "rentPrice",
            "condominiumPrice",
            "condominiumName",
            "exchangeable",
            "status",
            "financeable",
            "occupied",
            "notes",
            "creationDate",
            "lastAnalysisDate",
            "exclusionDate",
            "analyzable"
    };

    private final static LocalDateTime CLOCK_AT_20240101_123000 = LocalDateTime.of(2024, 1, 1, 12, 30, 45);

    @Autowired
    private UrbanPropertyMapper urbanPropertyMapper;

    @MockBean
    private Clock clock;

    @BeforeEach
    void setup() {
        when(clock.instant()).thenReturn(Instant.parse("2024-01-01T12:30:45Z"));
        when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    }

    @Test
    @DisplayName("given entities when calls toProto then expects")
    void givenEntities_whenCallsToProto_thenExpects() {
        var provider = ProviderMocks.testProviderWithIdAndMechanism(1, ProviderMechanism.JETIMOB_V1);
        var entities = List.of(
                UrbanProperty.builder()
                             .id(1)
                             .provider(provider)
                             .providerCode("01")
                             .url("https://www.example.com/")
                             .contract(UrbanPropertyContract.SELL)
                             .type(UrbanPropertyType.RESIDENTIAL)
                             .subType("Apartamento")
                             .dormitories(2)
                             .suites(1)
                             .bathrooms(2)
                             .garages(1)
                             .sellPrice(BigDecimal.valueOf(300000))
                             .rentPrice(BigDecimal.valueOf(125000))
                             .condominiumPrice(BigDecimal.valueOf(200))
                             .condominiumName("Residencial Exemplo")
                             .exchangeable(true)
                             .status(UrbanPropertyStatus.USED)
                             .financeable(true)
                             .occupied(false)
                             .notes("Ótimo apartamento desocupado")
                             .creationDate(CLOCK_AT_20240101_123000)
                             .lastAnalysisDate(CLOCK_AT_20240101_123000)
                             .exclusionDate(CLOCK_AT_20240101_123000)
                             .analyzable(true)
                             .build()
        );

        assertThat(urbanPropertyMapper.toProto(entities))
                .extracting(
                        "id",
                        "provider.name",
                        "providerCode",
                        "url",
                        "contract",
                        "type",
                        "subType",
                        "dormitories",
                        "suites",
                        "bathrooms",
                        "garages",
                        "sellPrice",
                        "rentPrice",
                        "condominiumPrice",
                        "condominiumName",
                        "exchangeable",
                        "status",
                        "financeable",
                        "occupied",
                        "notes",
                        "creationDate",
                        "lastAnalysisDate",
                        "exclusionDate",
                        "analyzable"
                )
                .containsExactly(tuple(
                        Int32Value.of(1),
                        StringValue.of("Test Provider 1"),
                        StringValue.of("01"),
                        StringValue.of("https://www.example.com/"),
                        StringValue.of(UrbanPropertyContract.SELL.name()),
                        StringValue.of(UrbanPropertyType.RESIDENTIAL.name()),
                        StringValue.of("Apartamento"),
                        Int32Value.of(2),
                        Int32Value.of(1),
                        Int32Value.of(2),
                        Int32Value.of(1),
                        DoubleValue.of(300000),
                        DoubleValue.of(125000),
                        DoubleValue.of(200),
                        StringValue.of("Residencial Exemplo"),
                        BoolValue.of(true),
                        StringValue.of(UrbanPropertyStatus.USED.name()),
                        BoolValue.of(true),
                        BoolValue.of(false),
                        StringValue.of("Ótimo apartamento desocupado"),
                        StringValue.of("2024-01-01T12:30:45"),
                        StringValue.of("2024-01-01T12:30:45"),
                        StringValue.of("2024-01-01T12:30:45"),
                        BoolValue.of(true)
                ));
    }

    @Test
    @DisplayName("given a provider and metadata when calls toEntity then expects")
    void givenAProviderAndMetadata_whenCallsToEntity_thenExpects() {
        Provider provider = Provider.builder().build();
        AbstractUrbanPropertyMetadata metadata = residentialSellingHouse();

        assertThat(urbanPropertyMapper.toEntity(provider, metadata))
                .extracting(EXTRACTED_ATTRIBUTES)
                .containsExactly(
                        null,
                        provider,
                        "123",
                        "http://property/123",
                        UrbanPropertyContract.SELL,
                        UrbanPropertyType.RESIDENTIAL,
                        "Casa",
                        3,
                        1,
                        2,
                        2,
                        BigDecimal.valueOf(500000),
                        BigDecimal.valueOf(1500),
                        null,
                        null,
                        true,
                        UrbanPropertyStatus.USED,
                        true,
                        false,
                        "Casa 3 dormitório em bairro calmo",
                        CLOCK_AT_20240101_123000,
                        CLOCK_AT_20240101_123000,
                        null,
                        true
                );
    }

    @Test
    @DisplayName("given a provider and metadata when calls copyToEntity then expects")
    void givenAProviderAndMetadata_whenCallsCopyToEntity_thenExpects() {
        Provider provider = Provider.builder().build();
        AbstractUrbanPropertyMetadata metadata = residentialSellingHouse();
        LocalDateTime clockAt_20240101_093045 = LocalDateTime.of(2024, 1, 1, 9, 30, 45);
        UrbanProperty urbanProperty = UrbanProperty.builder()
                                                   .provider(provider)
                                                   .creationDate(clockAt_20240101_093045)
                                                   .lastAnalysisDate(clockAt_20240101_093045)
                                                   .exclusionDate(clockAt_20240101_093045)
                                                   .analyzable(false)
                                                   .build();

        urbanPropertyMapper.toEntity(metadata, urbanProperty);

        assertThat(urbanProperty)
                .extracting(EXTRACTED_ATTRIBUTES)
                .containsExactly(
                        null,
                        provider,
                        "123",
                        "http://property/123",
                        UrbanPropertyContract.SELL,
                        UrbanPropertyType.RESIDENTIAL,
                        "Casa",
                        3,
                        1,
                        2,
                        2,
                        BigDecimal.valueOf(500000),
                        BigDecimal.valueOf(1500),
                        null,
                        null,
                        true,
                        UrbanPropertyStatus.USED,
                        true,
                        false,
                        "Casa 3 dormitório em bairro calmo",
                        clockAt_20240101_093045,
                        CLOCK_AT_20240101_123000,
                        null,
                        false
                );
    }

}
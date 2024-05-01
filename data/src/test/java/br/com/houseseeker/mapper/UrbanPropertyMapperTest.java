package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.property.AbstractUrbanPropertyMetadata;
import br.com.houseseeker.domain.property.UrbanPropertyContract;
import br.com.houseseeker.domain.property.UrbanPropertyStatus;
import br.com.houseseeker.domain.property.UrbanPropertyType;
import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.entity.UrbanProperty;
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

import static br.com.houseseeker.mock.UrbanPropertyMetadataMocks.residentialSellingHouse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = UrbanPropertyMapperImpl.class)
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
    @DisplayName("given a provider and metadata when calls createEntity then expects")
    void givenAProviderAndMetadata_whenCallsCreateEntity_thenExpects() {
        Provider provider = Provider.builder().build();
        AbstractUrbanPropertyMetadata metadata = residentialSellingHouse();

        assertThat(urbanPropertyMapper.createEntity(provider, metadata))
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

        urbanPropertyMapper.copyToEntity(metadata, urbanProperty);

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
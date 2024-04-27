package br.com.houseseeker.mock;

import br.com.houseseeker.domain.property.UrbanPropertyContract;
import br.com.houseseeker.domain.property.UrbanPropertyMediaType;
import br.com.houseseeker.domain.property.UrbanPropertyStatus;
import br.com.houseseeker.domain.property.UrbanPropertyType;
import br.com.houseseeker.domain.provider.UrbanPropertyMediaMetadata;
import br.com.houseseeker.domain.provider.UrbanPropertyMetadata;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.List;

@UtilityClass
public class UrbanPropertyMetadataMocks {

    public UrbanPropertyMetadata residentialSellingHouse() {
        return UrbanPropertyMetadata.builder()
                                    .providerCode("123")
                                    .url("http://property/123")
                                    .contract(UrbanPropertyContract.SELL)
                                    .type(UrbanPropertyType.RESIDENTIAL)
                                    .subType("Casa")
                                    .dormitories(3)
                                    .suites(1)
                                    .bathrooms(2)
                                    .garages(2)
                                    .sellPrice(BigDecimal.valueOf(500000))
                                    .rentPrice(BigDecimal.valueOf(1500))
                                    .isExchangeable(true)
                                    .status(UrbanPropertyStatus.USED)
                                    .isFinanceable(true)
                                    .isOccupied(false)
                                    .notes("Casa 3 dormitório em bairro calmo")
                                    .conveniences(List.of("Placa solar", "Piscina"))
                                    .state("RS")
                                    .city("Santa Maria")
                                    .district("Centro")
                                    .zipCode("97010100")
                                    .streetName("Rua Venâncio Aires")
                                    .streetNumber(100)
                                    .complement("Esquina com a rua Appel")
                                    .latitude(BigDecimal.valueOf(123456.78))
                                    .longitude(BigDecimal.valueOf(-231654.87))
                                    .totalArea(BigDecimal.valueOf(300))
                                    .privateArea(BigDecimal.valueOf(250))
                                    .usableArea(BigDecimal.valueOf(225))
                                    .terrainTotalArea(BigDecimal.valueOf(400))
                                    .terrainFront(BigDecimal.valueOf(20))
                                    .terrainBack(BigDecimal.valueOf(20))
                                    .terrainLeft(BigDecimal.valueOf(20))
                                    .terrainRight(BigDecimal.valueOf(20))
                                    .areaUnit("mº")
                                    .medias(List.of(
                                            UrbanPropertyMediaMetadata.builder()
                                                                      .link("http://property/123/img1.jpg")
                                                                      .linkThumb("http://property/123/img1-thumb.jpg")
                                                                      .mediaType(UrbanPropertyMediaType.IMAGE)
                                                                      .extension("jpg")
                                                                      .build()
                                    ))
                                    .build();
    }

}

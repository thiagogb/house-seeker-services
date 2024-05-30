package br.com.houseseeker.controller;

import br.com.houseseeker.domain.argument.UrbanPropertyInput;
import br.com.houseseeker.domain.dto.UrbanPropertyConvenienceDto;
import br.com.houseseeker.domain.dto.UrbanPropertyDto;
import br.com.houseseeker.domain.dto.UrbanPropertyLocationDto;
import br.com.houseseeker.domain.dto.UrbanPropertyMeasureDto;
import br.com.houseseeker.domain.dto.UrbanPropertyMediaDto;
import br.com.houseseeker.domain.dto.UrbanPropertyPriceVariationDto;
import br.com.houseseeker.domain.input.OrderInput;
import br.com.houseseeker.domain.input.PaginationInput;
import br.com.houseseeker.domain.property.UrbanPropertyContract;
import br.com.houseseeker.domain.property.UrbanPropertyMediaType;
import br.com.houseseeker.domain.property.UrbanPropertyPriceVariationType;
import br.com.houseseeker.domain.property.UrbanPropertyStatus;
import br.com.houseseeker.domain.property.UrbanPropertyType;
import br.com.houseseeker.domain.proto.PaginationResponseData;
import br.com.houseseeker.domain.proto.ProviderData;
import br.com.houseseeker.domain.proto.UrbanPropertyData;
import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.mapper.PaginationMapperImpl;
import br.com.houseseeker.mapper.ProtoBoolMapperImpl;
import br.com.houseseeker.mapper.ProtoBytesMapperImpl;
import br.com.houseseeker.mapper.ProtoDoubleMapperImpl;
import br.com.houseseeker.mapper.ProtoInt32MapperImpl;
import br.com.houseseeker.mapper.ProtoStringMapperImpl;
import br.com.houseseeker.mapper.ProviderMapperImpl;
import br.com.houseseeker.mapper.UrbanPropertyMapperImpl;
import br.com.houseseeker.service.UrbanPropertyService;
import br.com.houseseeker.service.batch.UrbanPropertyConvenienceBatchMappingService;
import br.com.houseseeker.service.batch.UrbanPropertyLocationBatchMappingService;
import br.com.houseseeker.service.batch.UrbanPropertyMeasureBatchMappingService;
import br.com.houseseeker.service.batch.UrbanPropertyMediaBatchMappingService;
import br.com.houseseeker.service.batch.UrbanPropertyPriceVariationBatchMappingService;
import br.com.houseseeker.service.proto.GetUrbanPropertiesResponse;
import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import com.google.protobuf.BytesValue;
import com.google.protobuf.DoubleValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@GraphQlTest(controllers = UrbanPropertyController.class)
@Import({
        ProtoInt32MapperImpl.class,
        ProtoDoubleMapperImpl.class,
        ProtoStringMapperImpl.class,
        ProtoBytesMapperImpl.class,
        ProtoBoolMapperImpl.class,
        ProviderMapperImpl.class,
        UrbanPropertyMapperImpl.class,
        PaginationMapperImpl.class
})
@ExtendWith(MockitoExtension.class)
class UrbanPropertyControllerGraphQLTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockBean
    private UrbanPropertyService urbanPropertyService;

    @MockBean
    private UrbanPropertyLocationBatchMappingService urbanPropertyLocationBatchMappingService;

    @MockBean
    private UrbanPropertyMeasureBatchMappingService urbanPropertyMeasureBatchMappingService;

    @MockBean
    private UrbanPropertyConvenienceBatchMappingService urbanPropertyConvenienceBatchMappingService;

    @MockBean
    private UrbanPropertyMediaBatchMappingService urbanPropertyMediaBatchMappingService;

    @MockBean
    private UrbanPropertyPriceVariationBatchMappingService urbanPropertyPriceVariationBatchMappingService;

    @BeforeEach
    void setup() {
        when(urbanPropertyService.findBy(anySet(), any())).thenReturn(
                GetUrbanPropertiesResponse.newBuilder()
                                          .addAllUrbanProperties(List.of(
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
                                          ))
                                          .setPagination(
                                                  PaginationResponseData.newBuilder()
                                                                        .setPageSize(1)
                                                                        .setPageNumber(1)
                                                                        .setTotalPages(1)
                                                                        .setTotalRows(1)
                                                                        .build()
                                          )
                                          .build()
        );
    }

    @Test
    @DisplayName("given a payload without input and all fields when calls urbanProperties then expects")
    void givenAPayloadWithoutInputAndAllFields_whenCallsUrbanProperties_thenExpects() {
        graphQlTester.documentName("get-urban-properties-sample-1")
                     .execute()
                     .path("urbanProperties")
                     .matchesJsonStrictly("""
                                                  {
                                                    "rows": [
                                                      {
                                                        "id": "1",
                                                        "provider": {
                                                          "id": "1"
                                                        },
                                                        "providerCode": "PC01",
                                                        "url": "http://localhost",
                                                        "contract": "SELL",
                                                        "type": "RESIDENTIAL",
                                                        "subType": "Casa",
                                                        "dormitories": 2,
                                                        "suites": 1,
                                                        "bathrooms": 2,
                                                        "garages": 1,
                                                        "sellPrice": 500000,
                                                        "rentPrice": 5000,
                                                        "condominiumPrice": 500,
                                                        "condominiumName": "Condomínio A",
                                                        "exchangeable": true,
                                                        "status": "UNUSED",
                                                        "financeable": true,
                                                        "occupied": false,
                                                        "notes": "Sem descrição",
                                                        "creationDate": "2024-01-01T12:30:45",
                                                        "lastAnalysisDate": "2024-01-01T12:30:45",
                                                        "exclusionDate": "2024-01-01T12:30:45",
                                                        "analyzable": true
                                                      }
                                                    ],
                                                    "pagination": {
                                                      "pageNumber": 1,
                                                      "pageSize": 1,
                                                      "totalPages": 1,
                                                      "totalRows": 1
                                                    }
                                                  }
                                                  """);

        verify(urbanPropertyService, times(1)).findBy(
                assertArg(arg -> assertThat(arg).contains(
                        "rows/id",
                        "rows/provider/id",
                        "rows/providerCode",
                        "rows/url",
                        "rows/contract",
                        "rows/type",
                        "rows/subType",
                        "rows/dormitories",
                        "rows/suites",
                        "rows/bathrooms",
                        "rows/garages",
                        "rows/sellPrice",
                        "rows/rentPrice",
                        "rows/condominiumPrice",
                        "rows/condominiumName",
                        "rows/exchangeable",
                        "rows/status",
                        "rows/financeable",
                        "rows/occupied",
                        "rows/notes",
                        "rows/creationDate",
                        "rows/lastAnalysisDate",
                        "rows/exclusionDate",
                        "rows/analyzable",
                        "pagination/pageNumber",
                        "pagination/pageSize",
                        "pagination/totalPages",
                        "pagination/totalRows"
                )),
                isNull()
        );
        verifyNoMoreInteractions(urbanPropertyService);
        verifyNoInteractions(urbanPropertyLocationBatchMappingService);
        verifyNoInteractions(urbanPropertyMeasureBatchMappingService);
        verifyNoInteractions(urbanPropertyConvenienceBatchMappingService);
        verifyNoInteractions(urbanPropertyMediaBatchMappingService);
        verifyNoInteractions(urbanPropertyPriceVariationBatchMappingService);
    }

    @Test
    @DisplayName("given a payload without input and some fields when calls urbanProperties then expects")
    void givenAPayloadWithoutInputAndSomeFields_whenCallsUrbanProperties_thenExpects() {
        graphQlTester.documentName("get-urban-properties-sample-2")
                     .execute()
                     .path("urbanProperties")
                     .matchesJsonStrictly("""
                                                  {
                                                    "rows": [
                                                      {
                                                        "id": "1",
                                                        "providerCode": "PC01",
                                                        "subType": "Casa",
                                                        "dormitories": 2,
                                                        "suites": 1,
                                                        "bathrooms": 2,
                                                        "garages": 1,
                                                        "sellPrice": 500000
                                                      }
                                                    ],
                                                    "pagination": {
                                                      "pageNumber": 1,
                                                      "pageSize": 1,
                                                      "totalPages": 1
                                                    }
                                                  }
                                                  """);

        verify(urbanPropertyService, times(1)).findBy(
                assertArg(arg -> assertThat(arg).contains(
                        "rows/id",
                        "rows/providerCode",
                        "rows/subType",
                        "rows/dormitories",
                        "rows/suites",
                        "rows/bathrooms",
                        "rows/garages",
                        "rows/sellPrice",
                        "pagination/pageNumber",
                        "pagination/pageSize",
                        "pagination/totalPages"
                )),
                isNull()
        );
        verifyNoMoreInteractions(urbanPropertyService);
        verifyNoInteractions(urbanPropertyLocationBatchMappingService);
        verifyNoInteractions(urbanPropertyMeasureBatchMappingService);
        verifyNoInteractions(urbanPropertyConvenienceBatchMappingService);
        verifyNoInteractions(urbanPropertyMediaBatchMappingService);
        verifyNoInteractions(urbanPropertyPriceVariationBatchMappingService);
    }

    @Test
    @DisplayName("given a payload with input containing clauses when calls urbanProperties then expects")
    void givenAPayloadWithInputContainingClauses_whenCallsUrbanProperties_thenExpects() {
        graphQlTester.documentName("get-urban-properties-sample-3")
                     .execute()
                     .path("urbanProperties")
                     .matchesJsonStrictly("""
                                                  {
                                                    "rows": [
                                                      {
                                                        "id": "1"
                                                      }
                                                    ]
                                                  }
                                                  """);

        verify(urbanPropertyService, times(1)).findBy(
                assertArg(arg -> assertThat(arg).contains("rows/id")),
                assertArg(arg -> assertThat(arg)
                        .hasAllNullFieldsOrPropertiesExcept("clauses")
                        .extracting(UrbanPropertyInput::getClauses)
                        .extracting(
                                a -> a.getId().getIsGreater().getValue(),
                                a -> a.getProviderCode().getIsNotBlank(),
                                a -> a.getUrl().getIsStartingWith().getValue(),
                                a -> a.getContract().getIsEqual().getValue(),
                                a -> a.getSubType().getIsIn().getValues(),
                                a -> a.getDormitories().getIsGreaterOrEqual().getValue(),
                                a -> String.format("%d-%d", a.getSuites().getIsBetween().getStart(), a.getSuites().getIsBetween().getEnd()),
                                a -> a.getSellPrice().getIsLesserOrEqual().getValue(),
                                a -> a.getCreationDate().getIsGreaterOrEqual().getValue(),
                                a -> a.getAnalyzable().getIsEqual().getValue()
                        )
                        .containsExactly(
                                0,
                                true,
                                "https://",
                                UrbanPropertyContract.SELL,
                                List.of("Apartamento", "Casa"),
                                2,
                                "1-2",
                                BigDecimal.valueOf(999999.99),
                                LocalDateTime.parse("2024-01-01T00:00", ISO_LOCAL_DATE_TIME),
                                true
                        )
                )
        );
        verifyNoMoreInteractions(urbanPropertyService);
        verifyNoInteractions(urbanPropertyLocationBatchMappingService);
        verifyNoInteractions(urbanPropertyMeasureBatchMappingService);
        verifyNoInteractions(urbanPropertyConvenienceBatchMappingService);
        verifyNoInteractions(urbanPropertyMediaBatchMappingService);
        verifyNoInteractions(urbanPropertyPriceVariationBatchMappingService);
    }

    @Test
    @DisplayName("given a payload with input containing orders when calls urbanProperties then expects")
    void givenAPayloadWithInputContainingOrders_whenCallsUrbanProperties_thenExpects() {
        graphQlTester.documentName("get-urban-properties-sample-4")
                     .execute()
                     .path("urbanProperties")
                     .matchesJsonStrictly("""
                                                  {
                                                    "rows": [
                                                      {
                                                        "id": "1"
                                                      }
                                                    ]
                                                  }
                                                  """);

        verify(urbanPropertyService, times(1)).findBy(
                assertArg(arg -> assertThat(arg).contains("rows/id")),
                assertArg(arg -> assertThat(arg)
                        .hasAllNullFieldsOrPropertiesExcept("orders")
                        .extracting(UrbanPropertyInput::getOrders)
                        .extracting(
                                UrbanPropertyInput.Orders::getId,
                                UrbanPropertyInput.Orders::getSubType,
                                UrbanPropertyInput.Orders::getDormitories,
                                UrbanPropertyInput.Orders::getSellPrice
                        )
                        .containsExactly(
                                OrderInput.builder().index(4).direction(OrderInput.Direction.ASC).build(),
                                OrderInput.builder().index(3).direction(OrderInput.Direction.ASC).build(),
                                OrderInput.builder().index(2).direction(OrderInput.Direction.DESC).build(),
                                OrderInput.builder().index(1).direction(OrderInput.Direction.DESC).build()
                        )
                )
        );
        verifyNoMoreInteractions(urbanPropertyService);
        verifyNoInteractions(urbanPropertyLocationBatchMappingService);
        verifyNoInteractions(urbanPropertyMeasureBatchMappingService);
        verifyNoInteractions(urbanPropertyConvenienceBatchMappingService);
        verifyNoInteractions(urbanPropertyMediaBatchMappingService);
        verifyNoInteractions(urbanPropertyPriceVariationBatchMappingService);
    }

    @Test
    @DisplayName("given a payload with input containing pagination when calls urbanProperties then expects")
    void givenAPayloadWithInputContainingPagination_whenCallsUrbanProperties_thenExpects() {
        graphQlTester.documentName("get-urban-properties-sample-5")
                     .execute()
                     .path("urbanProperties")
                     .matchesJsonStrictly("""
                                                  {
                                                    "rows": [
                                                      {
                                                        "id": "1"
                                                      }
                                                    ]
                                                  }
                                                  """);

        verify(urbanPropertyService, times(1)).findBy(
                assertArg(arg -> assertThat(arg).contains("rows/id")),
                assertArg(arg -> assertThat(arg)
                        .hasAllNullFieldsOrPropertiesExcept("pagination")
                        .extracting(UrbanPropertyInput::getPagination)
                        .extracting(PaginationInput::getPageNumber, PaginationInput::getPageSize)
                        .containsExactly(2, 10)
                )
        );
        verifyNoMoreInteractions(urbanPropertyService);
        verifyNoInteractions(urbanPropertyLocationBatchMappingService);
        verifyNoInteractions(urbanPropertyMeasureBatchMappingService);
        verifyNoInteractions(urbanPropertyConvenienceBatchMappingService);
        verifyNoInteractions(urbanPropertyMediaBatchMappingService);
        verifyNoInteractions(urbanPropertyPriceVariationBatchMappingService);
    }

    @Test
    @DisplayName("given a payload with projection location when calls urbanProperties then expects")
    void givenAPayloadWithProjectionLocation_whenCallsUrbanProperties_thenExpects() {
        when(urbanPropertyLocationBatchMappingService.map(anyList()))
                .thenAnswer(a -> {
                    var urbanPropertyDto = (UrbanPropertyDto) a.getArgument(0, List.class).getFirst();
                    var urbanPropertyLocationDto = UrbanPropertyLocationDto.builder()
                                                                           .id(1)
                                                                           .state("RS")
                                                                           .city("Santa Maria")
                                                                           .district("Centro")
                                                                           .zipCode("97010000")
                                                                           .streetName("Avenida Rio Branco")
                                                                           .streetNumber(1)
                                                                           .complement("Esquina com a Rua Andradas")
                                                                           .latitude(BigDecimal.valueOf(12.13456))
                                                                           .longitude(BigDecimal.valueOf(-12.123456))
                                                                           .build();
                    return Map.of(urbanPropertyDto, urbanPropertyLocationDto);
                });

        graphQlTester.documentName("get-urban-properties-sample-6")
                     .execute()
                     .path("urbanProperties")
                     .matchesJsonStrictly("""
                                                  {
                                                    "rows": [
                                                      {
                                                        "id": "1",
                                                        "location": {
                                                          "id": "1",
                                                          "state": "RS",
                                                          "city": "Santa Maria",
                                                          "district": "Centro",
                                                          "zipCode": "97010000",
                                                          "streetName": "Avenida Rio Branco",
                                                          "streetNumber": 1,
                                                          "complement": "Esquina com a Rua Andradas",
                                                          "latitude": 12.13456,
                                                          "longitude": -12.123456
                                                        }
                                                      }
                                                    ]
                                                  }
                                                  """);

        verify(urbanPropertyService, times(1)).findBy(
                assertArg(arg -> assertThat(arg).contains(
                        "rows/id",
                        "rows/location/id",
                        "rows/location/state",
                        "rows/location/city",
                        "rows/location/district",
                        "rows/location/zipCode",
                        "rows/location/streetName",
                        "rows/location/streetNumber",
                        "rows/location/complement",
                        "rows/location/latitude",
                        "rows/location/longitude"
                )),
                isNull()
        );
        verifyNoMoreInteractions(urbanPropertyService);

        verify(urbanPropertyLocationBatchMappingService, times(1)).map(
                assertArg(a -> assertThat(a)
                        .extracting("id")
                        .containsExactly(1)
                )
        );
        verifyNoMoreInteractions(urbanPropertyLocationBatchMappingService);

        verifyNoInteractions(urbanPropertyMeasureBatchMappingService);
        verifyNoInteractions(urbanPropertyConvenienceBatchMappingService);
        verifyNoInteractions(urbanPropertyMediaBatchMappingService);
        verifyNoInteractions(urbanPropertyPriceVariationBatchMappingService);
    }

    @Test
    @DisplayName("given a payload with projection measure when calls urbanProperties then expects")
    void givenAPayloadWithProjectionMeasure_whenCallsUrbanProperties_thenExpects() {
        when(urbanPropertyMeasureBatchMappingService.map(anyList()))
                .thenAnswer(a -> {
                    var urbanPropertyDto = (UrbanPropertyDto) a.getArgument(0, List.class).getFirst();
                    var urbanPropertyMeasureDto = UrbanPropertyMeasureDto.builder()
                                                                         .id(1)
                                                                         .totalArea(BigDecimal.valueOf(300))
                                                                         .privateArea(BigDecimal.valueOf(200))
                                                                         .usableArea(BigDecimal.valueOf(180))
                                                                         .terrainTotalArea(BigDecimal.valueOf(300))
                                                                         .terrainFront(BigDecimal.valueOf(30))
                                                                         .terrainBack(BigDecimal.valueOf(20))
                                                                         .terrainRight(BigDecimal.valueOf(30))
                                                                         .terrainLeft(BigDecimal.valueOf(30))
                                                                         .areaUnit("m²")
                                                                         .build();
                    return Map.of(urbanPropertyDto, urbanPropertyMeasureDto);
                });

        graphQlTester.documentName("get-urban-properties-sample-7")
                     .execute()
                     .path("urbanProperties")
                     .matchesJsonStrictly("""
                                                  {
                                                    "rows": [
                                                      {
                                                        "id": "1",
                                                        "measure": {
                                                          "id": "1",
                                                          "totalArea": 300,
                                                          "privateArea": 200,
                                                          "usableArea": 180,
                                                          "terrainTotalArea": 300,
                                                          "terrainBack": 20,
                                                          "terrainFront": 30,
                                                          "terrainLeft": 30,
                                                          "terrainRight": 30,
                                                          "areaUnit": "m²"
                                                        }
                                                      }
                                                    ]
                                                  }
                                                  """);

        verify(urbanPropertyService, times(1)).findBy(
                assertArg(arg -> assertThat(arg).contains(
                        "rows/id",
                        "rows/measure/id",
                        "rows/measure/totalArea",
                        "rows/measure/privateArea",
                        "rows/measure/usableArea",
                        "rows/measure/terrainTotalArea",
                        "rows/measure/terrainBack",
                        "rows/measure/terrainFront",
                        "rows/measure/terrainLeft",
                        "rows/measure/terrainRight",
                        "rows/measure/areaUnit"
                )),
                isNull()
        );
        verifyNoMoreInteractions(urbanPropertyService);

        verify(urbanPropertyMeasureBatchMappingService, times(1)).map(
                assertArg(a -> assertThat(a)
                        .extracting("id")
                        .containsExactly(1)
                )
        );
        verifyNoMoreInteractions(urbanPropertyMeasureBatchMappingService);

        verifyNoInteractions(urbanPropertyLocationBatchMappingService);
        verifyNoInteractions(urbanPropertyConvenienceBatchMappingService);
        verifyNoInteractions(urbanPropertyMediaBatchMappingService);
        verifyNoInteractions(urbanPropertyPriceVariationBatchMappingService);
    }

    @Test
    @DisplayName("given a payload with projection conveniences when calls urbanProperties then expects")
    void givenAPayloadWithProjectionConveniences_whenCallsUrbanProperties_thenExpects() {
        when(urbanPropertyConvenienceBatchMappingService.map(anyList()))
                .thenAnswer(a -> {
                    var urbanPropertyDto = (UrbanPropertyDto) a.getArgument(0, List.class).getFirst();
                    var urbanPropertyConvenienceDtos = List.of(
                            UrbanPropertyConvenienceDto.builder()
                                                       .id(1)
                                                       .description("PISCINA")
                                                       .build(),
                            UrbanPropertyConvenienceDto.builder()
                                                       .id(2)
                                                       .description("CHURRASQUEIRA")
                                                       .build()
                    );
                    return Map.of(urbanPropertyDto, urbanPropertyConvenienceDtos);
                });

        graphQlTester.documentName("get-urban-properties-sample-8")
                     .execute()
                     .path("urbanProperties")
                     .matchesJsonStrictly("""
                                                  {
                                                    "rows": [
                                                      {
                                                        "id": "1",
                                                        "conveniences": [
                                                          {
                                                            "id": "1",
                                                            "description": "PISCINA"
                                                          },
                                                          {
                                                            "id": "2",
                                                            "description": "CHURRASQUEIRA"
                                                          }
                                                        ]
                                                      }
                                                    ]
                                                  }
                                                  """);

        verify(urbanPropertyService, times(1)).findBy(
                assertArg(arg -> assertThat(arg).contains(
                        "rows/id",
                        "rows/conveniences/id",
                        "rows/conveniences/description"
                )),
                isNull()
        );
        verifyNoMoreInteractions(urbanPropertyService);

        verify(urbanPropertyConvenienceBatchMappingService, times(1)).map(
                assertArg(a -> assertThat(a)
                        .extracting("id")
                        .containsExactly(1)
                )
        );
        verifyNoMoreInteractions(urbanPropertyConvenienceBatchMappingService);

        verifyNoInteractions(urbanPropertyLocationBatchMappingService);
        verifyNoInteractions(urbanPropertyMeasureBatchMappingService);
        verifyNoInteractions(urbanPropertyMediaBatchMappingService);
        verifyNoInteractions(urbanPropertyPriceVariationBatchMappingService);
    }

    @Test
    @DisplayName("given a payload with projection medias when calls urbanProperties then expects")
    void givenAPayloadWithProjectionMedias_whenCallsUrbanProperties_thenExpects() {
        when(urbanPropertyMediaBatchMappingService.map(anyList()))
                .thenAnswer(a -> {
                    var urbanPropertyDto = (UrbanPropertyDto) a.getArgument(0, List.class).getFirst();
                    var urbanPropertyMediaDtos = List.of(
                            UrbanPropertyMediaDto.builder()
                                                 .id(1)
                                                 .link("http://image.link/1")
                                                 .linkThumb("http://image.link/1/thumb")
                                                 .mediaType(UrbanPropertyMediaType.IMAGE)
                                                 .extension("jpeg")
                                                 .build(),
                            UrbanPropertyMediaDto.builder()
                                                 .id(2)
                                                 .link("http://image.link/2")
                                                 .linkThumb("http://image.link/2/thumb")
                                                 .mediaType(UrbanPropertyMediaType.IMAGE)
                                                 .extension("png")
                                                 .build()
                    );
                    return Map.of(urbanPropertyDto, urbanPropertyMediaDtos);
                });

        graphQlTester.documentName("get-urban-properties-sample-9")
                     .execute()
                     .path("urbanProperties")
                     .matchesJsonStrictly("""
                                                  {
                                                    "rows": [
                                                      {
                                                        "id": "1",
                                                        "medias": [
                                                          {
                                                            "id": "1",
                                                            "link": "http://image.link/1",
                                                            "linkThumb": "http://image.link/1/thumb",
                                                            "mediaType": "IMAGE",
                                                            "extension": "jpeg"
                                                          },
                                                          {
                                                            "id": "2",
                                                            "link": "http://image.link/2",
                                                            "linkThumb": "http://image.link/2/thumb",
                                                            "mediaType": "IMAGE",
                                                            "extension": "png"
                                                          }
                                                        ]
                                                      }
                                                    ]
                                                  }
                                                  """);

        verify(urbanPropertyService, times(1)).findBy(
                assertArg(arg -> assertThat(arg).contains(
                        "rows/id",
                        "rows/medias/id",
                        "rows/medias/link",
                        "rows/medias/linkThumb",
                        "rows/medias/mediaType",
                        "rows/medias/extension"
                )),
                isNull()
        );
        verifyNoMoreInteractions(urbanPropertyService);

        verify(urbanPropertyMediaBatchMappingService, times(1)).map(
                assertArg(a -> assertThat(a)
                        .extracting("id")
                        .containsExactly(1)
                )
        );
        verifyNoMoreInteractions(urbanPropertyMediaBatchMappingService);

        verifyNoInteractions(urbanPropertyLocationBatchMappingService);
        verifyNoInteractions(urbanPropertyMeasureBatchMappingService);
        verifyNoInteractions(urbanPropertyConvenienceBatchMappingService);
        verifyNoInteractions(urbanPropertyPriceVariationBatchMappingService);
    }

    @Test
    @DisplayName("given a payload with projection price variations when calls urbanProperties then expects")
    void givenAPayloadWithProjectionPriceVariations_whenCallsUrbanProperties_thenExpects() {
        when(urbanPropertyPriceVariationBatchMappingService.map(anyList()))
                .thenAnswer(a -> {
                    var urbanPropertyDto = (UrbanPropertyDto) a.getArgument(0, List.class).getFirst();
                    var urbanPropertyPriceVariationDtos = List.of(
                            UrbanPropertyPriceVariationDto.builder()
                                                          .id(1)
                                                          .analysisDate(LocalDateTime.parse("2024-01-01T12:30:45", ISO_LOCAL_DATE_TIME))
                                                          .type(UrbanPropertyPriceVariationType.SELL)
                                                          .price(BigDecimal.valueOf(100000))
                                                          .variation(BigDecimal.valueOf(10))
                                                          .build(),
                            UrbanPropertyPriceVariationDto.builder()
                                                          .id(2)
                                                          .analysisDate(LocalDateTime.parse("2024-01-02T12:30:45", ISO_LOCAL_DATE_TIME))
                                                          .type(UrbanPropertyPriceVariationType.RENT)
                                                          .price(BigDecimal.valueOf(1000))
                                                          .variation(BigDecimal.valueOf(20))
                                                          .build()
                    );
                    return Map.of(urbanPropertyDto, urbanPropertyPriceVariationDtos);
                });

        graphQlTester.documentName("get-urban-properties-sample-10")
                     .execute()
                     .path("urbanProperties")
                     .matchesJsonStrictly("""
                                                  {
                                                    "rows": [
                                                      {
                                                        "id": "1",
                                                        "priceVariations": [
                                                          {
                                                            "id": "1",
                                                            "analysisDate": "2024-01-01T12:30:45",
                                                            "type": "SELL",
                                                            "price": 100000,
                                                            "variation": 10
                                                          },
                                                          {
                                                            "id": "2",
                                                            "analysisDate": "2024-01-02T12:30:45",
                                                            "type": "RENT",
                                                            "price": 1000,
                                                            "variation": 20
                                                          }
                                                        ]
                                                      }
                                                    ]
                                                  }
                                                  """);

        verify(urbanPropertyService, times(1)).findBy(
                assertArg(arg -> assertThat(arg).contains(
                        "rows/id",
                        "rows/priceVariations/id",
                        "rows/priceVariations/analysisDate",
                        "rows/priceVariations/type",
                        "rows/priceVariations/price",
                        "rows/priceVariations/variation"
                )),
                isNull()
        );
        verifyNoMoreInteractions(urbanPropertyService);

        verify(urbanPropertyPriceVariationBatchMappingService, times(1)).map(
                assertArg(a -> assertThat(a)
                        .extracting("id")
                        .containsExactly(1)
                )
        );
        verifyNoMoreInteractions(urbanPropertyPriceVariationBatchMappingService);

        verifyNoInteractions(urbanPropertyLocationBatchMappingService);
        verifyNoInteractions(urbanPropertyMeasureBatchMappingService);
        verifyNoInteractions(urbanPropertyConvenienceBatchMappingService);
        verifyNoInteractions(urbanPropertyMediaBatchMappingService);
    }

}
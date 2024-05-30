package br.com.houseseeker.service;

import br.com.houseseeker.domain.property.UrbanPropertyPriceVariationType;
import br.com.houseseeker.domain.proto.UrbanPropertyData;
import br.com.houseseeker.domain.proto.UrbanPropertyPriceVariationData;
import br.com.houseseeker.service.proto.GetUrbanPropertyPriceVariationsRequest.OrdersData;
import br.com.houseseeker.service.proto.GetUrbanPropertyPriceVariationsResponse;
import br.com.houseseeker.service.proto.UrbanPropertyPriceVariationDataServiceGrpc.UrbanPropertyPriceVariationDataServiceBlockingStub;
import com.google.protobuf.DoubleValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.normalizeSpace;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = UrbanPropertyPriceVariationService.class)
@ExtendWith(MockitoExtension.class)
class UrbanPropertyPriceVariationServiceTest {

    @Autowired
    private UrbanPropertyPriceVariationService urbanPropertyPriceVariationService;

    @Mock
    private UrbanPropertyPriceVariationDataServiceBlockingStub mockedUrbanPropertyPriceVariationDataServiceBlockingStub;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(
                urbanPropertyPriceVariationService,
                "urbanPropertyPriceVariationDataServiceBlockingStub",
                mockedUrbanPropertyPriceVariationDataServiceBlockingStub
        );
    }

    @Test
    @DisplayName("given a list of property ids when calls findByUrbanProperties then expects")
    void givenAListOfPropertyIds_whenCallsFindByUrbanProperties_thenExpects() {
        var response = GetUrbanPropertyPriceVariationsResponse.newBuilder()
                                                              .addAllUrbanPropertyPriceVariations(List.of(
                                                                      UrbanPropertyPriceVariationData
                                                                              .newBuilder()
                                                                              .setId(Int32Value.of(101))
                                                                              .setUrbanProperty(UrbanPropertyData.getDefaultInstance())
                                                                              .setAnalysisDate(StringValue.of("2024-01-01T12:30:45"))
                                                                              .setType(StringValue.of(UrbanPropertyPriceVariationType.RENT.name()))
                                                                              .setPrice(DoubleValue.of(1000))
                                                                              .setVariation(DoubleValue.of(0))
                                                                              .build()
                                                              ))
                                                              .build();

        when(mockedUrbanPropertyPriceVariationDataServiceBlockingStub.getUrbanPropertyPriceVariations(any()))
                .thenReturn(response);

        assertThat(urbanPropertyPriceVariationService.findByUrbanProperties(List.of(1, 2))).isEqualTo(response);

        verify(mockedUrbanPropertyPriceVariationDataServiceBlockingStub, times(1)).getUrbanPropertyPriceVariations(
                assertArg(a -> assertThat(a)
                        .satisfies(r -> assertThat(r.getProjections())
                                .extracting(p -> normalizeSpace(p.toString()))
                                .isEqualTo(
                                        "id: true urban_property: true analysis_date: true type: true " +
                                                "price: true variation: true"
                                )
                        )
                        .satisfies(r -> assertThat(r.getClauses())
                                .extracting(p -> normalizeSpace(p.toString()))
                                .isEqualTo("urban_property_id { is_in { values: 1 values: 2 } }")
                        )
                        .satisfies(r -> assertThat(r.getOrders()).isEqualTo(OrdersData.getDefaultInstance()))
                        .satisfies(r -> assertThat(r.getPagination())
                                .extracting(p -> normalizeSpace(p.toString()))
                                .isEqualTo("pageSize: 2147483647 pageNumber: 1")
                        )
                )
        );
        verifyNoMoreInteractions(mockedUrbanPropertyPriceVariationDataServiceBlockingStub);
    }

}
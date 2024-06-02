package br.com.houseseeker.service;

import br.com.houseseeker.domain.proto.UrbanPropertyConvenienceData;
import br.com.houseseeker.domain.proto.UrbanPropertyData;
import br.com.houseseeker.service.proto.GetUrbanPropertyConveniencesRequest.OrdersData;
import br.com.houseseeker.service.proto.GetUrbanPropertyConveniencesResponse;
import br.com.houseseeker.service.proto.UrbanPropertyConvenienceDataServiceGrpc.UrbanPropertyConvenienceDataServiceBlockingStub;
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

@SpringBootTest(classes = UrbanPropertyConvenienceService.class)
@ExtendWith(MockitoExtension.class)
class UrbanPropertyConvenienceServiceTest {

    @Autowired
    private UrbanPropertyConvenienceService urbanPropertyConvenienceService;

    @Mock
    private UrbanPropertyConvenienceDataServiceBlockingStub mockedUrbanPropertyConvenienceDataServiceBlockingStub;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(
                urbanPropertyConvenienceService,
                "urbanPropertyConvenienceDataServiceBlockingStub",
                mockedUrbanPropertyConvenienceDataServiceBlockingStub
        );
    }

    @Test
    @DisplayName("given a list of property ids when calls findByUrbanProperties then expects")
    void givenAListOfPropertyIds_whenCallsFindByUrbanProperties_thenExpects() {
        var response = GetUrbanPropertyConveniencesResponse.newBuilder()
                                                           .addAllUrbanPropertyConveniences(List.of(
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
                                                           ))
                                                           .build();

        when(mockedUrbanPropertyConvenienceDataServiceBlockingStub.getUrbanPropertyConveniences(any()))
                .thenReturn(response);

        assertThat(urbanPropertyConvenienceService.findByUrbanProperties(List.of(1, 2))).isEqualTo(response);

        verify(mockedUrbanPropertyConvenienceDataServiceBlockingStub, times(1)).getUrbanPropertyConveniences(
                assertArg(a -> assertThat(a)
                        .satisfies(r -> assertThat(r.getProjections())
                                .extracting(p -> normalizeSpace(p.toString()))
                                .isEqualTo("id: true urban_property: true description: true")
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
        verifyNoMoreInteractions(mockedUrbanPropertyConvenienceDataServiceBlockingStub);
    }

}
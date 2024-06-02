package br.com.houseseeker.service;

import br.com.houseseeker.domain.property.UrbanPropertyMediaType;
import br.com.houseseeker.domain.proto.UrbanPropertyData;
import br.com.houseseeker.domain.proto.UrbanPropertyMediaData;
import br.com.houseseeker.service.proto.GetUrbanPropertyMediasRequest.OrdersData;
import br.com.houseseeker.service.proto.GetUrbanPropertyMediasResponse;
import br.com.houseseeker.service.proto.UrbanPropertyMediaDataServiceGrpc.UrbanPropertyMediaDataServiceBlockingStub;
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

@SpringBootTest(classes = UrbanPropertyMediaService.class)
@ExtendWith(MockitoExtension.class)
class UrbanPropertyMediaServiceTest {

    @Autowired
    private UrbanPropertyMediaService urbanPropertyMediaService;

    @Mock
    private UrbanPropertyMediaDataServiceBlockingStub mockedUrbanPropertyMediaDataServiceBlockingStub;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(
                urbanPropertyMediaService,
                "urbanPropertyMediaDataServiceBlockingStub",
                mockedUrbanPropertyMediaDataServiceBlockingStub
        );
    }

    @Test
    @DisplayName("given a list of property ids when calls findByUrbanProperties then expects")
    void givenAListOfPropertyIds_whenCallsFindByUrbanProperties_thenExpects() {
        var response = GetUrbanPropertyMediasResponse.newBuilder()
                                                     .addAllUrbanPropertyMedias(List.of(
                                                             UrbanPropertyMediaData.newBuilder()
                                                                                   .setId(Int32Value.of(101))
                                                                                   .setUrbanProperty(UrbanPropertyData.getDefaultInstance())
                                                                                   .setLink(StringValue.of("http://localhost/img.jpg"))
                                                                                   .setLinkThumb(StringValue.of("http://localhost/img-thumb.jpg"))
                                                                                   .setMediaType(StringValue.of(UrbanPropertyMediaType.IMAGE.name()))
                                                                                   .setExtension(StringValue.of("jpg"))
                                                                                   .build()
                                                     ))
                                                     .build();

        when(mockedUrbanPropertyMediaDataServiceBlockingStub.getUrbanPropertyMedias(any()))
                .thenReturn(response);

        assertThat(urbanPropertyMediaService.findByUrbanProperties(List.of(1, 2))).isEqualTo(response);

        verify(mockedUrbanPropertyMediaDataServiceBlockingStub, times(1)).getUrbanPropertyMedias(
                assertArg(a -> assertThat(a)
                        .satisfies(r -> assertThat(r.getProjections())
                                .extracting(p -> normalizeSpace(p.toString()))
                                .isEqualTo(
                                        "id: true urban_property: true link: true link_thumb: true " +
                                                "media_type: true extension: true"
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
        verifyNoMoreInteractions(mockedUrbanPropertyMediaDataServiceBlockingStub);
    }

}
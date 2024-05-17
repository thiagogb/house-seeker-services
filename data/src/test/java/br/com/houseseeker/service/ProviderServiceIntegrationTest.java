package br.com.houseseeker.service;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.domain.exception.GrpcStatusException;
import br.com.houseseeker.domain.proto.EnumComparisonData;
import br.com.houseseeker.domain.proto.EnumListComparisonData;
import br.com.houseseeker.domain.proto.Int32ComparisonData;
import br.com.houseseeker.domain.proto.Int32SingleComparisonData;
import br.com.houseseeker.domain.proto.OrderDetailsData;
import br.com.houseseeker.domain.proto.OrderDirectionData;
import br.com.houseseeker.domain.proto.ProviderData;
import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.entity.QDslUrbanProperty;
import br.com.houseseeker.entity.QDslUrbanPropertyConvenience;
import br.com.houseseeker.entity.QDslUrbanPropertyLocation;
import br.com.houseseeker.entity.QDslUrbanPropertyMeasure;
import br.com.houseseeker.entity.QDslUrbanPropertyMedia;
import br.com.houseseeker.entity.QDslUrbanPropertyPriceVariation;
import br.com.houseseeker.mock.ProviderDataMocks;
import br.com.houseseeker.service.proto.GetProvidersDataRequest;
import br.com.houseseeker.service.proto.GetProvidersDataRequest.ClausesData;
import br.com.houseseeker.service.proto.GetProvidersDataRequest.OrdersData;
import br.com.houseseeker.service.proto.GetProvidersDataRequest.ProjectionsData;
import com.google.protobuf.Int32Value;
import io.grpc.Status;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;

import static br.com.houseseeker.domain.provider.ProviderMechanism.JETIMOB_V2;
import static br.com.houseseeker.domain.provider.ProviderMechanism.JETIMOB_V3;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

class ProviderServiceIntegrationTest extends AbstractJpaIntegrationTest {

    private static final int TEST_PROVIDER_10000 = 10000;
    private static final int TEST_PROVIDER_10002 = 10002;

    private static final ProviderData TEST_PROVIDER_DATA = ProviderDataMocks.testProviderWithId(1);

    private static final String[] EXTRACTED_ATTRIBUTES = new String[]{
            "name", "siteUrl", "dataUrl", "mechanism", "params", "cronExpression", "logo", "active"
    };

    @Autowired
    private ProviderService providerService;

    @BeforeEach
    @Override
    public void setup() {
        super.setup();
        Locale.setDefault(Locale.ENGLISH);
    }

    @Test
    @DisplayName("given a existing provider when calls findById then expects present")
    void givenAExistingProvider_whenCallsFindById_thenExpectsPresent() {
        assertThat(providerService.findById(TEST_PROVIDER_10002)).isPresent();
    }

    @Test
    @DisplayName("given a non existing provider when calls findById then expects empty")
    void givenANonExistingProvider_whenCallsFindById_thenExpectsEmpty() {
        assertThat(providerService.findById(99999)).isEmpty();
    }

    @Test
    @DisplayName("given a proto request when calls findBy then expects")
    void givenAProtoRequest_whenCallsFindBy_thenExpects() {
        var request = GetProvidersDataRequest.newBuilder()
                                             .setProjections(
                                                     ProjectionsData.newBuilder()
                                                                    .setId(true)
                                                                    .setName(true)
                                                                    .setMechanism(true)
                                                                    .build()
                                             )
                                             .setClauses(
                                                     ClausesData.newBuilder()
                                                                .setId(
                                                                        Int32ComparisonData.newBuilder()
                                                                                           .setIsGreaterOrEqual(
                                                                                                   Int32SingleComparisonData.newBuilder()
                                                                                                                            .setValue(10003)
                                                                                                                            .build()
                                                                                           )
                                                                                           .build()
                                                                )
                                                                .setMechanism(
                                                                        EnumComparisonData.newBuilder()
                                                                                          .setIsIn(
                                                                                                  EnumListComparisonData.newBuilder()
                                                                                                                        .addValues(JETIMOB_V2.name())
                                                                                                                        .addValues(JETIMOB_V3.name())
                                                                                                                        .build()
                                                                                          )
                                                                                          .build()
                                                                )
                                                                .build()
                                             )
                                             .setOrders(
                                                     OrdersData.newBuilder()
                                                               .setMechanism(
                                                                       OrderDetailsData.newBuilder()
                                                                                       .setIndex(1)
                                                                                       .setDirection(OrderDirectionData.DESC)
                                                                                       .build()
                                                               )
                                                               .build()
                                             )
                                             .build();

        var result = providerService.findBy(request);

        assertThat(result.getContent())
                .extracting("id", "name", "mechanism")
                .containsExactly(
                        tuple(10004, "Luiz Coelho Imóveis", JETIMOB_V3),
                        tuple(10003, "Maiquel Oliveira", JETIMOB_V2)
                );
    }

    @Test
    @DisplayName("given a proto data when calls insert then expects")
    void givenAProtoData_whenCallsInsert_thenExpects() {
        assertThat(providerService.insert(TEST_PROVIDER_DATA))
                .hasNoNullFieldsOrProperties()
                .extracting(EXTRACTED_ATTRIBUTES)
                .containsExactly(
                        TEST_PROVIDER_DATA.getName().getValue(),
                        TEST_PROVIDER_DATA.getSiteUrl().getValue(),
                        TEST_PROVIDER_DATA.getDataUrl().getValue(),
                        ProviderMechanism.JETIMOB_V1,
                        TEST_PROVIDER_DATA.getParams().getValue(),
                        TEST_PROVIDER_DATA.getCronExpression().getValue(),
                        TEST_PROVIDER_DATA.getLogo().getValue().toByteArray(),
                        TEST_PROVIDER_DATA.getActive().getValue()
                );
    }

    @Test
    @DisplayName("given a invalid proto data when calls insert then expects exception")
    void givenAInvalidProtoData_whenCallsInsert_thenExpectsException() {
        var providerData = ProviderData.getDefaultInstance();

        assertThatThrownBy(() -> providerService.insert(providerData))
                .isInstanceOf(GrpcStatusException.class)
                .hasFieldOrPropertyWithValue("status", Status.INVALID_ARGUMENT)
                .satisfies(e -> {
                    String expected = """
                            [
                              {
                                "defaultMessage": "must not be null",
                                "field": "mechanism"
                              },
                              {
                                "defaultMessage": "must not be blank",
                                "field": "name"
                              },
                              {
                                "defaultMessage": "must not be blank",
                                "field": "siteUrl"
                              },
                              {
                                "defaultMessage": "must not be null",
                                "field": "active"
                              }
                            ]
                            """;
                    JSONAssert.assertEquals(expected, e.getMessage(), false);
                });
    }

    @Test
    @DisplayName("given a non existing provider id when calls update then expects exception")
    void givenANonExistingProviderId_whenCallsUpdate_thenExpectsException() {
        var providerData = TEST_PROVIDER_DATA.toBuilder().setId(Int32Value.of(999999)).build();

        assertThatThrownBy(() -> providerService.update(providerData))
                .isInstanceOf(GrpcStatusException.class)
                .hasFieldOrPropertyWithValue("status", Status.NOT_FOUND)
                .hasMessage("Provider 999999 not found");
    }

    @Test
    @DisplayName("given a invalid proto data when calls update then expects exception")
    void givenAInvalidProtoData_whenCallsUpdate_thenExpectsException() {
        var providerData = ProviderData.newBuilder()
                                       .setId(Int32Value.of(TEST_PROVIDER_10002))
                                       .build();

        assertThatThrownBy(() -> providerService.update(providerData))
                .isInstanceOf(GrpcStatusException.class)
                .hasFieldOrPropertyWithValue("status", Status.INVALID_ARGUMENT)
                .satisfies(e -> {
                    String expected = """
                            [
                              {
                                "defaultMessage": "must not be null",
                                "field": "mechanism"
                              },
                              {
                                "defaultMessage": "must not be blank",
                                "field": "name"
                              },
                              {
                                "defaultMessage": "must not be blank",
                                "field": "siteUrl"
                              },
                              {
                                "defaultMessage": "must not be null",
                                "field": "active"
                              }
                            ]
                            """;
                    JSONAssert.assertEquals(expected, e.getMessage(), false);
                });
    }

    @Test
    @DisplayName("given a proto data when calls update then expects")
    void givenAProtoData_whenCallsUpdate_thenExpects() {
        var providerData = TEST_PROVIDER_DATA.toBuilder().setId(Int32Value.of(TEST_PROVIDER_10002)).build();

        assertThat(providerService.update(providerData))
                .hasNoNullFieldsOrProperties()
                .extracting(ArrayUtils.insert(0, EXTRACTED_ATTRIBUTES, "id"))
                .containsExactly(
                        TEST_PROVIDER_10002,
                        TEST_PROVIDER_DATA.getName().getValue(),
                        TEST_PROVIDER_DATA.getSiteUrl().getValue(),
                        TEST_PROVIDER_DATA.getDataUrl().getValue(),
                        ProviderMechanism.JETIMOB_V1,
                        TEST_PROVIDER_DATA.getParams().getValue(),
                        TEST_PROVIDER_DATA.getCronExpression().getValue(),
                        TEST_PROVIDER_DATA.getLogo().getValue().toByteArray(),
                        TEST_PROVIDER_DATA.getActive().getValue()
                );
    }

    @Test
    @DisplayName("given a existing provider with associated data when calls wipe then expects")
    void givenAExistingProviderWithAssociatedData_whenCallsWipe_thenExpects() {
        providerService.wipe(TEST_PROVIDER_10000);

        assertThat(
                countBy(
                        QDslUrbanProperty.urbanProperty,
                        QDslUrbanProperty.urbanProperty.provider.id.eq(TEST_PROVIDER_10000)
                )
        ).isZero();

        assertThat(
                countBy(
                        QDslUrbanPropertyLocation.urbanPropertyLocation,
                        QDslUrbanPropertyLocation.urbanPropertyLocation.urbanProperty.provider.id.eq(TEST_PROVIDER_10000)
                )
        ).isZero();

        assertThat(
                countBy(
                        QDslUrbanPropertyMeasure.urbanPropertyMeasure,
                        QDslUrbanPropertyMeasure.urbanPropertyMeasure.urbanProperty.provider.id.eq(TEST_PROVIDER_10000)
                )
        ).isZero();

        assertThat(
                countBy(
                        QDslUrbanPropertyConvenience.urbanPropertyConvenience,
                        QDslUrbanPropertyConvenience.urbanPropertyConvenience.urbanProperty.provider.id.eq(TEST_PROVIDER_10000)
                )
        ).isZero();

        assertThat(
                countBy(
                        QDslUrbanPropertyMedia.urbanPropertyMedia,
                        QDslUrbanPropertyMedia.urbanPropertyMedia.urbanProperty.provider.id.eq(TEST_PROVIDER_10000)
                )
        ).isZero();

        assertThat(
                countBy(
                        QDslUrbanPropertyPriceVariation.urbanPropertyPriceVariation,
                        QDslUrbanPropertyPriceVariation.urbanPropertyPriceVariation.urbanProperty.provider.id.eq(TEST_PROVIDER_10000)
                )
        ).isZero();
    }

    @Test
    @DisplayName("given a non existing provider when calls wipe then expects exception")
    void givenANonExistingProvider_whenCallsWipe_thenExpectsException() {
        assertThatThrownBy(() -> providerService.wipe(999999))
                .isInstanceOf(GrpcStatusException.class)
                .hasFieldOrPropertyWithValue("status", Status.NOT_FOUND)
                .hasMessage("Provider 999999 not found");
    }

}
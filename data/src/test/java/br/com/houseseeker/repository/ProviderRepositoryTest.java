package br.com.houseseeker.repository;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.domain.exception.GrpcStatusException;
import br.com.houseseeker.domain.proto.BoolComparisonData;
import br.com.houseseeker.domain.proto.BoolSingleComparisonData;
import br.com.houseseeker.domain.proto.BytesComparisonData;
import br.com.houseseeker.domain.proto.ClauseOperator;
import br.com.houseseeker.domain.proto.EnumComparisonData;
import br.com.houseseeker.domain.proto.EnumListComparisonData;
import br.com.houseseeker.domain.proto.Int32ComparisonData;
import br.com.houseseeker.domain.proto.Int32SingleComparisonData;
import br.com.houseseeker.domain.proto.OrderDetailsData;
import br.com.houseseeker.domain.proto.OrderDirectionData;
import br.com.houseseeker.domain.proto.PaginationRequestData;
import br.com.houseseeker.domain.proto.StringComparisonData;
import br.com.houseseeker.domain.proto.StringSingleComparisonData;
import br.com.houseseeker.domain.provider.ProviderMechanism;
import br.com.houseseeker.entity.Provider;
import br.com.houseseeker.service.proto.GetProvidersDataRequest;
import br.com.houseseeker.service.proto.GetProvidersDataRequest.ClausesData;
import br.com.houseseeker.service.proto.GetProvidersDataRequest.OrdersData;
import br.com.houseseeker.service.proto.GetProvidersDataRequest.ProjectionsData;
import io.grpc.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProviderRepositoryTest extends AbstractJpaIntegrationTest {

    @Autowired
    private ProviderRepository providerRepository;

    @Test
    @DisplayName("given a proto request without projects when calls findBy then expects exception")
    void givenAProtoRequestWithoutProjections_whenCallsFindBy_thenExpectsException() {
        var request = GetProvidersDataRequest.getDefaultInstance();

        assertThatThrownBy(() -> providerRepository.findBy(request))
                .isInstanceOf(GrpcStatusException.class)
                .hasFieldOrPropertyWithValue("status", Status.INVALID_ARGUMENT)
                .hasMessage("At least one projection must be defined");
    }

    @Test
    @DisplayName("given a proto request with projections id and name when calls findBy then expects")
    void givenAProtoRequestWithProjectionsIdAndName_whenCallsFindBy_thenExpects() {
        var request = GetProvidersDataRequest.newBuilder()
                                             .setProjections(
                                                     ProjectionsData.newBuilder()
                                                                    .setId(true)
                                                                    .setName(true)
                                                                    .build()
                                             )
                                             .setPagination(
                                                     PaginationRequestData.newBuilder()
                                                                          .setPageSize(1)
                                                                          .build()
                                             )
                                             .build();

        Page<Provider> result = providerRepository.findBy(request);

        assertThat(result.getContent())
                .hasSize(1)
                .first()
                .satisfies(r -> assertThat(r)
                        .hasAllNullFieldsOrPropertiesExcept("id", "name")
                        .extracting("id", "name")
                        .containsExactly(10000, "Oliveira Imóveis")
                );
    }

    @Test
    @DisplayName("given a proto request with remaining projections when calls findBy then expects")
    void givenAProtoRequestWithRemainingProjections_whenCallsFindBy_thenExpects() {
        var request = GetProvidersDataRequest.newBuilder()
                                             .setProjections(
                                                     ProjectionsData.newBuilder()
                                                                    .setSiteUrl(true)
                                                                    .setDataUrl(true)
                                                                    .setMechanism(true)
                                                                    .setParams(true)
                                                                    .setCronExpression(true)
                                                                    .setLogo(true)
                                                                    .setActive(true)
                                                                    .build()
                                             )
                                             .setPagination(
                                                     PaginationRequestData.newBuilder()
                                                                          .setPageSize(1)
                                                                          .build()
                                             )
                                             .build();

        Page<Provider> result = providerRepository.findBy(request);

        assertThat(result.getContent())
                .hasSize(1)
                .first()
                .satisfies(r -> assertThat(r)
                        .hasFieldOrPropertyWithValue("id", null)
                        .hasFieldOrPropertyWithValue("name", null)
                        .extracting("siteUrl", "dataUrl", "mechanism", "params", "cronExpression", "logo", "active")
                        .containsExactly(
                                "https://www.oliveiraimoveissm.com.br",
                                "https://www.oliveiraimoveissm.com.br/imoveis/a-venda/",
                                ProviderMechanism.JETIMOB_V1,
                                null,
                                "0 0 0 ? * * *",
                                null,
                                true
                        )
                );
    }

    @Test
    @DisplayName("given a proto request with clause for id when calls findBy then expects")
    void givenAProtoRequestWithClauseForId_whenCallsFindBy_thenExpects() {
        var request = GetProvidersDataRequest.newBuilder()
                                             .setProjections(
                                                     ProjectionsData.newBuilder()
                                                                    .setId(true)
                                                                    .build()
                                             )
                                             .addClauses(
                                                     ClausesData.newBuilder()
                                                                .setId(
                                                                        Int32ComparisonData.newBuilder()
                                                                                           .setIsEqual(
                                                                                                   Int32SingleComparisonData.newBuilder()
                                                                                                                            .setValue(10000)
                                                                                                                            .build()
                                                                                           )
                                                                                           .build()
                                                                )
                                                                .build()
                                             )
                                             .setPagination(
                                                     PaginationRequestData.newBuilder()
                                                                          .setPageSize(1)
                                                                          .build()
                                             )
                                             .build();

        Page<Provider> result = providerRepository.findBy(request);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("given a proto request with clause for name when calls findBy then expects")
    void givenAProtoRequestWithClauseForName_whenCallsFindBy_thenExpects() {
        var request = GetProvidersDataRequest.newBuilder()
                                             .setProjections(
                                                     ProjectionsData.newBuilder()
                                                                    .setId(true)
                                                                    .build()
                                             )
                                             .addClauses(
                                                     ClausesData.newBuilder()
                                                                .setName(
                                                                        StringComparisonData.newBuilder()
                                                                                            .setIsEndingWith(
                                                                                                    StringSingleComparisonData.newBuilder()
                                                                                                                              .setValue("Imóveis")
                                                                                                                              .build()
                                                                                            )
                                                                                            .build()
                                                                )
                                                                .build()
                                             )
                                             .build();

        Page<Provider> result = providerRepository.findBy(request);

        assertThat(result.getContent()).hasSize(5);
    }

    @Test
    @DisplayName("given a proto request with clause for siteUrl when calls findBy then expects")
    void givenAProtoRequestWithClauseForSiteUrl_whenCallsFindBy_thenExpects() {
        var request = GetProvidersDataRequest.newBuilder()
                                             .setProjections(
                                                     ProjectionsData.newBuilder()
                                                                    .setId(true)
                                                                    .build()
                                             )
                                             .addClauses(
                                                     ClausesData.newBuilder()
                                                                .setSiteUrl(
                                                                        StringComparisonData.newBuilder()
                                                                                            .setItContains(
                                                                                                    StringSingleComparisonData.newBuilder()
                                                                                                                              .setValue("imoveis")
                                                                                                                              .build()
                                                                                            )
                                                                                            .build()
                                                                )
                                                                .build()
                                             )
                                             .build();

        Page<Provider> result = providerRepository.findBy(request);

        assertThat(result.getContent()).hasSize(5);
    }

    @Test
    @DisplayName("given a proto request with clause for dataUrl when calls findBy then expects")
    void givenAProtoRequestWithClauseForDataUrl_whenCallsFindBy_thenExpects() {
        var request = GetProvidersDataRequest.newBuilder()
                                             .setProjections(
                                                     ProjectionsData.newBuilder()
                                                                    .setId(true)
                                                                    .build()
                                             )
                                             .addClauses(
                                                     ClausesData.newBuilder()
                                                                .setDataUrl(
                                                                        StringComparisonData.newBuilder()
                                                                                            .setIsNull(true)
                                                                                            .build()
                                                                )
                                                                .build()
                                             )
                                             .build();

        Page<Provider> result = providerRepository.findBy(request);

        assertThat(result.getContent()).hasSize(3);
    }

    @Test
    @DisplayName("given a proto request with clause for mechanism when calls findBy then expects")
    void givenAProtoRequestWithClauseForMechanism_whenCallsFindBy_thenExpects() {
        var request = GetProvidersDataRequest.newBuilder()
                                             .setProjections(
                                                     ProjectionsData.newBuilder()
                                                                    .setId(true)
                                                                    .build()
                                             )
                                             .addClauses(
                                                     ClausesData.newBuilder()
                                                                .setMechanism(
                                                                        EnumComparisonData.newBuilder()
                                                                                          .setIsIn(
                                                                                                  EnumListComparisonData.newBuilder()
                                                                                                                        .addAllValues(List.of(
                                                                                                                                ProviderMechanism.JETIMOB_V1.name(),
                                                                                                                                ProviderMechanism.JETIMOB_V4.name()
                                                                                                                        ))
                                                                                                                        .build()
                                                                                          )
                                                                                          .build()
                                                                )
                                                                .build()
                                             )
                                             .build();

        Page<Provider> result = providerRepository.findBy(request);

        assertThat(result.getContent()).hasSize(2);
    }

    @Test
    @DisplayName("given a proto request with clause for logo when calls findBy then expects")
    void givenAProtoRequestWithClauseForLogo_whenCallsFindBy_thenExpects() {
        var request = GetProvidersDataRequest.newBuilder()
                                             .setProjections(
                                                     ProjectionsData.newBuilder()
                                                                    .setId(true)
                                                                    .build()
                                             )
                                             .addClauses(
                                                     ClausesData.newBuilder()
                                                                .setLogo(
                                                                        BytesComparisonData.newBuilder()
                                                                                           .setIsNotNull(true)
                                                                                           .build()
                                                                )
                                                                .build()
                                             )
                                             .build();

        Page<Provider> result = providerRepository.findBy(request);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    @DisplayName("given a proto request with clause for cronExpression when calls findBy then expects")
    void givenAProtoRequestWithClauseForCronExpression_whenCallsFindBy_thenExpects() {
        var request = GetProvidersDataRequest.newBuilder()
                                             .setProjections(
                                                     ProjectionsData.newBuilder()
                                                                    .setId(true)
                                                                    .build()
                                             )
                                             .addClauses(
                                                     ClausesData.newBuilder()
                                                                .setCronExpression(
                                                                        StringComparisonData.newBuilder()
                                                                                            .setIsStartingWith(
                                                                                                    StringSingleComparisonData.newBuilder()
                                                                                                                              .setValue("0 0 0")
                                                                                                                              .build()
                                                                                            )
                                                                                            .build()
                                                                )
                                                                .build()
                                             )
                                             .build();

        Page<Provider> result = providerRepository.findBy(request);

        assertThat(result.getContent()).hasSize(7);
    }

    @Test
    @DisplayName("given a proto request with clause for params when calls findBy then expects")
    void givenAProtoRequestWithClauseForParams_whenCallsFindBy_thenExpects() {
        var request = GetProvidersDataRequest.newBuilder()
                                             .setProjections(
                                                     ProjectionsData.newBuilder()
                                                                    .setId(true)
                                                                    .build()
                                             )
                                             .addClauses(
                                                     ClausesData.newBuilder()
                                                                .setParams(
                                                                        StringComparisonData.newBuilder()
                                                                                            .setItContains(
                                                                                                    StringSingleComparisonData.newBuilder()
                                                                                                                              .setValue("jsonFile")
                                                                                                                              .build()
                                                                                            )
                                                                                            .build()
                                                                )
                                                                .build()
                                             )
                                             .build();

        Page<Provider> result = providerRepository.findBy(request);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("given a proto request with clause for active when calls findBy then expects")
    void givenAProtoRequestWithClauseForActive_whenCallsFindBy_thenExpects() {
        var request = GetProvidersDataRequest.newBuilder()
                                             .setProjections(
                                                     ProjectionsData.newBuilder()
                                                                    .setId(true)
                                                                    .build()
                                             )
                                             .addClauses(
                                                     ClausesData.newBuilder()
                                                                .setActive(
                                                                        BoolComparisonData.newBuilder()
                                                                                          .setIsNotEqual(
                                                                                                  BoolSingleComparisonData.newBuilder()
                                                                                                                          .setValue(true)
                                                                                                                          .build()
                                                                                          )
                                                                                          .build()
                                                                )
                                                                .build()
                                             )
                                             .build();

        Page<Provider> result = providerRepository.findBy(request);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    @DisplayName("given a proto request with order for id desc when calls findBy then expects")
    void givenAProtoRequestWithOrderForIdDesc_whenCallsFindBy_thenExpects() {
        var request = GetProvidersDataRequest.newBuilder()
                                             .setProjections(
                                                     ProjectionsData.newBuilder()
                                                                    .setId(true)
                                                                    .build()
                                             )
                                             .setOrders(
                                                     OrdersData.newBuilder()
                                                               .setId(
                                                                       OrderDetailsData.newBuilder()
                                                                                       .setIndex(1)
                                                                                       .setDirection(OrderDirectionData.DESC)
                                                                                       .build()
                                                               )
                                                               .build()
                                             )
                                             .setPagination(
                                                     PaginationRequestData.newBuilder()
                                                                          .setPageSize(3)
                                                                          .build()
                                             )
                                             .build();

        Page<Provider> result = providerRepository.findBy(request);

        assertThat(result.getContent())
                .extracting("id")
                .containsExactly(10006, 10005, 10004);
    }

    @Test
    @DisplayName("given a proto request with order for name desc when calls findBy then expects")
    void givenAProtoRequestWithOrderForNameDesc_whenCallsFindBy_thenExpects() {
        var request = GetProvidersDataRequest.newBuilder()
                                             .setProjections(
                                                     ProjectionsData.newBuilder()
                                                                    .setName(true)
                                                                    .build()
                                             )
                                             .setOrders(
                                                     OrdersData.newBuilder()
                                                               .setName(
                                                                       OrderDetailsData.newBuilder()
                                                                                       .setIndex(1)
                                                                                       .setDirection(OrderDirectionData.DESC)
                                                                                       .build()
                                                               )
                                                               .build()
                                             )
                                             .setPagination(
                                                     PaginationRequestData.newBuilder()
                                                                          .setPageSize(3)
                                                                          .build()
                                             )
                                             .build();

        Page<Provider> result = providerRepository.findBy(request);

        assertThat(result.getContent())
                .extracting("name")
                .containsExactly("Oliveira Imóveis", "Maiquel Oliveira", "Luiz Coelho Imóveis");
    }

    @Test
    @DisplayName("given a proto request with order for siteUrl desc when calls findBy then expects")
    void givenAProtoRequestWithOrderForSiteUrlDesc_whenCallsFindBy_thenExpects() {
        var request = GetProvidersDataRequest.newBuilder()
                                             .setProjections(
                                                     ProjectionsData.newBuilder()
                                                                    .setSiteUrl(true)
                                                                    .build()
                                             )
                                             .setOrders(
                                                     OrdersData.newBuilder()
                                                               .setSiteUrl(
                                                                       OrderDetailsData.newBuilder()
                                                                                       .setIndex(1)
                                                                                       .setDirection(OrderDirectionData.DESC)
                                                                                       .build()
                                                               )
                                                               .build()
                                             )
                                             .setPagination(
                                                     PaginationRequestData.newBuilder()
                                                                          .setPageSize(3)
                                                                          .build()
                                             )
                                             .build();

        Page<Provider> result = providerRepository.findBy(request);

        assertThat(result.getContent())
                .extracting("siteUrl")
                .containsExactly(
                        "https://www.oliveiraimoveissm.com.br",
                        "https://www.luizcoelhoimoveis.com.br",
                        "https://www.invistaimoveissm.com.br"
                );
    }

    @Test
    @DisplayName("given a proto request with order for dataUrl desc when calls findBy then expects")
    void givenAProtoRequestWithOrderForDataUrlDesc_whenCallsFindBy_thenExpects() {
        var request = GetProvidersDataRequest.newBuilder()
                                             .setProjections(
                                                     ProjectionsData.newBuilder()
                                                                    .setDataUrl(true)
                                                                    .build()
                                             )
                                             .addClauses(
                                                     ClausesData.newBuilder()
                                                                .setDataUrl(
                                                                        StringComparisonData.newBuilder()
                                                                                            .setIsNotNull(true)
                                                                                            .build()
                                                                )
                                                                .build()
                                             )
                                             .setOrders(
                                                     OrdersData.newBuilder()
                                                               .setDataUrl(
                                                                       OrderDetailsData.newBuilder()
                                                                                       .setIndex(1)
                                                                                       .setDirection(OrderDirectionData.DESC)
                                                                                       .build()
                                                               )
                                                               .build()
                                             )
                                             .setPagination(
                                                     PaginationRequestData.newBuilder()
                                                                          .setPageSize(3)
                                                                          .build()
                                             )
                                             .build();

        Page<Provider> result = providerRepository.findBy(request);

        assertThat(result.getContent())
                .extracting("dataUrl")
                .containsExactly(
                        "https://www.oliveiraimoveissm.com.br/imoveis/a-venda/",
                        "https://www.invistaimoveissm.com.br",
                        "https://www.cafeimobiliaria.com.br/jsons/"
                );
    }

    @Test
    @DisplayName("given a proto request with order for mechanism desc when calls findBy then expects")
    void givenAProtoRequestWithOrderForMechanismDesc_whenCallsFindBy_thenExpects() {
        var request = GetProvidersDataRequest.newBuilder()
                                             .setProjections(
                                                     ProjectionsData.newBuilder()
                                                                    .setMechanism(true)
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
                                             .setPagination(
                                                     PaginationRequestData.newBuilder()
                                                                          .setPageSize(3)
                                                                          .build()
                                             )
                                             .build();

        Page<Provider> result = providerRepository.findBy(request);

        assertThat(result.getContent())
                .extracting("mechanism")
                .containsExactly(
                        ProviderMechanism.UNIVERSAL_SOFTWARE,
                        ProviderMechanism.SUPER_LOGICA,
                        ProviderMechanism.JETIMOB_V4
                );
    }

    @Test
    @DisplayName("given a proto request with multiple group clauses using OR separator when calls findBy then expects")
    void givenAProtoRequestWithMultipleGroupClausesUsingOrSeparator_whenCallsFindBy_thenExpects() {
        var request = GetProvidersDataRequest.newBuilder()
                                             .setProjections(
                                                     ProjectionsData.newBuilder()
                                                                    .setId(true)
                                                                    .build()
                                             )
                                             .addAllClauses(List.of(
                                                     ClausesData.newBuilder()
                                                                .setId(
                                                                        Int32ComparisonData.newBuilder()
                                                                                           .setIsEqual(
                                                                                                   Int32SingleComparisonData.newBuilder()
                                                                                                                            .setValue(10000)
                                                                                                                            .build()
                                                                                           )
                                                                                           .build()
                                                                )
                                                                .build(),
                                                     ClausesData.newBuilder()
                                                                .setId(
                                                                        Int32ComparisonData.newBuilder()
                                                                                           .setIsEqual(
                                                                                                   Int32SingleComparisonData.newBuilder()
                                                                                                                            .setValue(10001)
                                                                                                                            .build()
                                                                                           )
                                                                                           .build()
                                                                )
                                                                .setOuterOperator(ClauseOperator.OR)
                                                                .build()
                                             ))
                                             .setOrders(
                                                     OrdersData.newBuilder()
                                                               .setMechanism(
                                                                       OrderDetailsData.newBuilder()
                                                                                       .setIndex(1)
                                                                                       .setDirection(OrderDirectionData.ASC)
                                                                                       .build()
                                                               )
                                                               .build()
                                             )
                                             .build();

        Page<Provider> result = providerRepository.findBy(request);

        assertThat(result.getContent())
                .extracting("id")
                .containsExactly(10000, 10001);
    }

    @Test
    @DisplayName("given a proto request with inner clauses using OR separator when calls findBy then expects")
    void givenAProtoRequestWithInnerClausesUsingOrSeparator_whenCallsFindBy_thenExpects() {
        var request = GetProvidersDataRequest.newBuilder()
                                             .setProjections(
                                                     ProjectionsData.newBuilder()
                                                                    .setId(true)
                                                                    .build()
                                             )
                                             .addClauses(
                                                     ClausesData.newBuilder()
                                                                .setId(
                                                                        Int32ComparisonData.newBuilder()
                                                                                           .setIsEqual(
                                                                                                   Int32SingleComparisonData.newBuilder()
                                                                                                                            .setValue(10000)
                                                                                                                            .build()
                                                                                           )
                                                                                           .build()
                                                                )
                                                                .setName(
                                                                        StringComparisonData.newBuilder()
                                                                                            .setIsEqual(
                                                                                                    StringSingleComparisonData.newBuilder()
                                                                                                                              .setValue("Cancian Imóveis")
                                                                                                                              .build()
                                                                                            )
                                                                                            .build()
                                                                )
                                                                .setInnerOperator(ClauseOperator.OR)
                                                                .build()
                                             )
                                             .setOrders(
                                                     OrdersData.newBuilder()
                                                               .setMechanism(
                                                                       OrderDetailsData.newBuilder()
                                                                                       .setIndex(1)
                                                                                       .setDirection(OrderDirectionData.ASC)
                                                                                       .build()
                                                               )
                                                               .build()
                                             )
                                             .build();

        Page<Provider> result = providerRepository.findBy(request);

        assertThat(result.getContent())
                .extracting("id")
                .containsExactly(10000, 10001);
    }

}
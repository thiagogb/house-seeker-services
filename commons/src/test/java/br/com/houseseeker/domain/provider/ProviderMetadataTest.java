package br.com.houseseeker.domain.provider;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest(classes = LocalValidatorFactoryBean.class)
class ProviderMetadataTest {

    @Autowired
    private Validator validator;

    @BeforeEach
    void setup() {
        Locale.setDefault(Locale.of("pt", "BR"));
    }

    @Test
    @DisplayName("given a provider metadata with all attributes null when calls validate then expects errors")
    void givenAProviderMetadataWithAllAttributesNull_whenCallsValidate_thenExpectErrors() {
        ProviderMetadata providerMetadata = ProviderMetadata.builder().build();

        assertThat(validator.validateObject(providerMetadata))
                .extracting(Errors::getFieldErrors, InstanceOfAssertFactories.LIST)
                .extracting("field", "rejectedValue", "codes", "defaultMessage")
                .hasSize(4)
                .contains(
                        tuple("id", null, new String[]{"NotNull"}, "não deve ser nulo"),
                        tuple("name", null, new String[]{"NotBlank"}, "não deve estar em branco"),
                        tuple("siteUrl", null, new String[]{"NotBlank"}, "não deve estar em branco"),
                        tuple("mechanism", null, new String[]{"NotNull"}, "não deve ser nulo")
                );
    }

    @Test
    @DisplayName("given a provider metadata with invalid attributes values when calls validate then expects errors")
    void givenAProviderMetadataWithInvalidAttributesValues_whenCallsValidate_thenExpectErrors() {
        ProviderMetadata providerMetadata = ProviderMetadata.builder()
                                                            .id(0)
                                                            .name("Test Provider")
                                                            .siteUrl("<invalid url>")
                                                            .dataUrl("<invalid url>")
                                                            .mechanism(ProviderMechanism.JETIMOB_V1)
                                                            .build();

        assertThat(validator.validateObject(providerMetadata))
                .extracting(Errors::getFieldErrors, InstanceOfAssertFactories.LIST)
                .extracting("field", "rejectedValue", "codes", "defaultMessage")
                .hasSize(3)
                .contains(
                        tuple("id", 0, new String[]{"Min"}, "deve ser maior que ou igual à 1"),
                        tuple("siteUrl", "<invalid url>", new String[]{"URL"}, "deve ser uma URL válida"),
                        tuple("dataUrl", "<invalid url>", new String[]{"URL"}, "deve ser uma URL válida")
                );
    }

    @Test
    @DisplayName("given a provider metadata with all attributes valid when calls validate then expects no errors")
    void givenAProviderMetadataWithAllAttributesValid_whenCallsValidate_thenExpectNoErrors() {
        ProviderMetadata providerMetadata = ProviderMetadata.builder()
                                                            .id(1)
                                                            .name("Test Provider")
                                                            .siteUrl("http://localhost")
                                                            .dataUrl("http://localhost/api")
                                                            .mechanism(ProviderMechanism.JETIMOB_V1)
                                                            .params("{}")
                                                            .build();

        assertThat(validator.validateObject(providerMetadata))
                .extracting(Errors::getFieldErrors, InstanceOfAssertFactories.LIST)
                .isEmpty();
    }
}
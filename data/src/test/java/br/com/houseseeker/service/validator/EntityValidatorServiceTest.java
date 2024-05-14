package br.com.houseseeker.service.validator;

import br.com.houseseeker.configuration.ObjectMapperConfiguration;
import br.com.houseseeker.domain.exception.GrpcStatusException;
import io.grpc.Status;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = {
        LocalValidatorFactoryBean.class,
        EntityValidatorService.class,
        ObjectMapperConfiguration.class,
        EntityValidatorService.class
})
class EntityValidatorServiceTest {

    @Autowired
    private EntityValidatorService entityValidatorService;

    @BeforeEach
    void setup() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @Test
    @DisplayName("given a class with all valid attributes when calls validate then expects no exception")
    void givenAClassWithAllValidAttributes_whenCallsValidate_thenExpectsNoExceptions() {
        TestValidatedClass subject = TestValidatedClass.builder().id(1).name("Value").build();

        entityValidatorService.validate(subject);
    }

    @Test
    @DisplayName("given a class with invalid attributes when calls validate then expects exception")
    void givenAClassWithInvalidAttributes_whenCallsValidate_thenExpectsException() {
        TestValidatedClass subject = TestValidatedClass.builder().build();

        assertThatThrownBy(() -> entityValidatorService.validate(subject))
                .isInstanceOf(GrpcStatusException.class)
                .hasFieldOrPropertyWithValue("status", Status.INVALID_ARGUMENT)
                .satisfies(r -> {
                    String expected = """
                            [
                              {
                                "defaultMessage": "must not be null",
                                "field": "id"
                              },
                              {
                                "defaultMessage": "must not be empty",
                                "field": "name"
                              }
                            ]
                            """;
                    JSONAssert.assertEquals(expected, r.getMessage(), false);
                });
    }

    @Builder
    private static final class TestValidatedClass {

        @NotNull
        private Integer id;

        @NotEmpty
        private String name;

    }

}
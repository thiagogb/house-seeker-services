package br.com.houseseeker.util;

import br.com.houseseeker.configuration.ObjectMapperConfiguration;
import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ObjectMapperConfiguration.class)
class ObjectMapperUtilsTest {

    private static final String TEST_CLASS_JSON = """
            {
              "attribute1": 1,
              "attribute2": "value",
              "attribute3": 33.33,
              "attribute4": "2024-01-01",
              "attribute5": "12:30:45",
              "attribute6": "2024-01-01T12:30:45"
            }
            """;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("given a valid string content when deserialize then expects")
    void givenAValidStringContent_whenDeserializing_thenExpects() {
        assertThat(ObjectMapperUtils.deserializeAs(objectMapper, TEST_CLASS_JSON, TestClass.class))
                .extracting("attribute1", "attribute2", "attribute3", "attribute4", "attribute5", "attribute6")
                .containsExactly(
                        1,
                        "value",
                        BigDecimal.valueOf(33.33),
                        LocalDate.of(2024, 1, 1),
                        LocalTime.of(12, 30, 45),
                        LocalDateTime.of(2024, 1, 1, 12, 30, 45)
                );
    }

    @Test
    @DisplayName("given a invalid string content when deserialize then expects exception")
    void givenAInvalidStringContent_whenDeserializing_thenExpectException() {
        String content = """
                {
                  "attribute1": "abc"
                }
                """;

        assertThatThrownBy(() -> ObjectMapperUtils.deserializeAs(objectMapper, content, TestClass.class))
                .isInstanceOf(ExtendedRuntimeException.class)
                .hasMessage("Content read failed");
    }

    @Test
    @DisplayName("given a object when serialize then expects")
    void givenAObject_whenSerialize_thenExpects() {
        TestClass testClass = TestClass.builder()
                                       .attribute1(1)
                                       .attribute2("value")
                                       .attribute3(BigDecimal.valueOf(33.33))
                                       .attribute4(LocalDate.of(2024, 1, 1))
                                       .attribute5(LocalTime.of(12, 30, 45))
                                       .attribute6(LocalDateTime.of(2024, 1, 1, 12, 30, 45))
                                       .build();

        assertThat(ObjectMapperUtils.serialize(objectMapper, testClass))
                .satisfies(actual -> JSONAssert.assertEquals(TEST_CLASS_JSON, actual, true));
    }

    @Test
    @DisplayName("given a fail when serialize then expects exception")
    void givenAFail_whenSerialize_thenExpectException() throws JsonProcessingException {
        ObjectMapper mockedObjectMapper = mock(ObjectMapper.class);
        TestClass testClass = TestClass.builder().build();

        when(mockedObjectMapper.writeValueAsString(testClass))
                .thenThrow(JsonProcessingException.class);

        assertThatThrownBy(() -> ObjectMapperUtils.serialize(mockedObjectMapper, testClass))
                .isInstanceOf(ExtendedRuntimeException.class)
                .hasMessage("Content write failed");
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    private static final class TestClass {

        private Integer attribute1;
        private String attribute2;
        private BigDecimal attribute3;
        private LocalDate attribute4;
        private LocalTime attribute5;
        private LocalDateTime attribute6;

    }

}
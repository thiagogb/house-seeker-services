package br.com.houseseeker.mapper;

import com.google.protobuf.Int32Value;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ProtoInt32MapperImpl.class)
class ProtoInt32MapperTest {

    @Autowired
    private ProtoInt32Mapper protoInt32Mapper;

    @ParameterizedTest
    @MethodSource("int32ValueSamples")
    @DisplayName("given int32Value when calls toInteger then expects")
    void givenInt32Value_whenCallsToInteger_thenExpects(Int32Value input, Integer expected) {
        assertThat(protoInt32Mapper.toInteger(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("integerSamples")
    @DisplayName("given integer when calls toInt32Value then expects")
    void givenInteger_whenCallsToInt32Value_thenExpects(Integer input, Int32Value expected) {
        assertThat(protoInt32Mapper.toInt32Value(input)).isEqualTo(expected);
    }

    private static Stream<Arguments> int32ValueSamples() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(Int32Value.getDefaultInstance(), 0),
                Arguments.of(Int32Value.of(100), 100)
        );
    }

    private static Stream<Arguments> integerSamples() {
        return Stream.of(
                Arguments.of(null, Int32Value.getDefaultInstance()),
                Arguments.of(100, Int32Value.of(100))
        );
    }

}
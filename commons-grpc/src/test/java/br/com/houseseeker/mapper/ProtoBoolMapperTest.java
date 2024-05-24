package br.com.houseseeker.mapper;

import com.google.protobuf.BoolValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ProtoBoolMapperImpl.class)
class ProtoBoolMapperTest {

    @Autowired
    private ProtoBoolMapper protoBoolMapper;

    @Test
    @DisplayName("given null when calls toBoolean then expects")
    void givenNull_whenCallsToBoolean_thenExpects() {
        assertThat(protoBoolMapper.toBoolean(null)).isNull();
    }

    @ParameterizedTest
    @MethodSource("boolValueSamples")
    @DisplayName("given a boolValue when calls toBoolean then expects")
    void givenABoolValue_whenCallsToBoolean_thenExpects(BoolValue input, Boolean expected) {
        assertThat(protoBoolMapper.toBoolean(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("boolSamples")
    @DisplayName("given a boolean when calls toBoolValue then expects")
    void givenABoolean_whenCallsToBoolValue_thenExpects(Boolean input, BoolValue expected) {
        assertThat(protoBoolMapper.toBoolValue(input)).isEqualTo(expected);
    }

    private static Stream<Arguments> boolValueSamples() {
        return Stream.of(
                Arguments.of(BoolValue.getDefaultInstance(), false),
                Arguments.of(BoolValue.of(false), false),
                Arguments.of(BoolValue.of(true), true)
        );
    }

    private static Stream<Arguments> boolSamples() {
        return Stream.of(
                Arguments.of(null, BoolValue.getDefaultInstance()),
                Arguments.of(false, BoolValue.of(false)),
                Arguments.of(true, BoolValue.of(true))
        );
    }

}
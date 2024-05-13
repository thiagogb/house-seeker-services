package br.com.houseseeker.util;

import br.com.houseseeker.domain.exception.GrpcStatusException;
import com.google.protobuf.Int32Value;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProtoWrapperUtilsTest {

    @Test
    @DisplayName("given a null int32Value when calls getValue then expects exception")
    void givenANullInt32Value_whenCallsGetValue_thenExpectException() {
        assertThatThrownBy(() -> ProtoWrapperUtils.getValue(null))
                .isInstanceOf(GrpcStatusException.class)
                .hasMessage("No value present");
    }

    @ParameterizedTest
    @MethodSource("int32ValueSamples")
    @DisplayName("given a int32Value when calls getValue then expects")
    void givenAInt32Value_whenCallsGetValue_thenExpects(Int32Value value, Integer expected) {
        assertThat(ProtoWrapperUtils.getValue(value)).isEqualTo(expected);
    }

    private static Stream<Arguments> int32ValueSamples() {
        return Stream.of(
                Arguments.of(Int32Value.getDefaultInstance(), 0),
                Arguments.of(Int32Value.of(1), 1),
                Arguments.of(Int32Value.newBuilder().setValue(2).build(), 2)
        );
    }

}
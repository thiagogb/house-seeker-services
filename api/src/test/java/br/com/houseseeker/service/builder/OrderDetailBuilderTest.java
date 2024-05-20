package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.input.OrderInput;
import br.com.houseseeker.domain.proto.OrderDetailsData;
import br.com.houseseeker.domain.proto.OrderDirectionData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class OrderDetailBuilderTest {

    @Test
    @DisplayName("given a empty input when calls build then expects")
    void givenAEmptyInput_whenCallsBuild_thenExpects() {
        assertThat(OrderDetailBuilder.build(null))
                .isEqualTo(OrderDetailsData.getDefaultInstance());
    }

    @ParameterizedTest
    @MethodSource("inputSamples")
    @DisplayName("given a input when calls build then expects")
    void givenAInputWithAscDirection_whenCallsBuild_thenExpects(OrderInput input, OrderDetailsData expected) {
        assertThat(OrderDetailBuilder.build(input)).isEqualTo(expected);
    }

    private static Stream<Arguments> inputSamples() {
        return Stream.of(
                Arguments.of(
                        OrderInput.builder().index(1).direction(OrderInput.Direction.ASC).build(),
                        OrderDetailsData.newBuilder().setIndex(1).setDirection(OrderDirectionData.ASC).build()
                ),
                Arguments.of(
                        OrderInput.builder().index(2).direction(OrderInput.Direction.DESC).build(),
                        OrderDetailsData.newBuilder().setIndex(2).setDirection(OrderDirectionData.DESC).build()
                )
        );
    }

}
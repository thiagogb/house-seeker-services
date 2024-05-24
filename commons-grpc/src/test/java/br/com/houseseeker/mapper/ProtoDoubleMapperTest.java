package br.com.houseseeker.mapper;

import com.google.protobuf.DoubleValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ProtoDoubleMapperImpl.class)
class ProtoDoubleMapperTest {

    @Autowired
    private ProtoDoubleMapper protoDoubleMapper;

    @ParameterizedTest
    @MethodSource("bigDecimalSamples")
    @DisplayName("given bigDecimal when calls toDoubleValue then expects")
    void givenBigDecimal_whenCallsToDoubleValue_thenExpects(BigDecimal input, DoubleValue expected) {
        assertThat(protoDoubleMapper.toDoubleValue(input)).isEqualTo(expected);
    }

    private static Stream<Arguments> bigDecimalSamples() {
        return Stream.of(
                Arguments.of(null, DoubleValue.getDefaultInstance()),
                Arguments.of(BigDecimal.ZERO, DoubleValue.of(0)),
                Arguments.of(BigDecimal.valueOf(333.33), DoubleValue.of(333.33))
        );
    }

}
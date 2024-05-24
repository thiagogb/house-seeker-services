package br.com.houseseeker.mapper;

import com.google.protobuf.ByteString;
import com.google.protobuf.BytesValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ProtoBytesMapperImpl.class)
class ProtoBytesMapperTest {

    private static final String DEFAULT_CONTENT = "content";

    @Autowired
    private ProtoBytesMapper protoBytesMapper;

    @ParameterizedTest
    @MethodSource("bytesSamples")
    @DisplayName("given bytes when calls toBytesValue then expects")
    void givenBytes_whenCallsToBytesValue_thenExpects(byte[] input, BytesValue expected) {
        assertThat(protoBytesMapper.toBytesValue(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("stringsSamples")
    @DisplayName("given string when calls toBytesValue then expects")
    void givenBytes_whenCallsToBytesValue_thenExpects(String input, BytesValue expected) {
        assertThat(protoBytesMapper.toBytesValue(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("byteValuesSamples")
    @DisplayName("given bytesValue when calls toBytes then expects")
    void givenByteValues_whenCallsToBytes_thenExpects(BytesValue input, byte[] expected) {
        assertThat(protoBytesMapper.toBytes(input)).isEqualTo(expected);
    }

    private static Stream<Arguments> bytesSamples() {
        return Stream.of(
                Arguments.of(null, BytesValue.getDefaultInstance()),
                Arguments.of(DEFAULT_CONTENT.getBytes(), BytesValue.of(ByteString.copyFrom(DEFAULT_CONTENT, StandardCharsets.UTF_8)))
        );
    }

    private static Stream<Arguments> stringsSamples() {
        return Stream.of(
                Arguments.of(null, BytesValue.getDefaultInstance()),
                Arguments.of(EMPTY, BytesValue.getDefaultInstance()),
                Arguments.of(SPACE, BytesValue.getDefaultInstance()),
                Arguments.of("Y29udGVudA==", BytesValue.of(ByteString.copyFrom(DEFAULT_CONTENT, StandardCharsets.UTF_8)))
        );
    }

    private static Stream<Arguments> byteValuesSamples() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(BytesValue.getDefaultInstance(), new byte[]{}),
                Arguments.of(BytesValue.of(ByteString.copyFrom(DEFAULT_CONTENT, StandardCharsets.UTF_8)), DEFAULT_CONTENT.getBytes())
        );
    }

}
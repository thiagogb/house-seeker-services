package br.com.houseseeker.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.assertj.core.api.Assertions.assertThat;

class UrlUtilsTest {

    @ParameterizedTest
    @MethodSource("urlSamples")
    @DisplayName("given a url when calls getExtension then expects")
    void givenAUrl_whenCallsGetExtension_thenExpects(String url, String expected) {
        assertThat(UrlUtils.getExtension(url)).isEqualTo(Optional.ofNullable(expected));
    }

    private static Stream<Arguments> urlSamples() {
        return Stream.of(
                Arguments.of(EMPTY, null),
                Arguments.of(SPACE, null),
                Arguments.of("http://localhost", EMPTY),
                Arguments.of("http://localhost/test", EMPTY),
                Arguments.of("http://localhost/test.jpg", "jpg"),
                Arguments.of("http://localhost/test.jpg?_=123456", "jpg"),
                Arguments.of("http://localhost/inner.path/test.png?_=123456", "png"),
                Arguments.of("http://localhost/inner.path/test.gif#abc", "gif")
        );
    }

}
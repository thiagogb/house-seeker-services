package br.com.houseseeker.util;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JsoupUtilsTest {

    @Mock
    private Element element;

    @Mock
    private Elements elements;

    @ParameterizedTest
    @MethodSource("elementContentSample")
    @DisplayName("given a element content when calls getNonBlankHtml then expects")
    void givenAElementContent_whenCallsGetNonBlankHtml_thenExpects(String input, String output) {
        when(element.html()).thenReturn(input);

        assertThat(JsoupUtils.getNonBlankHtml(element)).isEqualTo(Optional.ofNullable(output));
    }

    @ParameterizedTest
    @MethodSource("elementAttributeSample")
    @DisplayName("given a element attribute when calls getNonBlankAttribute then expects")
    void givenAElementContent_whenCallsGetNonBlankAttribute_thenExpects(String attributeValue, String output) {
        when(element.attr(eq("attr"))).thenReturn(attributeValue);

        assertThat(JsoupUtils.getNonBlankAttribute(element, "attr")).isEqualTo(Optional.ofNullable(output));
    }

    @Test
    @DisplayName("given elements and request element at existing index when calls getElementAtIndex then expects present")
    void givenElementsAndRequestElementAtExistingIndex_whenCallsGetElementAtIndex_thenExpectsPresent() {
        when(elements.get(0)).thenReturn(element);
        assertThat(JsoupUtils.getElementAtIndex(elements, 0)).hasValue(element);
    }

    @Test
    @DisplayName("given elements and request element at non existing index when calls getElementAtIndex then expects empty")
    void givenElementsAndRequestElementAtNonExistingIndex_whenCallsGetElementAtIndex_thenExpectsEmpty() {
        when(elements.get(0)).thenThrow(IndexOutOfBoundsException.class);
        assertThat(JsoupUtils.getElementAtIndex(elements, 0)).isEmpty();
    }

    private static Stream<Arguments> elementContentSample() {
        return Stream.of(
                Arguments.of(EMPTY, null),
                Arguments.of(SPACE, null),
                Arguments.of("value", "value")
        );
    }

    private static Stream<Arguments> elementAttributeSample() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(EMPTY, null),
                Arguments.of(SPACE, null),
                Arguments.of("value", "value")
        );
    }

}
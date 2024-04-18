package br.com.houseseeker.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MediaUtilsTest {

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("given null or empty url when calls getMediaExtension then expects optional empty")
    void givenNullOrEmptyUrl_whenCallsGetMediaExtension_thenExceptsOptionalEmpty(String input) {
        assertThat(MediaUtils.getMediaExtension(input)).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"https://example.com/file.ext", "https://example.com/file.ext?p=1"})
    @DisplayName("given non empty url containing file with extension when calls getMediaExtension then expects optional present")
    void givenNonEmptyUrlContainingFileWithExtension_whenCallsGetMediaExtension_thenExpectsOptionalPresent(String input) {
        assertThat(MediaUtils.getMediaExtension(input)).hasValue("ext");
    }

    @ParameterizedTest
    @ValueSource(strings = {"https://example.com/path", "https://example.com/path?p=1"})
    @DisplayName("given non empty url without file with extension when calls getMediaExtension then expects optional empty")
    void givenNonEmptyUrlWithoutFileWithExtension_whenCallsGetMediaExtension_thenExpectsOptionalEmpty(String input) {
        assertThat(MediaUtils.getMediaExtension(input)).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"https://example.com/path", "https://example.com/path?p=1", "https://example.com/path?p=1&p2=2&p3[]=3"})
    @DisplayName("given valid urls when calls clearQueryParams then expects url without query parameters")
    void givenValidUrls_whenCallsClearQueryParams_thenExpectUrlsWithoutQueryParameters(String input) {
        assertThat(MediaUtils.clearQueryParams(input)).isEqualTo("https://example.com/path");
    }

    @ParameterizedTest
    @ValueSource(strings = {"<invalidLink1>", "http://invalid link with space"})
    @DisplayName("given invalid urls when calls clearQueryParams then expects exception")
    void givenInvalidUrls_whenCallsClearQueryParams_thenExpectException(String input) {
        assertThatThrownBy(() -> MediaUtils.clearQueryParams(input))
                .isInstanceOf(RuntimeException.class)
                .hasCauseInstanceOf(URISyntaxException.class)
                .hasMessage("Failed to clear query params from url");
    }

}
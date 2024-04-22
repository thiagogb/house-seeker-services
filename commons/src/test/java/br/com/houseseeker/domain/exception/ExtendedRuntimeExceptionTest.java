package br.com.houseseeker.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExtendedRuntimeExceptionTest {

    @Test
    @DisplayName("given only a throwable cause when calls constructor then expects")
    void givenOnlyAThrowableCause_whenCallsConstructor_thenExpects() {
        assertThat(new ExtendedRuntimeException(new RuntimeException("Test exception")))
                .hasCauseInstanceOf(RuntimeException.class)
                .hasMessage("java.lang.RuntimeException: Test exception");
    }

    @Test
    @DisplayName("given a printf message when calls constructor then expects")
    void givenAPrintfMessage_whenCallsConstructor_thenExpects() {
        assertThat(new ExtendedRuntimeException("Test exception %d", 1))
                .hasNoCause()
                .hasMessage("Test exception 1");
    }

    @Test
    @DisplayName("given a throwable cause and prinft message when calls constructor then expects")
    void givenAThrowableCauseAndPrintfMessage_whenCallsConstructor_thenExpects() {
        RuntimeException cause = new RuntimeException("Cause exception");

        assertThat(new ExtendedRuntimeException(cause, "Test exception %d", 1))
                .hasCause(cause)
                .hasMessage("Test exception 1");
    }

}
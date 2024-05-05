package br.com.houseseeker.util;

import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetrofitUtilsTest {

    @Mock
    private Call<String> mockedCall;

    @Mock
    private Response<String> mockedResponse;

    @ParameterizedTest
    @ValueSource(strings = {"http://localhost", "http://localhost/", "http://127.0.0.1"})
    @DisplayName("given a url when calls normalizeBaseUrl then expects to result a string with slash at the end")
    void givenAUrl_whenCallsNormalizeBaseUrl_thenExpectsToResultAStringWithSlashAtTheEnd(String input) {
        assertThat(RetrofitUtils.normalizeBaseUrl(input)).endsWith("/");
    }

    @Test
    @DisplayName("given a call with successfully response when calls executeCall then expects to result a string")
    void givenACallWithSuccessfullyResponse_whenCallsExecuteCall_thenExpectsToResultAString() throws IOException {
        when(mockedCall.execute()).thenReturn(mockedResponse);
        when(mockedResponse.isSuccessful()).thenReturn(true);
        when(mockedResponse.body()).thenReturn("This is a test!");

        assertThat(RetrofitUtils.executeCall(mockedCall)).isEqualTo("This is a test!");
    }

    @Test
    @DisplayName("given a failed call when calls executeCall then expects exception")
    void givenAFailedCall_whenCallsExecuteCall_thenExpectsException() throws IOException {
        doThrow(new IOException("Call has failed")).when(mockedCall).execute();

        assertThatThrownBy(() -> RetrofitUtils.executeCall(mockedCall))
                .isInstanceOf(ExtendedRuntimeException.class)
                .hasMessage("Unknown request failure");
    }

    @Test
    @DisplayName("given a call with failed response when calls executeCall and failed to extract error message then expects exception")
    void givenACallWithFailedResponse_whenCallsExecuteCallAndFailedToExtractErrorMessage_thenExpectsException() throws IOException {
        when(mockedCall.execute()).thenReturn(mockedResponse);
        when(mockedResponse.isSuccessful()).thenReturn(false);
        when(mockedResponse.code()).thenReturn(500);
        when(mockedResponse.errorBody()).thenReturn(null);

        assertThatThrownBy(() -> RetrofitUtils.executeCall(mockedCall))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("given a call with failed response when calls executeCall and successfully extract error message then expects exception")
    void givenACallWithFailedResponse_whenCallsExecuteCallAndSuccessfullyExtractErrorMessage_thenExpectsException() throws IOException {
        when(mockedCall.execute()).thenReturn(mockedResponse);
        when(mockedResponse.isSuccessful()).thenReturn(false);
        when(mockedResponse.code()).thenReturn(500);
        when(mockedResponse.errorBody()).thenReturn(ResponseBody.create("Unexpected error", MediaType.get("text/plain")));

        assertThatThrownBy(() -> RetrofitUtils.executeCall(mockedCall))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("500 INTERNAL_SERVER_ERROR \"Request failed with error: Unexpected error\"");
    }

}
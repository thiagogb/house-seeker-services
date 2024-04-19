package br.com.houseseeker.util;

import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;
import okhttp3.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Optional;

import static java.util.Objects.nonNull;

@UtilityClass
public class RetrofitUtils {

    public String normalizeBaseUrl(@NotNull String baseUrl) {
        return !baseUrl.endsWith("/") ? baseUrl.concat("/") : baseUrl;
    }

    public <T> T executeCall(Call<T> call) {
        Response<T> response;
        int errorCode;
        String errorMessage;
        try {
            response = call.execute();
            if (response.isSuccessful())
                return response.body();

            errorCode = response.code();
            errorMessage = getResponseErrorBody(response).orElseThrow();

            throw new ResponseStatusException(HttpStatus.valueOf(errorCode), String.format("Request failed with error: %s", errorMessage));
        } catch (IOException e) {
            throw new ExtendedRuntimeException(e, "Unknown request failure");
        }
    }

    private <T> Optional<String> getResponseErrorBody(Response<T> response) throws IOException {
        ResponseBody responseBody = response.errorBody();
        try (responseBody) {
            return nonNull(responseBody)
                    ? StringUtils.getNonBlank(responseBody.string())
                    : Optional.empty();
        }
    }

}

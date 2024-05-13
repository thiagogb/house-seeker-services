package br.com.houseseeker.util;

import br.com.houseseeker.domain.exception.GrpcStatusException;
import io.grpc.Status;
import io.grpc.StatusException;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GrpcExceptionUtils {

    public StatusException fromGrpcException(@NotNull GrpcStatusException exception) {
        return exception.getStatus().withDescription(exception.getMessage()).asException();
    }

    public StatusException fromThrowable(@NotNull Throwable throwable) {
        return Status.INTERNAL.withDescription(throwable.getMessage()).asException();
    }

}

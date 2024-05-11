package br.com.houseseeker.util;

import io.grpc.Status;
import io.grpc.StatusException;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GrpcUtils {

    public StatusException fromThrowable(@NotNull Throwable throwable) {
        return Status.INTERNAL.withDescription(throwable.getMessage()).asException();
    }

}

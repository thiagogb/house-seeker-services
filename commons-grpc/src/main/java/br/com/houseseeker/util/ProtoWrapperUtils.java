package br.com.houseseeker.util;

import br.com.houseseeker.domain.exception.GrpcStatusException;
import com.google.protobuf.Int32Value;
import io.grpc.Status;
import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;

import java.util.Optional;

@UtilityClass
public class ProtoWrapperUtils {

    private static final GrpcStatusException INVALID_ARG_EXCEPTION = new GrpcStatusException(Status.INVALID_ARGUMENT, "No value present");

    public int getValue(@Nullable Int32Value value) {
        return Optional.ofNullable(value)
                       .map(Int32Value::getValue)
                       .orElseThrow(() -> INVALID_ARG_EXCEPTION);
    }

}

package br.com.houseseeker.util;

import br.com.houseseeker.domain.exception.GrpcStatusException;
import io.grpc.stub.StreamObserver;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@UtilityClass
@Slf4j
public class GrpcServiceUtils {

    public <T> void execute(@NotNull StreamObserver<T> streamObserver, @NotNull Supplier<T> supplier) {
        try {
            streamObserver.onNext(supplier.get());
            streamObserver.onCompleted();
        } catch (GrpcStatusException e) {
            log.error("GRPC: execution failure", e);
            streamObserver.onError(GrpcExceptionUtils.fromGrpcException(e));
        } catch (Exception e) {
            log.error("GRPC: execution failure", e);
            streamObserver.onError(GrpcExceptionUtils.fromThrowable(e));
        }
    }

}

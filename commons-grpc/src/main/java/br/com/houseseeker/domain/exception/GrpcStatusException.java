package br.com.houseseeker.domain.exception;

import io.grpc.Status;
import lombok.Getter;

import java.io.Serial;

@Getter
public class GrpcStatusException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -5592945584422002647L;

    private final transient Status status;

    public GrpcStatusException(Status status, String message, Object... args) {
        super(String.format(message, args));
        this.status = status;
    }

}

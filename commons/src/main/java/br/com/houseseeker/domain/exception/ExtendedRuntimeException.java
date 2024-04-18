package br.com.houseseeker.domain.exception;

import java.io.Serial;

public class ExtendedRuntimeException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -5876195348918179246L;

    public ExtendedRuntimeException(String message, Object... args) {
        super(String.format(message, args));
    }

    public ExtendedRuntimeException(Throwable cause, String message, Object... args) {
        super(String.format(message, args), cause);
    }

}

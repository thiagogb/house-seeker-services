package br.com.houseseeker.util;

import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import br.com.houseseeker.domain.exception.GrpcStatusException;
import io.grpc.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class GrpcExceptionUtilsTest {

    @ParameterizedTest
    @MethodSource("grpcExceptionSamples")
    @DisplayName("given a grpc status exception when calls fromGrpcException then expects")
    void givenAGrpcStatusException_whenCallingFromGrpcException_thenExpects(GrpcStatusException exception, String expected) {
        assertThat(GrpcExceptionUtils.fromGrpcException(exception)).hasMessage(expected);
    }

    @ParameterizedTest
    @MethodSource("throwableSamples")
    @DisplayName("given a throwable exception when calls fromThrowable then expects")
    void givenAThrowable_whenCallingFromThrowable_thenExpects(Throwable throwable, String expected) {
        assertThat(GrpcExceptionUtils.fromThrowable(throwable)).hasMessage(expected);
    }

    private static Stream<Arguments> grpcExceptionSamples() {
        return Stream.of(
                Arguments.of(new GrpcStatusException(Status.INTERNAL, "Unknown failure"), "INTERNAL: Unknown failure"),
                Arguments.of(new GrpcStatusException(Status.FAILED_PRECONDITION, "Data required"), "FAILED_PRECONDITION: Data required")
        );
    }

    private static Stream<Arguments> throwableSamples() {
        return Stream.of(
                Arguments.of(new RuntimeException("Unknown failure"), "INTERNAL: Unknown failure"),
                Arguments.of(new ExtendedRuntimeException("Fetch failure"), "INTERNAL: Fetch failure")
        );
    }

}
package br.com.houseseeker.util;

import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import br.com.houseseeker.domain.exception.GrpcStatusException;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class GrpcServiceUtilsTest {

    @Mock
    private StreamObserver<Object> mockedStreamObserver;

    @Test
    @DisplayName("given a supplier with generic errors when calls execute then expects to register a error")
    void givenASupplierWithGenericErrors_whenCallsExecute_thenExpectsToRegisterErrors() {
        GrpcServiceUtils.execute(mockedStreamObserver, () -> {
            throw new ExtendedRuntimeException("Execution failure");
        });

        verify(mockedStreamObserver, times(1)).onError(
                assertArg(a -> assertThat(a).hasMessage("INTERNAL: Execution failure"))
        );
        verifyNoMoreInteractions(mockedStreamObserver);
    }

    @Test
    @DisplayName("given a supplier with grpc errors when calls execute then expects to register a error")
    void givenASupplierWithGrpcErrors_whenCallsExecute_thenExpectsToRegisterErrors() {
        GrpcServiceUtils.execute(mockedStreamObserver, () -> {
            throw new GrpcStatusException(Status.CANCELLED, "Operation cancelled");
        });

        verify(mockedStreamObserver, times(1)).onError(
                assertArg(a -> assertThat(a).hasMessage("CANCELLED: Operation cancelled"))
        );
        verifyNoMoreInteractions(mockedStreamObserver);
    }

    @Test
    @DisplayName("given a supplier without errors when calls execute then expects to register complete")
    void givenASupplierWithoutErrors_whenCallsExecute_thenExpectsToRegisterComplete() {
        GrpcServiceUtils.execute(mockedStreamObserver, () -> "value");

        verify(mockedStreamObserver, times(1)).onNext("value");
        verify(mockedStreamObserver, times(1)).onCompleted();
        verifyNoMoreInteractions(mockedStreamObserver);
    }

}
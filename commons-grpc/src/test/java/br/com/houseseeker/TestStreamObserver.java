package br.com.houseseeker;

import io.grpc.stub.StreamObserver;
import lombok.Getter;

@Getter
public class TestStreamObserver<T> implements StreamObserver<T> {

    private T value;
    private Throwable throwable;

    @Override
    public void onNext(T value) {
        this.value = value;
    }

    @Override
    public void onError(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public void onCompleted() {

    }

}

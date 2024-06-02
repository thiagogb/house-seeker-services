package br.com.houseseeker.domain.input;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class AbstractIntervalInput<T> {

    private T start;
    private T end;

}

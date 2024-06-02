package br.com.houseseeker.domain.input;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class AbstractListInput<T> {

    private List<T> values;

}

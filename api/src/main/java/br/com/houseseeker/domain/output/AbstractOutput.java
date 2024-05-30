package br.com.houseseeker.domain.output;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
class AbstractOutput<T> {

    private List<T> rows;
    private PaginationOutput pagination;

}

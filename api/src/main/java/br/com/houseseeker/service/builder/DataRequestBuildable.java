package br.com.houseseeker.service.builder;

import jakarta.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

import java.util.Set;

interface DataRequestBuildable<T, U, V> {

    T withProjections(@NotNull Set<String> projections);

    T withInput(@Nullable U input);

    V build();

}

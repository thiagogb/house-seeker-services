package br.com.houseseeker.domain.input;

import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.constraints.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.requireNonNullElse;

abstract class AbstractEditionInput {

    private Set<String> changedArguments;

    public void detectedChangedArguments(@NotNull DataFetchingEnvironment dataFetchingEnvironment) {
        Map<String, Object> argumentsMap = dataFetchingEnvironment.getArgument("input");
        changedArguments = argumentsMap.keySet();
    }

    public boolean hasChanged(@NotNull String name) {
        return requireNonNullElse(changedArguments, Collections.emptySet()).contains(name);
    }

}

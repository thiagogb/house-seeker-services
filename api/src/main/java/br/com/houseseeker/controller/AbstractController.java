package br.com.houseseeker.controller;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.SelectedField;

import java.util.Set;
import java.util.stream.Collectors;

abstract class AbstractController {

    protected final Set<String> getProjections(DataFetchingEnvironment dataFetchingEnvironment) {
        return dataFetchingEnvironment.getSelectionSet()
                                      .getFields()
                                      .stream()
                                      .map(SelectedField::getQualifiedName)
                                      .collect(Collectors.toSet());
    }

}

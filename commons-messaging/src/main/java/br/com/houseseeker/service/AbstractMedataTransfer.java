package br.com.houseseeker.service;

import br.com.houseseeker.domain.property.AbstractUrbanPropertyMetadata;
import jakarta.validation.constraints.NotNull;

public abstract class AbstractMedataTransfer<T> {

    public abstract AbstractUrbanPropertyMetadata transfer(@NotNull T metadata);

}

package br.com.houseseeker.domain.jetimob.v1;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
public enum PropertyCharacteristicType {

    BUILD_AREA("fa-house-window"),
    PRIVATE_AREA("fa-house-user"),
    TERRAIN_AREA("fa-vector-square"),
    USABLE_AREA("fa-ruler-combined"),
    ARABLE_AREA("fa-tractor"),
    DORMITORIES("fa-bed"),
    ROOMS("fa-door-closed"),
    BATHROOM("fa-bath"),
    GARAGE("fa-car");

    private final String iconClass;

    public static Optional<PropertyCharacteristicType> getByClasses(@NotNull Set<String> classes) {
        for (var type : values())
            if (classes.contains(type.iconClass))
                return Optional.of(type);

        log.info("Undefined characteristic for: {}", classes);

        return Optional.empty();
    }

}

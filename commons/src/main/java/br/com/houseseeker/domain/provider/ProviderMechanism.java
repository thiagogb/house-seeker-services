package br.com.houseseeker.domain.provider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProviderMechanism {

    UNIVERSAL_SOFTWARE("universal-software.*"),
    JETIMOB_V1("jetimob.v1"),
    JETIMOB_V2("jetimob.v2"),
    JETIMOB_V3("jetimob.v3"),
    JETIMOB_V4("jetimob.v4"),
    SUPER_LOGICA("super-logica.*"),
    ALAN_WGT("alan_wgt.*");

    private final String routingKey;

}

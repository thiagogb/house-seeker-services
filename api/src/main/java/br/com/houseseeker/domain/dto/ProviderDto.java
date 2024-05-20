package br.com.houseseeker.domain.dto;

import br.com.houseseeker.domain.provider.ProviderMechanism;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;

@Value
public class ProviderDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -8859370410771978071L;

    Integer id;
    String name;
    String siteUrl;
    String dataUrl;
    ProviderMechanism mechanism;
    String params;
    String cronExpression;
    String logoUrl;
    Boolean active;

}
package br.com.houseseeker.domain.jetimob.v2;

import br.com.houseseeker.domain.property.UrbanPropertyContract;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
@Accessors(chain = true)
public class FilterOptionsMetadata {

    private UrbanPropertyContract contract;
    private List<String> cities;
    private List<String> types;

}

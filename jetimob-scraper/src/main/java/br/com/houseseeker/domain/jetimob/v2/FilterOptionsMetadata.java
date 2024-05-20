package br.com.houseseeker.domain.jetimob.v2;

import br.com.houseseeker.domain.property.UrbanPropertyContract;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

@Data
@Builder
@Accessors(chain = true)
public class FilterOptionsMetadata {

    private UrbanPropertyContract contract;
    @Builder.Default
    private List<String> cities = new LinkedList<>();
    @Builder.Default
    private List<String> types = new LinkedList<>();

}

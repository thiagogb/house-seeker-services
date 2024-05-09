package br.com.houseseeker.domain.jetimob.v2;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FilterOptionsMetadata {

    private List<String> cities;
    private List<String> types;

}

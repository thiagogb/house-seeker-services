package br.com.houseseeker.domain.input;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StringListInput {

    private List<String> values;

}

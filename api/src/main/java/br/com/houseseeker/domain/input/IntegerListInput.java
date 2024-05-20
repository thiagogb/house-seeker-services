package br.com.houseseeker.domain.input;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class IntegerListInput {

    private List<Integer> values;

}

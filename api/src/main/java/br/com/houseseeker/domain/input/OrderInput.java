package br.com.houseseeker.domain.input;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderInput {

    private Integer index;
    private Direction direction;

    public enum Direction {

        ASC, DESC

    }

}

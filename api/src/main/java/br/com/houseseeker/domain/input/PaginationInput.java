package br.com.houseseeker.domain.input;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationInput {

    private Integer pageNumber;
    private Integer pageSize;

}

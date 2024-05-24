package br.com.houseseeker.domain.output;

import lombok.Data;

@Data
public class PaginationOutput {

    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPages;
    private Long totalRows;

}

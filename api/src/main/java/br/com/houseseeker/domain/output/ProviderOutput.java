package br.com.houseseeker.domain.output;

import br.com.houseseeker.domain.dto.ProviderDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProviderOutput {

    private List<ProviderDto> rows;
    private PaginationOutput pagination;

}

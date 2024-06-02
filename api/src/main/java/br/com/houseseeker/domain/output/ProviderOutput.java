package br.com.houseseeker.domain.output;

import br.com.houseseeker.domain.dto.ProviderDto;
import lombok.Builder;

import java.util.List;

public class ProviderOutput extends AbstractOutput<ProviderDto> {

    @Builder
    public ProviderOutput(List<ProviderDto> rows, PaginationOutput pagination) {
        super(rows, pagination);
    }

}

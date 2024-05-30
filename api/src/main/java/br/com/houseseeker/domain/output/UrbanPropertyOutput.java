package br.com.houseseeker.domain.output;

import br.com.houseseeker.domain.dto.UrbanPropertyDto;
import lombok.Builder;

import java.util.List;

public class UrbanPropertyOutput extends AbstractOutput<UrbanPropertyDto> {

    @Builder
    public UrbanPropertyOutput(List<UrbanPropertyDto> rows, PaginationOutput pagination) {
        super(rows, pagination);
    }

}

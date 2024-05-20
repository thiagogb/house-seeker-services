package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.input.PaginationInput;
import br.com.houseseeker.domain.proto.PaginationRequestData;
import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;

import static java.util.Objects.isNull;

@UtilityClass
public class PaginationBuilder {

    public PaginationRequestData build(@Nullable PaginationInput input) {
        if (isNull(input))
            return PaginationRequestData.getDefaultInstance();

        return PaginationRequestData.newBuilder()
                                    .setPageNumber(input.getPageNumber())
                                    .setPageSize(input.getPageSize())
                                    .build();
    }

}

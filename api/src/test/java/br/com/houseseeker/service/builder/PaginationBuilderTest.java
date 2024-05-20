package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.input.PaginationInput;
import br.com.houseseeker.domain.proto.PaginationRequestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PaginationBuilderTest {

    @Test
    @DisplayName("given a null input when calls build then expects")
    void givenANullInput_whenCallsBuild_thenExpects() {
        assertThat(PaginationBuilder.build(null))
                .isEqualTo(PaginationRequestData.getDefaultInstance());
    }

    @Test
    @DisplayName("given a input when calls build then expects")
    void givenAInput_whenCallsBuild_thenExpects() {
        var input = PaginationInput.builder()
                                   .pageSize(100)
                                   .pageNumber(5)
                                   .build();

        assertThat(PaginationBuilder.build(input))
                .extracting(PaginationRequestData::getPageSize, PaginationRequestData::getPageNumber)
                .containsExactly(100, 5);
    }

}
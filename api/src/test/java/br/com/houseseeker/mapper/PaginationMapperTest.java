package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.output.PaginationOutput;
import br.com.houseseeker.domain.proto.PaginationResponseData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = PaginationMapperImpl.class)
class PaginationMapperTest {

    @Autowired
    private PaginationMapper paginationMapper;

    @Test
    @DisplayName("given a data when calls toOutput then expects")
    void givenAData_whenCallsToOutput_thenExpects() {
        var data = PaginationResponseData.newBuilder()
                                         .setPageNumber(5)
                                         .setPageSize(20)
                                         .setTotalPages(5)
                                         .setTotalRows(100)
                                         .build();

        assertThat(paginationMapper.toOutput(data))
                .extracting(
                        PaginationOutput::getPageNumber,
                        PaginationOutput::getPageSize,
                        PaginationOutput::getTotalPages,
                        PaginationOutput::getTotalRows
                )
                .containsExactly(5, 20, 5, 100L);
    }

}
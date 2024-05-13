package br.com.houseseeker.repository.builder;

import br.com.houseseeker.domain.exception.GrpcStatusException;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import io.grpc.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class ExpressionBuilderTest {

    @Mock
    private NumberPath<Integer> mockedNumberPath;

    @Mock
    private StringPath mockedStringPath;

    @Test
    @DisplayName("given a builder without appended expressions when calls build then expects exception")
    void givenABuilderWithoutAppendedExpressions_whenCallsBuild_thenExpectsException() {
        assertThatThrownBy(() -> ExpressionBuilder.newInstance().build())
                .isInstanceOf(GrpcStatusException.class)
                .hasFieldOrPropertyWithValue("status", Status.INVALID_ARGUMENT)
                .hasMessage("At least one projection must be defined");
    }

    @Test
    @DisplayName("given a builder with appended expressions not selected when calls build then expects exception")
    void givenABuilderWithAppendedExpressionsNotSelected_whenCallsBuild_thenExpectsException() {
        ExpressionBuilder builder = ExpressionBuilder.newInstance()
                                                     .append(mockedNumberPath, false)
                                                     .append(mockedStringPath, false);

        assertThatThrownBy(builder::build)
                .isInstanceOf(GrpcStatusException.class)
                .hasFieldOrPropertyWithValue("status", Status.INVALID_ARGUMENT)
                .hasMessage("At least one projection must be defined");
    }

    @Test
    @DisplayName("given a builder with appended expressions selected when calls build then expects")
    void givenABuilderWithAppendedExpressionsSelected_whenCallsBuild_thenExpects() {
        assertThat(ExpressionBuilder.newInstance()
                                    .append(mockedNumberPath, true)
                                    .append(mockedStringPath, true)
                                    .build())
                .containsExactly(mockedNumberPath, mockedStringPath);
    }

}
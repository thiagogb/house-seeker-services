package br.com.houseseeker.repository.builder;

import br.com.houseseeker.domain.exception.GrpcStatusException;
import br.com.houseseeker.domain.proto.OrderDetailsData;
import br.com.houseseeker.domain.proto.OrderDirectionData;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanPath;
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
class OrderBuilderTest {

    @Mock
    private NumberPath<Integer> mockedNumberPath;

    @Mock
    private StringPath mockedStringPath;

    @Mock
    private BooleanPath mockedBooleanPath;

    @Test
    @DisplayName("given a builder without appended orders when calls build when expects empty")
    void givenABuilderWithoutAppendedOrders_whenCallsBuild_thenExpectsEmpty() {
        assertThat(OrderBuilder.newInstance().build()).isEmpty();
    }

    @Test
    @DisplayName("given a builder with unsettled order index when calls build when expects empty")
    void givenABuilderWithUnsettledOrderIndex_whenCallsBuild_thenExpectsEmpty() {
        assertThat(OrderBuilder.newInstance()
                               .append(mockedNumberPath, OrderDetailsData.newBuilder().setIndex(0).build())
                               .append(mockedStringPath, OrderDetailsData.newBuilder().setIndex(0).build())
                               .build())
                .isEmpty();
    }

    @Test
    @DisplayName("given a builder with repeated order indexes when calls build when expects exception")
    void givenABuilderWithRepeatedOrderIndexes_whenCallsBuild_thenExpectsException() {
        assertThatThrownBy(() -> OrderBuilder.newInstance()
                                             .append(mockedNumberPath, OrderDetailsData.newBuilder().setIndex(1).build())
                                             .append(mockedStringPath, OrderDetailsData.newBuilder().setIndex(1).build())
                                             .build())
                .isInstanceOf(GrpcStatusException.class)
                .hasFieldOrPropertyWithValue("status", Status.INVALID_ARGUMENT)
                .hasMessage("There's already an order specifier with index 1");
    }

    @Test
    @DisplayName("given a builder with appended unordered indexes when calls build when expects ordered list")
    void givenABuilderWithAppendedUnorderedIndexes_whenCallsBuild_thenExpectsOrderedList() {
        assertThat(OrderBuilder.newInstance()
                               .append(mockedNumberPath, OrderDetailsData.newBuilder().setIndex(3).setDirection(OrderDirectionData.ASC).build())
                               .append(mockedBooleanPath, OrderDetailsData.newBuilder().setIndex(1).setDirection(OrderDirectionData.DESC).build())
                               .append(mockedStringPath, OrderDetailsData.newBuilder().setIndex(2).setDirection(OrderDirectionData.DESC).build())
                               .build())
                .extracting(OrderSpecifier::toString)
                .containsExactly("mockedBooleanPath DESC", "mockedStringPath DESC", "mockedNumberPath ASC");
    }

}
package br.com.houseseeker.repository.builder;

import br.com.houseseeker.domain.exception.GrpcStatusException;
import br.com.houseseeker.domain.proto.OrderDetailsData;
import br.com.houseseeker.domain.proto.OrderDirectionData;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import io.grpc.Status;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderBuilder {

    private final Map<Integer, OrderSpecifier<?>> orderSpecifierMap = new HashMap<>();

    public static OrderBuilder newInstance() {
        return new OrderBuilder();
    }

    public <T extends Comparable<?>> OrderBuilder append(@NotNull Expression<T> expression, @NotNull OrderDetailsData orderDetailsData) {
        if (orderDetailsData.getIndex() > 0) {
            if (orderSpecifierMap.containsKey(orderDetailsData.getIndex()))
                throw new GrpcStatusException(Status.INVALID_ARGUMENT, "There's already an order specifier with index %d", orderDetailsData.getIndex());

            orderSpecifierMap.put(
                    orderDetailsData.getIndex(),
                    new OrderSpecifier<>(getOrderDirection(orderDetailsData.getDirection()), expression).nullsLast()
            );
        }

        return this;
    }

    public OrderSpecifier<?>[] build() {
        AtomicReference<OrderSpecifier<?>[]> orderSpecifiers = new AtomicReference<>(new OrderSpecifier<?>[]{});

        orderSpecifierMap.entrySet()
                         .stream()
                         .sorted(Comparator.comparingInt(Map.Entry::getKey))
                         .forEach(e -> orderSpecifiers.getAndUpdate(current -> ArrayUtils.add(current, e.getValue())));

        return orderSpecifiers.get();
    }

    private Order getOrderDirection(OrderDirectionData orderDirectionData) {
        return orderDirectionData.equals(OrderDirectionData.ASC) ? Order.ASC : Order.DESC;
    }

}

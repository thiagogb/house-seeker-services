package br.com.houseseeker.service.builder;

import br.com.houseseeker.domain.input.OrderInput;
import br.com.houseseeker.domain.proto.OrderDetailsData;
import br.com.houseseeker.domain.proto.OrderDirectionData;
import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;

import static java.util.Objects.isNull;

@UtilityClass
public class OrderDetailBuilder {

    public OrderDetailsData build(@Nullable OrderInput input) {
        if (isNull(input))
            return OrderDetailsData.getDefaultInstance();

        return OrderDetailsData.newBuilder()
                               .setIndex(input.getIndex())
                               .setDirection(
                                       switch (input.getDirection()) {
                                           case ASC -> OrderDirectionData.ASC;
                                           case DESC -> OrderDirectionData.DESC;
                                       }
                               )
                               .build();
    }

}

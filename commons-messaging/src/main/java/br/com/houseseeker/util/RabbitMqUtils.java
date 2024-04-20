package br.com.houseseeker.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.core.Binding;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class RabbitMqUtils {

    public static Map<String, Object> getDeadLetterParamsUsingBinding(@NotNull Binding binding) {
        return new HashMap<>() {{
            put("x-dead-letter-exchange", binding.getExchange());
            put("x-dead-letter-routing-key", binding.getRoutingKey());
        }};
    }

}

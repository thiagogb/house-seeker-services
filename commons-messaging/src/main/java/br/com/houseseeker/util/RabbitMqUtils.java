package br.com.houseseeker.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.core.Binding;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class RabbitMqUtils {

    public static Map<String, Object> getDeadLetterParamsUsingBinding(@NotNull Binding binding) {
        Map<String, Object> params = new HashMap<>();
        params.put("x-dead-letter-exchange", binding.getExchange());
        params.put("x-dead-letter-routing-key", binding.getRoutingKey());
        return params;
    }

}

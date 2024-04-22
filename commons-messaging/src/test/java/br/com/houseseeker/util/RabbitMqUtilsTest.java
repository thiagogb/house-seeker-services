package br.com.houseseeker.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Binding;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RabbitMqUtilsTest {

    @Mock
    private Binding binding;

    @Test
    @DisplayName("given a biding when calls getDeadLetterParamsUsingBinding then expects parameters")
    void givenABiding_whenCallsGetDeadLetterParamsUsingBinding_thenExpectParameters() {
        when(binding.getExchange()).thenReturn("exchange-example");
        when(binding.getRoutingKey()).thenReturn("routing-key-example");

        assertThat(RabbitMqUtils.getDeadLetterParamsUsingBinding(binding))
                .hasToString("{x-dead-letter-exchange=exchange-example, x-dead-letter-routing-key=routing-key-example}");
    }

}
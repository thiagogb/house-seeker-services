package br.com.houseseeker.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.StopWatch;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StopWatchUtilsTest {

    @Mock
    private Clock clock;

    @Mock
    private StopWatch mockedStopWatch;

    @BeforeEach
    void setup() {
        when(clock.instant()).thenReturn(Instant.parse("2024-01-01T12:30:45Z"));
        when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    }

    @Test
    @DisplayName("given a stop watch with elapsed time of 30 seconds when calls getStart then expects")
    void givenAStopWatchWithElapsedTimeOf30Seconds_whenCallsGetStart_thenExpects() {
        when(mockedStopWatch.getTotalTimeMillis()).thenReturn(30000L);

        assertThat(StopWatchUtils.getStart(clock, mockedStopWatch)).hasToString("2024-01-01T12:30:15");
    }

}
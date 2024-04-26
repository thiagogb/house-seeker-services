package br.com.houseseeker.util;

import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@UtilityClass
public class StopWatchUtils {

    public LocalDateTime getStart(@NotNull StopWatch stopWatch) {
        return LocalDateTime.now().minus(stopWatch.getTotalTimeMillis(), ChronoUnit.MILLIS);
    }

}

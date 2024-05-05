package br.com.houseseeker.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class ThreadUtils {

    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.error("Retry wait time", e);
            Thread.currentThread().interrupt();
        }
    }

}

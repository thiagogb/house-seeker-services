package br.com.houseseeker.util;

import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.function.Function;

@UtilityClass
public class WebDriverUtils {

    private static final Duration DEFAULT_WAIT_TIMEOUT = Duration.ofSeconds(5);

    public void waitUntil(@NotNull WebDriver webDriver, @NotNull Function<WebDriver, Boolean> condition) {
        new WebDriverWait(webDriver, DEFAULT_WAIT_TIMEOUT).until(condition);
    }

}

package br.com.houseseeker.service;

import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import br.com.houseseeker.util.ThreadUtils;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.nonNull;

@Slf4j
public abstract class AbstractWebDriverScraperService<T> implements AutoCloseable {

    private final WebDriverFactoryService webDriverFactoryService;
    private WebDriver webDriver;

    public AbstractWebDriverScraperService(WebDriverFactoryService webDriverFactoryService) {
        this.webDriverFactoryService = webDriverFactoryService;
    }

    public final T scrap(@NotBlank String url) {
        return runInWebDriver(url, this::extractWithWebDriver);
    }

    @Override
    public void close() {
        if (nonNull(webDriver))
            webDriver.quit();
    }

    protected abstract T extractWithWebDriver(WebDriver webDriver);

    private T runInWebDriver(String url, Function<WebDriver, T> webDriverConsumer) {
        webDriver = Optional.ofNullable(webDriver).orElseGet(webDriverFactoryService::createWebDriver);
        T result = null;
        int attemptNumber = 1;
        Exception lastError = null;

        while (attemptNumber <= webDriverFactoryService.getRetryCount()) {
            try {
                webDriver.get(url);
                result = webDriverConsumer.apply(webDriver);
                lastError = null;
                break;
            } catch (Exception e) {
                log.info("Failed cause {}: attempt {} of {}", e.getMessage(), attemptNumber, webDriverFactoryService.getRetryCount());
                lastError = e;
                ThreadUtils.sleep(webDriverFactoryService.getRetryWait());
            } finally {
                attemptNumber++;
            }
        }

        if (nonNull(lastError)) {
            if (lastError instanceof ExtendedRuntimeException e)
                throw e;

            throw new ExtendedRuntimeException(lastError, "Scraper fail using WebDriver");
        }

        return result;
    }

}

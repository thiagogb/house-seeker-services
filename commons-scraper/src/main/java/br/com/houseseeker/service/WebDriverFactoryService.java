package br.com.houseseeker.service;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
public class WebDriverFactoryService {

    private static final String WEBDRIVER_CHROME_VERSION = "124.0.6367.207";
    private static final ChromeOptions WEBDRIVER_CHROME_OPTIONS = configureChromeOptions();

    @Value("${webdriver.retry-count:3}")
    private Integer retryCount;

    @Value("${webdriver.retry-wait:5000}")
    private Integer retryWait;

    public WebDriver createWebDriver() {
        return WebDriverManager.chromedriver()
                               .driverVersion(WEBDRIVER_CHROME_VERSION)
                               .capabilities(WEBDRIVER_CHROME_OPTIONS)
                               .create();
    }

    private static ChromeOptions configureChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        return chromeOptions;
    }

}

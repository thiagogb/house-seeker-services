package br.com.houseseeker.service;

import br.com.houseseeker.AbstractMockWebServerTest;
import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.atomic.AtomicInteger;

import static br.com.houseseeker.TestUtils.getTextFromResources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(
        classes = WebDriverFactoryService.class,
        properties = {
                "webdriver.retry-count=2",
                "webdriver.retry-wait=500"
        }
)
class AbstractWebDriverScraperServiceTest extends AbstractMockWebServerTest {

    private static final String SAMPLE_TEST_PAGE = "samples/test-page.html";

    @Autowired
    private WebDriverFactoryService webDriverFactoryService;

    @Test
    @DisplayName("given a test document when scrap then expects to retrieve value")
    void givenATestDocument_whenScrap_thenExpectsToRetrieveValue() {
        whenDispatch(recordedRequest -> new MockResponse().setResponseCode(200).setBody(getTextFromResources(SAMPLE_TEST_PAGE)));

        try (var scraper = new TestWebDriverSuccessScraper(webDriverFactoryService)) {
            assertThat(scraper.scrap(getBaseUrl())).isEqualTo("Page content");
        }
    }

    @Test
    @DisplayName("given a test document when scrap then expects exception")
    void givenATestDocument_whenScrap_thenExpectsException() {
        String baseUrl = getBaseUrl();

        whenDispatch(recordedRequest -> new MockResponse().setResponseCode(200).setBody(getTextFromResources(SAMPLE_TEST_PAGE)));

        try (var scraper = new TestWebDriverFailScraper(webDriverFactoryService)) {
            assertThatThrownBy(() -> scraper.scrap(baseUrl))
                    .isInstanceOf(ExtendedRuntimeException.class)
                    .hasMessage("Scraper fail using WebDriver");
        }
    }

    @Test
    @DisplayName("given a request with response code 500 when scrap then expects exception")
    void givenARequestWithResponseCode500_whenScrap_thenExpectsException() {
        String baseUrl = getBaseUrl();

        whenDispatch(recordedRequest -> new MockResponse().setResponseCode(500));

        try (var scraper = new TestWebDriverSuccessScraper(webDriverFactoryService)) {
            assertThatThrownBy(() -> scraper.scrap(baseUrl))
                    .isInstanceOf(ExtendedRuntimeException.class)
                    .hasMessage("Scraper fail using WebDriver");
        }
    }

    @Test
    @DisplayName("given a first request fail and second ok when scrap then expects to retrieve value")
    void givenAFirstRequestFailAndSecondOk_whenScrap_thenExpectsToRetrieveValue() {
        AtomicInteger requestCount = new AtomicInteger(0);
        whenDispatch(recordedRequest -> {
            if (requestCount.getAndIncrement() == 1)
                return new MockResponse().setResponseCode(500);

            return new MockResponse().setResponseCode(200).setBody(getTextFromResources(SAMPLE_TEST_PAGE));
        });

        try (var scraper = new TestWebDriverSuccessScraper(webDriverFactoryService)) {
            assertThat(scraper.scrap(getBaseUrl())).isEqualTo("Page content");
        }
    }

    private static final class TestWebDriverSuccessScraper extends AbstractWebDriverScraperService<String> {

        TestWebDriverSuccessScraper(WebDriverFactoryService webDriverFactoryService) {
            super(webDriverFactoryService);
        }

        @Override
        protected String extractWithWebDriver(WebDriver webDriver) {
            return webDriver.findElement(By.id("content")).getText();
        }
    }

    private static final class TestWebDriverFailScraper extends AbstractWebDriverScraperService<String> {

        TestWebDriverFailScraper(WebDriverFactoryService webDriverFactoryService) {
            super(webDriverFactoryService);
        }

        @Override
        protected String extractWithWebDriver(WebDriver webDriver) {
            return webDriver.findElement(By.id("undefined")).getText();
        }
    }

}
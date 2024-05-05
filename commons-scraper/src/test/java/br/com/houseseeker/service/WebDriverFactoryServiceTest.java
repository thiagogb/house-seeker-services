package br.com.houseseeker.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = WebDriverFactoryService.class)
class WebDriverFactoryServiceTest {

    @Autowired
    private WebDriverFactoryService webDriverFactoryService;

    @Test
    @DisplayName("given engine chrome defined in properties when calls createWebDriver then expects")
    void givenEngineChromeDefinedInProperties_whenCallsCreateWebDriver_thenExpects() {
        assertThat(webDriverFactoryService.createWebDriver()).isNotNull();
    }

}
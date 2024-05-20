package br.com.houseseeker.service.v2;

import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import br.com.houseseeker.domain.jetimob.v2.SearchPageMetadata;
import br.com.houseseeker.domain.jetimob.v2.SearchPageMetadata.Item;
import br.com.houseseeker.domain.jetimob.v2.SearchPageMetadata.Pagination;
import br.com.houseseeker.service.AbstractWebDriverScraperService;
import br.com.houseseeker.service.WebDriverFactoryService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static br.com.houseseeker.util.StringUtils.getNonBlank;
import static br.com.houseseeker.util.WebDriverUtils.waitUntil;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@Slf4j
public class SearchPageV2ScraperService extends AbstractWebDriverScraperService<SearchPageMetadata> {

    private static final String PROPERTY_LINK_IDENTIFIER = "/imovel/";
    private static final String PROPERTY_CODE_PREFIX = "cód.";
    private static final String PAGINATOR_NEXT_LABEL = "Próximo";

    public SearchPageV2ScraperService(WebDriverFactoryService webDriverFactoryService) {
        super(webDriverFactoryService);
    }

    @Override
    protected SearchPageMetadata extractWithWebDriver(WebDriver webDriver) {
        waitUntil(webDriver, this::hasPhoenixComponentsInScreen);
        return SearchPageMetadata.builder()
                                 .items(extractItems(webDriver))
                                 .pagination(extractPagination(webDriver))
                                 .build();
    }

    private boolean hasPhoenixComponentsInScreen(WebDriver webDriver) {
        return !webDriver.findElements(By.cssSelector("div[data-phx-component]")).isEmpty();
    }

    private List<Item> extractItems(WebDriver webDriver) {
        return webDriver.findElements(By.cssSelector("div[data-phx-component] > a[data-phx-link]"))
                        .stream()
                        .filter(this::isPropertyLink)
                        .map(this::extractItem)
                        .toList();
    }

    private boolean isPropertyLink(WebElement element) {
        return getNonBlank(element.getAttribute("href"))
                .map(href -> href.trim().toLowerCase().contains(PROPERTY_LINK_IDENTIFIER))
                .orElse(false);
    }

    private Item extractItem(WebElement element) {
        return Item.builder()
                   .pageLink(element.getAttribute("href"))
                   .providerCode(
                           element.findElements(By.cssSelector("div:nth-child(3) > div.text-xs"))
                                  .stream()
                                  .findFirst()
                                  .map(this::extractProviderCode)
                                  .orElseThrow(() -> new ExtendedRuntimeException("Provider code element not found"))
                   )
                   .build();
    }

    private String extractProviderCode(WebElement element) {
        String text = getNonBlank(element.getText())
                .map(t -> t.trim().toLowerCase())
                .orElse(EMPTY);

        if (isBlank(text) || !text.startsWith(PROPERTY_CODE_PREFIX))
            throw new ExtendedRuntimeException("Provider code element content is empty or invalid");

        List<String> providerCodeParts = Arrays.asList(text.split(SPACE));

        if (providerCodeParts.size() != 2)
            throw new ExtendedRuntimeException("Provider code is invalid");

        return providerCodeParts.get(1);
    }

    private Pagination extractPagination(WebDriver webDriver) {
        List<WebElement> paginationButtonElements = findPaginationButtonsSilently(webDriver);
        return Pagination.builder()
                         .isLastPage(
                                 paginationButtonElements.stream()
                                                         .noneMatch(e -> e.getText().equalsIgnoreCase(PAGINATOR_NEXT_LABEL))
                         )
                         .build();
    }

    private List<WebElement> findPaginationButtonsSilently(WebDriver webDriver) {
        try {
            return webDriver.findElements(By.cssSelector("div[data-phx-component] a[data-phx-link] > span"));
        } catch (Exception e) {
            log.warn("Pagination button not found in url {}", webDriver.getCurrentUrl(), e);
            return Collections.emptyList();
        }
    }

}

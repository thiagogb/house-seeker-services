package br.com.houseseeker.service.v2;

import br.com.houseseeker.AbstractMockWebServerTest;
import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import br.com.houseseeker.domain.jetimob.v2.SearchPageMetadata;
import br.com.houseseeker.domain.jetimob.v2.SearchPageMetadata.Item;
import br.com.houseseeker.domain.jetimob.v2.SearchPageMetadata.Pagination;
import br.com.houseseeker.service.WebDriverFactoryService;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static br.com.houseseeker.TestUtils.getTextFromResources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(
        classes = {
                WebDriverFactoryService.class,
                SearchPageV2ScraperService.class
        },
        properties = {
                "webdriver.retry-count=1",
                "webdriver.retry-wait=100"
        }
)
class SearchPageV2ScraperServiceTest extends AbstractMockWebServerTest {

    private static final String SAMPLE_WITH_ITEMS = "samples/v2/search/with-items.html";
    private static final String SAMPLE_WITHOUT_ITEMS = "samples/v2/search/without-items.html";
    private static final String SAMPLE_EMPTY = "samples/v2/search/empty.html";
    private static final String SAMPLE_EMPTY_PROVIDER_CODE = "samples/v2/search/empty-provider-code.html";
    private static final String SAMPLE_INVALID_PROVIDER_CODE = "samples/v2/search/invalid-provider-code.html";

    @Autowired
    private SearchPageV2ScraperService searchPageV2ScraperService;

    @Test
    @DisplayName("given a sample page with items when calls scrap then expects")
    void givenASamplePageWithItems_whenCallsScrap_thenExpects() {
        whenDispatch(recordedRequest -> new MockResponse().setResponseCode(200).setBody(getTextFromResources(SAMPLE_WITH_ITEMS)));

        assertThat(searchPageV2ScraperService.scrap(getBaseUrl()))
                .extracting(SearchPageMetadata::getItems, SearchPageMetadata::getPagination)
                .containsExactly(
                        List.of(
                                Item.builder()
                                    .pageLink(getBaseUrl() + "/imovel/terreno-plano-em-loteamento-aberto-prximo-a-camobi/041297")
                                    .providerCode("041297")
                                    .build()
                        ),
                        Pagination.builder()
                                  .isLastPage(false)
                                  .build()
                );
    }

    @Test
    @DisplayName("given a sample page without items when calls scrap then expects")
    void givenASamplePageWithoutItems_whenCallsScrap_thenExpects() {
        whenDispatch(recordedRequest -> new MockResponse().setResponseCode(200).setBody(getTextFromResources(SAMPLE_WITHOUT_ITEMS)));

        assertThat(searchPageV2ScraperService.scrap(getBaseUrl()))
                .extracting(SearchPageMetadata::getItems, SearchPageMetadata::getPagination)
                .containsExactly(
                        Collections.emptyList(),
                        Pagination.builder()
                                  .isLastPage(true)
                                  .build()
                );
    }

    @Test
    @DisplayName("given a sample empty page when calls scrap then expects exception")
    void givenASampleEmptyPage_whenCallsScrap_thenExpectsException() {
        whenDispatch(recordedRequest -> new MockResponse().setResponseCode(200).setBody(getTextFromResources(SAMPLE_EMPTY)));

        assertThatThrownBy(() -> searchPageV2ScraperService.scrap(getBaseUrl()))
                .isInstanceOf(ExtendedRuntimeException.class)
                .hasMessage("Scraper fail using WebDriver");
    }

    @Test
    @DisplayName("given a sample with empty provider code calls scrap then expects exception")
    void givenASampleWithEmptyProviderCode_whenCallsScrap_thenExpectsException() {
        whenDispatch(recordedRequest -> new MockResponse().setResponseCode(200).setBody(getTextFromResources(SAMPLE_EMPTY_PROVIDER_CODE)));

        assertThatThrownBy(() -> searchPageV2ScraperService.scrap(getBaseUrl()))
                .isInstanceOf(ExtendedRuntimeException.class)
                .hasMessage("Provider code element content is empty or invalid");
    }

    @Test
    @DisplayName("given a sample with invalid provider code calls scrap then expects exception")
    void givenASampleWithInvalidProviderCode_whenCallsScrap_thenExpectsException() {
        whenDispatch(recordedRequest -> new MockResponse().setResponseCode(200).setBody(getTextFromResources(SAMPLE_INVALID_PROVIDER_CODE)));

        assertThatThrownBy(() -> searchPageV2ScraperService.scrap(getBaseUrl()))
                .isInstanceOf(ExtendedRuntimeException.class)
                .hasMessage("Provider code is invalid");
    }

}
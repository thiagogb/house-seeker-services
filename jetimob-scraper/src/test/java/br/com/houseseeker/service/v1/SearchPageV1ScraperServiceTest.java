package br.com.houseseeker.service.v1;

import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import br.com.houseseeker.domain.jetimob.v1.SearchPageMetadata;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static br.com.houseseeker.TestUtils.getTextFromResources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = SearchPageV1ScraperService.class)
class SearchPageV1ScraperServiceTest {

    private static final String SAMPLE_EMPTY_DOCUMENT = "samples/empty-document.html";
    private static final String SAMPLE_WITHOUT_IMAGE_CONTAINER = "samples/v1/search/without-image-container.html";
    private static final String SAMPLE_WITHOUT_PAGE_LINK = "samples/v1/search/without-page-link.html";
    private static final String SAMPLE_WITHOUT_BADGES = "samples/v1/search/without-badges.html";
    private static final String SAMPLE_WITHOUT_PROVIDER_CODE = "samples/v1/search/without-provider-code.html";
    private static final String SAMPLE_WITH_TWO_ITEMS = "samples/v1/search/with-two-items.html";

    @Autowired
    private SearchPageV1ScraperService searchPageV1ScraperService;

    @Test
    @DisplayName("given search page without items when calls scrap then expects exception")
    void givenSearchPageWithoutItems_whenCallsScrap_thenExpectsException() {
        assertThatThrownBy(() -> scrapWithSample(SAMPLE_EMPTY_DOCUMENT))
                .isInstanceOf(ExtendedRuntimeException.class)
                .hasMessage("Failed to extract current page number");
    }

    @Test
    @DisplayName("given search page with items without image container when calls scrap then expects exception")
    void givenSearchPageWithItemsWithoutImageContainer_whenCallsScrap_thenExpectsException() {
        assertThatThrownBy(() -> scrapWithSample(SAMPLE_WITHOUT_IMAGE_CONTAINER))
                .isInstanceOf(ExtendedRuntimeException.class)
                .hasMessage("Image container element not found");
    }

    @Test
    @DisplayName("given search page with items without image container href when calls scrap then expects exception")
    void givenSearchPageWithItemsWithoutImageContainerHref_whenCallsScrap_thenExpectsException() {
        assertThatThrownBy(() -> scrapWithSample(SAMPLE_WITHOUT_PAGE_LINK))
                .isInstanceOf(ExtendedRuntimeException.class)
                .hasMessage("Required attribute href is blank");
    }

    @Test
    @DisplayName("given search page with items without image container badges when calls scrap then expects exception")
    void givenSearchPageWithItemsWithoutImageContainerBadges_whenCallsScrap_thenExpectsException() {
        assertThatThrownBy(() -> scrapWithSample(SAMPLE_WITHOUT_BADGES))
                .isInstanceOf(ExtendedRuntimeException.class)
                .hasMessage("Failed to extract subType");
    }

    @Test
    @DisplayName("given search page with items without image container provider code when calls scrap then expects exception")
    void givenSearchPageWithItemsWithoutImageContainerProviderCode_whenCallsScrap_thenExpectsException() {
        assertThatThrownBy(() -> scrapWithSample(SAMPLE_WITHOUT_PROVIDER_CODE))
                .isInstanceOf(ExtendedRuntimeException.class)
                .hasMessage("Failed to extract providerCode");
    }

    @Test
    @DisplayName("given search page with two items when calls scrap then expects search page metadata")
    void givenSearchPageWithTwoItems_whenCallsScrap_thenExpectsSearchPageMetadata() {
        assertThat(scrapWithSample(SAMPLE_WITH_TWO_ITEMS))
                .hasFieldOrPropertyWithValue("contract", null)
                .extracting("items", "pagination")
                .containsExactly(
                        List.of(
                                SearchPageMetadata.Item.builder()
                                                       .pageLink("https://sample.com/1")
                                                       .subType("SubType 1")
                                                       .providerCode("Provider Code 1")
                                                       .build(),
                                SearchPageMetadata.Item.builder()
                                                       .pageLink("https://sample.com/2")
                                                       .subType("SubType 2")
                                                       .providerCode("Provider Code 2")
                                                       .build()
                        ),
                        SearchPageMetadata.Pagination.builder()
                                                     .currentPage(1)
                                                     .lastPage(2)
                                                     .build()
                );
    }

    private SearchPageMetadata scrapWithSample(String sample) {
        return searchPageV1ScraperService.scrap(getTextFromResources(sample));
    }

}
package br.com.houseseeker.service.v1;

import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import br.com.houseseeker.domain.jetimob.v1.SearchPageMetadata;
import br.com.houseseeker.domain.jetimob.v1.SearchPageMetadata.Item;
import br.com.houseseeker.domain.jetimob.v1.SearchPageMetadata.Pagination;
import br.com.houseseeker.service.AbstractJsoupScraperService;
import br.com.houseseeker.util.ConverterUtils;
import br.com.houseseeker.util.JsoupUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SearchPageV1ScraperService extends AbstractJsoupScraperService<SearchPageMetadata> {

    @Override
    protected SearchPageMetadata scrapDocument(Document document) {
        return SearchPageMetadata.builder()
                                 .items(scrapItems(document))
                                 .pagination(scrapPagination(document))
                                 .build();
    }

    private List<Item> scrapItems(Document document) {
        return document.select("li.imoveis-list-item")
                       .stream()
                       .map(this::scrapItem)
                       .toList();
    }

    private Item scrapItem(Element element) {
        Element imageContainerElement = Optional.ofNullable(element.selectFirst("a.imovel-image-container"))
                                                .orElseThrow(() -> new ExtendedRuntimeException("Image container element not found"));

        Elements badgeElements = imageContainerElement.select(".imovel-badge");

        String pageLink = JsoupUtils.getNonBlankAttribute(imageContainerElement, "href")
                                    .orElseThrow(() -> new ExtendedRuntimeException("Required attribute href is blank"));

        String subType = Optional.ofNullable(badgeElements.select(".left.top").first())
                                 .flatMap(lt -> Optional.ofNullable(lt.select(".primary-bg").first()))
                                 .flatMap(JsoupUtils::getNonBlankHtml)
                                 .orElseThrow(() -> new ExtendedRuntimeException("Failed to extract subType"));

        String providerCode = Optional.ofNullable(badgeElements.select(".right.top").first())
                                      .flatMap(JsoupUtils::getNonBlankHtml)
                                      .orElseThrow(() -> new ExtendedRuntimeException("Failed to extract providerCode"));

        return Item.builder()
                   .pageLink(pageLink)
                   .subType(subType)
                   .providerCode(providerCode)
                   .build();
    }

    private Pagination scrapPagination(Document document) {
        return Pagination.builder()
                         .currentPage(
                                 Optional.ofNullable(document.selectFirst("#pagination-current"))
                                         .flatMap(e -> JsoupUtils.getNonBlankAttribute(e, "value"))
                                         .flatMap(ConverterUtils::tryToInteger)
                                         .orElseThrow(() -> new ExtendedRuntimeException("Failed to extract current page number"))
                         )
                         .lastPage(
                                 Optional.ofNullable(document.selectFirst("#pagination-max"))
                                         .flatMap(e -> JsoupUtils.getNonBlankAttribute(e, "value"))
                                         .flatMap(ConverterUtils::tryToInteger)
                                         .orElseThrow(() -> new ExtendedRuntimeException("Failed to extract max number"))
                         )
                         .build();
    }

}

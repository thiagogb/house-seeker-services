package br.com.houseseeker.service.v2;

import br.com.houseseeker.domain.jetimob.v2.FilterOptionsMetadata;
import br.com.houseseeker.service.AbstractJsoupScraperService;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static br.com.houseseeker.util.JsoupUtils.getNonBlankAttribute;

@Service
public class FilterOptionsV2ScraperService extends AbstractJsoupScraperService<FilterOptionsMetadata> {

    @Override
    protected FilterOptionsMetadata scrapDocument(Document document) {
        return FilterOptionsMetadata.builder()
                                    .cities(extractCities(document))
                                    .types(extractTypes(document))
                                    .build();
    }

    private List<String> extractCities(Element element) {
        return element.select("select[name=city] option")
                      .stream()
                      .map(e -> getNonBlankAttribute(e, "value"))
                      .filter(Optional::isPresent)
                      .map(Optional::get)
                      .toList();
    }

    private List<String> extractTypes(Element element) {
        return element.select("input[name='subtypes[]']")
                      .stream()
                      .map(e -> getNonBlankAttribute(e, "value"))
                      .filter(Optional::isPresent)
                      .map(Optional::get)
                      .toList();
    }

}

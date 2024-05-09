package br.com.houseseeker.service.v2;

import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import br.com.houseseeker.domain.jetimob.PropertyCharacteristic;
import br.com.houseseeker.domain.jetimob.PropertyDetail;
import br.com.houseseeker.domain.jetimob.v2.PropertyInfoMetadata;
import br.com.houseseeker.domain.jetimob.v2.PropertyInfoMetadata.Location;
import br.com.houseseeker.domain.jetimob.v2.PropertyInfoMetadata.Media;
import br.com.houseseeker.domain.jetimob.v2.PropertyInfoMetadata.Pricing;
import br.com.houseseeker.service.AbstractJsoupScraperService;
import br.com.houseseeker.util.JsoupUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static br.com.houseseeker.util.JsoupUtils.getNonBlankAttribute;
import static br.com.houseseeker.util.ListUtils.getAtIndex;
import static br.com.houseseeker.util.StringUtils.removeParentheses;
import static br.com.houseseeker.util.UrlUtils.getExtension;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
@Slf4j
public class PropertyPageV2ScraperService extends AbstractJsoupScraperService<PropertyInfoMetadata> {

    private static final String PROPERTY_ABOUT_TITLE = "Sobre esse Imóvel";
    private static final String PROPERTY_PRICING_TITLE = "Oportunidade";
    private static final String PROPERTY_SELL_LABEL = "Compra";
    private static final String PROPERTY_RENT_LABEL = "Aluguel";
    private static final String PROPERTY_CONDOMINIUM_LABEL = "Condomínio";
    private static final String PROPERTY_DETAILS_TITLE = "Características";
    private static final String PROPERTY_COMFORTS_TITLE = "Confortos";

    @Override
    protected PropertyInfoMetadata scrapDocument(Document document) {
        Element rootElement = Optional.ofNullable(document.selectFirst("div[data-phx-main] div.container"))
                                      .orElseThrow(() -> new ExtendedRuntimeException("Element container not found"));

        return PropertyInfoMetadata.builder()
                                   .description(extractDescriptionData(rootElement))
                                   .location(extractLocationData(rootElement))
                                   .pricing(extractPricingData(rootElement))
                                   .characteristics(extractCharacteristicsData(rootElement))
                                   .medias(extractMediaData(rootElement))
                                   .details(extractDetailsData(rootElement))
                                   .comforts(extractComfortsData(rootElement))
                                   .build();
    }

    private String extractDescriptionData(Element element) {
        Element rootElement = Optional.ofNullable(element.selectXpath(String.format("//h3[text()='%s']", PROPERTY_ABOUT_TITLE)).first())
                                      .map(Element::parent)
                                      .orElse(null);

        if (isNull(rootElement))
            return null;

        return rootElement.select("> div.prose > p")
                          .stream()
                          .map(JsoupUtils::getNonBlankHtml)
                          .filter(Optional::isPresent)
                          .map(Optional::get)
                          .collect(Collectors.joining(System.lineSeparator()));
    }

    private Location extractLocationData(Element element) {
        Location.LocationBuilder builder = Location.builder();

        Element rootElement = element.selectXpath("div[1]/div[1]/section[1]/div").first();

        if (nonNull(rootElement)) {
            String h2 = Optional.ofNullable(rootElement.selectFirst("h2"))
                                .map(JsoupUtils::getNonBlankHtml)
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .orElseThrow(() -> new ExtendedRuntimeException("Location h2 element not found"));

            List<String> locationParts = Arrays.asList(h2.split(","));

            builder.district(getAtIndex(locationParts, 0).map(String::trim).orElse(null))
                   .city(getAtIndex(locationParts, 1).map(String::trim).orElse(null))
                   .state(getAtIndex(locationParts, 2).map(String::trim).orElse(null));

            extractLocationCoords(element)
                    .ifPresent(coords -> builder.latitude(coords.getLeft()).longitude(coords.getRight()));
        }

        return builder.build();
    }

    private Optional<Pair<String, String>> extractLocationCoords(Element element) {
        Element rootElement = element.selectFirst("leaflet-map");
        return nonNull(rootElement)
                ? Optional.of(Pair.of(rootElement.attr("lat"), rootElement.attr("lng")))
                : Optional.empty();
    }

    private Pricing extractPricingData(Element element) {
        Pricing.PricingBuilder builder = Pricing.builder();

        Element rootElement = Optional.ofNullable(element.selectXpath(String.format("//span[text()='%s']", PROPERTY_PRICING_TITLE)).first())
                                      .map(Element::parent)
                                      .orElse(null);

        if (nonNull(rootElement)) {
            builder.sellPrice(extractPriceValue(rootElement, PROPERTY_SELL_LABEL))
                   .rentPrice(extractPriceValue(rootElement, PROPERTY_RENT_LABEL))
                   .condominiumPrice(Optional.ofNullable(rootElement.parent()).map(e -> extractPriceValue(e, PROPERTY_CONDOMINIUM_LABEL)).orElse(null));
        }

        return builder.build();
    }

    private String extractPriceValue(Element element, String label) {
        return Optional.ofNullable(element.selectXpath(String.format("//div/span[text()='%s']", label)).first())
                       .map(Element::nextElementSibling)
                       .flatMap(JsoupUtils::getNonBlankHtml)
                       .orElse(null);
    }

    private List<PropertyCharacteristic> extractCharacteristicsData(Element element) {
        List<PropertyCharacteristic> result = new LinkedList<>();

        element.select("> div > section:nth-child(2) > section:nth-child(1) > div.grid-cols-4 > div > div:nth-child(2) > span")
               .stream()
               .map(JsoupUtils::getNonBlankHtml)
               .filter(Optional::isPresent)
               .map(Optional::get)
               .forEach(text -> {
                   String value = removeParentheses(text.trim());
                   result.add(
                           PropertyCharacteristic.builder()
                                                 .value(value)
                                                 .type(PropertyCharacteristic.Type.typeOf(value))
                                                 .build()
                   );
               });

        return result;
    }

    private List<Media> extractMediaData(Element element) {
        return element.select("div.carousel__track img")
                      .stream()
                      .map(e -> getNonBlankAttribute(e, "src"))
                      .filter(Optional::isPresent)
                      .map(Optional::get)
                      .map(e -> Media.builder()
                                     .link(e)
                                     .extension(getExtension(e).orElse(null))
                                     .build()
                      )
                      .toList();
    }

    private List<PropertyDetail> extractDetailsData(Element element) {
        List<PropertyDetail> result = new LinkedList<>();

        Element rootElement = Optional.ofNullable(element.selectXpath(String.format("//h3[text()='%s']", PROPERTY_DETAILS_TITLE)).first())
                                      .map(Element::parent)
                                      .orElse(null);

        if (nonNull(rootElement)) {
            rootElement.select("> div > div > h4")
                       .stream()
                       .filter(e -> isNotBlank(e.html()))
                       .map(this::extractDetail)
                       .forEach(result::add);
        }

        return result;
    }

    private PropertyDetail extractDetail(Element element) {
        return PropertyDetail.builder()
                             .type(PropertyDetail.Type.typeOf(element.html()))
                             .items(
                                     element.parent()
                                            .select("> div > div > span")
                                            .stream()
                                            .map(JsoupUtils::getNonBlankHtml)
                                            .filter(Optional::isPresent)
                                            .map(Optional::get)
                                            .toList()
                             )
                             .build();
    }

    private List<String> extractComfortsData(Element element) {
        List<String> result = new LinkedList<>();

        Element rootElement = Optional.ofNullable(element.selectXpath(String.format("//h3[text()='%s']", PROPERTY_COMFORTS_TITLE)).first())
                                      .map(Element::parent)
                                      .orElse(null);

        if (nonNull(rootElement)) {
            rootElement.select("> div > div > span")
                       .stream()
                       .map(Element::html)
                       .filter(StringUtils::isNotBlank)
                       .forEach(result::add);
        }

        return result;
    }

}

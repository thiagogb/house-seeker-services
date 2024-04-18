package br.com.houseseeker.service.v1;

import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import br.com.houseseeker.domain.jetimob.v1.PropertyCharacteristicType;
import br.com.houseseeker.domain.jetimob.v1.PropertyInfoMetadata;
import br.com.houseseeker.domain.jetimob.v1.PropertyInfoMetadata.Characteristics;
import br.com.houseseeker.domain.jetimob.v1.PropertyInfoMetadata.Convenience;
import br.com.houseseeker.domain.jetimob.v1.PropertyInfoMetadata.Location;
import br.com.houseseeker.domain.jetimob.v1.PropertyInfoMetadata.Media;
import br.com.houseseeker.domain.jetimob.v1.PropertyInfoMetadata.Pricing;
import br.com.houseseeker.domain.jetimob.v1.PropertyPricingType;
import br.com.houseseeker.service.AbstractJsoupScraperService;
import br.com.houseseeker.util.JsoupUtils;
import br.com.houseseeker.util.MediaUtils;
import br.com.houseseeker.util.ObjectMapperUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNullElse;

@Service
@RequiredArgsConstructor
@Slf4j
public class PropertyPageV1ScraperService extends AbstractJsoupScraperService<PropertyInfoMetadata> {

    private final ObjectMapper objectMapper;

    @Override
    protected PropertyInfoMetadata scrapDocument(Document document) {
        Elements infoBoxElements = document.select(".imovel-info-box-content");
        Elements sectionCardElements = document.select("#imovel-infos > .section.container-card");
        return PropertyInfoMetadata.builder()
                                   .location(scrapLocation(infoBoxElements))
                                   .characteristics(scrapCharacteristics(infoBoxElements))
                                   .pricing(scrapPricing(infoBoxElements))
                                   .medias(scrapMedias(sectionCardElements))
                                   .convenience(scrapConvenience(sectionCardElements))
                                   .build();
    }

    private Location scrapLocation(Elements elements) {
        Element rootElement = JsoupUtils.getElementAtIndex(elements, 1)
                                        .orElseThrow(() -> new ExtendedRuntimeException("Location root element not found"));

        Elements paragraphElements = rootElement.select("> p");

        String streetName = Optional.ofNullable(paragraphElements.first())
                                    .map(Element::html)
                                    .orElse(null);

        String district = JsoupUtils.getElementAtIndex(paragraphElements, 1)
                                    .flatMap(e -> Optional.ofNullable(e.select("strong").first()))
                                    .map(Element::html)
                                    .orElse(null);

        String condominiumName = Optional.ofNullable(rootElement.select("> strong").first())
                                         .map(Element::html)
                                         .orElse(null);

        Pair<String, String> cityState = Optional.ofNullable(paragraphElements.last())
                                                 .map(e -> {
                                                     List<String> cityStateParts = Arrays.asList(e.html().split("-"));
                                                     return Pair.of(cityStateParts.getFirst().trim(), cityStateParts.getLast().trim());
                                                 })
                                                 .orElse(Pair.of(null, null));

        return Location.builder()
                       .streetName(streetName)
                       .district(district)
                       .condominiumName(condominiumName)
                       .city(cityState.getLeft())
                       .state(cityState.getRight())
                       .build();
    }

    private List<Characteristics> scrapCharacteristics(Elements elements) {
        Element rootElement = JsoupUtils.getElementAtIndex(elements, 2)
                                        .orElseThrow(() -> new ExtendedRuntimeException("Characteristics root element not found"));

        return rootElement.select("> .imovel-header-item")
                          .stream()
                          .map(this::scrapCharacteristic)
                          .toList();
    }

    private Characteristics scrapCharacteristic(Element element) {
        PropertyCharacteristicType type = Optional.ofNullable(element.select("i.imovel-icon").first())
                                                  .flatMap(e -> PropertyCharacteristicType.getByClasses(e.classNames()))
                                                  .orElseThrow(() -> new ExtendedRuntimeException("Unknown characteristic class"));

        String name = Optional.ofNullable(element.select("small.text").first())
                              .flatMap(JsoupUtils::getNonBlankHtml)
                              .orElseThrow(() -> new ExtendedRuntimeException("Failed to extract characteristic name"));

        String value = Optional.ofNullable(element.select("strong.text").first())
                               .flatMap(JsoupUtils::getNonBlankHtml)
                               .orElseThrow(() -> new ExtendedRuntimeException("Failed to extract characteristic value"));

        String additional = Optional.ofNullable(element.select("small:not(.text)").first())
                                    .flatMap(JsoupUtils::getNonBlankHtml)
                                    .orElse(null);

        return Characteristics.builder().type(type)
                              .name(name)
                              .value(value)
                              .additional(additional)
                              .build();
    }

    private List<Pricing> scrapPricing(Elements elements) {
        Element rootElement = JsoupUtils.getElementAtIndex(elements, 3)
                                        .orElseThrow(() -> new ExtendedRuntimeException("Pricing root element not found"));

        return rootElement.select("> .imovel-header-item")
                          .stream()
                          .map(this::scrapPricing)
                          .toList();
    }

    private Pricing scrapPricing(Element element) {
        String name = Optional.ofNullable(element.select("small.text").first())
                              .flatMap(JsoupUtils::getNonBlankHtml)
                              .orElseThrow(() -> new ExtendedRuntimeException("Failed to extract pricing name"));

        String value = Optional.ofNullable(element.select("strong.text").first())
                               .flatMap(JsoupUtils::getNonBlankHtml)
                               .orElseThrow(() -> new ExtendedRuntimeException("Failed to extract pricing value"));

        return Pricing.builder()
                      .name(name)
                      .value(value)
                      .type(PropertyPricingType.getByName(name).orElseThrow(() -> new ExtendedRuntimeException("Unknown pricing type")))
                      .build();
    }

    private List<Media> scrapMedias(Elements elements) {
        return Optional.ofNullable(elements.select("[data-media]").first())
                       .map(e -> JsoupUtils.getNonBlankAttribute(e, "data-media")
                                           .map(this::extractMediaData)
                                           .orElse(Collections.emptyList())
                       )
                       .orElse(Collections.emptyList());
    }

    private List<Media> extractMediaData(String data) {
        MediaMetadata mediaMetadata = ObjectMapperUtils.deserializeAs(objectMapper, data, MediaMetadata.class);
        return Stream.concat(
                             requireNonNullElse(mediaMetadata.propertyGallery, new LinkedList<MediaMetadata.GalleryItem>()).stream(),
                             requireNonNullElse(mediaMetadata.condominiumGallery, new LinkedList<MediaMetadata.GalleryItem>()).stream()
                     )
                     .flatMap(this::extractMediaFromGallery)
                     .toList();
    }

    private Stream<Media> extractMediaFromGallery(MediaMetadata.GalleryItem galleryItem) {
        if (!galleryItem.galleryClass.equals("images"))
            return Stream.empty();

        return galleryItem.gallery.stream()
                                  .map(mgi -> Media.builder()
                                                   .link(mgi.href)
                                                   .linkThumb(mgi.sizes.stream().findFirst().map(mgis -> mgis.href).orElse(null))
                                                   .type(galleryItem.galleryClass)
                                                   .extension(MediaUtils.getMediaExtension(mgi.href).orElse(null))
                                                   .build()
                                  );
    }

    private Convenience scrapConvenience(Elements elements) {
        Convenience.ConvenienceBuilder builder = Convenience.builder();
        List<String> items = new LinkedList<>();
        JsoupUtils.getElementAtIndex(elements, 3)
                  .ifPresent(e -> {
                      e.select(".list-group-item").forEach(lgi -> {
                          Optional.ofNullable(lgi.select(".section-subtitle").first())
                                  .flatMap(JsoupUtils::getNonBlankHtml)
                                  .ifPresent(st -> {
                                      switch (st.toLowerCase().trim()) {
                                          case "descrição":
                                              builder.description(
                                                      Optional.ofNullable(lgi.select("pre").first())
                                                              .flatMap(JsoupUtils::getNonBlankHtml)
                                                              .orElse(null)
                                              );
                                              break;
                                          case "características":
                                          case "comodidades":
                                              items.addAll(
                                                      lgi.select("li.list-item")
                                                         .stream()
                                                         .map(Element::html)
                                                         .toList()
                                              );
                                              break;
                                          default:
                                              log.info("Unknown convenience subtitle {}", st);
                                      }
                                  });
                      });
                  });
        return builder.items(items).build();
    }

    private static final class MediaMetadata {

        @JsonProperty("Imóvel")
        private List<GalleryItem> propertyGallery;

        @JsonProperty("Condomínio")
        private List<GalleryItem> condominiumGallery;

        public static final class GalleryItem {

            @JsonProperty("gallery")
            private List<Media> gallery;

            @JsonProperty("galleryClass")
            private String galleryClass;

            public static final class Media {

                @JsonProperty("href")
                private String href;

                @JsonProperty("sizes")
                private List<Size> sizes;

                public static final class Size {

                    @JsonProperty("href")
                    private String href;

                }

            }

        }

    }

}

package br.com.houseseeker.util;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Optional;

import static br.com.houseseeker.util.StringUtils.getNonBlank;

@UtilityClass
public class JsoupUtils {

    public Optional<String> getNonBlankHtml(@NotNull Element element) {
        return getNonBlank(element.html());
    }

    public Optional<String> getNonBlankAttribute(@NotNull Element element, @NotBlank String name) {
        return getNonBlank(element.attr(name));
    }

    public Optional<Element> getElementAtIndex(@NotNull Elements elements, int index) {
        try {
            return Optional.of(elements.get(index));
        } catch (IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

}

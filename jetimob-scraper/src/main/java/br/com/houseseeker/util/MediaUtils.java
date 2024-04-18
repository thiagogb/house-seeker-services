package br.com.houseseeker.util;

import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FilenameUtils;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.lang.Nullable;

import java.net.URISyntaxException;
import java.util.Optional;

import static br.com.houseseeker.util.StringUtils.getNonBlank;
import static org.apache.commons.lang3.StringUtils.isBlank;

@UtilityClass
public class MediaUtils {

    public Optional<String> getMediaExtension(@Nullable String url) {
        if (isBlank(url))
            return Optional.empty();

        return getNonBlank(FilenameUtils.getExtension(clearQueryParams(url)));
    }

    public String clearQueryParams(@NotNull String url) {
        try {
            return new URIBuilder(url).removeQuery().build().toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to clear query params from url", e);
        }
    }

}

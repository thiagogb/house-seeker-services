package br.com.houseseeker.util;

import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FilenameUtils;
import org.apache.hc.core5.net.URIBuilder;

import java.net.URISyntaxException;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;

@UtilityClass
public class UrlUtils {

    public Optional<String> getExtension(@NotNull String url) {
        if (isBlank(url))
            return Optional.empty();

        return Optional.of(FilenameUtils.getExtension(clearQueryParams(url)));
    }

    private String clearQueryParams(@NotNull String url) {
        try {
            return new URIBuilder(url).removeQuery().setFragment(EMPTY).build().toString();
        } catch (URISyntaxException e) {
            throw new ExtendedRuntimeException(e, "Failed to clear query params from url");
        }
    }

}

package br.com.houseseeker;

import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.fail;

@UtilityClass
public class TestUtils {

    private static final ClassLoader CLASS_LOADER = TestUtils.class.getClassLoader();

    public String getTextFromResources(@NotNull String resourcePath) {
        try {
            return IOUtils.resourceToString(resourcePath, Charset.defaultCharset(), CLASS_LOADER);
        } catch (IOException e) {
            return fail(String.format("Failed to load resource: %s", resourcePath), e);
        }
    }

}

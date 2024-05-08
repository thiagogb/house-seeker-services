package br.com.houseseeker.util;

import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@UtilityClass
public class ListUtils {

    public <T> Optional<T> getAtIndex(@NotNull List<T> values, int index) {
        return getAtIndex(toArray(values), index);
    }

    public <T> Optional<T> getAtIndex(@NotNull T[] values, int index) {
        return Optional.ofNullable(ArrayUtils.get(values, index));
    }

    @SuppressWarnings("unchecked")
    private <T> T[] toArray(@NotNull List<T> values) {
        T[] result = (T[]) new Object[values.size()];
        IntStream.range(0, values.size()).forEach(i -> result[i] = values.get(i));
        return result;
    }

}

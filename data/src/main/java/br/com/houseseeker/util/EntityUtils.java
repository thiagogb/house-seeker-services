package br.com.houseseeker.util;

import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.lang.Nullable;

import java.util.function.Function;

import static java.util.Objects.isNull;

@UtilityClass
public class EntityUtils {

    @SuppressWarnings("unchecked")
    public <T> boolean isEqual(@NotNull T source, @Nullable Object target, @NotNull Function<T, Boolean> matcher) {
        if (source == target) return true;
        if (isNull(target)) return false;
        if (getEffectiveClassOf(target) != getEffectiveClassOf(source)) return false;
        return matcher.apply((T) target);
    }

    public <T> int hashCode(@NotNull T obj) {
        return getEffectiveClassOf(obj).hashCode();
    }

    private Class<?> getEffectiveClassOf(Object object) {
        return object instanceof HibernateProxy hibernateProxy
                ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass()
                : object.getClass();
    }

}

package br.com.houseseeker.util;

import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EntityUtilsTest {

    @Mock
    private Function<Object, Boolean> mockedMatcher;

    @Test
    @DisplayName("given the same source and target when calls isEqual then expects true")
    void givenTheSameSourceAndTarget_whenCallsIsEqual_thenExpectsTrue() {
        String source = "value";
        String target = "value";

        assertThat(EntityUtils.isEqual(source, target, mockedMatcher)).isTrue();

        verifyNoInteractions(mockedMatcher);
    }

    @Test
    @DisplayName("given a null target when calls isEqual then expects false")
    void givenANullTarget_whenCallsIsEqual_thenExpectsFalse() {
        String source = "value";

        assertThat(EntityUtils.isEqual(source, null, mockedMatcher)).isFalse();

        verifyNoInteractions(mockedMatcher);
    }

    @Test
    @DisplayName("given a source and target with diff effective class when calls isEqual then expects false")
    void givenASourceAndTargetWithDiffEffectiveClass_whenCallsIsEqual_thenExpectsFalse() {
        String source = "value";
        Integer target = 1;

        assertThat(EntityUtils.isEqual(source, target, mockedMatcher)).isFalse();

        verifyNoInteractions(mockedMatcher);
    }

    @Test
    @DisplayName("given a source and target with same effective class and matcher returns true when calls isEqual then expects true")
    void givenASourceAndTargetWithSameEffectiveClassAndMatcherReturnsTrue_whenCallsIsEqual_thenExpectsFalse() {
        BigDecimal source = BigDecimal.valueOf(100);
        BigDecimal target = new BigDecimal("100.00");

        when(mockedMatcher.apply(any())).thenReturn(true);

        assertThat(EntityUtils.isEqual(source, target, mockedMatcher)).isTrue();

        verify(mockedMatcher, times(1)).apply(any());
        verifyNoMoreInteractions(mockedMatcher);
    }

    @Test
    @DisplayName("given a source and target with same effective class and matcher returns false when calls isEqual then expects false")
    void givenASourceAndTargetWithSameEffectiveClassAndMatcherReturnsFalse_whenCallsIsEqual_thenExpectsFalse() {
        BigDecimal source = BigDecimal.valueOf(100);
        BigDecimal target = new BigDecimal("100.00");

        when(mockedMatcher.apply(any())).thenReturn(false);

        assertThat(EntityUtils.isEqual(source, target, mockedMatcher)).isFalse();

        verify(mockedMatcher, times(1)).apply(any());
        verifyNoMoreInteractions(mockedMatcher);
    }

    @Test
    @DisplayName("given a source and target of class hibernateProxy and matcher returns true when calls isEqual then expects true")
    void givenASourceAndTargetOfClassHibernateProxyAndMatcherReturnsTrue_whenCallsIsEqual_thenExpectsFalse() {
        HibernateProxy source = mock(HibernateProxy.class);
        HibernateProxy target = mock(HibernateProxy.class);

        LazyInitializer sourceLazyInitializer = mock(LazyInitializer.class);
        LazyInitializer targetLazyInitializer = mock(LazyInitializer.class);

        when(source.getHibernateLazyInitializer()).thenReturn(sourceLazyInitializer);
        when(target.getHibernateLazyInitializer()).thenReturn(targetLazyInitializer);

        when(sourceLazyInitializer.getPersistentClass()).thenAnswer(a -> String.class);
        when(targetLazyInitializer.getPersistentClass()).thenAnswer(a -> String.class);

        when(mockedMatcher.apply(any())).thenReturn(true);

        assertThat(EntityUtils.isEqual(source, target, mockedMatcher)).isTrue();

        verify(mockedMatcher, times(1)).apply(any());
        verifyNoMoreInteractions(mockedMatcher);
    }

    @Test
    @DisplayName("given a object when calls hashCode then expects")
    void givenAObject_whenCallsHashCode_thenExpects() {
        String obj = "value";

        assertThat(EntityUtils.hashCode(obj)).isEqualTo(2120817136);
    }

    @Test
    @DisplayName("given a object of class hibernateProxy when calls hashCode then expects")
    void givenAObjectOfClassHibernateProxy_whenCallsHashCode_thenExpects() {
        HibernateProxy hibernateProxy = mock(HibernateProxy.class);
        LazyInitializer lazyInitializer = mock(LazyInitializer.class);

        when(hibernateProxy.getHibernateLazyInitializer()).thenReturn(lazyInitializer);
        when(lazyInitializer.getPersistentClass()).thenAnswer(a -> String.class);

        assertThat(EntityUtils.hashCode(hibernateProxy)).isEqualTo(2120817136);
    }

}
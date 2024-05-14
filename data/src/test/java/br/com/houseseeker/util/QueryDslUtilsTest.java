package br.com.houseseeker.util;

import br.com.houseseeker.AbstractJpaIntegrationTest;
import br.com.houseseeker.entity.QProvider;
import br.com.houseseeker.entity.QUrbanProperty;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class QueryDslUtilsTest extends AbstractJpaIntegrationTest {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @ParameterizedTest
    @MethodSource("entityPathSamples")
    @DisplayName("given a entity path when calls count then expects")
    void givenAEntityPath_whenCallsCount_thenExpects(EntityPathBase<?> entityPathBase, Long expected) {
        assertThat(QueryDslUtils.count(jpaQueryFactory, entityPathBase)).isEqualTo(expected);
    }

    private static Stream<Arguments> entityPathSamples() {
        return Stream.of(
                Arguments.of(QProvider.provider, 7L),
                Arguments.of(QUrbanProperty.urbanProperty, 5L)
        );
    }

}
package br.com.houseseeker.domain.jetimob;

import br.com.houseseeker.domain.exception.ExtendedRuntimeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PropertyTerrainSideTest {

    @Test
    @DisplayName("given a invalid property detail type when calls extract then expects exception")
    void givenAInvalidPropertyDetailType_whenCallsExtract_thenExpectsException() {
        PropertyDetail propertyDetail = PropertyDetail.builder()
                                                      .type(PropertyDetail.Type.GENERAL_INFO)
                                                      .build();

        assertThatThrownBy(() -> PropertyTerrainSide.FRONT.extract(propertyDetail))
                .isInstanceOf(ExtendedRuntimeException.class)
                .hasMessage("Invalid property detail type: got GENERAL_INFO expected MEASURES");
    }

    @ParameterizedTest
    @MethodSource("propertyDetailMeasureSamples")
    @DisplayName("given a property detail measure when calls extract then expects")
    void givenAPropertyDetailMeasure_whenCallsExtract_thenExpects(
            PropertyTerrainSide propertyTerrainSide,
            PropertyDetail propertyDetail,
            BigDecimal expected
    ) {
        assertThat(propertyTerrainSide.extract(propertyDetail)).isEqualTo(Optional.ofNullable(expected));
    }

    private static Stream<Arguments> propertyDetailMeasureSamples() {
        return Stream.of(
                Arguments.of(
                        PropertyTerrainSide.FRONT,
                        PropertyDetail.builder().type(PropertyDetail.Type.MEASURES).items(List.of("10.5 m²")).build(),
                        null
                ),
                Arguments.of(
                        PropertyTerrainSide.FRONT,
                        PropertyDetail.builder().type(PropertyDetail.Type.MEASURES).items(List.of("frente 10.5 m²")).build(),
                        BigDecimal.valueOf(10.5)
                ),
                Arguments.of(
                        PropertyTerrainSide.BACK,
                        PropertyDetail.builder().type(PropertyDetail.Type.MEASURES).items(List.of("21.55 m² fundos")).build(),
                        null
                ),
                Arguments.of(
                        PropertyTerrainSide.BACK,
                        PropertyDetail.builder().type(PropertyDetail.Type.MEASURES).items(List.of("fundos 21.55 m²")).build(),
                        BigDecimal.valueOf(21.55)
                ),
                Arguments.of(
                        PropertyTerrainSide.RIGHT,
                        PropertyDetail.builder().type(PropertyDetail.Type.MEASURES).items(List.of(EMPTY)).build(),
                        null
                ),
                Arguments.of(
                        PropertyTerrainSide.RIGHT,
                        PropertyDetail.builder().type(PropertyDetail.Type.MEASURES).items(List.of("direita 100.75 m²")).build(),
                        BigDecimal.valueOf(100.75)
                ),
                Arguments.of(
                        PropertyTerrainSide.LEFT,
                        PropertyDetail.builder().type(PropertyDetail.Type.MEASURES).items(List.of("esquerda 66.333 m²")).build(),
                        BigDecimal.valueOf(66.333)
                )
        );
    }

}
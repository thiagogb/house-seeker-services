package br.com.houseseeker.mapper;

import br.com.houseseeker.domain.property.UrbanPropertyContract;
import br.com.houseseeker.domain.property.UrbanPropertyType;
import br.com.houseseeker.domain.provider.ProviderMechanism;
import com.google.protobuf.StringValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static br.com.houseseeker.domain.provider.ProviderMechanism.ALAN_WGT;
import static br.com.houseseeker.domain.provider.ProviderMechanism.JETIMOB_V1;
import static br.com.houseseeker.domain.provider.ProviderMechanism.JETIMOB_V2;
import static br.com.houseseeker.domain.provider.ProviderMechanism.JETIMOB_V3;
import static br.com.houseseeker.domain.provider.ProviderMechanism.JETIMOB_V4;
import static br.com.houseseeker.domain.provider.ProviderMechanism.SUPER_LOGICA;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ProtoStringMapperImpl.class)
class ProtoStringMapperTest {

    @Autowired
    private ProtoStringMapper protoStringMapper;

    @ParameterizedTest
    @MethodSource("stringValueSamples")
    @DisplayName("given stringValue when calls toStringValue then expects")
    void givenStringValue_whenCallsToStringValue_thenExpects(StringValue input, String expected) {
        assertThat(protoStringMapper.toString(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("stringValueProviderMechanismSamples")
    @DisplayName("given stringValue when calls toProviderMechanism then expects")
    void givenStringValue_whenCallsToProviderMechanism_thenExpects(StringValue input, ProviderMechanism expected) {
        assertThat(protoStringMapper.toProviderMechanism(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("stringSamples")
    @DisplayName("given string when calls toStringValue then expects")
    void givenStringValue_whenCallsToStringValue_thenExpects(String input, StringValue expected) {
        assertThat(protoStringMapper.toStringValue(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("providerMechanismSamples")
    @DisplayName("given providerMechanism when calls toStringValue then expects")
    void givenProviderMechanism_whenCallsToStringValue_thenExpects(ProviderMechanism input, StringValue expected) {
        assertThat(protoStringMapper.toStringValue(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("urbanPropertyContractSamples")
    @DisplayName("given urbanPropertyContract when calls toStringValue then expects")
    void givenUrbanPropertyContract_whenCallsToStringValue_thenExpects(UrbanPropertyContract input, StringValue expected) {
        assertThat(protoStringMapper.toStringValue(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("urbanPropertyTypeSamples")
    @DisplayName("given urbanPropertyType when calls toStringValue then expects")
    void givenUrbanPropertyType_whenCallsToStringValue_thenExpects(UrbanPropertyType input, StringValue expected) {
        assertThat(protoStringMapper.toStringValue(input)).isEqualTo(expected);
    }

    private static Stream<Arguments> stringValueSamples() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(StringValue.getDefaultInstance(), EMPTY),
                Arguments.of(StringValue.of("value"), "value")
        );
    }

    private static Stream<Arguments> stringValueProviderMechanismSamples() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(StringValue.getDefaultInstance(), null),
                Arguments.of(StringValue.of(JETIMOB_V1.name()), JETIMOB_V1),
                Arguments.of(StringValue.of(JETIMOB_V2.name()), JETIMOB_V2),
                Arguments.of(StringValue.of(JETIMOB_V3.name()), JETIMOB_V3),
                Arguments.of(StringValue.of(JETIMOB_V4.name()), JETIMOB_V4),
                Arguments.of(StringValue.of(SUPER_LOGICA.name()), SUPER_LOGICA),
                Arguments.of(StringValue.of(ALAN_WGT.name()), ALAN_WGT)
        );
    }

    private static Stream<Arguments> stringSamples() {
        return Stream.of(
                Arguments.of(null, StringValue.getDefaultInstance()),
                Arguments.of(EMPTY, StringValue.getDefaultInstance()),
                Arguments.of(SPACE, StringValue.of(SPACE)),
                Arguments.of("value", StringValue.of("value"))
        );
    }

    private static Stream<Arguments> providerMechanismSamples() {
        return Stream.of(
                Arguments.of(null, StringValue.getDefaultInstance()),
                Arguments.of(JETIMOB_V1, StringValue.of(JETIMOB_V1.name())),
                Arguments.of(JETIMOB_V2, StringValue.of(JETIMOB_V2.name())),
                Arguments.of(JETIMOB_V3, StringValue.of(JETIMOB_V3.name())),
                Arguments.of(JETIMOB_V4, StringValue.of(JETIMOB_V4.name())),
                Arguments.of(SUPER_LOGICA, StringValue.of(SUPER_LOGICA.name())),
                Arguments.of(ALAN_WGT, StringValue.of(ALAN_WGT.name()))
        );
    }

    private static Stream<Arguments> urbanPropertyContractSamples() {
        return Stream.of(
                Arguments.of(null, StringValue.getDefaultInstance()),
                Arguments.of(UrbanPropertyContract.SELL, StringValue.of(UrbanPropertyContract.SELL.name())),
                Arguments.of(UrbanPropertyContract.RENT, StringValue.of(UrbanPropertyContract.RENT.name()))
        );
    }

    private static Stream<Arguments> urbanPropertyTypeSamples() {
        return Stream.of(
                Arguments.of(null, StringValue.getDefaultInstance()),
                Arguments.of(UrbanPropertyType.RESIDENTIAL, StringValue.of(UrbanPropertyType.RESIDENTIAL.name())),
                Arguments.of(UrbanPropertyType.COMMERCIAL, StringValue.of(UrbanPropertyType.COMMERCIAL.name()))
        );
    }

}
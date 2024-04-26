package br.com.houseseeker.entity.converter;

import br.com.houseseeker.entity.Scanner;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ScannerStatusToVarCharConverterTest {

    private final ScannerStatusToVarCharConverter scannerStatusToVarCharConverter = new ScannerStatusToVarCharConverter();

    @ParameterizedTest
    @MethodSource("scannerStatusSamples")
    @DisplayName("given a scanner status when calls convertToDatabaseColumn then expects")
    void givenAScannerStatus_whenCallsConvertToDatabaseColumn_thenExpects(Scanner.ScannerStatus input, String output) {
        assertThat(scannerStatusToVarCharConverter.convertToDatabaseColumn(input)).isEqualTo(output);
    }

    @ParameterizedTest
    @MethodSource("stringSamples")
    @DisplayName("given a string value when calls convertToEntityAttribute then expects")
    void givenAStringValue_whenCallsConvertToEntityAttribute_thenExpects(String input, Scanner.ScannerStatus output) {
        assertThat(scannerStatusToVarCharConverter.convertToEntityAttribute(input)).isEqualTo(output);
    }

    private static Stream<Arguments> scannerStatusSamples() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(Scanner.ScannerStatus.SUCCESS, "S"),
                Arguments.of(Scanner.ScannerStatus.FAILED, "F")
        );
    }

    private static Stream<Arguments> stringSamples() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(StringUtils.EMPTY, null),
                Arguments.of(StringUtils.SPACE, null),
                Arguments.of("S", Scanner.ScannerStatus.SUCCESS),
                Arguments.of("F", Scanner.ScannerStatus.FAILED),
                Arguments.of("<unknown>", null)
        );
    }

}
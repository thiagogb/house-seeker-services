package br.com.houseseeker.entity.converter;

import br.com.houseseeker.entity.Scanner;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Optional;

@Converter
public class ScannerStatusToVarCharConverter implements AttributeConverter<Scanner.ScannerStatus, String> {

    @Override
    public String convertToDatabaseColumn(Scanner.ScannerStatus value) {
        return Optional.ofNullable(value)
                       .map(Scanner.ScannerStatus::getValue)
                       .orElse(null);
    }

    @Override
    public Scanner.ScannerStatus convertToEntityAttribute(String value) {
        return Scanner.ScannerStatus.findByValue(value).orElse(null);
    }

}

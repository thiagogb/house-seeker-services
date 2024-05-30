package br.com.houseseeker.domain.input;

import lombok.Builder;

import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

public class DateTimeInput extends AbstractInput<LocalDateTime> {

    @Builder
    public DateTimeInput(String value) {
        super(LocalDateTime.parse(value, ISO_LOCAL_DATE_TIME));
    }

}

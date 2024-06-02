package br.com.houseseeker.domain.input;

import lombok.Builder;

import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

public class DateTimeIntervalInput extends AbstractIntervalInput<LocalDateTime> {

    @Builder
    public DateTimeIntervalInput(String start, String end) {
        super(LocalDateTime.parse(start, ISO_LOCAL_DATE_TIME), LocalDateTime.parse(end, ISO_LOCAL_DATE_TIME));
    }

}

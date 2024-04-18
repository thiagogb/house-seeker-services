package br.com.houseseeker.configuration;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;
import java.util.TimeZone;

@Configuration
@Slf4j
public class TimeZoneConfiguration {

    static final String DEFAULT_TIMEZONE = "GMT-03:00";

    @PostConstruct
    public void configure() {
        TimeZone.setDefault(TimeZone.getTimeZone(DEFAULT_TIMEZONE));
        log.info("Running with timezone: " + ZoneId.systemDefault());
    }

}

package br.com.houseseeker.configuration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TimeZoneConfiguration.class)
class TimeZoneConfigurationTest {

    @Test
    @DisplayName("expected to configure timezone on post constructor")
    void expectedToConfigureTimezoneOnPostConstructor() {
        assertThat(TimeZone.getDefault().toZoneId()).hasToString(TimeZoneConfiguration.DEFAULT_TIMEZONE);
    }

}
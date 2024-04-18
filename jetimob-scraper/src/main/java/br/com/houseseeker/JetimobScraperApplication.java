package br.com.houseseeker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class JetimobScraperApplication {

    public static void main(String[] args) {
        SpringApplication.run(JetimobScraperApplication.class, args);
    }

}

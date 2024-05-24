package br.com.houseseeker.configuration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfiguration {

    public static final String PROVIDER_LOGO_CACHE = "providerLogoCache";

}

package br.com.houseseeker;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public interface PostgreSQLIntegrationTest {

    @Container
    PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:16.1-alpine");

    @DynamicPropertySource
    static void datasourceConfig(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.hikari.jdbc-url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.hikari.username", POSTGRESQL_CONTAINER::getPassword);
        registry.add("spring.datasource.hikari.password", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.flyway.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.flyway.user", POSTGRESQL_CONTAINER::getPassword);
        registry.add("spring.flyway.password", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.quartz.properties.org.quartz.dataSource.quartzDataSource.URL", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.quartz.properties.org.quartz.dataSource.quartzDataSource.user", POSTGRESQL_CONTAINER::getPassword);
        registry.add("spring.quartz.properties.org.quartz.dataSource.quartzDataSource.password", POSTGRESQL_CONTAINER::getUsername);
    }

}

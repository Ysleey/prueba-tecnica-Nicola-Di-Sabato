package com.prueba_tecnica_nicola.prueba.infrastructure.database;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
class FlywayMigrationIntegrationTest {

    @Container
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0")
        .withDatabaseName("product_db")
        .withUsername("root")
        .withPassword("password");

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
    }

    @Test
    void shouldApplyV1MigrationOnEmptyDatabase() {
        Integer migrations = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM flyway_schema_history WHERE version = '1' AND success = 1",
            Integer.class
        );

        assertThat(migrations).isEqualTo(1);
    }

    @Test
    void shouldCreateProductTableWithExpectedColumns() {
        List<String> columns = jdbcTemplate.queryForList(
            "SELECT COLUMN_NAME FROM information_schema.columns "
                + "WHERE table_schema = DATABASE() AND table_name = 'product'",
            String.class
        );

        assertThat(columns).contains(
            "id",
            "name",
            "description",
            "price",
            "stock",
            "created_at",
            "updated_at"
        );
    }
}

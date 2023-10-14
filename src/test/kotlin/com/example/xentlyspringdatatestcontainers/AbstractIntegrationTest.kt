package com.example.xentlyspringdatatestcontainers

import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.lifecycle.Startables

/**
 * Ref: https://docs.spring.io/spring-boot/docs/3.1.4/reference/htmlsingle/#features.testing.testcontainers
 */
//@Testcontainers
abstract class AbstractIntegrationTest {
    companion object {
        @JvmStatic
//        @Container
//        @ServiceConnection
        val postgresContainer = POSTGRESQL_CONTAINER

        @JvmStatic
//        @Container
//        @ServiceConnection
        val elasticsearchContainer = ELASTICSEARCH_CONTAINER

        init {
            Startables.deepStart(postgresContainer, elasticsearchContainer).join()
        }

        @JvmStatic
        @BeforeAll
        @DynamicPropertySource
        fun setupProperties(@Autowired registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgresContainer::getUsername)
            registry.add("spring.datasource.password", postgresContainer::getPassword)

            registry.add("spring.flyway.user", postgresContainer::getUsername)
            registry.add("spring.flyway.password", postgresContainer::getPassword)

            registry.add("spring.elasticsearch.uris", elasticsearchContainer::getHttpHostAddress)
        }
    }
}
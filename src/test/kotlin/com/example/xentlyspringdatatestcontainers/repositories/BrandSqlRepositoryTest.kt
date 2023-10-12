package com.example.xentlyspringdatatestcontainers.repositories

import com.example.xentlyspringdatatestcontainers.ELASTICSEARCH_CONTAINER
import com.example.xentlyspringdatatestcontainers.POSTGRESQL_CONTAINER
import com.example.xentlyspringdatatestcontainers.models.Brand
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.data.domain.Pageable
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
class BrandSqlRepositoryTest {

    @Autowired
    lateinit var repository: BrandSqlRepository

    @BeforeEach
    fun setup() {
        repository.deleteAll()
        listOf(
            Brand.Entity(name = "Apple", slug = "apple"),
            Brand.Entity(name = "Asus", slug = "asus"),
            Brand.Entity(name = "Google", slug = "google"),
        ).let(repository::saveAll)
    }

    @ParameterizedTest
    @ValueSource(strings = ["le", "Le"])
    fun findAllByNameContainingIgnoreCase(query: String) {
        val actual = repository.findAllByNameContainingIgnoreCase(query, Pageable.unpaged())

        assertIterableEquals(listOf("Apple", "Google"), actual.content.map { it.name })
    }

    @Suppress("unused")
    companion object {
        @JvmStatic
        @Container
        @ServiceConnection
        val postgresContainer = POSTGRESQL_CONTAINER

        @JvmStatic
        @Container
        @ServiceConnection
        val elasticsearchContainer = ELASTICSEARCH_CONTAINER

        /*@JvmStatic
        @DynamicPropertySource
        fun setupProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgresContainer::getUsername)
            registry.add("spring.datasource.password", postgresContainer::getPassword)

            registry.add("spring.flyway.user", postgresContainer::getUsername)
            registry.add("spring.flyway.password", postgresContainer::getPassword)

            registry.add("spring.elasticsearch.uris", elasticsearchContainer::getHttpHostAddress)
        }*/
    }
}
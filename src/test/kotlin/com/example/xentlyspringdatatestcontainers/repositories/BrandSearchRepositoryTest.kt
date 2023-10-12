package com.example.xentlyspringdatatestcontainers.repositories

import com.example.xentlyspringdatatestcontainers.ELASTICSEARCH_CONTAINER
import com.example.xentlyspringdatatestcontainers.POSTGRESQL_CONTAINER
import com.example.xentlyspringdatatestcontainers.models.Brand
import org.junit.jupiter.api.Assertions
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
class BrandSearchRepositoryTest {

    @Autowired
    lateinit var repository: BrandSearchRepository

    @BeforeEach
    fun setup() {
        repository.deleteAll()
        listOf(
            Brand.Document(name = "Apple", slug = "apple"),
            Brand.Document(name = "Asus", slug = "asus"),
            Brand.Document(name = "Google", slug = "google"),
        ).let(repository::saveAll)
    }

    @ParameterizedTest
    @ValueSource(strings = ["le", "Le"])
    fun findAll(query: String) {
        val actual = repository.findAll(query, Pageable.unpaged())

        Assertions.assertIterableEquals(listOf("Apple", "Google"), actual.content.map { it.name })
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
    }
}
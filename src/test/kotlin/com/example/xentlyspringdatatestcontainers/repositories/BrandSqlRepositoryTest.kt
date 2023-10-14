package com.example.xentlyspringdatatestcontainers.repositories

import com.example.xentlyspringdatatestcontainers.AbstractIntegrationTest
import com.example.xentlyspringdatatestcontainers.models.Brand
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable

@SpringBootTest
class BrandSqlRepositoryTest : AbstractIntegrationTest() {

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
}
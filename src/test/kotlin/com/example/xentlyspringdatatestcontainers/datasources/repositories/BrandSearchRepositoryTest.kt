package com.example.xentlyspringdatatestcontainers.datasources.repositories

import com.example.xentlyspringdatatestcontainers.AbstractIntegrationTest
import com.example.xentlyspringdatatestcontainers.datasources.repositories.BrandSearchRepository
import com.example.xentlyspringdatatestcontainers.models.Brand
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable

@SpringBootTest
class BrandSearchRepositoryTest : AbstractIntegrationTest() {

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
}
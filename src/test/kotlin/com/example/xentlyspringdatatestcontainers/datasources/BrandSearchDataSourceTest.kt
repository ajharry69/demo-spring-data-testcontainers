package com.example.xentlyspringdatatestcontainers.datasources

import com.example.xentlyspringdatatestcontainers.ResponseType
import com.example.xentlyspringdatatestcontainers.models.Brand
import com.example.xentlyspringdatatestcontainers.repositories.BrandSearchRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito
import org.mockito.kotlin.argumentCaptor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

class BrandSearchDataSourceTest {
    @Nested
    inner class Save {
        @Test
        fun `auto-generates slugs if not present`() {
            val repository = Mockito.mock(BrandSearchRepository::class.java)
            val dataSource = BrandSearchDataSource(repository)
            val brands = listOf(Brand.Document(name = "Brand", slug = "brand"))

            dataSource.save(brands)

            val argumentCaptor = argumentCaptor<List<Brand.Document>>()
            Mockito.verify(repository).saveAll(argumentCaptor.capture())

            Assertions.assertIterableEquals(
                listOf("brand"),
                argumentCaptor.allValues.flatMap { entities -> entities.map { it.slug } },
            )
        }

        @ParameterizedTest
        @EnumSource(names = ["Document", "Entity", "View"])
        fun `retains non-blank slugs`(responseType: ResponseType) {
            val repository = Mockito.mock(BrandSearchRepository::class.java)
            val dataSource = BrandSearchDataSource(repository)
            val brand = when (responseType) {
                ResponseType.Document -> Brand.Document(name = "Example", slug = "example")
                ResponseType.Entity -> Brand.Entity(name = "Example", slug = "example")
                ResponseType.View -> Brand.View(name = "Example", slug = "example")
            }
            val brands = listOf(brand)

            dataSource.save(brands)

            val argumentCaptor = argumentCaptor<List<Brand.Document>>()
            Mockito.verify(repository).saveAll(argumentCaptor.capture())

            Assertions.assertIterableEquals(
                listOf("example"),
                argumentCaptor.allValues.flatMap { entities -> entities.map { it.slug } },
            )
        }
    }

    @Nested
    inner class GetMultiple {
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = ["      "])
        fun `findAll is called if query is null or blank`(query: String?) {
            val repository = Mockito.mock(BrandSearchRepository::class.java)
            Mockito.`when`(repository.findAll(Mockito.any<Pageable>()))
                .thenReturn(Page.empty())
            val dataSource = BrandSearchDataSource(repository)

            dataSource.get(query, Pageable.unpaged())

            val argumentCaptor = argumentCaptor<Pageable>()
            Mockito.verify(repository, Mockito.atMostOnce()).findAll(argumentCaptor.capture())
        }

        @Test
        fun `findAll is called if query is neither null or blank`() {
            val query = "example"
            val pageable = Pageable.unpaged()
            val repository = Mockito.mock(BrandSearchRepository::class.java)
            Mockito.`when`(repository.findAll(query, pageable))
                .thenReturn(Page.empty())
            val dataSource = BrandSearchDataSource(repository)

            dataSource.get(query, pageable)

            val queryArgumentCaptor = argumentCaptor<String>()
            val pageableArgumentCaptor = argumentCaptor<Pageable>()
            Mockito.verify(repository, Mockito.atMostOnce())
                .findAll(queryArgumentCaptor.capture(), pageableArgumentCaptor.capture())
        }
    }

    @Test
    fun `get calls findById`() {
        val slug = "example"
        val repository = Mockito.mock(BrandSearchRepository::class.java)
        Mockito.`when`(repository.findById(slug))
            .thenReturn(Optional.ofNullable(null))
        val dataSource = BrandSearchDataSource(repository)

        dataSource.get(slug)

        val slugArgumentCaptor = argumentCaptor<String>()
        Mockito.verify(repository, Mockito.atMostOnce())
            .findById(slugArgumentCaptor.capture())
    }
}
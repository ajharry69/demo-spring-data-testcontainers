package com.example.xentlyspringdatatestcontainers.repositories

import com.example.xentlyspringdatatestcontainers.datasources.BrandDataSource
import com.example.xentlyspringdatatestcontainers.models.Brand
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito
import org.mockito.kotlin.argumentCaptor
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class BrandRepositoryTest {
    @Nested
    @DisplayName("get pageable response optionally filtered by query string")
    inner class GetMultiple {
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = ["      ", "example"])
        fun `returns result from search optimised db if not empty`(query: String?) {
            val pageable = Pageable.unpaged()
            val sqlDataSource = Mockito.mock(BrandDataSource::class.java)
            val searchDataSource = Mockito.mock(BrandDataSource::class.java)
            val repository = BrandRepository(sqlDataSource, searchDataSource)
            val brands = listOf(Brand.View(name = "Example"))
            Mockito.`when`(searchDataSource.get(query, pageable))
                .thenReturn(PageImpl(brands))

            val response = repository.get(query, pageable)

            Mockito.verify(sqlDataSource, Mockito.never())
                .get(query, pageable)

            assertIterableEquals(listOf("Example"), response.content.map { it.name })
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = ["      ", "example"])
        fun `fallback to sql db result if search optimised db is empty`(query: String?) {
            val pageable = Pageable.unpaged()
            val sqlDataSource = Mockito.mock(BrandDataSource::class.java)
            val searchDataSource = Mockito.mock(BrandDataSource::class.java)
            val repository = BrandRepository(sqlDataSource, searchDataSource)
            Mockito.`when`(searchDataSource.get(query, pageable))
                .thenReturn(Page.empty())

            repository.get(query, pageable)

            Mockito.verify(sqlDataSource, Mockito.atMostOnce())
                .get(query, pageable)
        }
    }

    @Nested
    @DisplayName("get by id")
    inner class GetSingle {
        @Test
        fun `returns result from search optimised db if not null`() {
            val slug = "example"
            val sqlDataSource = Mockito.mock(BrandDataSource::class.java)
            val searchDataSource = Mockito.mock(BrandDataSource::class.java)
            val repository = BrandRepository(sqlDataSource, searchDataSource)
            val brands = listOf(Brand.View(name = "Example"))
            Mockito.`when`(searchDataSource.get(slug))
                .thenReturn(brands[0])

            val response = repository.get(slug)

            Mockito.verify(sqlDataSource, Mockito.never())
                .get(Mockito.anyString())
            Mockito.verify(searchDataSource, Mockito.atMostOnce())
                .get(slug)
            assertEquals("Example", response!!.name)
        }

        @Test
        fun `returns result from sql db if search optimised db returns is null`() {
            val slug = "example"
            val sqlDataSource = Mockito.mock(BrandDataSource::class.java)
            val searchDataSource = Mockito.mock(BrandDataSource::class.java)
            val repository = BrandRepository(sqlDataSource, searchDataSource)
            val brands = listOf(Brand.View(name = "Example"))
            Mockito.`when`(searchDataSource.get(slug))
                .thenReturn(null)
            Mockito.`when`(sqlDataSource.get(slug))
                .thenReturn(brands[0])

            val response = repository.get(slug)

            Mockito.verify(sqlDataSource, Mockito.atMostOnce())
                .get(slug)
            Mockito.verify(searchDataSource, Mockito.atMostOnce())
                .get(slug)
            assertEquals("Example", response!!.name)
        }

        @Test
        fun `saves sql db results to search optimised db if result is not null`() {
            val slug = "example"
            val sqlDataSource = Mockito.mock(BrandDataSource::class.java)
            val searchDataSource = Mockito.mock(BrandDataSource::class.java)
            val repository = BrandRepository(sqlDataSource, searchDataSource)
            val brands = listOf(Brand.View(name = "Example"))
            Mockito.`when`(searchDataSource.get(slug))
                .thenReturn(null)
            Mockito.`when`(sqlDataSource.get(slug))
                .thenReturn(brands[0])

            repository.get(slug)

            Mockito.verify(searchDataSource, Mockito.atMostOnce())
                .save(brands)
        }

        @Test
        fun `does not saves sql db results to search optimised db if result is null`() {
            val slug = "example"
            val sqlDataSource = Mockito.mock(BrandDataSource::class.java)
            val searchDataSource = Mockito.mock(BrandDataSource::class.java)
            val repository = BrandRepository(sqlDataSource, searchDataSource)
            val brands = listOf(Brand.View(name = "Example"))
            Mockito.`when`(searchDataSource.get(slug))
                .thenReturn(null)
            Mockito.`when`(sqlDataSource.get(slug))
                .thenReturn(null)

            repository.get(slug)

            Mockito.verify(searchDataSource, Mockito.never())
                .save(brands)
        }
    }

    @Test
    fun `save synchronises sql data with search optimised db`() {
        val sqlDataSource = Mockito.mock(BrandDataSource::class.java)
        val searchDataSource = Mockito.mock(BrandDataSource::class.java)
        val repository = BrandRepository(sqlDataSource, searchDataSource)
        val brands = listOf(Brand.View(name = "Example"))
        Mockito.`when`(sqlDataSource.save(brands)).thenReturn(brands.map { it.copy(slug = "saved") })

        repository.save(brands)

        val brandsArgumentCaptor = argumentCaptor<List<Brand>>()
        Mockito.verify(searchDataSource, Mockito.atMostOnce()).save(brandsArgumentCaptor.capture())

        assertIterableEquals(
            listOf("saved"),
            brandsArgumentCaptor.allValues.flatMap { entities ->
                entities.map { it.slug }
            },
        )
    }
}
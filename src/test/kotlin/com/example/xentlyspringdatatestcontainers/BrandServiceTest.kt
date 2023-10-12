package com.example.xentlyspringdatatestcontainers

import com.example.xentlyspringdatatestcontainers.exceptions.BrandNotFoundException
import com.example.xentlyspringdatatestcontainers.models.Brand
import com.example.xentlyspringdatatestcontainers.repositories.BrandRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EmptySource
import org.junit.jupiter.params.provider.NullSource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

class BrandServiceTest {
    @Test
    fun `save is done through corresponding repository function`() {
        val repository = Mockito.mock(BrandRepository::class.java)
        val brands = listOf(Brand.Document(name = "Example", slug = "example"))
        Mockito.`when`(repository.save(Mockito.anyIterable()))
            .thenReturn(brands)

        val service = BrandService(repository)
        service.save(brands)

        Mockito.verify(repository, Mockito.atMostOnce())
            .save(Mockito.anyIterable())
    }

    @Nested
    @DisplayName("get by id")
    inner class GetSingle {
        @Test
        fun `is done through corresponding repository function`() {
            val repository = Mockito.mock(BrandRepository::class.java)
            val brand = Brand.Document(name = "Example", slug = "example")
            Mockito.`when`(repository.get(Mockito.anyString()))
                .thenReturn(brand)

            val service = BrandService(repository)
            service.get("example")

            Mockito.verify(repository, Mockito.atMostOnce())
                .get(Mockito.anyString())
        }

        @Test
        fun `throws exception if repository returns null`() {
            val repository = Mockito.mock(BrandRepository::class.java)
            Mockito.`when`(repository.get(Mockito.anyString()))
                .thenReturn(null)

            val service = BrandService(repository)

            assertThrows<BrandNotFoundException> {
                service.get("example")
            }
        }
    }

    @Nested
    @DisplayName("get pageable response optionally filtered by query string")
    inner class GetMultiple {
        @ParameterizedTest
        @NullSource
        @EmptySource
        @ValueSource(strings = ["      ", "example"])
        fun `is done through corresponding repository function`(query: String?) {
            val pageable = Pageable.unpaged()
            val repository = Mockito.mock(BrandRepository::class.java)
            Mockito.`when`(repository.get(query, pageable))
                .thenReturn(Page.empty())

            val service = BrandService(repository)
            service.get(query, pageable)

            Mockito.verify(repository, Mockito.atMostOnce())
                .get(query, pageable)
        }
    }
}
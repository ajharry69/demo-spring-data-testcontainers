package com.example.xentlyspringdatatestcontainers.repositories

import com.example.xentlyspringdatatestcontainers.datasources.BrandDataSource
import com.example.xentlyspringdatatestcontainers.models.Brand
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class BrandRepository(
    @Qualifier("brandSqlDataSource")
    private val sqlDataSource: BrandDataSource,
    @Qualifier("brandSearchDataSource")
    private val searchDataSource: BrandDataSource,
) {
    fun save(brands: Iterable<Brand>): Iterable<Brand> {
        return sqlDataSource.save(brands)
            .let(searchDataSource::save)
    }

    fun get(slug: String): Brand? {
        var brand = searchDataSource.get(slug)
        if (brand == null) {
            brand = sqlDataSource.get(slug)?.also {
                searchDataSource.save(listOf(it))
            }
        }
        return brand
    }

    fun get(query: String?, pageable: Pageable): Page<Brand> {
        var page = searchDataSource.get(query, pageable)
        if (page.isEmpty) {
            page = sqlDataSource.get(query, pageable)
        }
        return page
    }
}
package com.example.xentlyspringdatatestcontainers.services

import com.example.xentlyspringdatatestcontainers.datasources.BrandDataSource
import com.example.xentlyspringdatatestcontainers.exceptions.BrandNotFoundException
import com.example.xentlyspringdatatestcontainers.models.Brand
import com.example.xentlyspringdatatestcontainers.models.mappers.toView
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BrandService(
    @Qualifier("brandSqlDataSource")
    private val sqlDataSource: BrandDataSource,
    @Qualifier("brandSearchDataSource")
    private val searchDataSource: BrandDataSource,
) {
    fun save(brands: Iterable<Brand>): List<Brand.View> {
        return sqlDataSource.save(brands)
            .let(searchDataSource::save)
            .map(Brand::toView)
    }

    fun get(query: String?, pageable: Pageable): Page<Brand.View> {
        var page = searchDataSource.get(query, pageable)
        if (page.isEmpty) {
            page = sqlDataSource.get(query, pageable)
        }
        return page.map(Brand::toView)
    }

    fun get(slug: String): Brand.View {
        var brand = searchDataSource.get(slug)
        if (brand == null) {
            brand = sqlDataSource.get(slug)?.also {
                searchDataSource.save(listOf(it))
            }
        }
        return brand?.toView()
            ?: throw BrandNotFoundException()
    }
}
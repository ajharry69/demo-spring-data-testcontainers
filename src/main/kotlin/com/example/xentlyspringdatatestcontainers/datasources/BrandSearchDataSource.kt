package com.example.xentlyspringdatatestcontainers.datasources

import com.example.xentlyspringdatatestcontainers.models.Brand
import com.example.xentlyspringdatatestcontainers.models.mappers.toDocument
import com.example.xentlyspringdatatestcontainers.datasources.repositories.BrandSearchRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class BrandSearchDataSource(private val repository: BrandSearchRepository) : BrandDataSource {
    override fun save(brands: Iterable<Brand>): Iterable<Brand> {
        return repository.saveAll(brands.map(Brand::toDocument))
    }

    override fun get(query: String?, pageable: Pageable): Page<Brand> {
        return if (query.isNullOrBlank()) {
            repository.findAll(pageable)
        } else {
            repository.findAll(query, pageable)
        }.map(Brand::toDocument)
    }

    override fun get(slug: String): Brand? {
        return repository.findByIdOrNull(slug)
    }
}
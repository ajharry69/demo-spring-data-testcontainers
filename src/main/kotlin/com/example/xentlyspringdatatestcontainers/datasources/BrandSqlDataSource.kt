package com.example.xentlyspringdatatestcontainers.datasources

import com.example.xentlyspringdatatestcontainers.models.Brand
import com.example.xentlyspringdatatestcontainers.models.mappers.toEntity
import com.example.xentlyspringdatatestcontainers.datasources.repositories.BrandSqlRepository
import com.example.xentlyspringdatatestcontainers.slugify
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class BrandSqlDataSource(private val repository: BrandSqlRepository) : BrandDataSource {
    override fun save(brands: Iterable<Brand>): Iterable<Brand> {
        return brands.map {
            it.toEntity().run {
                copy(slug = slug.ifBlank { it.slugify() })
            }
        }.let(repository::saveAll)
    }

    override fun get(query: String?, pageable: Pageable): Page<Brand> {
        return if (query.isNullOrBlank()) {
            repository.findAll(pageable)
        } else {
            repository.findAllByNameContainingIgnoreCase(query, pageable)
        }.map {
            it
        }
    }

    override fun get(slug: String): Brand? {
        return repository.findByIdOrNull(slug)
    }
}
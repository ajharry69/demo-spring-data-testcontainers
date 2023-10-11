package com.example.xentlyspringdatatestcontainers.datasources

import com.example.xentlyspringdatatestcontainers.repositories.BrandSqlRepository
import com.example.xentlyspringdatatestcontainers.models.Brand
import com.example.xentlyspringdatatestcontainers.models.toEntity
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

    override fun get(pageable: Pageable): Page<Brand> {
        return repository.findAll(pageable).map {
            it
        }
    }

    override fun get(query: String, pageable: Pageable): Page<Brand> {
        return repository.findAllByNameContainingIgnoreCase(query, pageable).map {
            it
        }
    }

    override fun get(slug: String): Brand? {
        return repository.findByIdOrNull(slug)
    }
}
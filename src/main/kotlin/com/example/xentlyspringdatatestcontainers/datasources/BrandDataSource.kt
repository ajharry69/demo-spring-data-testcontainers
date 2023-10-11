package com.example.xentlyspringdatatestcontainers.datasources

import com.example.xentlyspringdatatestcontainers.models.Brand
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface BrandDataSource {
    fun save(brands: Iterable<Brand>): Iterable<Brand>
    fun get(pageable: Pageable): Page<Brand>
    fun get(query: String, pageable: Pageable): Page<Brand>
    fun get(slug: String): Brand?
}
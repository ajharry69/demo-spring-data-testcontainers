package com.example.xentlyspringdatatestcontainers

import com.example.xentlyspringdatatestcontainers.exceptions.BrandNotFoundException
import com.example.xentlyspringdatatestcontainers.models.Brand
import com.example.xentlyspringdatatestcontainers.models.toView
import com.example.xentlyspringdatatestcontainers.repositories.BrandRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BrandService(private val repository: BrandRepository) {
    fun save(brands: Iterable<Brand>): List<Brand.View> {
        return repository.save(brands)
            .map(Brand::toView)
    }

    fun get(query: String?, pageable: Pageable): Page<Brand.View> {
        return repository.get(query, pageable)
            .map(Brand::toView)
    }

    fun get(slug: String): Brand.View {
        return repository.get(slug)?.toView()
            ?: throw BrandNotFoundException()
    }
}
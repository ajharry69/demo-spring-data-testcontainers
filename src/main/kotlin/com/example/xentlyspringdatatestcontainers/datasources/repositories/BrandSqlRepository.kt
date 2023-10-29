package com.example.xentlyspringdatatestcontainers.datasources.repositories

import com.example.xentlyspringdatatestcontainers.models.Brand
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BrandSqlRepository : JpaRepository<Brand.Entity, String> {
    fun findAllByNameContainingIgnoreCase(name: String, pageable: Pageable): Page<Brand.Entity>
}
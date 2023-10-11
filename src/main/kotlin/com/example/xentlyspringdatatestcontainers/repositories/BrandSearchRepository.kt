package com.example.xentlyspringdatatestcontainers.repositories

import com.example.xentlyspringdatatestcontainers.models.Brand
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.annotations.Query
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

@Repository
interface BrandSearchRepository : ElasticsearchRepository<Brand.Document, String> {
    @Query("""{"multi_match": { "query": "?0"} }""")
    fun findAll(query: String, pageable: Pageable): Page<Brand.Document>
}
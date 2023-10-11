package com.example.xentlyspringdatatestcontainers.controllers

import com.example.xentlyspringdatatestcontainers.BrandService
import com.example.xentlyspringdatatestcontainers.models.Brand
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/brands")
class BrandController(
    private val service: BrandService,
    private val assembler: BrandAssembler,
    private val pagingAssembler: PagedResourcesAssembler<Brand.View>,
) {
    @PostMapping
    fun save(@RequestBody brand: Brand.View) = listOf(brand)
        .let(service::save)
        .first()
        .let(assembler::toModel)
        .run { ResponseEntity.created(getRequiredLink(IanaLinkRelations.SELF).toUri()).body(this) }

    @PostMapping("/batch")
    fun save(@RequestBody brands: List<Brand.View>) = service.save(brands)
        .let(assembler::toCollectionModel)
        .run { ResponseEntity.status(HttpStatus.CREATED).body(this) }

    @GetMapping
    fun get(
        @RequestParam(name = "q", required = false)
        query: String?,
        pageable: Pageable,
    ) = service.get(query, pageable).let {
        assembler.setQuery(query)
        pagingAssembler.toModel(it, assembler)
    }.also {
        assembler.resetQuery()
    }

    @GetMapping("/{slug}")
    fun get(@PathVariable slug: String) = assembler.toModel(service.get(slug))
}
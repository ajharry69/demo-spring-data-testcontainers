package com.example.xentlyspringdatatestcontainers.controllers

import com.example.xentlyspringdatatestcontainers.models.Brand
import org.springframework.data.domain.PageRequest
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.stereotype.Component

@Component
class BrandAssembler : RepresentationModelAssembler<Brand.View, EntityModel<Brand.View>> {
    private var q: String? = null

    fun setQuery(query: String?) {
        this.q = query
    }

    fun resetQuery() {
        setQuery(null)
    }

    override fun toModel(entity: Brand.View): EntityModel<Brand.View> {
        return EntityModel.of(
            entity,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BrandController::class.java).get(entity.slug))
                .withSelfRel(),
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(BrandController::class.java)
                    .get(q, PageRequest.ofSize(30))
            ).withRel("brands"),
        )
    }
}
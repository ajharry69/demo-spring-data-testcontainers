package com.example.xentlyspringdatatestcontainers.models

import jakarta.persistence.Id
import jakarta.persistence.Table

sealed interface Brand {
    val name: String
    val slug: String

    data class View(
        override val name: String = "",
        override val slug: String = "",
    ) : Brand

    @jakarta.persistence.Entity
    @Table(name = "brands")
    data class Entity(
        override val name: String = "",
        @Id
        override val slug: String = "",
    ) : Brand

    data class Document(
        override val name: String = "",
        override val slug: String = "",
    ) : Brand
}
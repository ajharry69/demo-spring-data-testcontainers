package com.example.xentlyspringdatatestcontainers.models

import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.elasticsearch.annotations.*

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

    @org.springframework.data.elasticsearch.annotations.Document(
        indexName = "xently-spring-data-testcontainers-brands",
//        createIndex = false,
    )
    @Setting(settingPath = "elasticsearch/xently-brands-settings.json")
    data class Document(
        @MultiField(
            mainField = Field(type = FieldType.Text),
            otherFields = [
                InnerField(
                    suffix = "english",
                    type = FieldType.Text,
                    analyzer = "english",
                ),
                InnerField(
                    suffix = "ngram",
                    type = FieldType.Text,
                    analyzer = "ngram_analyzer",
                ),
                InnerField(
                    suffix = "edge_ngram",
                    type = FieldType.Text,
                    analyzer = "edge_ngram_analyzer",
                ),
            ],
        )
        override val name: String,
        @org.springframework.data.annotation.Id
        @Field(type = FieldType.Text)
        override val slug: String,
    ) : Brand
}
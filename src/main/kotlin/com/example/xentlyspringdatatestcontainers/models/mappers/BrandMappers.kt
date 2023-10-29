package com.example.xentlyspringdatatestcontainers.models.mappers

import com.example.xentlyspringdatatestcontainers.models.Brand


fun Brand.toView(): Brand.View {
    return when (this) {
        is Brand.View -> this
        is Brand.Entity -> Brand.View(
            name = name,
            slug = slug,
        )

        is Brand.Document -> Brand.View(
            name = name,
            slug = slug,
        )
    }
}

fun Brand.toEntity(): Brand.Entity {
    return when (this) {
        is Brand.Entity -> this
        is Brand.View -> Brand.Entity(
            name = name,
            slug = slug,
        )

        is Brand.Document -> Brand.Entity(
            name = name,
            slug = slug,
        )
    }
}

fun Brand.toDocument(): Brand.Document {
    return when (this) {
        is Brand.Document -> this
        is Brand.View -> Brand.Document(
            name = name,
            slug = slug,
        )

        is Brand.Entity -> Brand.Document(
            name = name,
            slug = slug,
        )
    }
}

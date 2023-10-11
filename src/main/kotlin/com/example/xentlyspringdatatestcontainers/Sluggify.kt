package com.example.xentlyspringdatatestcontainers

import com.example.xentlyspringdatatestcontainers.models.Brand


fun String.slug(): String {
    return trim()
        .lowercase()
        .replace("-+".toRegex(), "-")
        .replace("[^a-zA-Z0-9]+".toRegex(), "-")
        .removePrefix("-")
        .removeSuffix("-")
}

fun Brand.slugify(): String {
    return name.slug()
}
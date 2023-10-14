package com.example.xentlyspringdatatestcontainers.exceptions

import org.springframework.http.HttpStatus

abstract class XentlyHttpException(message: String) : RuntimeException(message) {
    abstract val status: HttpStatus
}
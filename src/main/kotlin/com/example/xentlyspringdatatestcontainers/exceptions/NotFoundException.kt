package com.example.xentlyspringdatatestcontainers.exceptions

import org.springframework.http.HttpStatus

open class NotFoundException(message: String) : XentlyHttpException(message) {
    override val status: HttpStatus
        get() = HttpStatus.NOT_FOUND
}
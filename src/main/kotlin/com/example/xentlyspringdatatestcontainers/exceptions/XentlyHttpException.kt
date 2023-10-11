package com.example.xentlyspringdatatestcontainers.exceptions

import org.springframework.http.HttpStatus

open class XentlyHttpException(message: String, val status: HttpStatus) : RuntimeException(message)
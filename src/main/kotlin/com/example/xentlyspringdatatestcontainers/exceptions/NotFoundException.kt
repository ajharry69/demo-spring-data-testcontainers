package com.example.xentlyspringdatatestcontainers.exceptions

import org.springframework.http.HttpStatus

open class NotFoundException(message: String) : XentlyHttpException(message, HttpStatus.NOT_FOUND)
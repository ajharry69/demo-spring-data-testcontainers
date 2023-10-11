package com.example.xentlyspringdatatestcontainers.exceptions

class BrandNotFoundException(id: Any) : NotFoundException("Brand with slug '${id}' not found.")

package com.example.xentlyspringdatatestcontainers

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class XentlySpringDataTestcontainersApplication {
    /*@Bean
    fun addBrands(service: BrandService): CommandLineRunner {
        return CommandLineRunner {
            List(100) {
                Brand.View(name = "Brand ${it + 1}")
            }.let(service::save)
        }
    }*/
}

fun main(args: Array<String>) {
    runApplication<XentlySpringDataTestcontainersApplication>(*args)
}

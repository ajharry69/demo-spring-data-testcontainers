package com.example.xentlyspringdatatestcontainers

import com.example.xentlyspringdatatestcontainers.datasources.BrandSqlDataSource
import com.example.xentlyspringdatatestcontainers.models.Brand
import com.example.xentlyspringdatatestcontainers.repositories.BrandSqlRepository
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@SpringBootTest
@Testcontainers
class XentlySpringDataTestcontainersApplicationTests {
    @Autowired
    lateinit var repository: BrandSqlRepository

    companion object {
        @Container
        val postgresContainer = PostgreSQLContainer(DockerImageName.parse("postgres:15.4-alpine"))

        @JvmStatic
        @DynamicPropertySource
        fun setupProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgresContainer::getUsername)
            registry.add("spring.datasource.password", postgresContainer::getPassword)
        }
    }

    @Test
    fun contextLoads() {
        val datasource = BrandSqlDataSource(repository)
        val brands = listOf(Brand.Entity(name = "Brand"))

        assertFalse(datasource.get(Pageable.unpaged()).hasNext())

        val response = datasource.save(brands)

        assertTrue(repository.findAll().iterator().hasNext())
//        assertTrue(datasource.get(Pageable.unpaged()).hasNext())
    }
}

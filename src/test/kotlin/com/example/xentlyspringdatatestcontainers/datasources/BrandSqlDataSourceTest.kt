package com.example.xentlyspringdatatestcontainers.datasources

import com.example.xentlyspringdatatestcontainers.models.Brand
import com.example.xentlyspringdatatestcontainers.repositories.BrandSqlRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@SpringBootTest
@Testcontainers
class BrandSqlDataSourceTest {
    @Autowired
    lateinit var repository: BrandSqlRepository

    companion object {
        @Container
        val postgresContainer = DockerImageName.parse("postgis/postgis:15-3.3-alpine")
            .asCompatibleSubstituteFor("postgres").run {
                PostgreSQLContainer(this)
            }

        @JvmStatic
        @DynamicPropertySource
        fun setupProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgresContainer::getUsername)
            registry.add("spring.datasource.password", postgresContainer::getPassword)
            registry.add("spring.flyway.user", postgresContainer::getUsername)
            registry.add("spring.flyway.password", postgresContainer::getPassword)
        }
    }

    @Test
    fun save() {
        val datasource = BrandSqlDataSource(repository)
        val brands = listOf(Brand.Entity(name = "Brand"))

        assertFalse(repository.findAll().iterator().hasNext())

        val response = datasource.save(brands)

        assertEquals("brand", response.first().slug)
        assertTrue(repository.findAll().iterator().hasNext())
    }
}
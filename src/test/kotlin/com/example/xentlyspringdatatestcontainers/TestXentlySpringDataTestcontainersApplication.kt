package com.example.xentlyspringdatatestcontainers

import org.springframework.boot.fromApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.boot.with
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.elasticsearch.ElasticsearchContainer
import org.testcontainers.utility.DockerImageName

@TestConfiguration(proxyBeanMethods = false)
class TestXentlySpringDataTestcontainersApplication {

    @Bean
    @ServiceConnection
    fun elasticsearchContainer(): ElasticsearchContainer {
        return DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:7.17.10")
            .run { ElasticsearchContainer(this) }
    }

    @Bean
    @ServiceConnection
    fun postgresContainer(): PostgreSQLContainer<*> {
        return DockerImageName.parse("postgis/postgis:15-3.3-alpine").asCompatibleSubstituteFor("postgres").run {
            PostgreSQLContainer(this)
        }
    }

}


fun main(args: Array<String>) {
    fromApplication<XentlySpringDataTestcontainersApplication>().with(TestXentlySpringDataTestcontainersApplication::class)
        .run(*args)
}

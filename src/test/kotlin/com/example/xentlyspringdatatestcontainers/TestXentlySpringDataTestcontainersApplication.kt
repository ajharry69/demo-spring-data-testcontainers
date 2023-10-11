package com.example.xentlyspringdatatestcontainers

import org.springframework.boot.fromApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.boot.with
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.elasticsearch.ElasticsearchContainer
import org.testcontainers.utility.DockerImageName


val POSTGRESQL_CONTAINER: PostgreSQLContainer<*> = DockerImageName.parse("postgis/postgis:15-3.3-alpine")
    .asCompatibleSubstituteFor("postgres")
    .run { PostgreSQLContainer(this) }

val ELASTICSEARCH_CONTAINER: ElasticsearchContainer =
    DockerImageName.parse("ajharry69/dynamic-synonym-elasticsearch:8.7.1")
        .asCompatibleSubstituteFor("docker.elastic.co/elasticsearch/elasticsearch:8.7.1")
        .run { ElasticsearchContainer(this) }
        .withEnv("xpack.security.enabled", "false")
        .withEnv("xpack.security.http.ssl.enabled", "false")

@TestConfiguration(proxyBeanMethods = false)
class TestXentlySpringDataTestcontainersApplication {

    @Bean
    @ServiceConnection
    fun elasticsearchContainer(): ElasticsearchContainer {
        return ELASTICSEARCH_CONTAINER
    }

    @Bean
    @ServiceConnection
    fun postgresContainer(): PostgreSQLContainer<*> {
        return POSTGRESQL_CONTAINER
    }

}


fun main(args: Array<String>) {
    fromApplication<XentlySpringDataTestcontainersApplication>().with(TestXentlySpringDataTestcontainersApplication::class)
        .run(*args)
}

package com.example.xentlyspringdatatestcontainers

import org.springframework.boot.devtools.restart.RestartScope
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
        .withEnv("ES_JAVA_OPTS", "-Xms1g -Xmx1g")
        .withSharedMemorySize(1L * 1_024 * 1_024)

/**
 * Ref: https://docs.spring.io/spring-boot/docs/3.1.4/reference/htmlsingle/#features.testing.testcontainers
 */
@TestConfiguration(proxyBeanMethods = false)
class TestXentlySpringDataTestcontainersApplication {

    @Bean
    @RestartScope
    @ServiceConnection
    fun elasticsearchContainer(): ElasticsearchContainer {
        return ELASTICSEARCH_CONTAINER
    }

    @Bean
    @RestartScope
    @ServiceConnection
    fun postgresContainer(): PostgreSQLContainer<*> {
        return POSTGRESQL_CONTAINER
    }

}


fun main(args: Array<String>) {
    fromApplication<XentlySpringDataTestcontainersApplication>()
        .with(TestXentlySpringDataTestcontainersApplication::class)
        .run(*args)
}

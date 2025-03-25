package ru.ershov.ddd.delivery.infrastructure.adapters.postgres.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import javax.sql.DataSource

@Profile("dev")
@Configuration
class DevDataSourceConfig {

    companion object {
        private val log = LoggerFactory.getLogger(DevDataSourceConfig::class.java)
    }

    class KPostgresSQLContainer(image: DockerImageName) : PostgreSQLContainer<KPostgresSQLContainer>(image)

    @Bean
    fun postgresSQLContainer(): KPostgresSQLContainer {
        val container = KPostgresSQLContainer(DockerImageName.parse("postgres:15"))
        container.start()
        Thread.sleep(5000)
        log.info("Database: {}", container.jdbcUrl)
        log.info("DB User: {}", container.username)
        log.info("DB Pass: {}", container.password)
        return container
    }

    @Bean
    @Primary
    fun dataSource(container: KPostgresSQLContainer): DataSource {
        val hikariConfig = HikariConfig()
        hikariConfig.password = container.password
        hikariConfig.username = container.username
        hikariConfig.jdbcUrl = container.jdbcUrl
        hikariConfig.driverClassName = container.driverClassName
        return HikariDataSource(hikariConfig)
    }
}
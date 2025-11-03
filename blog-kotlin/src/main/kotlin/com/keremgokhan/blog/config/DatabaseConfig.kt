package com.keremgokhan.blog.config

import com.keremgokhan.blog.models.Posts
import com.keremgokhan.blog.models.Users
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import mu.KotlinLogging
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

private val logger = KotlinLogging.logger {}

object DatabaseConfig {
    private lateinit var dataSource: HikariDataSource

    fun init(config: AppConfig) {
        logger.info { "Initializing database connection" }

        val hikariConfig = HikariConfig().apply {
            jdbcUrl = "jdbc:mysql://${config.database.host}:${config.database.port}/${config.database.name}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
            username = config.database.user
            password = config.database.password
            driverClassName = "com.mysql.cj.jdbc.Driver"
            maximumPoolSize = 10
            minimumIdle = 2
            idleTimeout = 600000
            connectionTimeout = 30000
            maxLifetime = 1800000

            // Recommended settings
            addDataSourceProperty("cachePrepStmts", "true")
            addDataSourceProperty("prepStmtCacheSize", "250")
            addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
        }

        dataSource = HikariDataSource(hikariConfig)
        Database.connect(dataSource)

        // Create tables if they don't exist
        transaction {
            SchemaUtils.createMissingTablesAndColumns(Users, Posts)
        }

        logger.info { "Database initialized successfully" }
    }

    fun close() {
        if (::dataSource.isInitialized && !dataSource.isClosed) {
            logger.info { "Closing database connection" }
            dataSource.close()
        }
    }
}

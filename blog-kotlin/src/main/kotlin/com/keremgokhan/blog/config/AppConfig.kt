package com.keremgokhan.blog.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

data class AppConfig(
    val server: ServerConfig,
    val database: DatabaseConfig,
    val session: SessionConfig,
    val analytics: AnalyticsConfig
) {
    data class ServerConfig(
        val port: Int,
        val host: String
    )

    data class DatabaseConfig(
        val host: String,
        val port: Int,
        val name: String,
        val user: String,
        val password: String
    )

    data class SessionConfig(
        val secret: String,
        val maxAge: Int
    )

    data class AnalyticsConfig(
        val enabled: Boolean,
        val googleAnalyticsId: String?
    )

    companion object {
        fun load(): AppConfig {
            val config = ConfigFactory.load()
            return AppConfig(
                server = ServerConfig(
                    port = config.getIntOrDefault("server.port", 7070),
                    host = config.getStringOrDefault("server.host", "0.0.0.0")
                ),
                database = DatabaseConfig(
                    host = config.getStringOrEnv("database.host", "DB_HOST", "localhost"),
                    port = config.getIntOrDefault("database.port", 3306),
                    name = config.getStringOrEnv("database.name", "DB_NAME", "blog"),
                    user = config.getStringOrEnv("database.user", "DB_USER", "root"),
                    password = config.getStringOrEnv("database.password", "DB_PASSWORD", "")
                ),
                session = SessionConfig(
                    secret = config.getStringOrEnv("session.secret", "SESSION_SECRET", "change-this-secret-in-production"),
                    maxAge = config.getIntOrDefault("session.maxAge", 86400) // 24 hours
                ),
                analytics = AnalyticsConfig(
                    enabled = config.getBooleanOrDefault("analytics.enabled", false),
                    googleAnalyticsId = config.getStringOrNull("analytics.googleAnalyticsId")
                )
            )
        }

        private fun Config.getStringOrEnv(path: String, envVar: String, default: String): String {
            return System.getenv(envVar)
                ?: if (hasPath(path)) getString(path) else default
        }

        private fun Config.getStringOrDefault(path: String, default: String): String {
            return if (hasPath(path)) getString(path) else default
        }

        private fun Config.getIntOrDefault(path: String, default: Int): Int {
            return if (hasPath(path)) getInt(path) else default
        }

        private fun Config.getBooleanOrDefault(path: String, default: Boolean): Boolean {
            return if (hasPath(path)) getBoolean(path) else default
        }

        private fun Config.getStringOrNull(path: String): String? {
            return if (hasPath(path)) getString(path) else null
        }
    }
}

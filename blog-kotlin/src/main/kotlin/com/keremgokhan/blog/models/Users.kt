package com.keremgokhan.blog.models

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

object Users : IntIdTable("User") {
    val name = varchar("name", 255).uniqueIndex()
    val password = varchar("password", 255)
    val updated = timestamp("updated").default(Instant.now())
}

data class User(
    val id: Int,
    val name: String,
    val password: String
)

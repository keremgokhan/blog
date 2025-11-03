package com.keremgokhan.blog.models

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant
import java.time.LocalDateTime

object Posts : IntIdTable("Post") {
    val title = varchar("title", 255)
    val body = text("body")
    val authorId = reference("author_id", Users)
    val created = datetime("created").default(LocalDateTime.now())
    val updated = timestamp("updated").default(Instant.now())
}

data class Post(
    val id: Int,
    val title: String,
    val body: String,
    val authorId: Int,
    val created: LocalDateTime
)

data class PostWithAuthor(
    val id: Int,
    val title: String,
    val body: String,
    val author: String,
    val created: LocalDateTime
)

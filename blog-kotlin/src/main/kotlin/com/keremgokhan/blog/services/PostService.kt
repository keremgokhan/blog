package com.keremgokhan.blog.services

import com.keremgokhan.blog.models.Post
import com.keremgokhan.blog.models.PostWithAuthor
import com.keremgokhan.blog.models.Posts
import com.keremgokhan.blog.models.Users
import mu.KotlinLogging
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

private val logger = KotlinLogging.logger {}

class PostService {
    fun getAllPosts(): List<PostWithAuthor> = transaction {
        (Posts innerJoin Users)
            .selectAll()
            .orderBy(Posts.created, SortOrder.DESC)
            .map { row ->
                PostWithAuthor(
                    id = row[Posts.id].value,
                    title = row[Posts.title],
                    body = row[Posts.body],
                    author = row[Users.name],
                    created = row[Posts.created]
                )
            }
    }

    fun getPostById(id: Int): PostWithAuthor? = transaction {
        (Posts innerJoin Users)
            .select { Posts.id eq id }
            .map { row ->
                PostWithAuthor(
                    id = row[Posts.id].value,
                    title = row[Posts.title],
                    body = row[Posts.body],
                    author = row[Users.name],
                    created = row[Posts.created]
                )
            }
            .singleOrNull()
    }

    fun createPost(title: String, body: String, authorId: Int): Post? = transaction {
        val id = Posts.insert {
            it[Posts.title] = title
            it[Posts.body] = body
            it[Posts.authorId] = authorId
            it[created] = LocalDateTime.now()
        } get Posts.id

        Posts.select { Posts.id eq id }
            .map { row ->
                Post(
                    id = row[Posts.id].value,
                    title = row[Posts.title],
                    body = row[Posts.body],
                    authorId = row[Posts.authorId].value,
                    created = row[Posts.created]
                )
            }
            .singleOrNull()
    }

    fun deletePost(id: Int): Boolean = transaction {
        Posts.deleteWhere { Posts.id eq id } > 0
    }

    fun updatePost(id: Int, title: String, body: String): Boolean = transaction {
        Posts.update({ Posts.id eq id }) {
            it[Posts.title] = title
            it[Posts.body] = body
        } > 0
    }
}

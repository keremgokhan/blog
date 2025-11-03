package com.keremgokhan.blog.services

import com.keremgokhan.blog.models.User
import com.keremgokhan.blog.models.Users
import mu.KotlinLogging
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

private val logger = KotlinLogging.logger {}

class UserService {
    fun findByUsername(username: String): User? = transaction {
        Users.select { Users.name eq username }
            .map { row ->
                User(
                    id = row[Users.id].value,
                    name = row[Users.name],
                    password = row[Users.password]
                )
            }
            .singleOrNull()
    }

    fun findById(id: Int): User? = transaction {
        Users.select { Users.id eq id }
            .map { row ->
                User(
                    id = row[Users.id].value,
                    name = row[Users.name],
                    password = row[Users.password]
                )
            }
            .singleOrNull()
    }

    fun createUser(username: String, password: String): User? = transaction {
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())

        val id = Users.insert {
            it[name] = username
            it[Users.password] = hashedPassword
        } get Users.id

        findById(id.value)
    }

    fun verifyPassword(password: String, hashedPassword: String): Boolean {
        return try {
            BCrypt.checkpw(password, hashedPassword)
        } catch (e: Exception) {
            logger.error(e) { "Error verifying password" }
            false
        }
    }
}

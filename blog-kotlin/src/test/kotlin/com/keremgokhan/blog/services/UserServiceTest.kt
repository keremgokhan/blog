package com.keremgokhan.blog.services

import com.keremgokhan.blog.models.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class UserServiceTest {
    private lateinit var userService: UserService

    @BeforeEach
    fun setup() {
        // Use in-memory H2 database for testing
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
        transaction {
            SchemaUtils.create(Users)
        }
        userService = UserService()
    }

    @AfterEach
    fun teardown() {
        transaction {
            SchemaUtils.drop(Users)
        }
    }

    @Test
    fun `createUser should create a user with hashed password`() {
        val user = userService.createUser("testuser", "password123")

        assertNotNull(user)
        assertEquals("testuser", user.name)
        assertTrue(user.password.startsWith("\$2a\$")) // BCrypt hash starts with $2a$
    }

    @Test
    fun `findByUsername should return user when exists`() {
        userService.createUser("testuser", "password123")

        val user = userService.findByUsername("testuser")

        assertNotNull(user)
        assertEquals("testuser", user.name)
    }

    @Test
    fun `findByUsername should return null when user does not exist`() {
        val user = userService.findByUsername("nonexistent")

        assertNull(user)
    }

    @Test
    fun `verifyPassword should return true for correct password`() {
        val user = userService.createUser("testuser", "password123")!!

        val result = userService.verifyPassword("password123", user.password)

        assertTrue(result)
    }

    @Test
    fun `verifyPassword should return false for incorrect password`() {
        val user = userService.createUser("testuser", "password123")!!

        val result = userService.verifyPassword("wrongpassword", user.password)

        assertTrue(!result)
    }

    @Test
    fun `findById should return user when exists`() {
        val createdUser = userService.createUser("testuser", "password123")!!

        val user = userService.findById(createdUser.id)

        assertNotNull(user)
        assertEquals(createdUser.id, user.id)
        assertEquals("testuser", user.name)
    }
}

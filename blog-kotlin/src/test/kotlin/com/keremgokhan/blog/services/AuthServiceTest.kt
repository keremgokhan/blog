package com.keremgokhan.blog.services

import com.keremgokhan.blog.models.User
import io.javalin.http.Context
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AuthServiceTest {
    private lateinit var authService: AuthService
    private lateinit var userService: UserService
    private lateinit var ctx: Context

    @BeforeEach
    fun setup() {
        userService = mockk()
        authService = AuthService(userService)
        ctx = mockk(relaxed = true)
    }

    @Test
    fun `authenticate should return user when credentials are valid`() {
        val user = User(1, "testuser", "\$2a\$10\$hashedpassword")
        every { userService.findByUsername("testuser") } returns user
        every { userService.verifyPassword("password123", user.password) } returns true

        val result = authService.authenticate("testuser", "password123")

        assertNotNull(result)
        assertEquals(user.id, result.id)
        assertEquals(user.name, result.name)
    }

    @Test
    fun `authenticate should return null when username does not exist`() {
        every { userService.findByUsername("nonexistent") } returns null

        val result = authService.authenticate("nonexistent", "password")

        assertNull(result)
    }

    @Test
    fun `authenticate should return null when password is incorrect`() {
        val user = User(1, "testuser", "\$2a\$10\$hashedpassword")
        every { userService.findByUsername("testuser") } returns user
        every { userService.verifyPassword("wrongpassword", user.password) } returns false

        val result = authService.authenticate("testuser", "wrongpassword")

        assertNull(result)
    }

    @Test
    fun `login should set session attributes`() {
        val user = User(1, "testuser", "password")

        authService.login(ctx, user)

        verify { ctx.sessionAttribute(AuthService.SESSION_USER_ID, user.id) }
        verify { ctx.sessionAttribute(AuthService.SESSION_USERNAME, user.name) }
    }

    @Test
    fun `getCurrentUser should return user when authenticated`() {
        val user = User(1, "testuser", "password")
        every { ctx.sessionAttribute<Int>(AuthService.SESSION_USER_ID) } returns 1
        every { userService.findById(1) } returns user

        val result = authService.getCurrentUser(ctx)

        assertNotNull(result)
        assertEquals(user.id, result.id)
    }

    @Test
    fun `getCurrentUser should return null when not authenticated`() {
        every { ctx.sessionAttribute<Int>(AuthService.SESSION_USER_ID) } returns null

        val result = authService.getCurrentUser(ctx)

        assertNull(result)
    }

    @Test
    fun `isAuthenticated should return true when user is logged in`() {
        every { ctx.sessionAttribute<Int>(AuthService.SESSION_USER_ID) } returns 1

        val result = authService.isAuthenticated(ctx)

        assertTrue(result)
    }

    @Test
    fun `isAuthenticated should return false when user is not logged in`() {
        every { ctx.sessionAttribute<Int>(AuthService.SESSION_USER_ID) } returns null

        val result = authService.isAuthenticated(ctx)

        assertTrue(!result)
    }
}

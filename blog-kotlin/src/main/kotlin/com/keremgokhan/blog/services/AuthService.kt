package com.keremgokhan.blog.services

import com.keremgokhan.blog.models.User
import io.javalin.http.Context
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class AuthService(
    private val userService: UserService
) {
    companion object {
        const val SESSION_USER_ID = "userId"
        const val SESSION_USERNAME = "username"
    }

    fun authenticate(username: String, password: String): User? {
        val user = userService.findByUsername(username) ?: return null

        return if (userService.verifyPassword(password, user.password)) {
            logger.info { "User authenticated successfully: $username" }
            user
        } else {
            logger.warn { "Failed authentication attempt for user: $username" }
            null
        }
    }

    fun login(ctx: Context, user: User) {
        ctx.sessionAttribute(SESSION_USER_ID, user.id)
        ctx.sessionAttribute(SESSION_USERNAME, user.name)
    }

    fun logout(ctx: Context) {
        ctx.req().session.invalidate()
    }

    fun getCurrentUser(ctx: Context): User? {
        val userId = ctx.sessionAttribute<Int>(SESSION_USER_ID) ?: return null
        return userService.findById(userId)
    }

    fun isAuthenticated(ctx: Context): Boolean {
        return ctx.sessionAttribute<Int>(SESSION_USER_ID) != null
    }

    fun requireAuth(ctx: Context): User {
        return getCurrentUser(ctx) ?: throw IllegalStateException("User not authenticated")
    }
}

package com.keremgokhan.blog.controllers

import com.keremgokhan.blog.services.AuthService
import com.keremgokhan.blog.utils.CsrfUtil
import io.javalin.http.Context
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class AdminController(
    private val authService: AuthService
) {
    fun index(ctx: Context) {
        val currentUser = authService.getCurrentUser(ctx)

        if (currentUser == null) {
            // Show login form
            val error = ctx.queryParam("error")
            ctx.render("admin/login.jte", mapOf(
                "error" to error,
                "csrfToken" to CsrfUtil.generateToken(ctx)
            ))
        } else {
            // Show admin dashboard
            ctx.render("admin/index.jte", mapOf(
                "currentUser" to currentUser,
                "isAuthenticated" to true
            ))
        }
    }

    fun login(ctx: Context) {
        // Verify CSRF token
        if (!CsrfUtil.validateToken(ctx)) {
            logger.warn { "CSRF token validation failed on login" }
            ctx.redirect("/admin?error=csrf")
            return
        }

        val username = ctx.formParam("username")?.trim()
        val password = ctx.formParam("password")

        if (username.isNullOrBlank() || password.isNullOrBlank()) {
            ctx.redirect("/admin?error=empty")
            return
        }

        val user = authService.authenticate(username, password)
        if (user != null) {
            authService.login(ctx, user)
            logger.info { "User logged in: $username" }
            ctx.redirect("/admin")
        } else {
            logger.warn { "Failed login attempt for username: $username" }
            ctx.redirect("/admin?error=invalid")
        }
    }

    fun logout(ctx: Context) {
        authService.logout(ctx)
        logger.info { "User logged out" }
        ctx.redirect("/")
    }

    fun showCreatePost(ctx: Context) {
        val currentUser = authService.getCurrentUser(ctx)

        if (currentUser == null) {
            ctx.redirect("/admin")
            return
        }

        val error = ctx.queryParam("error")

        ctx.render("admin/create.jte", mapOf(
            "currentUser" to currentUser,
            "isAuthenticated" to true,
            "error" to error,
            "csrfToken" to CsrfUtil.generateToken(ctx)
        ))
    }
}

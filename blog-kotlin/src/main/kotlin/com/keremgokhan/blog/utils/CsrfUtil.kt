package com.keremgokhan.blog.utils

import io.javalin.http.Context
import java.security.SecureRandom
import java.util.*

object CsrfUtil {
    private const val CSRF_TOKEN_SESSION_KEY = "csrfToken"
    private const val CSRF_TOKEN_PARAM = "csrf_token"
    private val secureRandom = SecureRandom()

    fun generateToken(ctx: Context): String {
        val token = ctx.sessionAttribute<String>(CSRF_TOKEN_SESSION_KEY)
        if (token != null) {
            return token
        }

        val newToken = generateRandomToken()
        ctx.sessionAttribute(CSRF_TOKEN_SESSION_KEY, newToken)
        return newToken
    }

    fun validateToken(ctx: Context): Boolean {
        val sessionToken = ctx.sessionAttribute<String>(CSRF_TOKEN_SESSION_KEY)
        val requestToken = ctx.formParam(CSRF_TOKEN_PARAM) ?: ctx.header("X-CSRF-Token")

        return sessionToken != null && requestToken != null && sessionToken == requestToken
    }

    fun getToken(ctx: Context): String? {
        return ctx.sessionAttribute(CSRF_TOKEN_SESSION_KEY)
    }

    private fun generateRandomToken(): String {
        val bytes = ByteArray(32)
        secureRandom.nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }
}

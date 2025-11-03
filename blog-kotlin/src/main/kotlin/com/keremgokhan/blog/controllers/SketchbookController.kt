package com.keremgokhan.blog.controllers

import com.keremgokhan.blog.services.AuthService
import io.javalin.http.Context

class SketchbookController(
    private val authService: AuthService
) {
    fun index(ctx: Context) {
        // For now, using placeholder images like the Perl version
        // In the future, this could be enhanced to read from a database or file system
        val images = (1..17).map { i ->
            mapOf(
                "id" to i,
                "url" to "/images/sketch.jpg",
                "title" to "Sketch $i"
            )
        }

        val currentUser = authService.getCurrentUser(ctx)

        ctx.render("sketchbook/index.jte", mapOf(
            "images" to images,
            "currentUser" to currentUser,
            "isAuthenticated" to authService.isAuthenticated(ctx)
        ))
    }
}

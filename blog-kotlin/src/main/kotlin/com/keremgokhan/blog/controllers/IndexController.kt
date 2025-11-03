package com.keremgokhan.blog.controllers

import com.keremgokhan.blog.services.AuthService
import com.keremgokhan.blog.services.PostService
import com.keremgokhan.blog.utils.DateUtil
import com.keremgokhan.blog.utils.HtmlSanitizer
import io.javalin.http.Context

class IndexController(
    private val postService: PostService,
    private val authService: AuthService
) {
    fun index(ctx: Context) {
        val posts = postService.getAllPosts().map { post ->
            mapOf(
                "id" to post.id,
                "title" to post.title,
                "body" to HtmlSanitizer.sanitize(post.body),
                "author" to post.author,
                "date" to DateUtil.formatDateHolocene(post.created),
                "time" to DateUtil.formatTime(post.created)
            )
        }

        val currentUser = authService.getCurrentUser(ctx)

        ctx.render("posts/index.jte", mapOf(
            "posts" to posts,
            "currentUser" to currentUser,
            "isAuthenticated" to authService.isAuthenticated(ctx)
        ))
    }
}

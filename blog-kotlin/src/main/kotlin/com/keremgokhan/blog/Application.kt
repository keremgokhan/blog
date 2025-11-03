package com.keremgokhan.blog

import com.keremgokhan.blog.config.AppConfig
import com.keremgokhan.blog.config.DatabaseConfig
import com.keremgokhan.blog.controllers.*
import com.keremgokhan.blog.services.AuthService
import com.keremgokhan.blog.services.PostService
import com.keremgokhan.blog.services.UserService
import gg.jte.ContentType
import gg.jte.TemplateEngine
import gg.jte.resolve.DirectoryCodeResolver
import io.javalin.Javalin
import io.javalin.http.staticfiles.Location
import io.javalin.rendering.template.JavalinJte
import mu.KotlinLogging
import java.nio.file.Path

private val logger = KotlinLogging.logger {}

fun main() {
    val config = AppConfig.load()

    logger.info { "Starting Blog application..." }

    // Initialize database
    DatabaseConfig.init(config)

    // Initialize services
    val userService = UserService()
    val postService = PostService()
    val authService = AuthService(userService)

    // Initialize controllers
    val indexController = IndexController(postService, authService)
    val postController = PostController(postService, authService)
    val adminController = AdminController(authService)
    val sketchbookController = SketchbookController(authService)

    // Configure template engine
    val isDevelopment = System.getenv("ENV") != "production"
    val templateEngine = if (isDevelopment) {
        logger.info { "Running in development mode - templates will hot reload" }
        TemplateEngine.create(
            DirectoryCodeResolver(Path.of("src/main/resources/templates")),
            ContentType.Html
        )
    } else {
        logger.info { "Running in production mode" }
        TemplateEngine.createPrecompiled(ContentType.Html)
    }
    JavalinJte.init(templateEngine)

    // Create Javalin app
    val app = Javalin.create { javalinConfig ->
        // Static files
        javalinConfig.staticFiles.add { staticFiles ->
            staticFiles.hostedPath = "/"
            staticFiles.directory = "/public"
            staticFiles.location = Location.CLASSPATH
        }

        // Session configuration
        javalinConfig.jetty.modifyServletContextHandler { handler ->
            handler.sessionHandler.apply {
                sessionCookie.name = "blog_session"
                sessionCookie.isHttpOnly = true
                sessionCookie.isSecure = !isDevelopment // Use secure cookies in production
                maxInactiveInterval = config.session.maxAge
            }
        }
    }.start(config.server.host, config.server.port)

    logger.info { "Server started on ${config.server.host}:${config.server.port}" }

    // Define routes
    app.routes {
        // Public routes
        app.get("/", indexController::index)
        app.get("/post/{id}", postController::show)

        // Admin routes
        app.get("/admin", adminController::index)
        app.post("/admin/login", adminController::login)
        app.get("/admin/logout", adminController::logout)
        app.get("/admin/create", adminController::showCreatePost)

        // Post creation
        app.post("/post", postController::create)

        // Sketchbook
        app.get("/sketchbook", sketchbookController::index)
    }

    // Error handlers
    app.error(404) { ctx ->
        ctx.render("errors/404.jte", mapOf(
            "message" to "Page not found"
        ))
    }

    app.error(500) { ctx ->
        logger.error { "Internal server error: ${ctx.result()}" }
        ctx.render("errors/500.jte", mapOf(
            "message" to "Internal server error"
        ))
    }

    // Shutdown hook
    Runtime.getRuntime().addShutdownHook(Thread {
        logger.info { "Shutting down application..." }
        app.stop()
        DatabaseConfig.close()
        logger.info { "Application stopped" }
    })
}

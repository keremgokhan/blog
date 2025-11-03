# Blog - Kotlin/Javalin Edition

A modern, secure personal blog application written in Kotlin using the Javalin web framework. This is a complete rewrite of the original Perl/Mojolicious blog with significant improvements in security, maintainability, and developer experience.

## Features

- **Blog Posts**: Create, view, and manage blog posts with rich text content
- **Authentication**: Secure user authentication with BCrypt password hashing
- **Sketchbook Gallery**: Image gallery feature for showcasing artwork
- **Security**: CSRF protection, HTML sanitization, and input validation
- **Responsive Design**: Mobile-friendly UI using Skeleton CSS framework
- **RESTful API**: Clean, RESTful routing structure

## Technology Stack

- **Framework**: [Javalin 6.x](https://javalin.io/) - Lightweight web framework
- **Language**: Kotlin 1.9.22
- **Database ORM**: [Exposed](https://github.com/JetBrains/Exposed) - JetBrains Kotlin SQL library
- **Template Engine**: [Jte](https://jte.gg/) - Fast, type-safe templates
- **Database**: MySQL 8.0+ (with H2 for testing)
- **Connection Pool**: HikariCP
- **Security**: BCrypt for password hashing, OWASP HTML Sanitizer
- **Testing**: JUnit 5, MockK
- **Logging**: Logback with kotlin-logging

## Prerequisites

- JDK 17 or higher
- MySQL 8.0 or higher
- Gradle 8.x (or use the included wrapper)

## Quick Start

### 1. Database Setup

Create a MySQL database:

```sql
CREATE DATABASE blog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

The application will automatically create the necessary tables on first run.

### 2. Configuration

Configure the application using environment variables or `application.conf`:

```bash
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=blog
export DB_USER=your_username
export DB_PASSWORD=your_password
export SESSION_SECRET=your-secret-key-here
```

Alternatively, edit `src/main/resources/application.conf` directly.

### 3. Create Admin User

After starting the application for the first time, create an admin user:

```bash
# Connect to your MySQL database
mysql -u your_username -p blog

# Create admin user (the password will be hashed by the application)
# You'll need to run the app once first to create the tables, then manually insert a user
# OR use the UserService.createUser() method programmatically
```

For development, you can add this code temporarily to your `Application.kt` main function:

```kotlin
// Create default admin user if none exists
transaction {
    if (UserService().findByUsername("admin") == null) {
        UserService().createUser("admin", "changeme")
        logger.info { "Created default admin user" }
    }
}
```

### 4. Run the Application

Using Gradle:

```bash
./gradlew run
```

Or build and run the JAR:

```bash
./gradlew shadowJar
java -jar build/libs/blog-kotlin-1.0.0-all.jar
```

The application will start on `http://localhost:7070`

### 5. Run Tests

```bash
./gradlew test
```

## Project Structure

```
blog-kotlin/
├── src/
│   ├── main/
│   │   ├── kotlin/com/keremgokhan/blog/
│   │   │   ├── Application.kt           # Main application entry point
│   │   │   ├── config/                  # Configuration classes
│   │   │   │   ├── AppConfig.kt
│   │   │   │   └── DatabaseConfig.kt
│   │   │   ├── controllers/             # HTTP request handlers
│   │   │   │   ├── AdminController.kt
│   │   │   │   ├── IndexController.kt
│   │   │   │   ├── PostController.kt
│   │   │   │   └── SketchbookController.kt
│   │   │   ├── models/                  # Data models
│   │   │   │   ├── Posts.kt
│   │   │   │   └── Users.kt
│   │   │   ├── services/                # Business logic
│   │   │   │   ├── AuthService.kt
│   │   │   │   ├── PostService.kt
│   │   │   │   └── UserService.kt
│   │   │   └── utils/                   # Utility classes
│   │   │       ├── CsrfUtil.kt
│   │   │       ├── DateUtil.kt
│   │   │       └── HtmlSanitizer.kt
│   │   └── resources/
│   │       ├── templates/               # Jte templates
│   │       │   ├── layouts/
│   │       │   ├── posts/
│   │       │   ├── admin/
│   │       │   ├── sketchbook/
│   │       │   └── errors/
│   │       ├── public/                  # Static assets
│   │       │   ├── css/
│   │       │   └── images/
│   │       ├── application.conf         # Application configuration
│   │       └── logback.xml             # Logging configuration
│   └── test/                           # Unit tests
└── build.gradle.kts                    # Gradle build configuration
```

## API Routes

| Method | Path | Description | Auth Required |
|--------|------|-------------|---------------|
| GET | `/` | List all blog posts | No |
| GET | `/post/:id` | View single post | No |
| GET | `/admin` | Admin dashboard or login | No |
| POST | `/admin/login` | Authenticate user | No |
| GET | `/admin/logout` | Logout | Yes |
| GET | `/admin/create` | Show post creation form | Yes |
| POST | `/post` | Create new post | Yes |
| GET | `/sketchbook` | View image gallery | No |

## Security Features

### Implemented Security Measures

1. **Password Security**: BCrypt hashing with automatic salt generation
2. **CSRF Protection**: Token-based protection on all state-changing operations
3. **HTML Sanitization**: All user-generated content is sanitized using OWASP HTML Sanitizer
4. **Input Validation**: Server-side validation on all forms
5. **Session Management**: Secure session handling with HttpOnly cookies
6. **SQL Injection Protection**: Parameterized queries via Exposed ORM
7. **XSS Prevention**: Automatic HTML escaping in templates

### Security Improvements Over Original Perl Version

- ✅ Fixed XSS vulnerability in post body rendering
- ✅ Added CSRF protection on all forms
- ✅ Implemented HTML sanitization for user content
- ✅ Added comprehensive input validation
- ✅ Upgraded from PBKDF2 to BCrypt (industry standard)
- ✅ Environment-based configuration (no credentials in code)
- ✅ Secure session cookies in production

## Development

### Development Mode

Set the `ENV` environment variable to enable development features:

```bash
ENV=development ./gradlew run
```

Development mode features:
- Template hot reloading (no need to restart on template changes)
- More verbose logging
- Non-secure session cookies (for HTTP testing)

### Adding a New Route

1. Create a method in the appropriate controller
2. Add the route in `Application.kt`
3. Create corresponding Jte templates
4. Add tests

### Database Migrations

The application uses Exposed's `SchemaUtils.createMissingTablesAndColumns()` for automatic schema creation. For production environments, consider using a proper migration tool like Flyway or Liquibase.

## Deployment

### Production Checklist

- [ ] Set `ENV=production`
- [ ] Use a strong `SESSION_SECRET` (32+ random characters)
- [ ] Configure database credentials via environment variables
- [ ] Enable HTTPS (configure reverse proxy like Nginx)
- [ ] Set up proper logging (configure `logback.xml`)
- [ ] Configure database connection pool size appropriately
- [ ] Set up automated backups for MySQL database
- [ ] Consider using a process manager (systemd, supervisor, etc.)

### Docker Deployment

Create a `Dockerfile`:

```dockerfile
FROM gradle:8-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle shadowJar

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/blog-kotlin-1.0.0-all.jar app.jar
EXPOSE 7070
CMD ["java", "-jar", "app.jar"]
```

Build and run:

```bash
docker build -t blog-kotlin .
docker run -p 7070:7070 \
  -e DB_HOST=host.docker.internal \
  -e DB_NAME=blog \
  -e DB_USER=root \
  -e DB_PASSWORD=yourpassword \
  -e SESSION_SECRET=your-secret \
  blog-kotlin
```

## Testing

Run all tests:

```bash
./gradlew test
```

Run specific test:

```bash
./gradlew test --tests UserServiceTest
```

View test report:

```bash
open build/reports/tests/test/index.html
```

## License

This is personal blog software. Feel free to use it as a template for your own blog.

## Migration from Perl Version

If you're migrating from the original Perl blog:

1. **Export your data** from the Perl MySQL database
2. **Import to new database** (schema is compatible)
3. **Re-hash passwords**: Passwords need to be re-hashed with BCrypt. Users will need to reset passwords OR you can write a migration script
4. **Copy images**: Copy image files to `src/main/resources/public/images/`
5. **Update configuration**: Set environment variables for database connection
6. **Test thoroughly**: Verify all posts display correctly

## Troubleshooting

### Application won't start

- Check database connection settings
- Verify MySQL is running and accessible
- Check logs in `logs/blog.log`

### Tests failing

- Ensure H2 database is in classpath
- Check test database configuration

### Templates not updating

- Make sure you're running in development mode (`ENV=development`)
- Restart the application

## Future Enhancements

Potential features to add:

- [ ] Post editing and deletion
- [ ] Rich text editor for posts
- [ ] Image upload functionality for posts
- [ ] Post categories and tags
- [ ] RSS feed
- [ ] Comments system
- [ ] Search functionality
- [ ] Markdown support
- [ ] Draft/publish workflow
- [ ] Multiple users with role-based access

## Support

For issues or questions, please open an issue on GitHub or contact the maintainer.

---

Built with ❤️ using Kotlin and Javalin

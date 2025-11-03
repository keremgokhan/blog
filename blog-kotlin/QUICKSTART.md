# Quick Start Guide

Get your Kotlin blog running in 5 minutes!

## Prerequisites Check

Before starting, verify you have:

```bash
# Check Java version (need 17+)
java -version

# Check MySQL is running
mysql --version
mysql -u root -p -e "SELECT 1;"
```

## Step 1: Database Setup (2 minutes)

```bash
# Create database
mysql -u root -p << EOF
CREATE DATABASE blog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
EOF
```

## Step 2: Configure Environment (1 minute)

Create a file named `.env` or set environment variables:

```bash
# In terminal (Linux/Mac)
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=blog
export DB_USER=root
export DB_PASSWORD=your_mysql_password
export SESSION_SECRET=$(openssl rand -base64 32)
```

Or on Windows:
```powershell
$env:DB_HOST="localhost"
$env:DB_PORT="3306"
$env:DB_NAME="blog"
$env:DB_USER="root"
$env:DB_PASSWORD="your_mysql_password"
$env:SESSION_SECRET="change-this-to-random-string"
```

## Step 3: Run the Application (1 minute)

```bash
# Make gradlew executable (Linux/Mac only)
chmod +x gradlew

# Run the application
./gradlew run

# Windows:
# gradlew.bat run
```

Wait for this message:
```
Server started on 0.0.0.0:7070
```

## Step 4: Create Admin User (1 minute)

The application will auto-create tables on first run.

**Temporary method** - Add this to `src/main/kotlin/com/keremgokhan/blog/Application.kt` after `DatabaseConfig.init(config)`:

```kotlin
// Create default admin user (remove after first run)
transaction {
    if (userService.findByUsername("admin") == null) {
        userService.createUser("admin", "changeme")
        logger.info { "Created default admin user: admin/changeme" }
    }
}
```

Restart the app, then **remove that code** and restart again.

## Step 5: Test It! (30 seconds)

1. Open browser: http://localhost:7070
2. Should see empty blog (no posts yet)
3. Go to: http://localhost:7070/admin
4. Login with: `admin` / `changeme`
5. Create your first post!

## Alternative: Docker (Fastest!)

If you have Docker installed:

```bash
# Start everything (app + database)
docker-compose up

# First time? Create admin user in container:
docker exec -it blog-app bash
# Then run mysql commands to insert user
```

## Troubleshooting

### Can't connect to database

```bash
# Check MySQL is running
sudo systemctl status mysql   # Linux
brew services list           # Mac
# Services.msc on Windows

# Test connection
mysql -u root -p -e "SHOW DATABASES;"
```

### Port 7070 already in use

Change the port:
```bash
export SERVER_PORT=8080
./gradlew run
```

### Gradle download is slow

First run downloads Gradle + dependencies. Be patient! Subsequent runs are fast.

### "No 'java' command found"

Install JDK 17 or higher:
- **Ubuntu/Debian**: `sudo apt install openjdk-17-jdk`
- **Mac**: `brew install openjdk@17`
- **Windows**: Download from https://adoptium.net/

## What's Next?

1. ✅ **Change admin password**: Create new user, delete default
2. ✅ **Write your first post**: Use the admin interface
3. ✅ **Customize**: Edit CSS in `src/main/resources/public/css/thestyle.css`
4. ✅ **Add images**: Put them in `src/main/resources/public/images/`
5. ✅ **Deploy**: See README.md for production deployment

## Common Tasks

### Create a new user

```kotlin
// In Application.kt, temporarily add:
transaction {
    userService.createUser("newuser", "password123")
}
```

### View logs

```bash
# While running
tail -f logs/blog.log

# Or check console output
```

### Stop the application

```bash
# Press Ctrl+C in the terminal where gradlew is running
```

### Run tests

```bash
./gradlew test

# View test report
open build/reports/tests/test/index.html  # Mac/Linux
start build/reports/tests/test/index.html # Windows
```

### Build production JAR

```bash
./gradlew shadowJar

# Run the JAR
java -jar build/libs/blog-kotlin-1.0.0-all.jar
```

## Development Mode

Enable hot-reloading for templates:

```bash
ENV=development ./gradlew run
```

Now you can edit `.jte` templates and see changes immediately (no restart needed)!

## Getting Help

1. Check [README.md](README.md) for detailed documentation
2. Check [MIGRATION.md](MIGRATION.md) if migrating from Perl
3. Check logs in `logs/blog.log`
4. Open an issue on GitHub

## Success!

If you see your blog at http://localhost:7070, you're all set! 🎉

Now start writing and enjoy your modern, secure blog platform!

---

**Pro tip**: Bookmark these URLs:
- Blog: http://localhost:7070
- Admin: http://localhost:7070/admin
- New post: http://localhost:7070/admin/create
- Sketchbook: http://localhost:7070/sketchbook

# Migration Guide: Perl to Kotlin

This guide will help you migrate from the Perl/Mojolicious blog to the Kotlin/Javalin version.

## Database Compatibility

The Kotlin version uses the same database schema as the Perl version, with minor improvements. The tables are compatible.

### Schema Comparison

**Users Table** - Compatible ✅
```sql
CREATE TABLE User (
  id        INT PRIMARY KEY AUTO_INCREMENT,
  name      VARCHAR(255),
  password  VARCHAR(255),
  updated   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

**Posts Table** - Compatible ✅
```sql
CREATE TABLE Post (
  id        INT PRIMARY KEY AUTO_INCREMENT,
  title     VARCHAR(255),
  body      TEXT,
  author_id INT,
  created   DATETIME,
  updated   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (author_id) REFERENCES User(id)
);
```

## Migration Steps

### 1. Backup Your Database

```bash
mysqldump -u username -p blog > blog_backup.sql
```

### 2. Handle Password Migration

The Perl version uses PBKDF2 password hashing, while the Kotlin version uses BCrypt. You have two options:

#### Option A: Reset All Passwords (Simplest)

1. Deploy the new application
2. Users reset their passwords through a password reset flow
3. New passwords will be BCrypt hashed

#### Option B: Migrate Passwords (Advanced)

Create a temporary migration script that:

1. Reads old PBKDF2 hashes
2. Prompts for password reset
3. Creates new BCrypt hashes

Example migration script (add to Application.kt temporarily):

```kotlin
fun migratePasswords() {
    transaction {
        // Find users with PBKDF2 hashes (they start with specific pattern)
        val oldUsers = Users.selectAll().where {
            Users.password.like("PBKDF2%")
        }

        // For each user, you'll need to reset the password
        // This requires user cooperation or a password reset flow
        println("Users requiring password reset: ${oldUsers.count()}")
    }
}
```

**Recommended**: Option A is simpler and more secure.

### 3. Export and Import Data

Your existing data should work as-is:

```bash
# Use the same database, or create a new one and import
mysql -u username -p new_blog < blog_backup.sql
```

### 4. Configure the Kotlin Application

Set environment variables:

```bash
export DB_HOST=localhost
export DB_NAME=blog  # or new_blog if you created a new database
export DB_USER=your_username
export DB_PASSWORD=your_password
export SESSION_SECRET=$(openssl rand -base64 32)
```

### 5. Copy Static Assets

If you've added custom images beyond the defaults:

```bash
cp -r perl-blog/public/images/* kotlin-blog/src/main/resources/public/images/
```

### 6. Test the Migration

1. Start the Kotlin application:
   ```bash
   ./gradlew run
   ```

2. Verify:
   - [ ] All posts are visible on the homepage
   - [ ] Individual post pages work
   - [ ] Post dates display correctly (Holocene calendar)
   - [ ] Images load correctly
   - [ ] Login works (after password reset)
   - [ ] Creating new posts works

### 7. Update Users on Password Changes

Send a notification to users that they need to reset their passwords due to a security upgrade.

## Feature Differences

### What's the Same

- ✅ All blog post viewing functionality
- ✅ Post creation workflow
- ✅ Admin authentication
- ✅ Sketchbook gallery
- ✅ Visual design (same CSS)
- ✅ Holocene calendar date format

### What's Improved

- ✅ **Security**: BCrypt passwords, CSRF protection, HTML sanitization
- ✅ **Performance**: Connection pooling, optimized queries
- ✅ **Code Quality**: Type-safe, modern Kotlin
- ✅ **Testing**: Comprehensive unit tests
- ✅ **Configuration**: Environment-based (no credentials in code)
- ✅ **Logging**: Structured logging with Logback

### What's Different

- ⚠️ **Port**: Default port changed from 3000 to 7070 (configurable)
- ⚠️ **Sessions**: New session format (users will be logged out)
- ⚠️ **Passwords**: Must be reset (BCrypt vs PBKDF2)

### What's Not Yet Implemented

- ❌ File upload functionality (was incomplete in Perl version anyway)
- ❌ Post editing/deletion (can be added if needed)

## Rollback Plan

If you need to rollback to the Perl version:

1. Keep the Perl application code available
2. Database is unchanged (compatible with both versions)
3. Switch DNS/proxy back to Perl application
4. Your data is safe - both versions use the same schema

## Gradual Migration Strategy

You can run both versions simultaneously:

1. Run Kotlin version on a different port (e.g., 7070)
2. Test thoroughly while Perl version serves production traffic
3. Use a reverse proxy to gradually shift traffic
4. Monitor both applications

Example Nginx configuration:

```nginx
upstream perl_blog {
    server localhost:3000;
}

upstream kotlin_blog {
    server localhost:7070;
}

server {
    listen 80;
    server_name blog.example.com;

    location / {
        # Start with Perl version
        proxy_pass http://perl_blog;

        # After testing, switch to:
        # proxy_pass http://kotlin_blog;
    }
}
```

## Post-Migration Checklist

- [ ] Verify all posts are visible
- [ ] Test login functionality
- [ ] Create a test post
- [ ] Verify sketchbook gallery works
- [ ] Check mobile responsiveness
- [ ] Test all routes
- [ ] Monitor logs for errors
- [ ] Set up monitoring/alerting
- [ ] Configure automated backups
- [ ] Update documentation

## Troubleshooting

### Posts not appearing

- Check database connection
- Verify `Post` table has data
- Check logs for SQL errors

### Login not working

- Remember: passwords need to be reset
- Check session configuration
- Verify `User` table data

### Images not loading

- Check static file configuration
- Verify images are in `src/main/resources/public/images/`
- Check file permissions

### Dates look wrong

- Dates should be in Holocene format (year + 10000)
- Check timezone settings in database connection

## Getting Help

If you encounter issues:

1. Check the logs in `logs/blog.log`
2. Review the README.md for configuration details
3. Open an issue with:
   - Error messages
   - Configuration (without passwords!)
   - Steps to reproduce

## Success Criteria

Your migration is successful when:

1. ✅ All existing posts display correctly
2. ✅ New posts can be created
3. ✅ Authentication works (after password reset)
4. ✅ No errors in logs during normal operation
5. ✅ Performance is acceptable (should be faster than Perl version)
6. ✅ Mobile layout works correctly

---

Good luck with your migration! The new Kotlin version provides better security, performance, and maintainability while preserving all the features you love.

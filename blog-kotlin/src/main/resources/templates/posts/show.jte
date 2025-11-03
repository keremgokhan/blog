@import com.keremgokhan.blog.models.User
@param Map<String, Object> post
@param User? currentUser = null
@param Boolean isAuthenticated = false

@template.layouts.blog(title = post.get("title").toString(), currentUser = currentUser, isAuthenticated = isAuthenticated, content = @`
    <article>
        <h5>${post.get("title")}</h5>
        <p style="color: #999; font-size: 0.9rem;">
            ${post.get("date")} at ${post.get("time")}
        </p>
        <div style="margin-top: 2rem;">
            $unsafe{post.get("body")}
        </div>
    </article>

    <div style="margin-top: 3rem;">
        <a href="/">&larr; Back to all posts</a>
    </div>
`)

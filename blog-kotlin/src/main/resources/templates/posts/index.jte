@import com.keremgokhan.blog.models.User
@param List<Map<String, Object>> posts
@param User? currentUser = null
@param Boolean isAuthenticated = false

@template.layouts.blog(title = "Blog", currentUser = currentUser, isAuthenticated = isAuthenticated, content = @`
    @for(var post : posts)
        <div class="post" style="margin-bottom: 3rem;">
            <h5>
                <a href="/post/${post.get("id")}" style="text-decoration: none; color: white;">
                    ${post.get("title")}
                </a>
            </h5>
            <p style="color: #ccc; font-size: 0.9rem;">
                <span style="float: right;">${post.get("date")}</span>
            </p>
            <div style="margin-top: 1rem;">
                $unsafe{post.get("body")}
            </div>
        </div>
    @endfor

    @if(posts.isEmpty())
        <p style="color: #999;">No posts yet.</p>
    @endif
`)

@import com.keremgokhan.blog.models.User
@param User currentUser
@param Boolean isAuthenticated = true
@param String? error = null
@param String csrfToken

@template.layouts.blog(title = "Create Post", currentUser = currentUser, isAuthenticated = isAuthenticated, content = @`
    <h5>Create New Post</h5>

    @if(error != null)
        <div style="padding: 1rem; background: #ff6b6b; color: white; margin-bottom: 1rem; border-radius: 4px;">
            @if(error.equals("empty"))
                Please fill in both title and body
            @elseif(error.equals("failed"))
                Failed to create post. Please try again.
            @else
                An error occurred. Please try again.
            @endif
        </div>
    @endif

    <form method="post" action="/post">
        <input type="hidden" name="csrf_token" value="${csrfToken}">

        <div>
            <label for="title">Title</label>
            <input type="text" id="title" name="title" class="u-full-width" required autofocus>
        </div>

        <div>
            <label for="body">Body</label>
            <textarea id="body" name="body" class="u-full-width" style="min-height: 300px;" required></textarea>
            <small style="color: #999;">HTML is allowed but will be sanitized for security</small>
        </div>

        <button type="submit" class="button-primary">Create Post</button>
        <a href="/admin" class="button" style="margin-left: 1rem;">Cancel</a>
    </form>
`)

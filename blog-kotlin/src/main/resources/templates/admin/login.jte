@param String? error = null
@param String csrfToken

@template.layouts.blog(title = "Login", content = @`
    <h5>Login</h5>

    @if(error != null)
        <div style="padding: 1rem; background: #ff6b6b; color: white; margin-bottom: 1rem; border-radius: 4px;">
            @if(error.equals("invalid"))
                Invalid username or password
            @elseif(error.equals("empty"))
                Please fill in all fields
            @elseif(error.equals("csrf"))
                Security token validation failed. Please try again.
            @else
                An error occurred. Please try again.
            @endif
        </div>
    @endif

    <form method="post" action="/admin/login">
        <input type="hidden" name="csrf_token" value="${csrfToken}">

        <div>
            <label for="username">Username</label>
            <input type="text" id="username" name="username" class="u-full-width" required autofocus>
        </div>

        <div>
            <label for="password">Password</label>
            <input type="password" id="password" name="password" class="u-full-width" required>
        </div>

        <button type="submit" class="button-primary">Login</button>
    </form>
`)

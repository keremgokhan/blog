@import com.keremgokhan.blog.models.User
@param User currentUser
@param Boolean isAuthenticated = true

@template.layouts.blog(title = "Admin", currentUser = currentUser, isAuthenticated = isAuthenticated, content = @`
    <h5>Admin Dashboard</h5>
    <p>Welcome back, ${currentUser.getName()}!</p>

    <div style="margin-top: 2rem;">
        <h6>Quick Actions</h6>
        <ul>
            <li><a href="/admin/create">Create new post</a></li>
            <li><a href="/">View blog</a></li>
            <li><a href="/sketchbook">View sketchbook</a></li>
        </ul>
    </div>
`)

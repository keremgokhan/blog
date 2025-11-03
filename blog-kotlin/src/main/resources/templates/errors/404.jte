@param String message = "Page not found"

@template.layouts.blog(title = "404 Not Found", content = @`
    <h5>404 - Not Found</h5>
    <p>${message}</p>
    <p><a href="/">Return to home page</a></p>
`)

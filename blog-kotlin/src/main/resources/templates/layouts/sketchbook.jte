@import com.keremgokhan.blog.models.User
@param String title = "Sketchbook"
@param User? currentUser = null
@param Boolean isAuthenticated = false
@param gg.jte.Content content

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="description" content="Kerem Gokhan's sketchbook">
    <meta name="author" content="Kerem Gokhan">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- CSS -->
    <link rel="stylesheet" href="/css/normalize.css">
    <link rel="stylesheet" href="/css/skeleton.css">
    <link rel="stylesheet" href="/css/sketchbook.css">

    <!-- Favicon -->
    <link rel="apple-touch-icon" sizes="180x180" href="/apple-touch-icon.png">
    <link rel="icon" type="image/png" sizes="32x32" href="/favicon-32x32.png">
    <link rel="icon" type="image/png" sizes="16x16" href="/favicon-16x16.png">
    <link rel="manifest" href="/manifest.json">
    <link rel="mask-icon" href="/safari-pinned-tab.svg" color="#5bbad5">
    <meta name="theme-color" content="#ffffff">
</head>
<body>
    <div class="container">
        <div class="row">
            <div class="twelve columns" style="margin-top: 10%">
                <h4><a href="/" style="text-decoration: none;">keremgokhan</a></h4>
                <h5><a href="/sketchbook" style="text-decoration: none;">sketchbook</a></h5>

                @if(isAuthenticated)
                <nav style="margin-bottom: 2rem;">
                    <a href="/">Blog</a> |
                    <a href="/admin">Admin</a> |
                    <a href="/admin/logout">Logout</a>
                </nav>
                @endif

                ${content}
            </div>
        </div>
    </div>
</body>
</html>

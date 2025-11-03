@import com.keremgokhan.blog.models.User
@param List<Map<String, Object>> images
@param User? currentUser = null
@param Boolean isAuthenticated = false

@template.layouts.sketchbook(title = "Sketchbook", currentUser = currentUser, isAuthenticated = isAuthenticated, content = @`
    <div class="row">
        @for(var image : images)
            <div class="four columns" style="margin-bottom: 2rem;">
                <img src="${image.get("url")}" alt="${image.get("title")}" style="width: 100%; height: auto;">
            </div>
        @endfor
    </div>
`)

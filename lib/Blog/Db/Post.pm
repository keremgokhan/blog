package Blog::Db::Post;

use base 'Blog::Db';

Blog::Db::Post->table('Post');

Blog::Db::Post->columns(
    All => qw/id title body author_id created updated/
);

1;

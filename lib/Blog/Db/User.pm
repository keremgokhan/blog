package Blog::Db::User;

use base 'Blog::Db';

Blog::Db::User->table('User');

Blog::Db::User->columns(
    All => qw/id name password updated/;
);

Blog::Db::User->has_many('posts', 'Blog::Db::Post' => 'author_id');

1;

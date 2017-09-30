package Blog::Db::User;

use base 'Blog::Db';

__PACKAGE__->table('User');

__PACKAGE__->columns(
    All => qw/id name password updated/
);

__PACKAGE__->columns(
    Primary => qw/id/
);

__PACKAGE__->has_many('posts', 'Blog::Db::Post' => 'author_id');

1;

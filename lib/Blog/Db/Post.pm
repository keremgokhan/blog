package Blog::Db::Post;

use base 'Blog::Db';
use Time::Piece::MySQL;
use Class::DBI::Plugin::RetrieveAll;

__PACKAGE__->table('Post');

__PACKAGE__->columns(
    All => qw/id title body author_id created updated/
);

__PACKAGE__->has_a(author_id => 'Blog::Db::User');

__PACKAGE__->has_a(created => 'Time::Piece',
    inflate => from_mysql_datetime,
    deflate => mysql_datetime,
);

sub all_posts {
    my $self = shift;

    my @all = Blog::Db::Post->retrieve_all_sorted_by("created DESC");

    my @posts = map {
        {
            id          => $_->id,
            title       => $_->title,
            body        => $_->body,
            created     => "1".$_->created->ymd('.')
        }
    } @all;

    return \@posts;
}

1;
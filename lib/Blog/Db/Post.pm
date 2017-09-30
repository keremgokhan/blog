package Blog::Db::Post;

use base 'Blog::Db';
use Time::Piece::MySQL;
use Class::DBI::Plugin::RetrieveAll;

__PACKAGE__->table('Post');

__PACKAGE__->columns(
    All => qw/id title body author_id created updated/
);

__PACKAGE__->columns(
    Primary => qw/id/
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
            id              => $_->id,
            title           => $_->title,
            body            => $_->body,
            created_date    => $_->formatted_date,
            created_time    => $_->formatted_time,
        }
    } @all;

    return \@posts;
}

sub formatted_date {
    my $self = shift;
    my $gregorian = $self->created->ymd('.');

    # This code will work until 10000 in Gregorian Calendar.
    # Well, that sounds enough.
    my $holocene = "1$gregorian";
    
    return $holocene;
}

sub formatted_time {
    my $self = shift;

    my $time = $self->created->hms;

    return $time;
}

sub add_new {
    my ($self, $param) = @_;

    my $title = $param->{title};
    my $body = $param->{body};
    my $user = $param->{user};

    return unless defined $user;
    return if (!$title && !$body);

    my $post = Blog::Db::Post->insert({
        author_id => $user->{id}, 
        title => $title,
        body => $body,
    });

    return $post;
}

1;

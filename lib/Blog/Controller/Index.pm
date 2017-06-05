package Blog::Controller::Index;

use Mojo::Base 'Mojolicious::Controller';

use Blog::Db::Post;
use Blog::Db::User;
use Data::Dumper;

sub index {
    my $self = shift;

    my $posts = Blog::Db::Post->all_posts();

    $self->render(template => 'index', posts => $posts);
}

1;

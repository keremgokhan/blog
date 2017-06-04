package Blog::Controller::Post;

use Mojo::Base 'Mojolicious::Controller';

sub index {
    my $self = shift;

    $self->render(template => 'index');
}

1;

package Blog::Controller::Index;

use Mojo::Base 'Mojolicious::Controller';

sub index {
    my $self = shift;

    $self->render(template => 'index');
}

1;

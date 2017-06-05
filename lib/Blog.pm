package Blog;

use  Mojo::Base 'Mojolicious';

sub startup {
    my $self = shift;

    # Dev mode: better debugging
    # $self->mode('development');

    my $r = $self->routes;

    $r->get('/')->to('index#index');
    $r->get('/post/:id')->to('post#index');
}

1;

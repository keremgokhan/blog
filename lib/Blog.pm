package Blog;

use Mojo::Base 'Mojolicious';
use Mojolicious::Plugin::Authentication;

use Blog::Authentication;

sub startup {
    my $self = shift;

    # Dev mode: better debugging
    # $self->mode('development');
    
    $self->plugin('authentication' => {
	'autoload_user'         => 1,
	'session_key'           => 'blog',
	'load_user'             => \&Blog::Authentication::load_user,
	'validate_user'         => \&Blog::Authentication::validate_user,
	'current_user_fn'       => 'user'
    });

    my $route_map = {
        '/' => {
            method      => 'GET',
            name        => 'index',
            controller  => 'index',
            action      => 'index'
        },
        '/post/:id' => {
            method      => 'GET',
            name        => 'show_post',
            controller  => 'post',
            action      => 'index'
        },
        '/post' => {
            method      => 'POST',
            name        => 'create_post',
            controller  => 'post',
            action      => 'create'
        },
        '/admin' => {
            method      => 'GET',
            name        => 'admin',
            controller  => 'admin',
            action      => 'index'
        },
        '/admin/login' => {
            method      => 'POST',
            name        => 'login',
            controller  => 'admin',
            action      => 'user_login'
        },
        '/admin/logout' => {
            method      => 'GET',
            name        => 'logout',
            controller  => 'admin',
            action      => 'user_logout'
        },
        '/admin/create' => {
            method      => 'GET',
            name        => 'create',
            controller  => 'admin',
            action      => 'create_post'
        }
    };

    $self->load_routes( $route_map );
}

sub load_routes {
    my ( $self, $route_map ) = @_;

    my $r = $self->routes;

    foreach my $route ( keys %{$route_map} ) {
        my $data        = $route_map->{$route};
        my $method      = delete $data->{method} // '';

        $r->get($route)->to($data)      if $method eq 'GET';
        $r->post($route)->to($data)     if $method eq 'POST';
    }
}

1;

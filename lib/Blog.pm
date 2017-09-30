package Blog;

use Mojo::Base 'Mojolicious';
use Mojolicious::Plugin::Authentication;

use Blog::Authentication;

sub startup {
    my $self = shift;

    # Dev mode: better debugging
    # $self->mode('development');
    
    $self->plugin('authentication' => {
	'autoload_user' => 1,
	'session_key' => 'blog',
	'load_user' => \&Blog::Authentication::load_user,
	'validate_user' => \&Blog::Authentication::validate_user,
	'current_user_fn' => 'user',
    });

    my $r = $self->routes;

    $r->get('/')->to('index#index')->name('index');
    $r->get('/post/:id')->to('post#index')->name('show_post');
    $r->post('/post')->to('post#create')->name('create_post');

    $r->get('/admin')->to('admin#index')->name('admin');
    $r->post('/admin/login')->to('admin#user_login')->name('login');
    $r->get('/admin/logout')->to('admin#user_logout')->name('logout');

    $r->get('/admin/create')->to('admin#create_post')->name('create');
}

1;

package Blog::Controller::Admin;

use Mojo::Base 'Mojolicious::Controller';

use Blog::Db::User;

sub index {
    my $self = shift;

    unless ( $self->is_user_authenticated ) {
        $self->render( template => 'login' );
        return;
    }

    my $user = $self->user;

    $self->render( template => 'admin', username => $user->name);
}

sub login {
    my $self = shift;

    my $username = $self->param('username');
    my $password = $self->param('password');

    unless ( $username && $password ) {
        $self->flash( error => "Give me some valid credentials kid." );
        $self->redirect_to('admin');
        return;
    }

    if ($self->authenticate($username, $password, { from_web => 1 })) {
        $self->redirect_to('admin');
    } else {
        $self->flash( error => "If you don't know your password, what are you doing here?" );
        $self->redirect_to('admin');
    }
}

sub logout {
    my $self = shift;

    $self->logout;

    $self->redirect_to('admin');
}

1;

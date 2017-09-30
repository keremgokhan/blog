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

    my %data = (
        template => 'admin',
        username => $user->name,
    );

    $self->get_user( \%data );

    $self->render( %data );
}

sub get_user {
    my ($self, $data) = @_;
    $data->{user} = $self->is_user_authenticated ? $self->user : undef;
}

sub user_login {
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

sub user_logout {
    my $self = shift;

    $self->logout;

    $self->redirect_to('admin');
}

sub create_post {
    my $self = shift;

    unless ( $self->is_user_authenticated ) {
        $self->render( template => 'login' );
        return;
    }

    my %data = ( template => 'create' );

    $self->get_user( \%data );

    $self->render( %data );
}

1;

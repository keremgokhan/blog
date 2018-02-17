package Blog::Controller;

use Mojo::Base 'Mojolicious::Controller';

sub get_user {
    my ($self, $data) = @_;

    my $user = undef;

    if ( $self->is_user_authenticated ) {
        $user = $self->user;
    }

    $data->{user} = $user;
}

1;

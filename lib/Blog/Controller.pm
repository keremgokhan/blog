package Blog::Controller;

use Mojo::Base 'Mojolicious::Controller';

sub init {
    my ($self, $data) = @_;

    $self->get_user( $data );
    $self->get_date( $data );
}

sub get_user {
    my ($self, $data) = @_;

    my $user = undef;

    if ( $self->is_user_authenticated ) {
        $user = $self->user;
    }

    $data->{user} = $user;
}

sub get_date {
    my ($self, $data) = @_;

    my $date = Mojo::Date->new;

    $data->{today} = $date->to_string;
}

1;

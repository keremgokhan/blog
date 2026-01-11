package Blog::Controller;

use Mojo::Base 'Mojolicious::Controller';
use DateTime;

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

    my $date = DateTime->now;

    my $day_name = $date->day_name;
    my $date_string = $date->ymd('.');

    $data->{today} = "Today is $day_name 1$date_string in Amsterdam.";
}

1;

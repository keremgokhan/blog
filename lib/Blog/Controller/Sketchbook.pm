package Blog::Controller::Sketchbook;

use Mojo::Base 'Blog::Controller';

sub index {
    my $self = shift;

    my %data = (template => 'sketchbook_home');

    $self->init(\%data);

    $self->get_images(\%data);

    $self->render(%data);
}

sub get_images {
    my ($self, $data) = @_;

    my $images = [
        {oh => 'yeah'},
        {oh => 'yeah'},
        {oh => 'yeah'},
        {oh => 'yeah'},
        {oh => 'yeah'},
        {oh => 'yeah'},
        {oh => 'yeah'},
        {oh => 'yeah'},
        {oh => 'yeah'},
        {oh => 'yeah'},
        {oh => 'yeah'},
        {oh => 'yeah'},
        {oh => 'yeah'},
        {oh => 'yeah'},
        {oh => 'yeah'},
        {oh => 'yeah'},
        {oh => 'yeah'},
    ];

    $data->{images} = $images;
}

1;

package Blog::Controller::Index;

use Mojo::Base 'Blog::Controller';

use Blog::Db::Post;
use Blog::Db::User;

sub index {
    my $self = shift;

    my %data = (template => 'index');

    $self->get_user(\%data);
    $self->get_posts(\%data);

    $self->render(%data);
}

sub get_posts {
    my ($self, $data) = @_;

    my $posts = Blog::Db::Post->all_posts();

    $data->{posts} = $posts;
}

1;

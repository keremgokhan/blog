package Blog::Controller::Post;

use Mojo::Base 'Mojolicious::Controller';

use Blog::Db::Post;

sub index {
    my $self = shift;

    my $id = $self->param('id');

    my $post = Blog::Db::Post->retrieve($id)
        or return $self->reply->not_found;

    $self->render(
        template => 'post',
        title => $post->title,
        body => $post->body,
        created => $post->formatted_date 
    );
}

1;

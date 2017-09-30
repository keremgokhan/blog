package Blog::Controller::Post;

use Mojo::Base 'Mojolicious::Controller';

use Blog::Db::Post;
use Blog::Db::User;

sub index {
    my $self = shift;

    my $id = $self->param('id');

    my $post = Blog::Db::Post->retrieve($id)
        or return $self->reply->not_found;

    $self->render(
        template => 'post',
        title => $post->title,
        body => $post->body,
        created_date => $post->formatted_date,
        created_time => $post->formatted_time 
    );
}

sub create {
    my $self = shift;

    my $user;
    if ( $self->is_user_authenticated ) {
        $user = $self->user;
    } else { 
        $self->flash( error => "Try logging in before posting stuff." );
        $self->redirect_to('admin');
        return;
    }

    my $title = $self->param('title');
    my $body = $self->param('body');

    if ( !$title && !$body ) {
        $self->flash( error => "Don't leave everything empty." );
        $self->redirect_to('create');
        return;
    }

    my $post = Blog::Db::Post->add_new({
        user => $user,
        title => $title,
        body => $body
    });

    $self->redirect_to('index');
}

1;

use Mojolicious::Lite;

get '/' => {text => 'Hola amigos.'};

app->start;
